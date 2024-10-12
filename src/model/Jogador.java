package model;

class Jogador extends Participante {
	static final int maosMaxJogador = 2;
	static final int balancoInicial = 2400;
	int balanco = balancoInicial;
	int[] apostaMao = { 0, 0 }; // Apostas em cada mao
	
	// METODOS DE CLASSE
	/**
	 * Indica se carta1 e carta2 possuem mesmo valor.
	 * @return true se tiverem mesmo valor, e false do contrario. 
	 */
	static boolean verificaCartasMesmoValor(Carta carta1, Carta carta2) {
		return mapeamentoAux(carta1) == mapeamentoAux(carta2);
	}
	
	/**
	 * Funcao auxiliar que mapeia os valores simbolicos das cartas
	 * em numeros inteiros.
	 * @return o valor numerico correspondente ao simbolo.
	 * Ases sao representados por 1; Reis, Rainhas e Valetes, por 10. 
	 */
	private static int mapeamentoAux(Carta carta) {
		String valor;
		
		switch((valor = carta.GetValor())) {
		case "A":
			return 1;
		case "J":
		case "K":
		case "Q":
			return 10;
		}
		
		return Integer.parseInt(valor);
	}
	
	// CONSTRUTOR
	Jogador() {
		super(maosMaxJogador);
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
