package model;

import java.util.*;

class Mao {
	int pontos = 0;
	int numCartas = 0;
	boolean temAs = false;
	List<Carta> cartas = new ArrayList<Carta>();

	/**
	 * Calcula a pontuação total da mão
	 * @return O valor da pontuação
	 */
	int calculaPontosMao() {
		int total = 0;

		for (Carta carta: cartas) {
			String valor = carta.getValor();
			
			if (valor.equals("A")){
				total += 11;
				temAs = true;
			}
			else if (valor.equals("K") || valor.equals("Q") || valor.equals("J")) {
				total += 10;
			}
			else {
				total += Integer.parseInt(valor);
			}
		}
		
		if (temAs && total <= 11) {
			total += 10;
		}
		
		return total;
	}
	
	int getNumCartas() {
		return numCartas;
	}
	
	void insere(Carta carta) {
		cartas.add(carta);
		numCartas++;
	}
	
	void limpaMao() {
		cartas.clear();
		numCartas = 0;
	}
}
