package model;

import java.util.*;

public abstract class Participante {
	// VARIAVEIS DE CLASSE
    static final int apostaMin = 50;
    static Dealer dealer;
    static List<Jogador> jogador;
    
    // VARIAVEIS DE INSTANCIA
    List<Mao> mao;
    int numMaosMax;
    boolean[] maosAtivas;
    int numMaosAtivas = 1; // Todos comecam com uma mao ativa
    
    // CONSTRUTOR
    Participante(int numMaosMax) {
    	this.numMaosMax = numMaosMax;
    	mao = new ArrayList<Mao>();
    	
    	maosAtivas = new boolean[numMaosMax];
		ativaMao(0);
		
		for (int i = 1; i < numMaosMax; i++)
			maosAtivas[i] = false;
    }
    
    // METODOS DE CLASSE
    /**
     * 
     * Valida se valor a ser aposta eh maior ou igual a aposta minima.
     * @return Se valido, true; se invalido, false.
     */
    static boolean validaAposta(int valor) {
		return valor >= apostaMin;
	}
    
 	/**
 	 * Indica se carta1 e carta2 possuem mesmo valor.
 	 * @return -1 se tiverem valor diferente. Se tiverem mesmo valor, retorna o valor.
 	 */
 	static int verificaCartasMesmoValor(Carta carta1, Carta carta2) {
 		int mapeamento = mapeamentoAux(carta1);
 		
 		if (mapeamento == mapeamentoAux(carta2))
 			return mapeamento;
 		return -1;
 	}

 	/*
 	static boolean verificaVencedor(Jogador jogador, int indMao, int asesSplitFlag) {
 		
 	}
 	
 	
 	/**
 	 * Funcao auxiliar que mapeia os valores simbolicos das cartas
 	 * em numeros inteiros.
 	 * @return o valor numerico correspondente ao simbolo.
 	 * Ases sao representados por 1; Reis, Rainhas e Valetes, por 10. 
 	 */
 	private static int mapeamentoAux(Carta carta) {
 		String valor;
 		
 		switch((valor = carta.getValor())) {
 		case "A":
 			return 1;
 		case "J":
 		case "K":
 		case "Q":
 			return 10;
 		}
 		
 		return Integer.parseInt(valor);
 	}
    
    // METODOS DE INSTANCIA
    /**
     * Calcula pontos da mao indMao.
     * @param indiceMao.
     * @return Valor de pontos.
     */
    int calculaPontos(int indiceMao) {
        return mao.get(indiceMao).calculaPontosMao();
    }
    
    /**
     * Verifica se mao indMao supera 21 pontos (quebra).
     * @param indiceMao
     * @return Se quebra, true; senao, false.
     */
    boolean checaQuebra(int indMao) {
        return calculaPontos(indMao) > 21;
    }
    
    void ativaMao(int indMao) {
    	maosAtivas[indMao] = true;
    }
    
    boolean checaMaoInativa(int indMao) {
    	 return maosAtivas[indMao] == false;
    }

    /**
     * Adicion uma carta na mao indMao.
     * @param indMao
     */
    void hit(int indMao) {
        Carta novaCarta = FonteCarta.compraCarta();
        mao.get(indMao).insere(novaCarta);
    }

    /**
     * Método para ficar com a mão atual sem pegar mais cartas
     * @param indiceMao
     */
    void stand(int indMao) {
        maosAtivas[indMao] = false;
        numMaosAtivas--;
    }
}
