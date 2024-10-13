package model;

import java.util.List;

abstract class Participante {
	static final int apostaMin = 50; 	// Valor minimo a ser apostado
	static Dealer dealer;				// Banca (dealer)
	static List<Jogador> jogador;		// Lista de jogadores ativos

	int numMaosMax,						// Numero maximo de maos
		numMaosAtivas;					// Numero de maos ativas
	List<Mao> mao;						// Maos do participante

	Participante(int numMaos) {
		numMaosMax = numMaos;
		numMaosAtivas = 1;
		
		// Falta colocar a criacao das maos
	}
	
	// OK
	static boolean validaAposta(int valor) {
		return valor >= apostaMin;
	}
	
//	static boolean possuiBlackjack(int indMao) {
//		String as = "A", dezes = "JKQ";
//		
//		/*
//		 Um participante tera blackjack se possuir duas cartas
//		 e seu somatorio corresponder a 21. Para isso, deve possuir
//		 um as e uma carta com valor de 10 pontos, isto eh, "J", "K" ou "Q".
//		 Isso explica o uso de contains.
//		*/
//		
//		if (mao[indMao].length == 2 &&
//				(
//						(
//								as.contains([indMao][0].getValor()) &&
//								dezes.contains([indMao][1].getValor()
//						) ||
//						(
//								as.contains([indMao][1].getValor()) &&
//								dezes.contains([indMao][0].getValor()
//						)
//				)
//			) {
//			return true;
//		}
//		
//		return false;
//	}
}
