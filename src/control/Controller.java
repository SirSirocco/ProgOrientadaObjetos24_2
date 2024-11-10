package control;

import java.util.*;
import model.FachadaModel;
import view.*;

class MecanicaJogo implements Runnable {
	@Override
	public void run() {
		Controller ctrl = Controller.getController();
		ctrl.painelJogo();
	}
}

class Controller implements Observable {
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

	private final int MUD_DEALER_MAO = -1;
	private final int MUD_JOGADOR_MAO = 100;
	private final int MUD_JOGADOR_APOSTA = 200;
	private final int MUD_JOGADOR_BALANCO = 300;

	// VARIAVEIS DE CONTROLE
	private int maoCorrente;
	private int numJogadores;
	private int maosMax;
	private int hitUntil;
	private int cartasNovaRodada;
	private boolean apostaOK = false;
	private boolean buttonsSwitch = false;

	// INSTANCIACAO
	private Controller() {
	}

	/**
	 * Retorna referencia para o singleton. Cria instancia de controller se esta nao
	 * existir.
	 * 
	 * @return
	 */
	static Controller getController() {
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

	public Object get() {
		return estadoObservable; // VERIFICAR DEPOIS
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
		JanelaJogador janela = new JanelaJogador(idxMao);
		return janela;
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

		menu = criaMenu();
		janelaBanca = criaJBanca();

		for (int i = 0; i < maosMax; i++)
			janelaJogador.add(criaJJogador(i));

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
		ctrl.inicializaMecanicaJogo();
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
		if (model.jogadorVerificaBalancoMinimo(0) == false) // Se houver mais participantes, faca um for
		{
			System.out.println("Fim do Jogo");
			// EXIT
		}
		
		limpaParticipantes();
		model.embaralhaFonte();
		apostaOK = false;

		for (int i = 0; i < cartasNovaRodada; i++)
			dealerHit();

		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < cartasNovaRodada; j++)
				jogadorHit(i, 0); // Jogadores compram duas cartas na mao de indice 0
		}

		estadoJogo = JOGADOR;
	}

	void jogadorVez(int indexJ) {

		estadoJogo = DEALER;
	}

	void dealerVez() // OK
	{
		if (model.dealerPossuiBlackjack() == true) {
			estadoJogo = CHECA_VENCEDOR;
			return;
		}

		while (model.dealerCalculaPontos() < hitUntil) {
			dealerHit();

			if (model.dealerQuebra() == true) {
				estadoJogo = DEALER_QUEBRA;
				return;
			}
		}
	}

	void dealerQuebra() // OK
	{
		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < maosMax; j++) {
				if (model.jogadorQuebrado(i, j) == false)
					jogadorVenceAposta(i, j);
			}
		}
	}

	void checaVencedor() { // OK
		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < maosMax; j++) {
				if (model.jogadorVerificaVitoria(i, j) > 0)
					jogadorVenceAposta(i, j);
			}
		}
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

	/* MUDANCA */
	void mudancaJogadorBalanco() {
		estadoObservable = MUD_JOGADOR_BALANCO;
		notificaEventoJogador();
		notificaEventoBanca();
	}

	void mudancaJogadorAposta(int indexMao) {
		estadoObservable = MUD_JOGADOR_APOSTA + indexMao;
		notificaEventoJogador();
	}

	void mudancaJogadorMao(int indexMao) {
		estadoObservable = MUD_JOGADOR_MAO + indexMao;
		notificaEventoJogador();
	}

	/* HIT */ // OK
	int jogadorHitCond(int indexJ, int indexMao) {
		int status = 0;
		
		if (!apostaOK)
			status = 1;
		
		else if (!model.jogadorQuebrado(indexJ, indexMao))
			status = 2;
		
		else if (!model.jogadorMaoAtiva(indexJ, indexMao))
			status = 3;
		
		switch (status) {
		case 0:
			jogadorHit(indexJ, indexMao);
			break;
		
		case 1:
			System.out.println("Você ainda não realizou sua aposta. Não é possível fazer hit.");
			break;
			
		case 2:
			System.out.println("Você não pode fazer hit depois de uma quebra.");
			break;
			
		case 3:
			System.out.println("Você não pode fazer hit depois de um stand.");
			break;
		}
		
		return status;
	}

	void jogadorHit(int indexJ, int indexMao) {
		model.jogadorHit(indexJ, indexMao);
		mudancaJogadorMao(indexMao);
	}
	
	/* STAND */ // OK
	int jogadorStandCond(int indexJ, int indexMao) {
		int status = 0;
		
		if (!apostaOK)
			status = 1;
		
		else if (!model.jogadorQuebrado(indexJ, indexMao))
			status = 2;
		
		else if (!model.jogadorMaoAtiva(indexJ, indexMao))
			status = 3;
		
		switch (status) {
		case 0:
			jogadorHit(indexJ, indexMao);
			break;
		
		case 1:
			System.out.println("Você ainda não realizou sua aposta. Não é possível fazer stand.");
			break;
			
		case 2:
			break;
			
		case 3:
			break;
		}
		
		return status;
	}

	/* APOSTA */
	void jogadorIncrementaApostaInicial(int indexJ, int valor) {
		boolean status = model.jogadorIncrementaAposta(indexJ, 0, valor);

		if (status) {
			mudancaJogadorBalanco();
			mudancaJogadorAposta(0);
		} else
			System.out.println("Saldo insuficiente."); // Depois gerar uma tela com a mensagem
	}

	/**
	 * Deal eh sempre feito na mao de indice 0.
	 * 
	 * @param indexJ
	 */
	void jogadorDealCond(int indexJ) {
		boolean status = model.jogadorValidaApostaInicial(indexJ);
		
		if (status)
			apostaOK = true;
		
		else
			System.out.println("Valor abaixo da aposta minima."); // Depois gerar uma tela com a mensagem
	}

	void jogadorVenceAposta(int indexJ, int indexMao) {
		model.jogadorVenceAposta(indexJ, indexMao);
		mudancaJogadorBalanco();
	}
}
