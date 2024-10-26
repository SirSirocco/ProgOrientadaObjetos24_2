package model;

class Dealer extends Participante {
	private static final int maosMaxDealer = 1; // Numero maximo de maos do dealer
	static Dealer dealer = null;
	
	private Dealer() {
		super(maosMaxDealer);
	}
	
	static Dealer getDealer() {
		if (dealer == null)
			dealer = new Dealer();
		
		return dealer;
	}
}
