package model;

import java.util.List;
import java.util.ArrayList;

public class FachadaModel {
	static FachadaModel fachada = null;
	private final int numJogadores = 1;
	private final int cartasNovaRodada = 2;

	Dealer dealer; // Objeto dealer a ser criado
	List<Jogador> jogadores; // Lista de jogadores a serem criados

	///////////////////////////////
	// FACHADA
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

	///////////////////////////////
	// ELEMENTOS FACHADA
	public Dealer getDealer() {
		return dealer;
	}

	public Jogador getJogador(int index) {
		return jogadores.get(index);
	}

	public int getNumJogadores() {
		return numJogadores;
	}

	public int getMaosMax() {
		return Jogador.maosMaxJogador;
	}

	public int getHitUntil() {
		return dealer.hitUntil;
	}

	public int getCartasNovaRodada() {
		return cartasNovaRodada;
	}

	//////////////////////////////////
	// METODOS GERAIS

	/* Fonte de cartas */
	public void embaralhaFonte() {
		if (FonteCarta.checaEmbaralha() == true)
			FonteCarta.embaralha();
	}
	
	
	/* Jogadores e dealer */
	public void limpaParticipantes() {
		dealer.limpa();
		for (Jogador jogador : jogadores)
			jogador.limpa();
	}

	//////////////////////////////////
	// DEALER
	
	/* Gets */
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
	
	public int dealerCalculaPontos() {
		return dealer.calculaPontos(0);
	}
	
	
	/* Verificacoes*/
	
	public boolean dealerPossuiBlackjack() {
		return dealer.possuiBlackjack(0) == 1;
	}
	
	
	/* Acoes */
	public void dealerHit() {
		dealer.hit(0);
	}

	public boolean dealerQuebra() {
		boolean quebra = dealer.checaQuebra(0);

		if (quebra && !dealer.checaQuebrada(0))
			dealer.quebraMao(0);

		return quebra;
	}
	
	//////////////////////////////////
	// JOGADOR
	
	/* Gets */
	public int jogadorBalanco(int ind) {
		return jogadores.get(ind).getBalanco();
	}

	public int jogadorCalculaPontos(int indexJ, int indexMao) {
		return jogadores.get(indexJ).calculaPontos(indexMao);
	}
	
	
	/* Verificacoes */
	
	public boolean jogadorVerificaBalancoMinimo(int indexJ) {
		return jogadores.get(indexJ).verificaBalancoMinimo();
	}
	
	public boolean jogadorValidaApostaInicial(int indexJ) {
		return jogadores.get(indexJ).validaApostaInicial();
	}
	
	public boolean jogadorPossuiBlackjack(int indexJ, int indexMao) {
		return jogadores.get(indexJ).possuiBlackjack(indexMao) == 1;
	}
	
	public boolean jogadorMaoAtiva(int indexJ, int indexMao) {
		return jogadores.get(indexJ).checaMaoAtiva(indexMao);
	}
	
	public boolean jogadorQuebrado(int indexJ, int indexMao) {
		return jogadores.get(indexJ).checaQuebrada(indexMao);
	}
	
	public int jogadorVerificaVitoria(int indexJ, int indexMao) {
		if (jogadores.get(indexJ).checaQuebra(indexMao) == true)
			return -1;

		return Participante.verificaVencedor(jogadores.get(indexJ), indexMao, dealer.calculaPontos(0));
	}
	
	
	/* Acoes */
	public void jogadorHit(int indexJ, int indexMao) {
		jogadores.get(indexJ).hit(indexMao);
	}

	public boolean jogadorQuebra(int indexJ, int indexMao) {
		boolean quebra = jogadores.get(indexJ).checaQuebra(indexMao);

		if (quebra && !jogadores.get(indexJ).checaQuebrada(0))
			dealer.quebraMao(indexMao);

		return quebra;
	}
	
	/**
	 * 
	 * @param indexJ
	 * @param indexMao
	 * @param valor
	 * @return True, se incremento for menor que o balanco. False, do contrario.
	 */
	public boolean jogadorIncrementaAposta(int indexJ, int indexMao, int valor) {
		return jogadores.get(indexJ).aposta(indexMao, valor);
	}
	
	public void jogadorVenceAposta(int indexJ, int indexMao) {
		jogadores.get(indexJ).venceAposta(indexMao);
	}
	
	public void jogadorStand(int indexJ, int indexMao) {
		jogadores.get(indexJ).stand(indexMao);
	}
}
