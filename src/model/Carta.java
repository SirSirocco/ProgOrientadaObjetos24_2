package model;

public class Carta {
	private String naipe;
	private String valor;
	
	public Carta(String naipe, String valor) {
		this.naipe = naipe;
		this.valor = valor;
	}

	public String GetNaipe() {
		return naipe;
	}
	
	public String GetValor() {
		return valor;
	}
}
