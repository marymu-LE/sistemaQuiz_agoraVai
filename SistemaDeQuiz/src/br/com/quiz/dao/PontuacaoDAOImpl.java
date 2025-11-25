package br.com.quiz.dao;

import br.com.quiz.model.Pontuacao;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PontuacaoDAOImpl implements PontuacaoDAO {
    private static final String ARQUIVO_PLACAR = "placar_geral.csv";
    private static final String DELIMITADOR = ";";

    @Override
    public List<Pontuacao> carregarPlacar() {
        List<Pontuacao> placar = new ArrayList<>();
        String linha;

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PLACAR))) {
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(DELIMITADOR);

                if (dados.length == 5) {
                    try {
                        String nomeUsuario = dados[0].trim();
                        int pontuacaoTotal = Integer.parseInt(dados[1].trim());
                        int tempoTotal = Integer.parseInt(dados[2].trim());
                        LocalDateTime dataRegistro = LocalDateTime.parse(dados[3].trim());
                        String nivel = dados[4].trim(); // NOVO CAMPO LIDO

                        Pontuacao p = new Pontuacao(nomeUsuario, pontuacaoTotal, tempoTotal, dataRegistro, nivel);
                        placar.add(p);
                    } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                        System.err.println("Erro de formato de dados no placar: " + linha);
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de placar: " + e.getMessage());
        }
        return placar;
    }

    @Override
    public void salvarPontuacao(Pontuacao pontuacao) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_PLACAR, true))) {
            bw.write(pontuacao.toCSVString());
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao salvar a pontuação: " + e.getMessage(),
                    "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
        }
    }
}