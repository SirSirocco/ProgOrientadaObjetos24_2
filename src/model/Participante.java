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
}
