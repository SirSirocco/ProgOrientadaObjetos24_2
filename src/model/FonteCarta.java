package model;

import java.util.*;

public class FonteCarta {
	final int numBaralhos = 8;
	int contCartas;
	private List<Carta> fonte, copiaOrdenada;
	
	public FonteCarta() {
		fonte = new ArrayList<>();
		copiaOrdenada = new ArrayList<>();
		String[] naipes = {"Paus", "Ouros", "Espadas", "Copas"};
		String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}; 
		
		for (String naipe: naipes) {
			for (String valor: valores) {
				fonte.add(new Carta(naipe, valor));
				copiaOrdenada.add(new Carta(naipe, valor));
			}
		}
		
	}
	
	public boolean checaEmbaralha() {
		return !fonte.equals(copiaOrdenada);
	}
	
	public void embaralha() {
		Collections.shuffle(fonte);
	}
	
	public Carta compraCarta() {
		return fonte.remove(0);
	}

}
