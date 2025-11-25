package br.com.quiz.service;

import br.com.quiz.dao.QuestaoDAO;
import br.com.quiz.dao.QuestaoDAOImpl;
import br.com.quiz.dao.PontuacaoDAO;
import br.com.quiz.dao.PontuacaoDAOImpl;
import br.com.quiz.model.Questao;
import br.com.quiz.model.Pontuacao;
import br.com.quiz.model.Usuario;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class QuizService {

    private final QuestaoDAO questaoDAO;
    private final PontuacaoDAO pontuacaoDAO;

    private QuizTimer timer;
    private static final int TEMPO_POR_QUESTAO = 15;
    private static final int MAX_QUESTOES = 10;

    private List<Questao> perguntasQuiz;
    private Usuario usuarioAtual;
    private int indiceQuestaoAtual;
    private int pontuacao;
    private long tempoTotalMillis;
    private long inicioQuestaoMillis;
    private String nivelPartida;

    private QuizTimer.TimerListener uiListener;

    public QuizService() {
        this.questaoDAO = new QuestaoDAOImpl();
        this.pontuacaoDAO = new PontuacaoDAOImpl();
        this.perguntasQuiz = new ArrayList<>();
    }

    public void setUiListener(QuizTimer.TimerListener listener) {
        this.uiListener = listener;
    }

    public void adicionarQuestao(Questao questao) {
        questaoDAO.adicionarQuestao(questao);
    }

    public List<String> getCategoriasDisponiveis() {
        return questaoDAO.carregarPerguntas().stream()
                .map(Questao::getCategoria)
                .distinct()
                .collect(Collectors.toList());
    }

    private void iniciarTemporizador() {
        pararTemporizador();
        inicioQuestaoMillis = System.currentTimeMillis();
        timer = new QuizTimer(TEMPO_POR_QUESTAO, this::tempoEsgotado, uiListener);
        timer.execute();
    }

    private void pararTemporizador() {
        if (timer != null) {
            long tempoGasto = System.currentTimeMillis() - inicioQuestaoMillis;
            tempoTotalMillis += tempoGasto;
            timer.cancel(true);
            timer = null;
        }
    }

    public void tempoEsgotado() {
        pararTemporizador();
        avancarQuestao();
    }

    public void iniciarQuiz(Usuario usuario, String nivelFiltro, String categoriaFiltro) {
        this.usuarioAtual = usuario;
        this.indiceQuestaoAtual = 0;
        this.pontuacao = 0;
        this.tempoTotalMillis = 0;
        this.nivelPartida = nivelFiltro; // Salva o nível da partida

        List<Questao> todasPerguntas = questaoDAO.carregarPerguntas();

        List<Questao> perguntasFiltradas = todasPerguntas.stream()
                .filter(q -> "Todos".equalsIgnoreCase(nivelFiltro) || q.getNivel().equalsIgnoreCase(nivelFiltro))
                .filter(q -> "Todas".equalsIgnoreCase(categoriaFiltro) || q.getCategoria().equalsIgnoreCase(categoriaFiltro))
                .collect(Collectors.toList());

        Collections.shuffle(perguntasFiltradas);

        if (perguntasFiltradas.size() > MAX_QUESTOES) {
            this.perguntasQuiz = perguntasFiltradas.subList(0, MAX_QUESTOES);
        } else if (perguntasFiltradas.isEmpty()) {
            throw new RuntimeException("Nenhuma pergunta encontrada com os filtros selecionados.");
        } else {
            this.perguntasQuiz = perguntasFiltradas;
        }

        if (!this.perguntasQuiz.isEmpty()) {
            iniciarTemporizador();
        } else {
            throw new RuntimeException("Nenhuma pergunta encontrada com os filtros selecionados.");
        }
    }

    public boolean responder(String respostaSelecionada) {
        pararTemporizador();
        Questao atual = getQuestaoAtual();
        if (atual == null) return false;

        boolean acertou = atual.verificarResposta(respostaSelecionada);
        if (acertou) { this.pontuacao += 10; }

        avancarQuestao();
        return acertou;
    }

    private void avancarQuestao() {
        this.indiceQuestaoAtual++;

        if (!quizFinalizado()) {
            iniciarTemporizador();
        } else {
            finalizarQuiz();
        }
    }

    public void finalizarQuiz() {
        int segundosTotais = (int) (tempoTotalMillis / 1000);

        Pontuacao resultado = new Pontuacao(usuarioAtual.getNome(), this.pontuacao, segundosTotais, LocalDateTime.now(), nivelPartida);
        pontuacaoDAO.salvarPontuacao(resultado);
    }

    public Questao getQuestaoAtual() {
        if (indiceQuestaoAtual < perguntasQuiz.size()) {
            return perguntasQuiz.get(indiceQuestaoAtual);
        }
        return null;
    }
    public boolean quizFinalizado() { return indiceQuestaoAtual >= perguntasQuiz.size(); }
    public int getPontuacao() { return pontuacao; }
    public Usuario getUsuarioAtual() { return usuarioAtual; }

    public List<Pontuacao> getPlacarGeral(String nivelFiltro) {
        List<Pontuacao> todasPontuacoes = pontuacaoDAO.carregarPlacar();

        if ("Geral".equalsIgnoreCase(nivelFiltro) || "Todos".equalsIgnoreCase(nivelFiltro)) {
            return todasPontuacoes;
        }

        return todasPontuacoes.stream()
                .filter(p -> p.getNivel().equalsIgnoreCase(nivelFiltro))
                .collect(Collectors.toList());
    }

    public List<Pontuacao> getPlacarGeral() {
        return getPlacarGeral("Geral");
    }
}