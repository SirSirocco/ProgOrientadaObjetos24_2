package model;

import java.util.*;

class FonteCarta {
	final int numBaralhos = 8;
	final double limiteEmbaralha = 0.1;
	int contCartas = 0;
	List<Carta> fonte;

	public FonteCarta() {
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
	 * Checa se o baralho precisa embaralhar
	 * 
	 * @return Verdadeiro ou falso
	 */
	public boolean checaEmbaralha() {
		return contCartas >= numBaralhos * 52 * limiteEmbaralha;
	}

	/**
	 * Embaralha todos os baralhos
	 */
	public void embaralha() {
		Collections.shuffle(fonte);
	}

	/**
	 * Retira uma carta do início da fila e coloca no final
	 * @return Carta retirada
	 */
	public Carta compraCarta() {
		Carta cartaRetorno = fonte.remove(0);
		contCartas++;
		fonte.addLast(cartaRetorno);
		return cartaRetorno;
	}

}
