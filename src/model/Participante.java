package model;

import java.util.*;

abstract class Participante {
	// VARIAVEIS DE CLASSE
	private static final int apostaMin = 50; // Aposta minima
	private static final int fatorGanhoAposta = 2; // Se jogador vence, tera de volta o dobro do que apostou

	// VARIAVEIS DE INSTANCIA
	List<Mao> mao;
	int numMaosMax;			// Numero maximo de maos
	boolean[] maosAtivas;	// Indica se a i-esima mao esta ativa (true) ou inativa (false)
	boolean[] maosFinalizadas;
	boolean[] maosQuebradas;
	int numMaosAtivas = 0;
	int numMaosFinalizadas = 0;

	// CONSTRUTOR
	Participante(int numMaosMax) {
		this.numMaosMax = numMaosMax;

		mao = new ArrayList<Mao>();
		for (int i = 0; i < numMaosMax; i++)
			mao.add(new Mao());

		maosAtivas = new boolean[numMaosMax];
		maosQuebradas = new boolean[numMaosMax];
		maosFinalizadas = new boolean[numMaosMax];
		
		for (int i = 0; i < numMaosMax; i++)
			maosAtivas[i] = maosQuebradas[i] = maosFinalizadas[i] = false;
		
		ativaMao(0);
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
	 * Indica se a mao indMao de jogador vence da mao do dealer.
	 * @param asesSplitFlag indica se ocorreu um split de ases. Nesse caso,
	 * ainda que faca 21 pontos com duas cartas, o jogador nao tera um Blackjack.
	 * @return 0, se houver empate; um inteiro maior que zero, caso o jogador venca;
	 * um inteiro menor que zero, caso o dealer venca.
	 */
	static int verificaVencedor(Jogador jogador, int indMao, int pontosDealer) {
		int pontosJogador = jogador.possuiBlackjack(indMao) + jogador.mao.get(indMao).calculaPontosMao();

		return pontosJogador - pontosDealer;
	}

	// METODOS DE INSTANCIA
	
	int getNumMaosAtivas() {
		return numMaosAtivas;
	}
	
	int getNumMaosFinalizadas() {
		return numMaosFinalizadas;
	}
	
	int getNumCartas(int indexMao) {
		return mao.get(indexMao).getNumCartas();
	}
	
	/**
	 * Indica se dada mao eh um Blackjack.
	 * @param asesSplitFlag indica se ocorreu um split de ases. Nesse caso,
	 * ainda que faca 21 pontos com duas cartas, o jogador nao tera um Blackjack.
	 * @return 1, se for Blackjack. Do contrario, 0.
	 */
	int possuiBlackjack(int indMao) {
		if (mao.get(indMao).getNumCartas() == 2 && mao.get(indMao).calculaPontosMao() == 21)
			return 1;
		return 0;
	}
	
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
	
	//////////////////////////////////////////
	boolean checaQuebrada(int indMao) {
		return maosQuebradas[indMao];
	}
	
	
	boolean checaFinalizada(int indMao) {
		return maosFinalizadas[indMao];
	}
	//////////////////////////////////////////

	void ativaMao(int indMao) {
		maosAtivas[indMao] = true;
		numMaosAtivas++;
	}
	
	/////////////////////////////////
	void quebraMao(int indMao) {
		maosQuebradas[indMao] = true;
		stand(indMao);
	}
	////////////////////////////////

	boolean checaMaoAtiva(int indMao) {
		return maosAtivas[indMao] == true;
	}

	/**
	 * Adiciona uma carta a mao indMao.
	 */
	void hit(int indMao) {
		Carta novaCarta = FonteCarta.compraCarta();
		mao.get(indMao).insere(novaCarta);
		System.out.println("HIT"); ////////////////////////
	}

	/**
	 * Inativa mao indMao.
	 */
	void stand(int indMao) {
		maosFinalizadas[indMao] = true;
		numMaosFinalizadas++;
	}
	
	/////////////////////////////////////////////
	void limpa() {
		for (int i = 0; i < numMaosMax; i++)
			mao.get(i).limpaMao();
		
		for (int i = 0; i < numMaosMax; i++)
			maosAtivas[i] = maosFinalizadas[i] = maosQuebradas[i] = false;
		
		numMaosAtivas = numMaosFinalizadas = 0;
		
		ativaMao(0);
		System.out.println("LIMPA"); ////////////////////////
	}
	/////////////////////////////////////////////
	
	boolean verificaPrimDuasCartasMesmoValor() {
		return Participante.verificaCartasMesmoValor(mao.get(0).cartas.get(0), mao.get(0).cartas.get(1)) != -1;
	}
}
