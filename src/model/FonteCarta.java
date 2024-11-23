package model;

import java.util.*;

/**
 * Classe estatica que implementa a fonte de cartas do jogo, isto eh, o conjunto
 * de baralhos embaralhados de onde serao extraidas as cartas.
 * 
 * @since 23-11-2024
 * @author Joao Marcello Amaral
 */
class FonteCarta {
	private static final int numBaralhos = 8; // Numero de baralhos de 52 cartas que compoem a fonte

	/**
	 * Razao entre cartas compradas e cartas totais a partir da qual a fonte devera
	 * ser embaralhada.
	 */
	private static final double limiteEmbaralha = 0.1;

	private static int contCartas = 0; // Contador de cartas compradas
	private static List<Carta> fonte;

	// Bloco estatico para inicializacao da fonte de cartas
	static {
		String[] naipes = { "Paus", "Ouros", "Espadas", "Copas" };
		String[] valores = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
		fonte = new ArrayList<Carta>();

		for (int i = 0; i < numBaralhos; i++) {
			for (String naipe : naipes) {
				for (String valor : valores) {
					fonte.add(new Carta(naipe, valor));
				}
			}
		}

		embaralha();
	}

	/**
	 * Checa se o baralho precisa embaralhar.
	 * 
	 * @return true caso precise embaralhar, false caso contrario.
	 */
	static boolean checaEmbaralha() {
		return contCartas >= (int) (numBaralhos * 52 * limiteEmbaralha);
	}

	/**
	 * Embaralha todos os baralhos.
	 */
	static void embaralha() {
		// Logging para depuracao
		// System.out.println("EMBARALHA");

		Collections.shuffle(fonte);
		contCartas = 0; // Reinicia contador de cartas
	}

	/**
	 * Retira uma carta do inicio da fila e coloca no final.
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
