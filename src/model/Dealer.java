package model;

class Dealer extends Participante {
	static final int maosMaxDealer = 1;	// Numero maximo de maos do Dealer
	
	Dealer() {
		super(maosMaxDealer);
		Participante.dealer = this; // Define dealer;
	}
}
