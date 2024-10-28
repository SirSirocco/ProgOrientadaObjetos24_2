package control;

import model.FachadaModel;
import view.*;

class Controller {
	// SINGLETON
	static Controller ctrl = null;
	
	// JANELAS
	private FachadaModel model;
	
	private JanelaInicial menu;
	private JanelaBanca janelaBanca;
	private JanelaJogador janelaJogador;
	
	private int estado = 0; // -1 para banca e index para jogadores
	private final int PRIM_JOGADOR = 0;
	private final int DEALER = -1;
	private final int DEALER_QUEBRA = -2;
	private final int CHECA_VENCEDOR = -3;
	private final int NOVA_RODADA = -4;
	
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
	
	// MANIPULACAO DE JANELAS
	JanelaInicial criaMenu() {
		JanelaInicial janela = new JanelaInicial();
		
		janela.getBtnJogoNovo().addActionListener(new JogoNovo());
		janela.getBtnJogoSalvo().addActionListener(new JogoSalvo());
		
		return janela;
	}
	
	JanelaBanca criaJBanca() {
		JanelaBanca janela = new JanelaBanca();
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
	
	// ROTINAS DE CONTROLE DE JOGO
	void init() {
		/* Necessario criar janelas aqui em vez de no construtor,
		 * para evitar chamada recursiva de getController */
		model = FachadaModel.getFachada();
		
		maosMax = model.getMaosMax();
		numJogadores = model.getNumJogadores();
		hitUntil = model.getHitUntil();
		
		menu = criaMenu();
		janelaBanca = criaJBanca();
		janelaJogador = criaJJogador();
		
		menu.setVisible(true);
	}
	
	void saiMenuEntraBanca() {
		menu.setVisible(false);
		janelaBanca.setVisible(true);
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
			
			case DEALER:
				dealerVez();
				
			case DEALER_QUEBRA:
				dealerQuebra();
				
			default:
				
			}
		}
	}
	
	void novaRodada() {
		model.limpaParticipantes();
		estado = PRIM_JOGADOR;
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
}