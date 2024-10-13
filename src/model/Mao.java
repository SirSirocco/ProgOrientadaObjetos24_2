package model;

import java.util.*;

class Mao {
	int limiteCartas;
	List<Carta> cartas = new ArrayList<Carta>();
	int pontos;

	/**
	 * Calcula a pontuação total da mão
	 * @return O valor da pontuação
	 */
	int calcPontos() {
		int total = 0;
		int ases = 0;

		for (Carta carta: cartas) {
			String valor = carta.getValor();
			
			if (valor.equals("A")){
				total += 11;
				ases++;
			}
			else if (valor.equals("K") || valor.equals("Q") || valor.equals("J")) {
				total += 10;
			}
			else {
				total += Integer.parseInt(valor);
			}
		}
		
		while (total > 21 && ases > 0) {
			total -= 10;
			ases--;
		}
		
		return total;
	}
	
	void insere(Carta carta) {
		cartas.add(carta);
	}
	
	void limpa() {
		cartas.clear();
	}
}
