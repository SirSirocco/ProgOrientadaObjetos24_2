package view;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Banca extends JFrame{
	int x, y, width, heigth;
	JPanel p;
	Image i;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public Banca() {
		// Define o nome da janela
		super("Banca");
		setLayout(null);
		Container c = getContentPane();

		// Pega a imagem de fundo
		try {
			i = ImageIO.read(new File("Imagens/blackjack.png"));
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		// Define o tamanho e posição da janela
		width = i.getWidth(null) + 15;
		heigth = i.getHeight(null) + 39;
		x = ((int)screenSize.getWidth() - width)/2;
		
		// Define o botão de salvamento
		JButton saveButton = new JButton("Salvar");
		saveButton.setBounds(15, 530, 170, 40);

		// Define o painel da imagem de fundo
		p = new BPanel(i);
		p.setBackground(Color.WHITE);
		p.setBounds(0, 0, width, heigth);
		
		// Adiciona os componentes ao painel
		c.add(saveButton);
		c.add(p);

		setBounds(x, 0, width, heigth);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
