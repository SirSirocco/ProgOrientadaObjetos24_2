package model;

import java.util.List;

public abstract class Participante {
    protected static final int apostaMin = 50;
    protected static Dealer dealer;
    protected static List<Jogador> jogador;
    protected int numMaosMax;
    protected List<Mao> mao;
    protected int numMaosAtivas;

    // Método estático para limpar as mãos de todos os participantes
    public static void limpa() {
        dealer.mao.limpa();
        for (Jogador j : jogador) {
            j.getMao().forEach(Mao::limpa);
        }
    }

    // Método estático para iniciar uma nova rodada
    public static void novaRodada() {
        limpa();
        dealer.distribuirCartas();
    }

    // Método estático para determinar se um vencedor foi encontrado
    public static int determinaVencedor() {
        // Lógica para determinar o vencedor entre o dealer e os jogadores
        return 0;
    }

    // Cálculo dos pontos da mão do participante
    public int calcPontos(int indiceMao) {
        return mao.get(indiceMao).calcularPontos();
    }

    // Verifica se a mão atual do participante está ativa ou está quebrada
    public boolean checaQuebra(int indiceMao) {
        return calcPontos(indiceMao) > 21;
    }

    // Método para adicionar uma carta na mão do participante
    public void hit(int indiceMao) {
        if (numMaosAtivas > 0) {
            Carta novaCarta = dealer.comprarCarta();
            mao.get(indiceMao).insere(novaCarta);
            if (checaQuebra(indiceMao)) {
                numMaosAtivas--;
            }
        }
    }

    // Método para ficar com a mão atual sem pegar mais cartas
    public void stand(int indiceMao) {
        numMaosAtivas--;
    }
}
