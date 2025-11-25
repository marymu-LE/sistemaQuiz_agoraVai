package br.com.quiz.model;

import java.time.LocalDateTime;

public class Pontuacao {
    private String nomeUsuario;
    private int pontuacaoTotal;
    private int tempoTotal;
    private LocalDateTime dataRegistro;
    private String nivel;

    public Pontuacao(String nomeUsuario, int pontuacaoTotal, int tempoTotal, LocalDateTime dataRegistro, String nivel) {
        this.nomeUsuario = nomeUsuario;
        this.pontuacaoTotal = pontuacaoTotal;
        this.tempoTotal = tempoTotal;
        this.dataRegistro = dataRegistro;
        this.nivel = nivel;
    }

    public String toCSVString() {
        return String.format("%s;%d;%d;%s;%s",
                nomeUsuario,
                pontuacaoTotal,
                tempoTotal,
                dataRegistro.toString(),
                nivel
        );
    }

    public String getNomeUsuario() { return nomeUsuario; }
    public int getPontuacaoTotal() { return pontuacaoTotal; }
    public int getTempoTotal() { return tempoTotal; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public String getNivel() { return nivel; }
}