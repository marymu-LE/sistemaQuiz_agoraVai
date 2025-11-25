package br.com.quiz.ui;

import br.com.quiz.model.Usuario;
import br.com.quiz.service.QuizService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OpcoesPartidaFrame extends JFrame {

    private final Usuario usuario;
    private final QuizService quizService = new QuizService();

    private JComboBox<String> cmbNivel;
    private JComboBox<String> cmbCategoria;

    public OpcoesPartidaFrame(Usuario usuario) {
        super("Opções de Partida para " + usuario.getNome());
        this.usuario = usuario;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 300);
        setLocationRelativeTo(null);

        List<String> categorias = quizService.getCategoriasDisponiveis();
        String[] niveis = {"Todos", "Fácil", "Médio", "Difícil"};

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Escolha o Nível:"));
        cmbNivel = new JComboBox<>(niveis);
        panel.add(cmbNivel);

        panel.add(new JLabel("Escolha a Categoria:"));
        cmbCategoria = new JComboBox<>();
        cmbCategoria.addItem("Todas");
        for (String cat : categorias) {
            cmbCategoria.addItem(cat);
        }
        panel.add(cmbCategoria);

        JButton btnIniciar = new JButton("INICIAR QUIZ!");
        btnIniciar.addActionListener(e -> iniciarQuizFiltrado());
        panel.add(btnIniciar);

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> voltarAoMenu());
        panel.add(btnVoltar);

        this.add(panel);
    }

    private void iniciarQuizFiltrado() {
        String nivel = (String) cmbNivel.getSelectedItem();
        String categoria = (String) cmbCategoria.getSelectedItem();

        try {
            QuizFrame quizFrame = new QuizFrame(usuario, nivel, categoria);
            quizFrame.setVisible(true);
            this.dispose();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Inicialização", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarAoMenu() {
        LoginFrame login = new LoginFrame();
        login.setVisible(true);
        this.dispose();
    }
}