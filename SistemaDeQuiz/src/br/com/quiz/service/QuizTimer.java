package br.com.quiz.service;

import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

public class QuizTimer extends SwingWorker<Void, Integer> {

    private int tempoRestante;
    private final Runnable callbackTempoEsgotado;
    private final TimerListener listener;

    public interface TimerListener {
        void tempoAtualizado(int tempoRestante);
    }

    public QuizTimer(int tempoEmSegundos, Runnable callback, TimerListener listener) {
        this.tempoRestante = tempoEmSegundos;
        this.callbackTempoEsgotado = callback;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while (tempoRestante > 0 && !isCancelled()) {
            TimeUnit.SECONDS.sleep(1);
            tempoRestante--;
            publish(tempoRestante);
        }
        return null;
    }

    @Override
    protected void process(java.util.List<Integer> chunks) {
        int tempoAtual = chunks.get(chunks.size() - 1);
        if (listener != null) {
            listener.tempoAtualizado(tempoAtual);
        }
    }

    @Override
    protected void done() {
        if (!isCancelled() && tempoRestante == 0) {
            callbackTempoEsgotado.run();
        }
    }
}