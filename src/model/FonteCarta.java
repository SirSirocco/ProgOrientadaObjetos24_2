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

		// Teste fichas
		// fonte.add(new Carta("Copas", "10"));
		// fonte.add(new Carta("Ouros", "J"));
		//
		// fonte.add(new Carta("Copas", "7"));
		// fonte.add(new Carta("Espadas", "10"));
		// fonte.add(new Carta("Espadas", "5"));

		// Teste Dealer BJ
		// fonte.add(new Carta("Copas", "A"));
		// fonte.add(new Carta("Ouros", "K"));
		//
		// fonte.add(new Carta("Copas", "7"));
		// fonte.add(new Carta("Espadas", "10"));

		// Teste Jogador BJ(+ BJ > 21)
		// fonte.add(new Carta("Espadas", "5"));
		// fonte.add(new Carta("Copas", "6"));
		//
		// fonte.add(new Carta("Copas", "Q"));
		// fonte.add(new Carta("Espadas", "A"));
		//
		// fonte.add(new Carta("Copas", "10"));

		// Teste Double
		// fonte.add(new Carta("Espadas", "3"));
		// fonte.add(new Carta("Ouros", "2"));
		//
		// fonte.add(new Carta("Copas", "Q"));
		// fonte.add(new Carta("Espadas", "5"));
		//
		// fonte.add(new Carta("Paus", "6"));
		//
		// fonte.add(new Carta("Paus", "6"));
		// fonte.add(new Carta("Copas", "6"));

		////////////////
		// Split sem Hit

		// Vitoria dupla
		// fonte.add(new Carta("Espadas", "10"));
		// fonte.add(new Carta("Ouros", "7"));
		//
		// fonte.add(new Carta("Copas", "Q"));
		// fonte.add(new Carta("Espadas", "Q"));
		//
		// fonte.add(new Carta("Paus", "8"));
		// fonte.add(new Carta("Paus", "9"));

		// Vitoria em uma mao
		// fonte.add(new Carta("Espadas", "10"));
		// fonte.add(new Carta("Ouros", "9"));
		//
		// fonte.add(new Carta("Copas", "9"));
		// fonte.add(new Carta("Espadas", "9"));
		//
		// fonte.add(new Carta("Paus", "9"));
		// fonte.add(new Carta("Copas", "A"));

		// Derrota em ambas as maos (+ split ases)
		// fonte.add(new Carta("Copas", "J"));
		// fonte.add(new Carta("Paus", "A"));
		//
		// fonte.add(new Carta("Copas", "A"));
		// fonte.add(new Carta("Copas", "A"));
		//
		// fonte.add(new Carta("Paus", "10"));
		// fonte.add(new Carta("Copas", "K"));

		// Rendicao sem Hit
		// fonte.add(new Carta("Copas", "10"));
		// fonte.add(new Carta("Paus", "10"));
		//
		// fonte.add(new Carta("Copas", "9"));
		// fonte.add(new Carta("Ouros", "10"));

		////////////////
		// Split com Hit

		// Vitoria dupla
		// fonte.add(new Carta("Espadas", "10"));
		// fonte.add(new Carta("Ouros", "3"));
		//
		// fonte.add(new Carta("Copas", "Q"));
		// fonte.add(new Carta("Copas", "Q"));
		//
		// fonte.add(new Carta("Paus", "5"));
		// fonte.add(new Carta("Copas", "10"));
		//
		// fonte.add(new Carta("Paus", "6")); // Mao 0
		// fonte.add(new Carta("Paus", "A")); // Mao 1
		//
		// fonte.add(new Carta("Ouros", "5"));

		// Vitoria em uma mao
		// fonte.add(new Carta("Espadas", "10"));
		// fonte.add(new Carta("Ouros", "3"));
		//
		// fonte.add(new Carta("Copas", "Q"));
		// fonte.add(new Carta("Copas", "Q"));
		//
		// fonte.add(new Carta("Paus", "5"));
		// fonte.add(new Carta("Copas", "10"));
		//
		// fonte.add(new Carta("Paus", "3")); // Mao 0
		// fonte.add(new Carta("Paus", "A")); // Mao 1
		//
		// fonte.add(new Carta("Ouros", "5"));

		// Derrota em ambas as maos
		// fonte.add(new Carta("Espadas", "10"));
		// fonte.add(new Carta("Ouros", "3"));
		//
		// fonte.add(new Carta("Copas", "Q"));
		// fonte.add(new Carta("Copas", "Q"));
		//
		// fonte.add(new Carta("Paus", "5"));
		// fonte.add(new Carta("Copas", "8"));
		//
		// fonte.add(new Carta("Paus", "3")); // Mao 0
		// fonte.add(new Carta("Paus", "A")); // Mao 1
		//
		// fonte.add(new Carta("Ouros", "7"));

		// Rendicao com Hit
		// fonte.add(new Carta("Copas", "10"));
		// fonte.add(new Carta("Paus", "10"));
		//
		// fonte.add(new Carta("Copas", "4"));
		// fonte.add(new Carta("Ouros", "10"));
		//
		// fonte.add(new Carta("Ouros", "5"));

		// Salvamento
		// fonte.add(new Carta("Copas", "2"));
		// fonte.add(new Carta("Copas", "2"));
		//
		// fonte.add(new Carta("Copas", "A"));
		// fonte.add(new Carta("Copas", "A"));
		//
		// for (int i = 0; i < 42; i++)
		// fonte.add(new Carta("Copas", "A"));
		//
		// fonte.add(new Carta("Copas", "2"));
		// fonte.add(new Carta("Copas", "2"));
		// fonte.add(new Carta("Copas", "3"));
		// for (int i = 0; i < 6; i++)
		// fonte.add(new Carta("Copas", "A"));

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
