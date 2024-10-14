package model;

class Jogador extends Participante {
	// VARIAVEIS DE CLASSE
	private static final int maosMaxJogador = 2; // Numero maximo de maos do jogador
	private static final int balancoInicial = 2400;

	// VARIAVEIS DE INSTANCIA
	private int balanco = balancoInicial;
	private int[] apostaMao; // Apostas em cada mao

	// CONSTRUTOR
	Jogador() {
		super(maosMaxJogador);
		Participante.jogador.add(this); // Adiciona jogador atual a lista de jogadores
		apostaMao = new int[maosMaxJogador];
		limpaApostas();
	}

	// METODOS DE INSTANCIA
	int getBalanco() {
		return balanco;
	}
	
	int[] getApostas() {
		return apostaMao;
	}

	/**
	 * @return Retorna true, se valor menor ou igual ao balanco. Do contrario, false.
	 */
	boolean validaBalanco(int valor) {
		return valor <= balanco;
	}

	/**
	 * Supoe que dealer nao possua Blackjack. Recupera metade do valor apostado.
	 * Valido apenas para mao de indice 0.
	 * @return Retorna metade de valor (arredonda para baixo).
	 */
	void surrender() {
		balanco += apostaMao[0] / 2;
	}

	/**
	 * Reinicializa com zero vetor de apostas.
	 */
	void limpaApostas() {
		for (int i = 0; i < maosMaxJogador; i++) {
			apostaMao[i] = 0;
		}
	}

	/**
	 * Incrementa aposta da mao indMao com valor.
	 * 
	 * @return Se aposta valida, retorna true. Do contrario, false.
	 */
	boolean aposta(int valor, int indMao) {
		boolean valido = Participante.validaAposta(valor) && validaBalanco(valor);

		if (valido) {
			balanco -= valor;
			apostaMao[indMao] += valor; // Fazemos += para reutilizarmos com double_
		}

		return valido;
	}

	/**
	 * Dobra aposta da mao indMao.
	 * @return Se aposta valida, retorna true. Do contrario, false.
	 */
	boolean double_(int indMao) {
		return aposta(apostaMao[indMao], indMao);
	}

/* A SER IMPLEMENTADO
	void split() {		
	}
*/
}

package model;

class Jogador extends Participante {
	// VARIAVEIS DE CLASSE
	private static final int maosMaxJogador = 2; // Numero maximo de maos do jogador
	private static final int balancoInicial = 2400;

	// VARIAVEIS DE INSTANCIA
	private int balanco = balancoInicial;
	private int[] apostaMao; // Apostas em cada mao

	// CONSTRUTOR
	Jogador() {
		super(maosMaxJogador);
		Participante.jogador.add(this); // Adiciona jogador atual a lista de jogadores
		apostaMao = new int[maosMaxJogador];
		limpaApostas();
	}

	// METODOS DE INSTANCIA
	int getBalanco() {
		return balanco;
	}
	
	int[] getApostas() {
		return apostaMao;
	}

	/**
	 * @return Retorna true, se valor menor ou igual ao balanco. Do contrario, false.
	 */
	boolean validaBalanco(int valor) {
		return valor <= balanco;
	}

	/**
	 * Supoe que dealer nao possua Blackjack. Recupera metade do valor apostado.
	 * Valido apenas para mao de indice 0.
	 * @return Retorna metade de valor (arredonda para baixo).
	 */
	void surrender() {
		balanco += apostaMao[0] / 2;
	}

	/**
	 * Reinicializa com zero vetor de apostas.
	 */
	void limpaApostas() {
		for (int i = 0; i < maosMaxJogador; i++) {
			apostaMao[i] = 0;
		}
	}

	/**
	 * Incrementa aposta da mao indMao com valor.
	 * 
	 * @return Se aposta valida, retorna true. Do contrario, false.
	 */
	boolean aposta(int valor, int indMao) {
		boolean valido = Participante.validaAposta(valor) && validaBalanco(valor);

		if (valido) {
			balanco -= valor;
			apostaMao[indMao] += valor; // Fazemos += para reutilizarmos com double_
		}

		return valido;
	}

	/**
	 * Dobra aposta da mao indMao.
	 * @return Se aposta valida, retorna true. Do contrario, false.
	 */
	boolean double_(int indMao) {
		return aposta(apostaMao[indMao], indMao);
	}

/* A SER IMPLEMENTADO
	void split() {		
	}
*/
}
