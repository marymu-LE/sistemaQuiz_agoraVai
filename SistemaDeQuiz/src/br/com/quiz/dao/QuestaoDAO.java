package br.com.quiz.dao;

import br.com.quiz.model.Questao;
import java.util.List;

public interface QuestaoDAO {
    List<Questao> carregarPerguntas();
    void adicionarQuestao(Questao questao);
}