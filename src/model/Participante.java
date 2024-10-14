package model;

import java.util.*;

abstract class Participante {
	// VARIAVEIS DE CLASSE
	private static final int apostaMin = 50; 					// Aposta minima
	static Dealer dealer; 										// Objeto dealer a ser criado
	static List<Jogador> jogador = new ArrayList<Jogador>(); 	// Lista de jogadores a serem criados

	// VARIAVEIS DE INSTANCIA
	List<Mao> mao;
	int numMaosMax;			// Numero maximo de maos
	boolean[] maosAtivas;	// Indica se a i-esima mao esta ativa (true) ou inativa (false)
	int numMaosAtivas = 1; 	// Todo participante comeca com uma mao ativa

	// CONSTRUTOR
	Participante(int numMaosMax) {
		this.numMaosMax = numMaosMax;

		mao = new ArrayList<Mao>();
		for (int i = 0; i < numMaosMax; i++)
			mao.add(new Mao());

		maosAtivas = new boolean[numMaosMax];
		ativaMao(0);
		for (int i = 1; i < numMaosMax; i++)
			maosAtivas[i] = false;
	}

	// METODOS DE CLASSE
	/**
	 * Valida se valor a ser aposta eh maior ou igual a aposta minima.
	 * @return Se valido, retorna true. Se invalido, false.
	 */
	static boolean validaAposta(int valor) {
		return valor >= apostaMin;
	}
	
	/**
	 * Funcao auxiliar que mapeia os valores simbolicos das cartas em numeros
	 * inteiros.
	 * 
	 * @return o valor numerico correspondente ao simbolo. Ases sao representados
	 * por 1; JKQ, por 10.
	 */
	private static int mapeamentoAux(Carta carta) {
		String valor;

		switch ((valor = carta.getValor())) {
		case "A":
			return 1;
		case "J":
		case "K":
		case "Q":
			return 10;
		}

		return Integer.parseInt(valor);
	}

	/**
	 * Indica se carta1 e carta2 possuem mesmo valor.
	 * @return -1 se tiverem valor diferente. Se tiverem mesmo valor, retorna o valor.
	 * Ases tem valor 1, JKG tem valor 10.
	 */
	static int verificaCartasMesmoValor(Carta carta1, Carta carta2) {
		int mapeamento = mapeamentoAux(carta1);
		
		if (mapeamento == mapeamentoAux(carta2))
			return mapeamento;
		return -1;
	}
	
	/**
	 * Indica se dada mao eh um Blackjack.
	 * @param asesSplitFlag indica se ocorreu um split de ases. Nesse caso,
	 * ainda que faca 21 pontos com duas cartas, o jogador nao tera um Blackjack.
	 * @return 1, se for Blackjack. Do contrario, 0.
	 */
	int possuiBlackjack(int indMao, boolean asesSplitFlag) {
		if (mao.get(indMao).getNumCartas() == 2 && mao.get(indMao).calculaPontosMao() == 21 && !asesSplitFlag)
			return 1;
		return 0;
	}
	
	/**
	 * Indica se a mao indMao de jogador vence da mao do dealer.
	 * @param asesSplitFlag indica se ocorreu um split de ases. Nesse caso,
	 * ainda que faca 21 pontos com duas cartas, o jogador nao tera um Blackjack.
	 * @return 0, se houver empate; um inteiro maior que zero, caso o jogador venca;
	 * um inteiro menor que zero, caso o dealer venca.
	 */
	static int verificaVencedor(Jogador jogador, int indMao, boolean asesSplitFlag, int pontosDealer) {
		int pontosJogador = jogador.possuiBlackjack(indMao, asesSplitFlag) + jogador.mao.get(indMao).calculaPontosMao();

		return pontosJogador - pontosDealer;
	}

	// METODOS DE INSTANCIA
	/**
	 * Calcula pontos da mao indMao.
	 * @return valor de pontos.
	 */
	int calculaPontos(int indiceMao) {
		return mao.get(indiceMao).calculaPontosMao();
	}

	/**
	 * Verifica se mao indMao supera 21 pontos (quebra).
	 * @return Se quebra, true. Do contrario, false.
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
	 * Adiciona uma carta a mao indMao.
	 */
	void hit(int indMao) {
		Carta novaCarta = FonteCarta.compraCarta();
		mao.get(indMao).insere(novaCarta);
	}

	/**
	 * Inativa mao indMao.
	 */
	void stand(int indMao) {
		maosAtivas[indMao] = false;
		numMaosAtivas--;
	}
}
