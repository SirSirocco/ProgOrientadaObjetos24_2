package model;

class Dealer extends Participante {
	private static final int maosMaxDealer = 1; // Numero maximo de maos do dealer
	
	Dealer() {
		super(maosMaxDealer);
		Participante.dealer = this; // Preenche campo estatico de Participante com objeto dealer criado
	}
}
