package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe singleton que implementa o padrao facade, de maneira a realizar a
 * interacao entre o Model e o Control. Com esta implementacao, reduz-se o
 * acoplamento entre esses dois pacotes. Por isso, deve ser publica.
 * 
 * @since 23-11-2024
 * @author Pedro Barizon
 */
public class FachadaModel {
	// CONSTANTES DE CLASSE
	private final int numJogadores = 1, numCartasNovaRodada = 2;

	// VARIAVEIS DE INSTANCIA
	Dealer dealer; 				// Objeto dealer a ser criado
	List<Jogador> jogadores; 	// Lista de jogadores a serem criados

	// SINGLETON
	private static FachadaModel fachada = null;

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

	///////////////////////
	// VARIAVEIS DA FACHADA

	///////////
	/* GERAL */

	public int getHitUntil() {
		return dealer.hitUntil;
	}

	public int getNumCartasNovaRodada() {
		return numCartasNovaRodada;
	}

	/////////////
	/* JOGADOR */
	public int getJogadoresNum() {
		return numJogadores;
	}

	public int getJogadoresMaosMax() {
		return Jogador.maosMaxJogador;
	}

	/////////////////////
	// METODOS DA FACHADA

	///////////
	/* GERAL */

	/**
	 * Verifica se fonte de cartas deve ser embaralhada e, se necessario, embaralha.
	 */
	public void embaralhaFonte() {
		if (FonteCarta.checaEmbaralha() == true)
			FonteCarta.embaralha();
	}

	////////////
	/* DEALER */

	////////////
	/** GETS **/

	/**
	 * Obtem cartas da mao do dealer.
	 * 
	 * @return Um array de arrays de dois elementos da forma (naipe, valor).
	 */
	public ArrayList<ArrayList<String>> dealerGetCartas() {
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		List<Carta> cartas = dealer.mao.get(0).cartas;

		for (int i = 0; i < cartas.size(); i++) {
			ArrayList<String> linha = new ArrayList<String>();
			linha.add(cartas.get(i).getNaipe());
			linha.add(cartas.get(i).getValor());
			resultado.add(linha);
		}

		return resultado;
	}

	public int dealerGetPontos() {
		return dealer.getPontos(0);
	}

	////////////
	/** SETS **/

	/**
	 * Define mao do dealer a partir do array de cartas fornecido.
	 * 
	 * @param cartas Um array de arrays de dois elementos da forma (naipe, valor).
	 */
	public void dealerSetMao(ArrayList<ArrayList<String>> cartas) {
		dealer.mao.get(0).limpaMao();

		for (ArrayList<String> carta : cartas) {
			dealer.mao.get(0).insere(new Carta(carta.get(0), carta.get(1)));
		}
	}

	////////////////////
	/** VERIFICACOES **/

	public boolean dealerVerificaBlackjack() {
		return dealer.verificaBlackjack(0) == 1;
	}

	/////////////
	/** ACOES **/
	public void dealerHit() {
		dealer.hit(0);
	}

	public boolean dealerQuebra() {
		boolean quebra = dealer.verificaQuebra(0);

		if (quebra && !dealer.verificaMaoQuebrada(0))
			dealer.quebraMao(0);

		return quebra;
	}

	/////////////
	/* JOGADOR */
	/*
	 * As funcoes de jogador foram projetadas considerando uma futura expansao do
	 * jogo para mais de um jogador. Por isso, passamos o indice de Jogador
	 * (indiceJ) como parametro das funcoes abaixo.
	 */

	////////////
	/** GETS **/

	public int jogadorGetAposta(int indiceJ, int indiceMao) {
		return jogadores.get(indiceJ).getAposta(indiceMao);
	}

	public int jogadorGetBalanco(int indiceJ) {
		return jogadores.get(indiceJ).getBalanco();
	}

	/**
	 * Obtem cartas da mao indiceMao do jogador indiceJ.
	 * 
	 * @return Um array de arrays de dois elementos da forma (naipe, valor).
	 */
	public ArrayList<ArrayList<String>> getCartasJogador(int indiceJ, int indiceMao) {
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		List<Carta> cartas = jogadores.get(indiceJ).mao.get(indiceMao).cartas;

		for (int i = 0; i < cartas.size(); i++) {
			ArrayList<String> linha = new ArrayList<String>();
			linha.add(cartas.get(i).getNaipe());
			linha.add(cartas.get(i).getValor());
			resultado.add(linha);
		}

		return resultado;
	}

	public int jogadorGetNumCartas(int indiceJ, int indiceMao) {
		return jogadores.get(indiceJ).getNumCartas(indiceMao);
	}

	public int jogadorGetNumMaosAtivas(int indiceJ) {
		return jogadores.get(indiceJ).getNumMaosAtivas();
	}

	public int jogadorGetNumMaosFinalizadas(int indiceJ) {
		return jogadores.get(indiceJ).getNumMaosFinalizadas();
	}

	public int jogadorGetPontos(int indiceJ, int indiceMao) {
		return jogadores.get(indiceJ).getPontos(indiceMao);
	}

	////////////
	/** SETS **/

	public void jogadorSetApostaMao(int indiceJ, int indiceMao, int valor) {
		jogadores.get(indiceJ).setApostaMao(indiceMao, valor);
	}

	public void jogadorSetBalanco(int indiceJ, int valor) {
		jogadores.get(indiceJ).setBalanco(valor);
	}

	/**
	 * Define mao indiceMao do jogador indiceJ a partir do array de cartas
	 * fornecido.
	 * 
	 * @param cartas Um array de arrays de dois elementos da forma (naipe, valor).
	 */
	public void jogadorSetMao(int indiceJ, int indiceMao, ArrayList<ArrayList<String>> cartas) {
		Mao mao = jogadores.get(indiceJ).mao.get(indiceMao);
		mao.limpaMao();
		for (ArrayList<String> carta : cartas) {
			mao.insere(new Carta(carta.get(0), carta.get(1)));
		}
	}

	public void jogadorSetNumMaosAtivas(int indiceMao, int numMaos) {
		for (int i = 0; i < numMaos; i++) {
			jogadores.get(indiceMao).ativaMao(i);
		}
	}

	////////////////////
	/** VERIFICACOES **/

	/////////////////
	/*** APOSTAS ***/
	public boolean jogadorVerificaApostaInicial(int indiceJ) {
		return jogadores.get(indiceJ).verificaApostaInicial();
	}

	public boolean jogadorVerificaDobraAposta(int indiceJ) {
		return jogadores.get(indiceJ).verificaDobraAposta();
	}

	/////////////////
	/*** BALANCO ***/
	public boolean jogadorVerificaBalancoMinimo(int indiceJ) {
		return jogadores.get(indiceJ).verificaBalancoMinimo();
	}

	///////////////////////
	/*** MAOS E CARTAS ***/
	public boolean jogadorVerificaBlackjack(int indiceJ, int indiceMao) {
		return jogadores.get(indiceJ).verificaBlackjack(indiceMao) == 1;
	}

	public boolean jogadorVerificaMaoAtiva(int indiceJ, int indiceMao) {
		return jogadores.get(indiceJ).verificaMaoAtiva(indiceMao);
	}

	public boolean jogadorVerificaMaoFinalizada(int indiceJ, int indiceMao) {
		return jogadores.get(indiceJ).verificaMaoFinalizada(indiceMao);
	}

	public boolean jogadorVerificaMaoQuebrada(int indiceJ, int indiceMao) {
		return jogadores.get(indiceJ).verificaMaoQuebrada(indiceMao);
	}

	public boolean jogadorVerificaPrimCartasMesmoValor(int indiceJ) {
		return jogadores.get(indiceJ).verificaPrimDuasCartasMesmoValor();
	}

	public void jogadorVerificaSplitAses(int indiceJ) {
		if (jogadores.get(indiceJ).verificaSplitAses())
			jogadores.get(indiceJ).setSplitAses(true);
	}

	public int jogadorVerificaVitoria(int indiceJ, int indiceMao) {
		if (jogadores.get(indiceJ).verificaQuebra(indiceMao) == true)
			return -1;

		return Participante.verificaVencedor(jogadores.get(indiceJ), indiceMao,
				dealer.getPontos(0) + dealer.verificaBlackjack(0));
	}

	/////////////
	/** ACOES **/

	////////////////
	/*** APOSTA ***/

	public boolean jogadorIncrementaAposta(int indiceJ, int indiceMao, int valor) {
		return jogadores.get(indiceJ).aposta(indiceMao, valor);
	}

	public void jogadorRecuperaAposta(int indiceJ, int indiceMao) {
		jogadores.get(indiceJ).recuperaAposta(indiceMao);
	}

	public void jogadorVenceAposta(int indiceJ, int indiceMao) {
		jogadores.get(indiceJ).venceAposta(indiceMao);
	}

	///////////////////////////
	/*** INTERFACE GRAFICA ***/

	public void jogadorDouble(int indiceJ) {
		jogadores.get(indiceJ).double_(0);
	}

	public void jogadorHit(int indiceJ, int indiceMao) {
		jogadores.get(indiceJ).hit(indiceMao);
	}

	public boolean jogadorQuebra(int indiceJ, int indiceMao) {
		boolean quebra = jogadores.get(indiceJ).verificaQuebra(indiceMao);

		if (quebra && !jogadores.get(indiceJ).verificaMaoQuebrada(indiceMao))
			jogadores.get(indiceJ).quebraMao(indiceMao);

		return quebra;
	}

	public void jogadorSplit(int indiceJ) {
		jogadores.get(indiceJ).split();
	}

	public void jogadorStand(int indiceJ, int indiceMao) {
		jogadores.get(indiceJ).stand(indiceMao);
	}

	public void jogadorSurrender(int indiceJ) {
		jogadores.get(indiceJ).surrender();
	}

	public void jogadorClear(int indiceJ) {
		jogadores.get(indiceJ).clear();
	}

	/////////////////////
	/* REINICIALIZACAO */

	public void limpaParticipantes() {
		dealer.limpa();
		for (Jogador jogador : jogadores)
			jogador.limpa();
	}
}
