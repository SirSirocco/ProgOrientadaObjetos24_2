package control;

import model.FachadaModel;
import view.*;

class Controller {
	// SINGLETON
	static Controller ctrl = null;
	
	private FachadaModel fm = FachadaModel.getFachada();
	// JANELAS
	private FachadaModel model;
	
	private JanelaInicial menu;
	private JanelaBanca janelaBanca;
	private JanelaJogador janelaJogador;
	
	private int estado = 0; // -1 para banca e index para jogadores
	private final int DEALER = -1;
	private final int DEALER_QUEBRA = -2;
	private final int CHECA_VENCEDOR = -3;
	
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
		
		janela.getBtnHit().addActionListener(new DealerCompraCarta());
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
	
	void jogo() {
		while (true) {
			switch (estado) {
			case DEALER:
				if (model.dealerPossuiBlackjack() == true) {
					estado = CHECA_VENCEDOR;
					break;
				}
				
				while (model.dealerCalculaPontos() < 17) {
					
				}
			}
		}
	}
	
	void balancoJogador(int ind) {
		janelaJogador.atualizaBalanco(fm.balancoJogador(0));
	}
	
	void dealerCompraCarta() {
		fm.dealerCompraCarta();
		janelaBanca.mostraCartas(fm.getCartasDealer());
	}
	
	void cartasDealer(){
	}
}
