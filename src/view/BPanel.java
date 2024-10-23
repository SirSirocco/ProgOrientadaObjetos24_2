package view;

import javax.swing.*;
import java.awt.*;

public class BPanel extends JPanel{
	Image img;
	public BPanel(Image i) {
		img = i;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}
}
