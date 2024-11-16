package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class JJPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image img;
	Image deck;
	ArrayList<Image> fichas = new ArrayList<>();
	ArrayList<Image> cartas = new ArrayList<>();
	
	final int offset = 135;
	final int padding = 10;
	
	public JJPanel() {
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (cartas != null) {
			for (int i = 0; i < cartas.size(); i++) {
				g.drawImage(cartas.get(i), 300 + 80*i, 400, null);
			}
		}
	}
	
	public void adicionaImagem(ArrayList<Image> novas_cartas) {
		cartas = novas_cartas;
		revalidate();
		repaint();
	}
}
