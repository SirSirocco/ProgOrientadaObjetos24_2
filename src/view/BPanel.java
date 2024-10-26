package view;

import javax.swing.*;
import java.awt.*;

class BPanel extends JPanel{
	Image img;
	Image fichas[];
	final int offset = 135;
	final int padding = 10;
	
	public BPanel(Image i, Image[] fs) {
		img = i;
		fichas = fs;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
		int x = img.getWidth(null);
		int y = fichas[0].getHeight(null);
		for (int i = 0; i < fichas.length; i++) {
			g.drawImage(fichas[i], x - 75, i * (y + padding) + offset, null);
		}
	}
}
