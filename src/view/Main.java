package view;

import java.awt.Image;
import java.io.*;
import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) {
		Banca b;
		Image i = null;
		int x, y;
		
		try {
			i = ImageIO.read(new File("Imagens/blackjack.png"));
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		x = i.getWidth(null) + 15;
		y = i.getHeight(null) + 39;
		
		System.out.println(x);
		System.out.println(y);
		
		b = new Banca ("banca", i, x, y);
		b.setVisible(true);

	}

}
