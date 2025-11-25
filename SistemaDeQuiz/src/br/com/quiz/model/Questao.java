package br.com.quiz.model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Questao {
    private String texto;
    private List<String> opcoes;
    private String respostaCorreta;
    private String nivel;
    private String categoria;

    public Questao(String texto, List<String> opcoes, String respostaCorreta, String nivel, String categoria) {
        this.texto = texto;
        this.opcoes = opcoes;
        this.respostaCorreta = respostaCorreta;
        this.nivel = nivel;
        this.categoria = categoria;
    }

    public boolean verificarResposta(String resposta) {
        return this.respostaCorreta.trim().equalsIgnoreCase(resposta.trim());
    }

    public List<String> getOpcoesEmbaralhadas() {
        List<String> copiaOpcoes = new ArrayList<>(this.opcoes);
        Collections.shuffle(copiaOpcoes);
        return copiaOpcoes;
    }

    public String getTexto() { return texto; }
    public String getNivel() { return nivel; }
    public String getRespostaCorreta() { return respostaCorreta; }
    public String getCategoria() { return categoria; }
    public List<String> getOpcoes() { return opcoes; }
}