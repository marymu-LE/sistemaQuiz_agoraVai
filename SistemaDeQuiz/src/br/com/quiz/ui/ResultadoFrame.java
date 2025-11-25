package br.com.quiz.ui;

import br.com.quiz.model.Pontuacao;
import br.com.quiz.service.QuizService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class ResultadoFrame extends JFrame {

    private final QuizService quizService;

    public ResultadoFrame(QuizService service) {
        super("Resultado do Quiz e Placar Geral");
        this.quizService = service;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650); // Ajustado o tamanho
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelEstatistica = new JPanel(new GridLayout(1, 1));
        panelEstatistica.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        int pontuacaoFinal = quizService.getPontuacao();
        JLabel lblEstatistica = new JLabel(String.format(
                "<html><div style='text-align: center;'>Parabéns, %s!<br>Você finalizou com <b>%d pontos</b>!</div></html>",
                quizService.getUsuarioAtual().getNome(), pontuacaoFinal
        ), SwingConstants.CENTER);
        lblEstatistica.setFont(new Font("Arial", Font.PLAIN, 18));
        panelEstatistica.add(lblEstatistica);

        this.add(panelEstatistica, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        String[] niveis = {"Geral", "Fácil", "Médio", "Difícil"};

        for (String nivel : niveis) {
            List<Pontuacao> placarFiltrado = quizService.getPlacarGeral(nivel);

            JTable tabela = criarTabelaPlacar(placarFiltrado);
            JScrollPane scrollPane = new JScrollPane(tabela);

            tabbedPane.addTab(nivel + " (" + placarFiltrado.size() + ")", scrollPane);
        }

        this.add(tabbedPane, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Sair");
        btnFechar.addActionListener(e -> System.exit(0)); // e é o ActionEvent, e não é usado (unused)
        this.add(btnFechar, BorderLayout.SOUTH);
    }

    private JTable criarTabelaPlacar(List<Pontuacao> placar) {
        placar.sort(Comparator.comparing(Pontuacao::getPontuacaoTotal).reversed()
                .thenComparing(Pontuacao::getTempoTotal));

        String[] colunas = {"Posição", "Nível", "Jogador", "Pontuação", "Tempo (s)", "Data/Hora"};

        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        int rank = 1;
        for (Pontuacao p : placar) {
            model.addRow(new Object[]{
                    rank++,
                    p.getNivel(),
                    p.getNomeUsuario(),
                    p.getPontuacaoTotal(),
                    p.getTempoTotal(),
                    p.getDataRegistro().format(formatter)
            });
        }
        return new JTable(model);
    }
}