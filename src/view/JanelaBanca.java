package view;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class JanelaBanca extends JFrame{
	int x, y, width, heigth;
	JPanel p;
	Image backgroundImage, fichas[] = new Image[6];
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public JanelaBanca() {
		// Define o nome da janela
		super("Banca");
		setLayout(null);
		Container c = getContentPane();

		// Pega a imagem de fundo
		backgroundImage = pegaImagem("Imagens/blackjack.png");
		
		// Pega as imagens das fichas
		fichas[0] = pegaImagem("Imagens/ficha 1$.png");
		fichas[1] = pegaImagem("Imagens/ficha 5$.png");
		fichas[2] = pegaImagem("Imagens/ficha 10$.png");
		fichas[3] = pegaImagem("Imagens/ficha 20$.png");
		fichas[4] = pegaImagem("Imagens/ficha 50$.png");
		fichas[5] = pegaImagem("Imagens/ficha 100$.png");


		// Define o tamanho e posição da janela
		width = backgroundImage.getWidth(null) + 15;
		heigth = backgroundImage.getHeight(null) + 39;
		x = ((int)screenSize.getWidth() - width)/2;
		
		// Define o botão de salvamento
		JButton saveButton = new JButton("Salvar");
		saveButton.setBounds(15, 530, 170, 40);

		// Define o painel da imagem de fundo
		p = new BPanel(backgroundImage, fichas);
		p.setBackground(Color.WHITE);
		p.setBounds(0, 0, width, heigth);
		
		// Adiciona os componentes ao painel
		c.add(saveButton);
		c.add(p);

		setBounds(x, 0, width, heigth);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	Image pegaImagem(String caminho) {
		Image result = null;
		try {
			result = ImageIO.read(new File(caminho));
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		return result;
	}
}
