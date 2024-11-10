package view;

import javax.swing.*;
import java.awt.*;

public class JanelaInicial extends JFrame {
	private static final int LARG_DFL = 1366; // pixels
	private static final int ALT_DFL = 768;  // pixels
	
	private JButton btnJogoNovo = new JButton("Começar novo jogo");
	private JButton btnJogoSalvo = new JButton("Retomar jogo salvo");
	private JPanel 	painel = new JPanel();
	
	public JanelaInicial() {
		super("Menu inicial");
		
		int x, y;
		
		painel.add(btnJogoNovo);
		painel.add(btnJogoSalvo);
		painel.setBackground(Color.WHITE);
		getContentPane().add(painel);
		
		x = ScreenSize.getWidth() / 2 - LARG_DFL / 2;
		y = ScreenSize.getHeight() / 2 - ALT_DFL / 2;
		setBounds(x, y, LARG_DFL, ALT_DFL);
		 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JButton getBtnJogoNovo() {
		return btnJogoNovo;
	}
	
	public JButton getBtnJogoSalvo() {
		return btnJogoSalvo;
	}
}
