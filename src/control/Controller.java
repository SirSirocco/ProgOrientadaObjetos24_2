package control;

import model.FachadaModel;
import view.*;

class Controller {
	// SINGLETON
	static Controller ctrl = null;
	
	private FachadaModel model = FachadaModel.getFachada();
	// JANELAS
	private JanelaInicial menu;
	private JanelaBanca janelaBanca;
	private JanelaJogador janelaJogador;
	
	private final int CARTAS_NOVA_RODADA = 2;
	private final int PRIM_JOGADOR = 0;
	private final int DEALER = -1;
	private final int DEALER_QUEBRA = -2;
	private final int CHECA_VENCEDOR = -3;
	private final int NOVA_RODADA = -4;
	private int estado; // -1 para banca e index para jogadores
	
	int numJogadores;
	int maosMax;
	int hitUntil;
	
	// INSTANCIACAO
	private Controller() {
	}
	
	/**
	 * Retorna referencia para o singleton.
	 * Cria instancia de controller se esta nao existir.
	 * @return
	 */
	static Controller getController() {
		if (ctrl == null)
			ctrl = new Controller();
		
		return ctrl;
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
	
	JanelaJogador criaJJogador() {
		JanelaJogador janela = new JanelaJogador();
		return janela;
	}
	
	JanelaInicial getMenu() {
		return menu;
	}
	
	JanelaBanca getJBanca() {
		return janelaBanca;
	}
	
	JanelaJogador getJJogador() {
		return janelaJogador;
	}
	
	///////////////////////////////
	// ROTINAS DE CONTROLE DE JOGO
	void init() {
		/* Necessario criar janelas aqui em vez de no construtor,
		 * para evitar chamada recursiva de getController */
		model = FachadaModel.getFachada();
		
		maosMax 		= model.getMaosMax();
		numJogadores 	= model.getNumJogadores();
		hitUntil 		= model.getHitUntil();
		estado 			= NOVA_RODADA;
		
		menu 			= criaMenu();
		janelaBanca 	= criaJBanca();
		janelaJogador 	= criaJJogador();
		
		menu.setVisible(true);
		System.out.println("INIT");///////////////////////////
	}
	
	void saiMenuEntraBanca() {
		menu.setVisible(false);
		janelaBanca.setVisible(true);
	}
	
	void salvaJogo() {
	}
	
	void recuperaJogoSalvo() {
		/* ITERACAO 03 */
	}
	
	void painelJogo()
	{
		while (true)
		{
			switch (estado)
			{
			case NOVA_RODADA:
				novaRodada();
				break;
			
			case DEALER:
				dealerVez();
				break;
				
			case DEALER_QUEBRA:
				dealerQuebra();
				break;
			
			case PRIM_JOGADOR:
			default:
				jogadorVez(estado);
				break;
			}
		}
	}
	
	///////////////////////////////
	// ETAPAS DO JOGO
	
	void novaRodada() {
		model.limpaParticipantes();
		model.embaralhaFonte();
		
		for (int i = 0; i < CARTAS_NOVA_RODADA; i++)
			model.dealerHit();
		/* ADICIONAR OBSERVER */
		
		for (int i = 0;  i < numJogadores; i++)
		{
			for (int j = 0; j < CARTAS_NOVA_RODADA; j++)
				model.jogadorHit(i, 0); // Jogadores compram duas cartas na mao de indice 0
				/* ADICIONAR OBSERVER */
		}
		
		estado = PRIM_JOGADOR;
		estado = DEALER;
	}
	
	void jogadorVez(int indexJ) {
//		model.limpaParticipantes();
//		model.embaralhaFonte();
//		
//		for (int i = 0; i < CARTAS_NOVA_RODADA; i++)
//			model.dealerHit();
//		/* ADICIONAR OBSERVER */
//		
//		for (int i = 0;  i < numJogadores; i++)
//		{
//			for (int j = 0; j < CARTAS_NOVA_RODADA; j++)
//				model.jogadorHit(i, 0); // Jogadores compram duas cartas na mao de indice 0
//				/* ADICIONAR OBSERVER */
//		}
		
		estado = DEALER;
	}
	
	void dealerVez()
	{
		if (model.dealerPossuiBlackjack() == true)
		{
			estado = CHECA_VENCEDOR;
			return;
		}
		
		while (model.dealerCalculaPontos() < hitUntil)
		{
			model.dealerHit();
			
			if (model.dealerQuebra() == true)
			{
				estado = DEALER_QUEBRA;
				return;
			}
		}
	}
	
	void dealerQuebra()
	{
		for (int i = 0; i < numJogadores; i++)
		{
			for (int j = 0; j < maosMax; j++)
			{
				if (model.jogadorQuebrado(i, j) == false)
					model.jogadorVenceAposta(i, j);
			}
		}
	}
	
	void dealerHit() {
		model.dealerHit();
		janelaBanca.atualizaValorCartas(model.dealerValorCartas());
		janelaBanca.mostraCartas(model.getCartasDealer());
	}
	
	void cartasDealer() {
	}
	
	//////////////////////////////////////////////
	// JOGADOR
	
	void balancoJogador(int ind) {
		janelaJogador.atualizaBalanco(model.balancoJogador(0));
	}
	
	void inicializaMecanicaJogo() {
		Thread tarefa = new Thread(new MecanicaJogo());
		tarefa.start();
	}
}

class MecanicaJogo implements Runnable {
    @Override
    public void run() {
    	Controller ctrl = Controller.getController();
    	ctrl.painelJogo();
    }
}
