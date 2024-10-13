package model;

class Jogador extends Participante {
	// VARIAVEIS DE CLASSE
	static final int maosMaxJogador = 2;
	static final int balancoInicial = 2400;
	
	// VARIAVEIS DE INSTANCIA
	int balanco = balancoInicial;
	int[] apostaMao; // Apostas em cada mao
	
	// CONSTRUTOR
	Jogador() {
		super(maosMaxJogador);
		Participante.jogador.add(this); // Adiciona jogador atual a lista de jogadores
		apostaMao = new int[maosMaxJogador];
		limpaApostas();
	}
	
	// METODOS DE INSTANCIA
	int getBalanco() { // OK
		return balanco;
	}

	/**
	 * Supoe que Dealer nao possui blackjack.
	 * Do contrario nao serah possivel fazer surrender.
	 * Surrender soh eh valido para primeira mao.
	 */
	boolean validaBalanco(int valor) { // OK
		return valor <= balanco;
	}
	
	/**
	 * Supoe que o Dealer nao possua Blackjack.
	 * Recupera metade do valor apostado.
	 * Se valor nao for divisivel por 2, retorna metade de (valor - 1).
	 */
	void surrender() { // OK
		balanco += apostaMao[0] / 2; // Recupera metade da aposta
	}
	
	void limpaApostas() {
		for (int i = 0; i < maosMaxJogador; i++) {
			apostaMao[i] = 0;
		}
	}
	
	/**
	 * Incrementa aposta da mao indMao com valor.
	 * Se aposta valida retorna true. De contrario, false.
	 */
	boolean aposta(int valor, int indMao) { // OK
		boolean valido = Participante.validaAposta(valor)
							&& validaBalanco(valor);
		
		if (valido) {
			balanco -= valor;
			apostaMao[indMao] += valor; // Fazemos += para reutilizarmos com double
		}
		
		return valido;
	}
	
	/**
	 * Dobra aposta da mao indMao.
	 * Se aposta for invalida, retorna false. Do contrario, true.
	 */
	boolean double_(int indMao) {
		return aposta(apostaMao[indMao], indMao);
	}
	
//	void split() {
//		
//	}
}
