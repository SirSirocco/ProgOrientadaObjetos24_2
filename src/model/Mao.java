package model;

import java.util.*;

/**
 * Classe que implementa as maos de cartas dos participantes do jogo.
 * 
 * @since 23-11-2023
 * @author Joao Marcello Amaral
 */
class Mao {
	int numCartas = 0;
	boolean temAs = false;
	List<Carta> cartas = new ArrayList<Carta>();

	void limpaMao() {
		cartas.clear();
		numCartas = 0;
		temAs = false;
	}

	void insere(Carta carta) {
		cartas.add(carta);
		numCartas++;
	}

	/**
	 * Calcula a pontuacao total da mao.
	 * 
	 * @return O valor da pontuacao.
	 */
	int calculaPontosMao() {
		int total = 0;

		for (Carta carta : cartas) {
			String valor = carta.getValor();

			// Usar contentEquals em vez de equals, para igualar os conteudos e nao as
			// referencias das strings
			if (valor.contentEquals("A")) {
				total += 1;
				temAs = true;
			} else if (valor.contentEquals("K") || valor.contentEquals("Q") || valor.contentEquals("J")) {
				total += 10;
			} else {
				total += Integer.parseInt(valor);
			}
		}

		if (temAs && total <= 11) { // "total <= 11" evita quebra de mao
			total += 10;
		}

		return total;
	}

	int getNumCartas() {
		return numCartas;
	}
}
