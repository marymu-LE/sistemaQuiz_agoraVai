package br.com.quiz.ui;

import br.com.quiz.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private JTextField txtNomeUsuario;

    public LoginFrame() {
        super("Menu Inicial do Sistema de Quiz");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);

        JLabel lblTitulo = new JLabel("BEM-VINDO(A)!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Seu Nome/Login:"), gbc);
        txtNomeUsuario = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtNomeUsuario, gbc);

        JButton btnPartida = new JButton("Iniciar Nova Partida");
        btnPartida.addActionListener(this::abrirOpcoesPartida);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnPartida, gbc);

        JButton btnAdmin = new JButton("Gerenciar Perguntas (Admin)");
        btnAdmin.addActionListener(this::abrirGerenciamento);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnAdmin, gbc);

        this.add(panel);
    }

    private void abrirOpcoesPartida(ActionEvent event) {
        String nome = txtNomeUsuario.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite seu nome para iniciar.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuario = new Usuario(nome);
        OpcoesPartidaFrame opcoes = new OpcoesPartidaFrame(usuario);
        opcoes.setVisible(true);
        this.dispose();
    }

    private void abrirGerenciamento(ActionEvent event) {
        PerguntaAdminFrame admin = new PerguntaAdminFrame();
        admin.setVisible(true);
    }
}