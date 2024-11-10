package view;

import java.awt.*;
import javax.swing.*;

public class JanelaInicial extends JFrame {
	private static final int LARG_DFL = 1366; // pixels
	private static final int ALT_DFL = 768;  // pixels
	
	private JButton btnJogoNovo = new JButton("Começar novo jogo");
	private JButton btnJogoSalvo = new JButton("Retomar jogo salvo");
	private JPanel 	painel = new JPanel();
	
	public JanelaInicial() {
		super("Menu inicial");
		
		/*** REFATORAR COM MYJFrame ***/
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenDims = tk.getScreenSize();
		/*** REFATORAR COM MYJFrame ***/
		int x, y;
		
		painel.add(btnJogoNovo);
		painel.add(btnJogoSalvo);
		painel.setBackground(Color.WHITE);
		getContentPane().add(painel);
		
		x = screenDims.width / 2 - LARG_DFL / 2;
		y = screenDims.height /2 - ALT_DFL / 2;
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
