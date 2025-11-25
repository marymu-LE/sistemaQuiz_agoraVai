package br.com.quiz.ui;

import br.com.quiz.model.Questao;
import br.com.quiz.model.Usuario;
import br.com.quiz.service.QuizService;
import javax.swing.*;
import java.awt.*;

public class QuizFrame extends JFrame implements br.com.quiz.service.QuizTimer.TimerListener {

    private final QuizService quizService;
    private JLabel lblTempo;
    private JTextArea txtQuestao;
    private JPanel panelRespostas;
    private JLabel lblPontuacao;

    public QuizFrame(Usuario usuario, String nivelFiltro, String categoriaFiltro) {
        super("Quiz de LPOO - Jogador: " + usuario.getNome());

        this.quizService = new QuizService();
        this.quizService.setUiListener(this);

        try {
            this.quizService.iniciarQuiz(usuario, nivelFiltro, categoriaFiltro);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Inicialização", JOptionPane.ERROR_MESSAGE);
            new OpcoesPartidaFrame(usuario).setVisible(true);
            this.dispose();
            return;
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel(new BorderLayout());
        lblTempo = new JLabel("Tempo: 15s", SwingConstants.CENTER);
        lblTempo.setFont(new Font("Arial", Font.BOLD, 18));

        lblPontuacao = new JLabel("Pontuação: 0", SwingConstants.RIGHT);
        lblPontuacao.setFont(new Font("Arial", Font.PLAIN, 14));

        panelHeader.add(lblTempo, BorderLayout.CENTER);
        panelHeader.add(lblPontuacao, BorderLayout.EAST);
        this.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtQuestao = new JTextArea("Carregando Questão...", 5, 50);
        txtQuestao.setWrapStyleWord(true);
        txtQuestao.setLineWrap(true);
        txtQuestao.setEditable(false);
        txtQuestao.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCentral.add(new JScrollPane(txtQuestao), BorderLayout.NORTH);

        panelRespostas = new JPanel(new GridLayout(4, 1, 10, 10));
        panelCentral.add(panelRespostas, BorderLayout.CENTER);

        this.add(panelCentral, BorderLayout.CENTER);

        exibirProximaQuestao();
    }

    @Override
    public void tempoAtualizado(int tempoRestante) {
        SwingUtilities.invokeLater(() -> {
            lblTempo.setText("Tempo Restante: " + tempoRestante + "s");
            lblTempo.setForeground(tempoRestante <= 5 ? Color.RED : Color.BLACK);
        });
    }

    private void exibirProximaQuestao() {
        if (quizService.quizFinalizado()) {
            ResultadoFrame resultado = new ResultadoFrame(quizService);
            resultado.setVisible(true);
            this.dispose();
            return;
        }

        Questao questaoAtual = quizService.getQuestaoAtual();
        lblPontuacao.setText("Pontuação: " + quizService.getPontuacao());

        panelRespostas.removeAll();
        txtQuestao.setText(questaoAtual.getTexto() +
                "\n\n(Nível: " + questaoAtual.getNivel() +
                " | Categoria: " + questaoAtual.getCategoria() + ")");

        for (String opcao : questaoAtual.getOpcoesEmbaralhadas()) {
            JButton btnOpcao = new JButton(opcao);
            btnOpcao.setFont(new Font("Arial", Font.PLAIN, 14));
            btnOpcao.addActionListener(e -> responder(opcao));
            panelRespostas.add(btnOpcao);
        }

        panelRespostas.revalidate();
        panelRespostas.repaint();
    }

    private void responder(String resposta) {
        String respostaCorreta = quizService.getQuestaoAtual().getRespostaCorreta();
        boolean acertou = quizService.responder(resposta);

        String mensagem = acertou
                ? "Correto! Você ganhou 10 pontos."
                : "Incorreto. A resposta correta era: " + respostaCorreta;

        JOptionPane.showMessageDialog(this, mensagem, "Resultado",
                acertou ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

        exibirProximaQuestao();
    }
}