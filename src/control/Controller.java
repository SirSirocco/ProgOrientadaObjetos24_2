package control;

import view.*;
import model.FachadaModel;

import java.util.ArrayList;

import control.*;

class Controller {
	// SINGLETON
	static Controller ctrl = null;
	
	private FachadaModel fm = FachadaModel.getFachada();
	// JANELAS
	private JanelaInicial menu;
	private JanelaBanca janelaBanca;
	private JanelaJogador janelaJogador;
	
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
		menu = criaMenu();
		janelaBanca = criaJBanca();
		janelaJogador = criaJJogador();
		
		menu.setVisible(true);
	}
	
	void recuperaJogo() {
	}
	
	void retomaJogo() {
		
	}
	
	void dealerCompraCarta() {
		fm.dealerCompraCarta();
		janelaBanca.mostraCartas(fm.getCartasDealer());
	}
	
	void cartasDealer(){
	}
}
