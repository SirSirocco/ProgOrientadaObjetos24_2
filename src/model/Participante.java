package model;

import java.util.*;

/**
 * Classe abstrata que implementa os participantes do jogo, que se especializam
 * em Dealer e Jogador. Implementa metodos compartilhados por essas duas
 * classes.
 * 
 * @since 23-11-2024
 * @author Guilherme Senko
 */
abstract class Participante {
	// CONSTANTES DE CLASSE
	private static final int apostaMin = 50; 	// Aposta minima

	// VARIAVEIS DE INSTANCIA
	List<Mao> mao;				// Maos do participante

	int numMaosMax; 			// Numero maximo de maos
	int numMaosAtivas = 0; 		// Numero de maos em jogo
	int numMaosFinalizadas = 0; // Numero de maos que finalizaram o turno (sempre menor ou igual a
								 // numMaosAtivas)

	boolean[] maosAtivas; 		// Indica se a i-esima mao esta ativa (true)
	boolean[] maosQuebradas;	// Indica se a i-esima mao esta quebrada (true). Implica finalizada
	boolean[] maosFinalizadas;	// Indica se a i-esima mao esta finalizada (true)

	// CONSTRUTOR
	Participante(int numMaosMax) {
		this.numMaosMax = numMaosMax;

		// Cria maos
		mao = new ArrayList<Mao>();
		for (int i = 0; i < numMaosMax; i++)
			mao.add(new Mao());

		// Inicializa vetores de estado das maos
		maosAtivas = new boolean[numMaosMax];
		maosQuebradas = new boolean[numMaosMax];
		maosFinalizadas = new boolean[numMaosMax];
		for (int i = 0; i < numMaosMax; i++)
			maosAtivas[i] = maosQuebradas[i] = maosFinalizadas[i] = false;
		ativaMao(0);
	}

	////////////////////
	// METODOS DE CLASSE
	/**
	 * Valida se valor a ser apostado eh maior ou igual a aposta minima.
	 * 
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
	 *         por 1; JKQ, por 10.
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
	 * 
	 * @return -1 se tiverem valor diferente. Se tiverem mesmo valor, retorna o
	 *         valor. Ases tem valor 1, JKG tem valor 10.
	 */
	static int verificaCartasMesmoValor(Carta carta1, Carta carta2) {
		int mapeamento = mapeamentoAux(carta1);

		if (mapeamento == mapeamentoAux(carta2))
			return mapeamento;
		return -1;
	}

	/**
	 * Indica se a mao indiceMao de jogador vence da mao do dealer.
	 * 
	 * @param asesSplitFlag indica se ocorreu um split de ases. Nesse caso, ainda
	 *                      que faca 21 pontos com duas cartas, o jogador nao tera
	 *                      um Blackjack.
	 * @return 0, se houver empate; um inteiro maior que zero, caso o jogador venca;
	 *         um inteiro menor que zero, caso o dealer venca.
	 */
	static int verificaVencedor(Jogador jogador, int indiceMao, int pontosDealer) {
		int pontosJogador = jogador.verificaBlackjack(indiceMao) + jogador.mao.get(indiceMao).calculaPontosMao();

		return pontosJogador - pontosDealer;
	}

	///////////////////////
	// METODOS DE INSTANCIA

	//////////
	/* GETS */

	int getNumCartas(int indiceMao) {
		return mao.get(indiceMao).getNumCartas();
	}

	int getNumMaosAtivas() {
		return numMaosAtivas;
	}

	int getNumMaosFinalizadas() {
		return numMaosFinalizadas;
	}
	
	/**
	 * Retorna pontos da mao indiceMao.
	 * 
	 * @return valor de pontos.
	 */
	int getPontos(int indiceMao) {
		return mao.get(indiceMao).calculaPontosMao();
	}

	//////////////////
	/* VERIFICACOES */

	/**
	 * Indica se dada mao eh um Blackjack.
	 * 
	 * @return 1, se for Blackjack. Do contrario, 0.
	 */
	int verificaBlackjack(int indiceMao) {
		if (mao.get(indiceMao).getNumCartas() == 2 && mao.get(indiceMao).calculaPontosMao() == 21)
			return 1;
		return 0;
	}

	/**
	 * Verifica se mao indiceMao esta ativa.
	 * 
	 * @return true, se ativa; false, do contrario.
	 */
	boolean verificaMaoAtiva(int indiceMao) {
		return maosAtivas[indiceMao] == true;
	}

	/**
	 * Verifica se mao indiceMao ja finalizou turno.
	 * 
	 * @return true, se ja finalizou; false, do contrario.
	 */
	boolean verificaMaoFinalizada(int indiceMao) {
		return maosFinalizadas[indiceMao];
	}

	/**
	 * Verifica se mao indiceMao ja quebrou.
	 * 
	 * @return true, se ja quebrada; false, do contrario.
	 */
	boolean verificaMaoQuebrada(int indiceMao) {
		return maosQuebradas[indiceMao];
	}

	/**
	 * Verifica se as duas primeiras cartas da mao de indice 0 tem mesmo valor.
	 * 
	 * @return true, se possuem mesmo valor; false, do contrario.
	 */
	boolean verificaPrimDuasCartasMesmoValor() {
		return Participante.verificaCartasMesmoValor(mao.get(0).cartas.get(0), mao.get(0).cartas.get(1)) != -1;
	}

	/**
	 * Verifica se mao indiceMao supera 21 pontos (quebra).
	 * 
	 * @return true, se quebrada; false, do contrario.
	 */
	boolean verificaQuebra(int indiceMao) {
		return getPontos(indiceMao) > 21;
	}

	///////////
	/* ACOES */

	/**
	 * Define que mao de indiceMao esta em jogo. Incrementa contador de numero de
	 * maos ativas.
	 */
	void ativaMao(int indiceMao) {
		if (maosAtivas[indiceMao] == false) {
			maosAtivas[indiceMao] = true;
			numMaosAtivas++;
		}
	}

	/**
	 * Adiciona uma carta a mao de indiceMao.
	 */
	void hit(int indiceMao) {
		// Logging para depuracao
		System.out.println("HIT");

		Carta novaCarta = FonteCarta.compraCarta();
		mao.get(indiceMao).insere(novaCarta);
	}

	/**
	 * Define que mao de indiceMao quebrou. Ativa {@code stand} para a mesma mao.
	 */
	void quebraMao(int indiceMao) {
		maosQuebradas[indiceMao] = true;
		stand(indiceMao);
	}

	/**
	 * Define que mao de indiceMao finalizou seu turno. Incrementa contador de
	 * numero de maos finalizadas.
	 */
	void stand(int indiceMao) {
		maosFinalizadas[indiceMao] = true;
		numMaosFinalizadas++;
	}

	/////////////////////
	/* REINICIALIZACAO */

	/**
	 * Reinicializa maos do participante e suas variaveis de estado das maos.
	 * Reativa apenas a mao de indice 0.
	 */
	void limpa() {
		// Logging para depuracao
		System.out.println("LIMPA");

		// Limpa cartas das maos
		for (int i = 0; i < numMaosMax; i++)
			mao.get(i).limpaMao();

		// Limpa variaveis de estado
		for (int i = 0; i < numMaosMax; i++)
			maosAtivas[i] = maosFinalizadas[i] = maosQuebradas[i] = false;
		numMaosAtivas = numMaosFinalizadas = 0;

		// Reinicializa apenas a primeira mao
		ativaMao(0);
	}
}
