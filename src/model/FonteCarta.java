package model;

import java.util.*;

class FonteCarta {
	private static final int numBaralhos = 8;
	private static final double limiteEmbaralha = 0.1;
	private static int contCartas = 0;
	private static List<Carta> fonte;

	static {
		fonte = new ArrayList<>();
		String[] naipes = { "Paus", "Ouros", "Espadas", "Copas" };
		String[] valores = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

		for (int i = 0; i < numBaralhos; i++) {
			for (String naipe : naipes) {
				for (String valor : valores) {
					fonte.add(new Carta(naipe, valor));
				}
			}
		}

	}

	/**
	 * Checa se o baralho precisa embaralhar.
	 * 
	 * @return True caso preciso embaralhar, falso caso contrário.
	 */
	static boolean checaEmbaralha() {
		System.out.println("EMBARALHA"); ////////////////////////
		return contCartas >= numBaralhos * 52 * limiteEmbaralha;
	}

	/**
	 * Embaralha todos os baralhos.
	 */
	static void embaralha() {
		Collections.shuffle(fonte);
		contCartas = 0; // Reinicia contador de cartas
	}
	
	/**
	 * Retira uma carta do início da fila e coloca no final.
	 * 
	 * @return Carta retirada.
	 */
	static Carta compraCarta() {
		Carta cartaRetorno = fonte.remove(0);
		contCartas++;
		fonte.addLast(cartaRetorno);
		return cartaRetorno;
	}
}
