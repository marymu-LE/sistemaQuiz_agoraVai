package br.com.quiz.dao;

import br.com.quiz.model.Pontuacao;
import java.util.List;

public interface PontuacaoDAO {
    List<Pontuacao> carregarPlacar();
    void salvarPontuacao(Pontuacao pontuacao);
}