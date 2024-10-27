package view;

import javax.swing.*;
import java.awt.*;

class BPanel extends JPanel{
	Image img;
	Image fichas[];
	Image deck;
	final int offset = 135;
	final int padding = 10;
	
	public BPanel(Image i, Image[] fs, Image d) {
		img = i;
		fichas = fs;
		deck = d;
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
	}
}
