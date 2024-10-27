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
	
	public static FachadaModel getFachada() {
		if (fachada == null)
			fachada = new FachadaModel();
		
		return fachada;
	}
	
//	Dealer getDealer() {
//		return dealer;
//	}
//	
//	Jogador getJogador(int index) {
//		return jogadores.get(index);
//	}
	
	public boolean dealerPossuiBlackjack() {
		return dealer.possuiBlackjack(0, false) == 1;
	}
	
	public boolean jogadorPossuiBlackjack(int indexJ, int indexMao, boolean asesSplitFlag) {
		return jogadores.get(indexJ).possuiBlackjack(indexMao, asesSplitFlag) == 1;
	}
	
	public int dealerCalculaPontos() {
		return dealer.calculaPontos(0);
	}
	
	public int jogadorCalculaPontos(int indexJ, int indexMao) {
		return jogadores.get(indexJ).calculaPontos(indexMao);
	}
	
	public void dealerHit() {
		dealer.hit(0);
	}
	
	public void jogadorHit(int indexJ, int indexMao) {
		jogadores.get(indexJ).hit(indexMao);
	}
	
	public void dealerCompraCarta() {
		dealer.hit(0);
	}
	
	public int balancoJogador(int ind) {
		return jogadores.get(ind).getBalanco();
	}
	
	public ArrayList<ArrayList<String>> getCartasDealer() {
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		List<Carta> cartas = dealer.mao.get(0).cartas;

		for (int i = 0; i < cartas.size(); i++) {
			ArrayList<String> linha = new ArrayList<>();
			linha.add(cartas.get(i).getNaipe());
			linha.add(cartas.get(i).getValor());
			result.add(linha);
		}
		
		return result;
	}
	
	public int valorCartasDealer() {
		return dealer.calculaPontos(0);
	}
	
	public void embaralhaFonte() {
		FonteCarta.embaralha();
	}
}
