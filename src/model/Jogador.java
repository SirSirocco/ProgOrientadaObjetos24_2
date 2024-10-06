package model;

class Jogador extends Participante {
	static final int maosMaxJogador = 2;
	static int[] valorFichas = { 10, 20, 50 };

	int[] qtdFichas;
	int balanco;

	Jogador() {
		super(maosMaxJogador);
		
		qtdFichas = new int[Jogador.valorFichas.length];
		qtdFichas[0] = 50;
		qtdFichas[1] = 20;
		qtdFichas[2] = 10;
		
		calcBalanco();
	}

	int calcBalanco() {
		balanco = 0;

		for (int i = 0; i < Jogador.valorFichas.length; i++)
			balanco += qtdFichas[i] * valorFichas[i];

		return balanco;
	}

	int atualizaBalanco(int[] qtd) {
		for (int i = 0; i < Jogador.valorFichas.length; i++)
			qtdFichas[i] += qtd[i];

		return calcBalanco();
	}

	boolean verificaBalancoSuficiente() {
		return balanco >= Participante.apostaMin;
	}
	
	void surrender() {
		
	}
	
	void double_() {
		
	}
	
	void split() {
		
	}
}
