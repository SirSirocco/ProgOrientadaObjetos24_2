package model;

/**
 * Classe que implementa as cartas do jogo.
 * 
 * @since 23-11-2023
 * @author Joao Marcello Amaral
 */
class Carta {
	private String naipe, valor;

	Carta(String naipe, String valor) {
		this.naipe = naipe;
		this.valor = valor;
	}

	String getNaipe() {
		return naipe;
	}

	String getValor() {
		return valor;
	}
}
