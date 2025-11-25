package br.com.quiz.dao;

import br.com.quiz.model.Questao;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

public class QuestaoDAOImpl implements QuestaoDAO {
    private static final String ARQUIVO = "banco_de_perguntas.csv";
    private static final String DELIMITADOR = ";";

    @Override
    public List<Questao> carregarPerguntas() {
        List<Questao> perguntas = new ArrayList<>();
        String linha;

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(DELIMITADOR);

                if (dados.length == 8) {
                    String nivel = dados[0].trim();
                    String texto = dados[1].trim();

                    List<String> opcoes = Arrays.asList(dados[2].trim(), dados[3].trim(), dados[4].trim(), dados[5].trim());

                    String respostaCorreta = dados[6].trim();
                    String categoria = dados[7].trim();

                    Questao q = new Questao(texto, opcoes, respostaCorreta, nivel, categoria);
                    perguntas.add(q);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro de Persistência: Banco de perguntas não encontrado ou ilegível.",
                    "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        }
        return perguntas;
    }

    @Override
    public void adicionarQuestao(Questao questao) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {

            String linhaCsv = String.format("%s;%s;%s;%s;%s;%s;%s;%s",
                    questao.getNivel(),
                    questao.getTexto(),
                    questao.getOpcoes().get(0),
                    questao.getOpcoes().get(1),
                    questao.getOpcoes().get(2),
                    questao.getOpcoes().get(3),
                    questao.getRespostaCorreta(),
                    questao.getCategoria()
            );

            bw.write(linhaCsv);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar a questão: " + e.getMessage(),
                    "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
        }
    }
}