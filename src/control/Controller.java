package control;

import java.util.*;

import javax.swing.JOptionPane;

import model.FachadaModel;
import view.*;

class MecanicaJogo implements Runnable {
	@Override
	public void run() {
		Controller ctrl = Controller.getController();
		ctrl.painelJogo();
	}
}

public class Controller implements Observable {
	// SINGLETON
	static Controller ctrl = null;

	// FACADE
	private FachadaModel model = FachadaModel.getFachada();

	// OBSERVERS
	private List<Observer> lObserverBanca = new ArrayList<Observer>();
	private List<Observer> lObserverJogador = new ArrayList<Observer>();

	// JANELAS
	private JanelaInicial menu;
	private JanelaBanca janelaBanca;
	private List<JanelaJogador> janelaJogador = new ArrayList<JanelaJogador>(); // Adicionar indice de janela

	// Tabela de estados do fluxo de jogo
	private int estadoJogo; // -1 para banca e index para jogadores

	private final int JOGADOR = 0;
	private final int DEALER = -1;
	private final int DEALER_QUEBRA = -2;
	private final int CHECA_VENCEDOR = -3;
	private final int NOVA_RODADA = -4;

	// Tabela de estados do observable
	private int estadoObservable;

	public final int MUD_DEALER_MAO = -1;
	public final int MUD_JOGADOR_MAO = 100;
	public final int MUD_JOGADOR_APOSTA = 200;
	public final int MUD_JOGADOR_BALANCO = 300;

	// VARIAVEIS DE CONTROLE
	private int maoCorrente;
	private int numJogadores;
	private int maosMax;
	private int hitUntil;
	private int cartasNovaRodada;

	private int jogadorAtivo = 0;
	private boolean apostaOK = false;
	private boolean dealerBlackjack = false;
	private volatile boolean buttonsSwitch = true; // A flag compartilhada
	
	// INSTANCIACAO
	private Controller() {
	}

	/**
	 * Retorna referencia para o singleton. Cria instancia de controller se esta nao
	 * existir.
	 * 
	 * @return
	 */
	public static Controller getController() {
		if (ctrl == null)
			ctrl = new Controller();

		return ctrl;
	}

	///////////////////////////////
	// PADRAO OBSERVER
	public void addObserver(Observer o, char tipoEvento) {
		switch (tipoEvento) {
		case 'B':
			lObserverBanca.add(o);
			break;

		case 'J':
			lObserverJogador.add(o);
			break;
		}
	}

	public void removeObserver(Observer o, char tipoEvento) {
		switch (tipoEvento) {
		case 'B':
			lObserverBanca.remove(o);

		case 'J':
			lObserverJogador.remove(o);
			break;
		}
	}

	public int get() {
		return estadoObservable;
	}

	public void notificaEventoBanca() {
		for (Observer o : lObserverBanca)
			o.notify(this);
	}

	public void notificaEventoJogador() {
		for (Observer o : lObserverJogador)
			o.notify(this);
	}

	///////////////////////////////
	// MANIPULACAO DE JANELAS
	JanelaInicial criaMenu() {
		JanelaInicial janela = new JanelaInicial();

		janela.getBtnJogoNovo().addActionListener(new JogoNovo());
		janela.getBtnJogoSalvo().addActionListener(new JogoSalvo());

		return janela;
	}

	JanelaBanca criaJBanca() {
		JanelaBanca janela = new JanelaBanca();

		janela.getBtnSave().addActionListener(new SalvarJogo());

		return janela;
	}

	JanelaJogador criaJJogador(int idxMao) {
		JanelaJogador janela = new JanelaJogador(0, idxMao);
		return janela;
	}
	
	public void defineMaoCorrente(int indexMao) {
		maoCorrente = indexMao;
		System.out.println("MAO COORENTE " + maoCorrente);
		
		for (JanelaJogador j : janelaJogador) {
			j.toggleJanelaCor(maoCorrente);
		}
	}

	///////////////////////////////
	// ROTINAS DE CONTROLE DE JOGO
	void init() {
		/*
		 * Necessario criar janelas aqui em vez de no construtor, para evitar chamada
		 * recursiva de getController
		 */
		model = FachadaModel.getFachada();

		maosMax = model.getMaosMax();
		numJogadores = model.getNumJogadores();
		hitUntil = model.getHitUntil();
		cartasNovaRodada = model.getCartasNovaRodada();
		maoCorrente = 0;

		estadoJogo = NOVA_RODADA;
		apostaOK = false;

		menu = criaMenu();
		janelaBanca = criaJBanca();
		this.addObserver(janelaBanca, 'B');

		for (int i = 0; i < maosMax; i++) {
			janelaJogador.add(criaJJogador(i));
			this.addObserver(janelaJogador.get(i), 'J');
		}

		menu.setVisible(true);
		System.out.println("INIT");///////////////////////////
	}

	void initJanelas() {
		menu.setVisible(false);
		janelaBanca.setVisible(true);
		janelaJogador.get(0).setVisible(true);
	}

	void inicializaMecanicaJogo() {
		Thread tarefa = new Thread(new MecanicaJogo());
		tarefa.start();
	}

	void jogoNovo() {
		estadoJogo = NOVA_RODADA;
		initJanelas();
		inicializaMecanicaJogo();
	}

	void recuperaJogo() {
		// RECUPERA JOGO
		initJanelas();
		inicializaMecanicaJogo();
	}

	void salvaJogo() {
	}

	//////////////////////////////////////
	// PAINEL DO JOGO

	void painelJogo() {
		while (true) {
			switch (estadoJogo) {
			case NOVA_RODADA: // OK
				novaRodada();
				break;

			case DEALER: // OK
				dealerVez();
				break;

			case DEALER_QUEBRA: // OK
				dealerQuebra();
				break;

			case CHECA_VENCEDOR:
				checaVencedor();
				break;

			case JOGADOR:
			default:
				jogadorVez(estadoJogo);
				break;
			}
		}
	}

	///////////////////////////////
	// ETAPAS DO JOGO

	void novaRodada() { // OK
		System.out.println("NOVA RODADA EXECUTADA");
		if (model.jogadorVerificaBalancoMinimo(0) == false) // Se houver mais participantes, faca um for
		{
			JOptionPane.showMessageDialog(null, "Saldo insuficiente. Fim do Jogo");
//			System.out.println("Encerrando em dez segundos");
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			System.exit(0);
		}

		limpaParticipantes();
		model.embaralhaFonte();
		apostaOK = false;

		jogadorAtivo = 0;
		janelaJogador.get(1).setVisible(false);

		mudancaNovaRodada();
		estadoJogo = JOGADOR;
		System.out.println("FIM DE NOVA RODADA");
	}

	void compraInicialCartas() {
		for (int i = 0; i < cartasNovaRodada; i++)
			dealerHit();

		dealerBlackjack = model.dealerPossuiBlackjack();

		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < cartasNovaRodada; j++)
				jogadorHit(i, 0); // Jogadores compram duas cartas na mao de indice 0
		}
	}

	void jogadorVez(int indexJ) {
		buttonsSwitch = true;
		System.out.println("JOGADOR VEZ EXECUTADO");
		while (buttonsSwitch);
		System.out.println("FIM DE JOGADOR VEZ");
	}

	void dealerVez() // OK
	{
		System.out.println("DEALER VEZ EXECUTADO");
		if (dealerBlackjack) {
			estadoJogo = CHECA_VENCEDOR;
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("DEALER BLACKJACK RETURN"); // TODO JOptionPane
			return;
		}
		
		else if (model.dealerCalculaPontos() >= hitUntil) {
			System.out.println("BANCA NAO FARÁ HIT"); // TODO JOptionPane
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		while (model.dealerCalculaPontos() < hitUntil) {
			System.out.println("BANCA FEZ HIT"); // TODO JOptionPane
			dealerHit();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (model.dealerQuebra() == true) {
				estadoJogo = DEALER_QUEBRA;
				JOptionPane.showMessageDialog(null, "DEALER QUEBROU!!!"); // TODO JOptionPane
				return;
			}
		}
		
		estadoJogo = CHECA_VENCEDOR;
		System.out.println("FIM DE DEALER VEZ");
	}

	void dealerQuebra() // OK
	{
		String msg;
		System.out.println("DEALER QUEBRA EXECUTADO");
		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < maosMax; j++) {
				if (model.jogadorMaoAtiva(i, j) == true) {
					msg = String.format("Jogador %d | Mão %d: " , i + 1, j + 1);
					
					if (model.jogadorMaoQuebrada(i, j)) {
						jogadorRecuperaAposta(i, j);
						msg += "EMPATE\n";
					}
					else {
						jogadorVenceAposta(i, j);
						msg += "JOGADOR VENCE\n";
					}
					
					JOptionPane.showMessageDialog(null, msg);
				}
			}
		}
		
		estadoJogo = NOVA_RODADA;
		System.out.println("FIM DE DEALER QUEBRA");
	}

	void checaVencedor() { // OK
		String msg;
		int jogadorVencedorFlag;
		/* 
		 * maior que 0	-> Jogador vence
		 * igual a 0 	-> Empate
		 * menor que 0 	-> Dealer vence
		 */
		System.out.println("CHECA VENCEDOR EXECUTADO");
		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < maosMax; j++) {
				if (model.jogadorMaoAtiva(i, j) == true) {
					msg = String.format("Jogador %d | Mão %d: " , i + 1, j + 1);
					
					if ((jogadorVencedorFlag = model.jogadorVerificaVitoria(i, j)) > 0) {
						jogadorVenceAposta(i, j);
						msg += "JOGADOR VENCE\n";
					}
						
					else if (jogadorVencedorFlag == 0) {
						jogadorRecuperaAposta(i, j);
						msg += "EMPATE\n";
					}
					
					else {
						msg += "BANCA VENCE\n";
					}
					JOptionPane.showMessageDialog(null, msg);
				}
			}
		}
		
		estadoJogo = NOVA_RODADA;
		System.out.println("FIM DE CHECA VENCEDOR");
	}

	/////////////////////////////////////////////
	// GERAL

	void mudancaNovaRodada() {
		mudancaDealerMao();
		mudancaJogadorBalanco();

		for (int i = 0; i < maosMax; i++) {
			mudancaJogadorMao(i);
			mudancaJogadorAposta(i);
		}
	}

	void limpaParticipantes() {
		model.limpaParticipantes();
		mudancaNovaRodada();
	}

	//////////////////////////////////////////////
	// DEALER

	/* Gets */

	public int getDealerPontos() {
		return model.dealerCalculaPontos();
	}

	public ArrayList<ArrayList<String>> getDealerCartas() {
		return model.getCartasDealer();
	}

	void mudancaDealerMao() {
		estadoObservable = MUD_DEALER_MAO;
		notificaEventoBanca();
	}

	void dealerHit() {
		model.dealerHit();
		mudancaDealerMao();
	}

	//////////////////////////////////////////////
	// JOGADOR

	/* Gets */
	public int getJogadorAposta(int indexJ, int indexMao) {
		return model.jogadorAposta(indexJ, indexMao);
	}

	public int getJogadorBalanco(int indexJ) {
		return model.jogadorBalanco(indexJ);
	}

	public ArrayList<ArrayList<String>> getJogadorCartas(int indexJ, int indexMao) {
		return model.getCartasJogador(indexJ, indexMao);
	}

	public int getJogadorPontos(int indexJ, int indexMao) {
		return model.jogadorCalculaPontos(indexJ, indexMao);
	}

	/* MUDANCA */
	void mudancaJogadorBalanco() {
		estadoObservable = MUD_JOGADOR_BALANCO;
		notificaEventoJogador();
	}

	void mudancaJogadorAposta(int indexMao) {
		estadoObservable = MUD_JOGADOR_APOSTA + indexMao;
		notificaEventoJogador();
		estadoObservable = MUD_JOGADOR_BALANCO;
		notificaEventoJogador();
	}

	void mudancaJogadorMao(int indexMao) {
		estadoObservable = MUD_JOGADOR_MAO + indexMao;
		notificaEventoJogador();
	}

	/* APOSTA */
	public void jogadorIncrementaApostaInicial(int valor) { // OK
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		int status = 0;

		if (model.jogadorIncrementaAposta(jogadorAtivo, 0, valor) == false)
			status = 1;

		else if (apostaOK)
			status = 2;

		switch (status) {
		case 0:
			mudancaJogadorBalanco();
			mudancaJogadorAposta(0);
			System.out.println("Aposta inicial incrementada.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Aposta já realizada.");
			break;
		}
	}

	void jogadorDeal() { // OK
		apostaOK = true;
		compraInicialCartas();
		mudancaNovaRodada();
	}

	/**
	 * Deal eh sempre feito na mao de indice 0.
	 * 
	 * @param indexJ
	 */
	public void jogadorDealCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			System.out.println("Não está na vez do jogador.");
			return;
		}
		int status = 0;

		if (model.jogadorValidaApostaInicial(jogadorAtivo) == false)
			status = 1;

		else if (apostaOK)
			status = 2;

		switch (status) {
		case 0:
			jogadorDeal();
			System.out.println("Aposta realizada com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Valor abaixo da aposta minima."); // Depois gerar uma tela com a mensagem
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Aposta já realizada."); // Depois gerar uma tela com a mensagem
			break;
		}
	}

	void jogadorClear() { // OK
		model.jogadorClear(jogadorAtivo);
		mudancaJogadorAposta(0);
		mudancaJogadorBalanco();
	}

	public void jogadorClearCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		int status = 0;
		
		if (apostaOK)
			status = 1;
		
		switch(status) {
		case 0:
			jogadorClear();
			System.out.println("Clear realizado com sucesso.");
			break;
			
		case 1:
			JOptionPane.showMessageDialog(null, "Clear não permitido depois de aposta já realizada.");
			break;
		}
	}

	void jogadorVenceAposta(int indexJ, int indexMao) {
		model.jogadorVenceAposta(indexJ, indexMao);
		mudancaJogadorBalanco();
	}
			
	void jogadorRecuperaAposta(int indexJ, int indexMao) {
		model.jogadorRecuperaAposta(indexJ, indexMao);
		mudancaJogadorBalanco();
	}

	/* SURRENDER */
	void jogadorSurrender(int indexJ) { // OK
		model.jogadorSurrender(indexJ);
		mudancaJogadorBalanco();
	}

	public void jogadorSurrenderCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		
		int status = 0;

		if (apostaOK == false)
			status = 1;

		else if (model.jogadorNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorGetNumCartas(jogadorAtivo, 0) > 2)
			status = 3;

		else if (dealerBlackjack)
			status = 4;

		switch (status) {
		case 0:
			estadoJogo = NOVA_RODADA;
			jogadorSurrender(jogadorAtivo);
			buttonsSwitch = false;
			System.out.println("Surrender realizado com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Surrender permitido depois da aposta inicial."); // Depois gerar uma tela com a mensagem
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Surrender permitido apenas com uma mão."); // Depois gerar uma tela com a mensagem
			break;
		case 3:
			JOptionPane.showMessageDialog(null, "Surrender permitido apenas na mão inicial."); // Depois gerar uma tela com a mensagem
			break;

		case 4:
			JOptionPane.showMessageDialog(null, "Surrender não permitido, porque Dealer tem Blackjack.");
			break;
		}
	}

	/* HIT */
	void jogadorHit(int indexJ, int indexMao) {
		String msg;
		model.jogadorHit(indexJ, indexMao);
		mudancaJogadorMao(indexMao);

		if (model.jogadorQuebra(indexJ, indexMao) == true)
		{
			msg = "Mao " + (indexMao + 1) + " quebrou";
			JOptionPane.showMessageDialog(null, msg);
			
			// TODO extrair funcao
			if (model.jogadorNumMaosAtivas(indexJ) == model.jogadorNumMaosFinalizadas(indexJ))
				buttonsSwitch = false;
				estadoJogo = DEALER;
		}
	}

	public void jogadorHitCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		
		
		int status = 0;

		if (apostaOK == false)
			status = 1;

		else if (model.jogadorMaoQuebrada(jogadorAtivo, maoCorrente) == true)
			status = 2;

		else if (model.jogadorMaoFinalizada(jogadorAtivo, maoCorrente) == true)
			status = 3;

		switch (status) {
		case 0:
			jogadorHit(jogadorAtivo, maoCorrente);
			System.out.printf("Hit na mao %d realizado com sucesso.\n", maoCorrente + 1);
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Hit permitido depois da aposta inicial.");
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Hit não permitido para uma mão quebrada.");
			break;

		case 3:
			JOptionPane.showMessageDialog(null, "Hit não permitido depois do fim do turno.");
			break;
		}
	}

	/* STAND */
	void jogadorStand(int indexJ, int indexMao) {
		model.jogadorStand(indexJ, indexMao);
		
		if (model.jogadorNumMaosAtivas(indexJ) == model.jogadorNumMaosFinalizadas(indexJ))
		{
			buttonsSwitch = false;
			estadoJogo = DEALER;
		}
	}

	public void jogadorStandCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		String msg;
		int status = 0;
		
		if (apostaOK == false)
			status = 1;

		else if (model.jogadorMaoQuebrada(jogadorAtivo, maoCorrente) == true)
			status = 2;

		else if (model.jogadorMaoFinalizada(jogadorAtivo, maoCorrente) == true)
			status = 3;

		switch (status) {
		case 0:
			msg = "Stand na mao " + (maoCorrente + 1) + " realizado com sucesso.\n";
			JOptionPane.showMessageDialog(null,  msg);
			jogadorStand(jogadorAtivo, maoCorrente);
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Stand permitido depois da aposta inicial.");
			break;

		case 2:
		case 3:
			JOptionPane.showMessageDialog(null, "Mão atual já finalizou o turno.");
			break;
		}
	}

	/* DOUBLE */
	void jogadorDouble(int indexJ) {
		model.jogadorDouble(indexJ);
		mudancaJogadorBalanco();
		mudancaJogadorAposta(0);
		mudancaJogadorMao(0);
		
		if (model.jogadorNumMaosAtivas(indexJ) == model.jogadorNumMaosFinalizadas(indexJ))
		{
			buttonsSwitch = false;
			estadoJogo = DEALER;
		}
	}

	public void jogadorDoubleCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		int status = 0;

		if (apostaOK == false)
			status = 1;

		else if (model.jogadorNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorMaoQuebrada(jogadorAtivo, 0) == true)
			status = 3;

		else if (model.jogadorMaoFinalizada(jogadorAtivo, 0) == true)
			status = 4;

		else if (model.jogadorSaldoSuficienteDobra(jogadorAtivo) == false)
			status = 5;

		switch (status) {
		case 0:
			jogadorDouble(jogadorAtivo);
			System.out.println("Double realizado com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Double permitido depois da aposta inicial.");
			break;
			
		case 2:
			JOptionPane.showMessageDialog(null, "Double permitido apenas com uma mao.");
			break;

		case 3:
		case 4:
			JOptionPane.showMessageDialog(null, "Mão atual já finalizou o turno.");
			break;

		case 5:
			JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
			break;
		}
	}

	/* SPLIT */
	void jogadorSplit(int indexJ) {
		model.jogadorSplit(indexJ);
		mudancaJogadorBalanco();
		for (int i = 0; i < maosMax; i++) {
			mudancaJogadorMao(i);
			mudancaJogadorAposta(i);
		}
		
		defineMaoCorrente(0);
		janelaJogador.get(1).setVisible(true);
	}

	public void jogadorSplitCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		int status = 0;

		if (apostaOK == false)
			status = 1;

		else if (model.jogadorNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorMaoQuebrada(jogadorAtivo, 0) == true)
			status = 3;

		else if (model.jogadorMaoFinalizada(jogadorAtivo, 0) == true)
			status = 4;

		else if (model.jogadorSaldoSuficienteDobra(jogadorAtivo) == false)
			status = 5;

		else if (model.jogadorPrimCartasMesmoValor(jogadorAtivo) == false)
			status = 6;

		switch (status) {
		case 0:
			jogadorSplit(jogadorAtivo);
			System.out.println("Split realizado com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Split permitido depois da aposta inicial.");
			break;
		
		case 2:
			JOptionPane.showMessageDialog(null, "Split permitido apenas com uma mao.");
			break;


		case 3:
		case 4:
			JOptionPane.showMessageDialog(null, "Mão atual já finalizou o turno.");
			break;

		case 5:
			JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
			break;

		case 6:
			JOptionPane.showMessageDialog(null, "Cartas precisam ter mesmo valor para realizar Split");
			break;
		}
	}
	
	public void exit()
	{
		System.exit(0);
	}
}
