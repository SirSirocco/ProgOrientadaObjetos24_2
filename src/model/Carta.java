package model;

class Carta {
	private String naipe;
	private String valor;
	
	public Carta(String naipe, String valor) {
		this.naipe = naipe;
		this.valor = valor;
	}

	public String getNaipe() {
		return naipe;
	}
	
	public String getValor() {
		return valor;
	}
}
