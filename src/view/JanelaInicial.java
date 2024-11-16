package view;

import javax.swing.*;
import java.awt.*;

/**
 * Implementa o menu inicial do jogo de Blackjack.
 * Os listeners sao anexados aos botoes do menu no Controller,
 * a fim de tornar o acoplamento minimo.
 */
public class JanelaInicial extends JFrame {
	// Identificador serial da classe
	private static final long	serialVersionUID = 1L;
	
	
	// Constantes auxiliares
	private static final int LARG_DFL = 512,	// Pixels
							 ALT_DFL = 364; 	// Pixels
	
	// Elementos de tela
	private JButton btnJogoNovo = new JButton("Começar novo jogo"),
					btnJogoSalvo = new JButton("Retomar jogo salvo");
	
	private JJPanel 	painel = new JJPanel();
	
	///////////////////////////////////////
	// Construtor
	public JanelaInicial() {
		super("Menu inicial");
		int x, y;
		
		painel.add(btnJogoNovo);
		painel.add(btnJogoSalvo);
		painel.setBackground(Color.WHITE);
		getContentPane().add(painel);
		
		// Centraliza janela
		x = ScreenSize.getWidth() / 2 - LARG_DFL / 2;
		y = ScreenSize.getHeight() / 2 - ALT_DFL / 2;
		setBounds(x, y, LARG_DFL, ALT_DFL);
		 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	///////////////////////////////////////
	// Metodos de instacia
	public JButton getBtnJogoNovo() {
		return btnJogoNovo;
	}
	
	public JButton getBtnJogoSalvo() {
		return btnJogoSalvo;
	}
}
