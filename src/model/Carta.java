package model;

public class Carta {
	private String naipe;
	private String valor;
	
	public Carta(String naipe, String valor) {
		this.naipe = naipe;
		this.valor = valor;
	}

	String GetNaipe() {
		return naipe;
	}
	
	String GetValor() {
		return valor;
	}
}
