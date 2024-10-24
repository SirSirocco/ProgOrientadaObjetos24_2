package view;

import javax.swing.*;
import java.awt.*;

public class BPanel extends JPanel{
	Image img;
	Image fichas[];
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
			g.drawImage(fichas[i], x - 70, i *(y + 10) + 35, null);
		}
	}
}
