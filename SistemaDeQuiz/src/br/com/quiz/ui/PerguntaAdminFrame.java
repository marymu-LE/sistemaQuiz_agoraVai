package br.com.quiz.ui;

import br.com.quiz.model.Questao;
import br.com.quiz.service.QuizService;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class PerguntaAdminFrame extends JFrame {

    private final QuizService quizService = new QuizService();

    private JTextField txtTexto, txtOp1, txtOp2, txtOp3, txtOp4, txtCorreta, txtCategoria;
    private JComboBox<String> cmbNivel;

    public PerguntaAdminFrame() {
        super("Gerenciamento de Perguntas (Admin)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(11, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cmbNivel = new JComboBox<>(new String[]{"Fácil", "Médio", "Difícil"});
        txtTexto = new JTextField();
        txtOp1 = new JTextField(); txtOp2 = new JTextField();
        txtOp3 = new JTextField(); txtOp4 = new JTextField();
        txtCorreta = new JTextField();
        txtCategoria = new JTextField();

        panel.add(new JLabel("Nível:")); panel.add(cmbNivel);
        panel.add(new JLabel("Texto da Pergunta:")); panel.add(txtTexto);
        panel.add(new JLabel("Opção A:")); panel.add(txtOp1);
        panel.add(new JLabel("Opção B:")); panel.add(txtOp2);
        panel.add(new JLabel("Opção C:")); panel.add(txtOp3);
        panel.add(new JLabel("Opção D:")); panel.add(txtOp4);
        panel.add(new JLabel("Resposta Correta:")); panel.add(txtCorreta);
        panel.add(new JLabel("Categoria:")); panel.add(txtCategoria);

        JButton btnAdicionar = new JButton("Adicionar Pergunta");
        btnAdicionar.addActionListener(e -> adicionarPergunta());

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> dispose());

        panel.add(btnAdicionar);
        panel.add(btnVoltar);

        this.add(panel);
    }

    private void adicionarPergunta() {
        String nivel = (String) cmbNivel.getSelectedItem();
        String texto = txtTexto.getText().trim();
        String op1 = txtOp1.getText().trim();
        String op2 = txtOp2.getText().trim();
        String op3 = txtOp3.getText().trim();
        String op4 = txtOp4.getText().trim();
        String correta = txtCorreta.getText().trim();
        String categoria = txtCategoria.getText().trim();

        if (texto.isEmpty() || correta.isEmpty() || categoria.isEmpty() || op1.isEmpty() || op2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos da questão.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            java.util.List<String> opcoes = Arrays.asList(op1, op2, op3, op4);
            Questao novaQuestao = new Questao(texto, opcoes, correta, nivel, categoria);

            quizService.adicionarQuestao(novaQuestao);

            JOptionPane.showMessageDialog(this, "Pergunta adicionada com sucesso! Reinicie o Quiz para usar.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            txtTexto.setText(""); txtCorreta.setText(""); txtCategoria.setText("");
            txtOp1.setText(""); txtOp2.setText(""); txtOp3.setText(""); txtOp4.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a pergunta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}