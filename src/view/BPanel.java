package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class BPanel extends JPanel{
	Image img;
	Image fichas[];
	Image deck;
	ArrayList<Image> cartas = new ArrayList<>();
	
	final int offset = 135;
	final int padding = 10;
	
	public BPanel(Image i, Image[] fs, Image d, ArrayList<Image> cs) {
		img = i;
		fichas = fs;
		deck = d;
		cartas = cs;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
		int x = img.getWidth(null);
		int y = fichas[0].getHeight(null);
		for (int i = 0; i < fichas.length; i++) {
			g.drawImage(fichas[i], x - 75, i * (y + padding) + offset, null);
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
		System.out.println(cartas);
		revalidate();
		repaint();
	}
}
