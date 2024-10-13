package model;

class Jogador extends Participante {
	static final int maosMaxJogador = 2;
	static final int balancoInicial = 2400;
	int balanco = balancoInicial;
	int[] apostaMao = { 0, 0 }; // Apostas em cada mao

	Jogador() {
		super(maosMaxJogador);
	}

	/**
	 * Supoe que Dealer nao possui blackjack.
	 * Do contrario nao serah possivel fazer surrender.
	 * Surrender soh eh valido para primeira mao.
	 */
	
	boolean aposta(int valor, int indMao) {
		boolean valido = Participante.validaAposta(Participante.apostaMin, valor)
							&& Participante.validaAposta(balanco, valor);
		
		if (valido) {
			balanco -= valor;
			apostaMao[indMao] = valor;
		}
		
		return valido;
	}
	
//	boolean surrender() {
//		
//		
//	}
//	
//	void double_() {
//		
//	}
//	
//	void split() {
//		
//	}
}
