package model;

import java.util.List;
import java.util.ArrayList;

public class FachadaModel {
	static FachadaModel fachada = null;
	static final int numJogadores = 1;
	
	Dealer dealer; 			 // Objeto dealer a ser criado
	List<Jogador> jogadores; // Lista de jogadores a serem criados
	
	private FachadaModel() {
		dealer = Dealer.getDealer();
		jogadores = new ArrayList<Jogador>();
		
		for (int i = 0; i < numJogadores; i++)
			jogadores.add(new Jogador());
	}
	
	static FachadaModel getFachada() {
		if (fachada == null)
			fachada = new FachadaModel();
		
		return fachada;
	}
	
	Dealer getDealer() {
		return dealer;
	}
	
	Jogador getJogador(int index) {
		return jogadores.get(index);
	}
}
