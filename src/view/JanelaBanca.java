package view;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.imageio.ImageIO;

public class JanelaBanca extends JFrame{
	int x, y, width, heigth;
	Container c = getContentPane();
	BPanel p;
	Image deck, backgroundImage, fichas[] = new Image[6];
	JLabel valor = new JLabel("0");
	ArrayList<Image> imagens = new ArrayList<Image>();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private JButton saveButton = new JButton("Salvar");
	
	public JanelaBanca() {
		// Define o nome da janela
		super("Banca");
		setLayout(null);

		// Pega a imagem de fundo
		backgroundImage = pegaImagem("Imagens/blackjack.png");
		
		// Pega a imagem do deck de cartas
		deck = pegaImagem("Imagens/deck1.gif");
		
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
		
		// Define o painel da imagem de fundo
		p = new BPanel(imagens);
		p.setBackground(Color.WHITE);
		p.setBounds(0, 0, width, heigth);
		
		// Define o botão de salvamento
		saveButton.setBounds(15, 530, 170, 40);

		// Define o JLabel para o valor das cartas
		valor.setBounds(backgroundImage.getWidth(null)/2, 520, 200, 30);
		valor.setFont(new Font("Serif", Font.PLAIN, 24));
		valor.setForeground(Color.WHITE);
		
		// Adiciona os componentes ao painel
		add(valor);
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
		
		imagens.add(result);
		return result;
	}
	
	/**
	 * Função para pegar a imagem da carta de acordo com os naipes e valores
	 * @param naipe
	 * @param valor
	 * @return imagem da carta
	 */
	Image pegaCarta(String naipe, String valor) {
		try {
			Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			valor = valor.toLowerCase();
		}

		switch (naipe) {
		case "Paus":
			naipe = "c";
			break;
		case "Ouros":
			naipe = "d";
			break;
		case "Espadas":
			naipe = "s";
			break;
		case "Copas":
			naipe = "h";
			break;
		default:
			System.out.println("naipe inválido");
			break;
		}
		
		String caminho = "Imagens/" + valor + naipe + ".gif";
		Image result = null;
		try {
			result = ImageIO.read(new File(caminho));
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		
		return result;
	}
	
	public void mostraCartas(ArrayList<ArrayList<String>> cs) {
		ArrayList<Image> result = new ArrayList<>();
		for (ArrayList<String> c: cs) {
			result.add(pegaCarta(c.get(0), c.get(1)));
		}
		p.adicionaImagem(result);
	}
	
	public void atualizaValorCartas(int val) {
		valor.setText(Integer.toString(val));
	}

	public JButton getBtnSave() {
		return saveButton;
	}
}
