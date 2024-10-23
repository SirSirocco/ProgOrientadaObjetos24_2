package view;

import java.awt.*;
import javax.swing.*;

public class Banca extends JFrame{
	JPanel p;
	
	public Banca(String s, Image image, int x, int y) {
		super(s);
		setLayout(null);
		
		p = new BPanel(image);
		p.setBackground(Color.WHITE);
		p.setBounds(0, 0, x, y);
		getContentPane().add(p);
		
		setSize(x, y);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
