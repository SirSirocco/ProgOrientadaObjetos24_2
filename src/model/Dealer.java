package model;

/**
 * Classe singleton que implementa o dealer, que representa a banca contra a
 * qual o(s) jogador(es) jogara(ao).
 * 
 * @since 23-11-2024
 * @author Pedro Barizon
 */
class Dealer extends Participante {
	// CONSTANTES DE CLASSE
	static final int maosMaxDealer = 1; // Numero maximo de maos do dealer
	
	// CONSTANTES DE INSTANCIA
	final int hitUntil = 17;			// Numero de pontos a partir do qual o dealer para de comprar cartas

	// SINGLETON
	private static Dealer dealer = null;

	private Dealer() {
		super(maosMaxDealer);
	}

	static Dealer getDealer() {
		if (dealer == null)
			dealer = new Dealer();

		return dealer;
	}
}
