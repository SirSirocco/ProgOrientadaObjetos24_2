package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class BPanel extends JPanel{
	Image img;
	Image deck;
	ArrayList<Image> fichas = new ArrayList<>();
	ArrayList<Image> cartas = new ArrayList<>();
	
	final int offset = 135;
	final int padding = 10;
	
	public BPanel(ArrayList<Image> imagens) {
		img = imagens.get(0);
		deck = imagens.get(1);
		for (int i = 2; i < imagens.size(); i++) {
			fichas.add(imagens.get(i));
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
		int x = img.getWidth(null);
		int y = fichas.get(0).getHeight(null);
		for (int i = 0; i < fichas.size(); i++) {
			g.drawImage(fichas.get(i), x - 75, i * (y + padding) + offset, null);
		}
		g.drawImage(deck, x/2 - deck.getWidth(null)/2, 130, null);
		
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
