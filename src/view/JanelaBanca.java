package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.imageio.ImageIO;

import control.Observer;
import control.Controller;
import control.Observable;

public class JanelaBanca extends JFrame implements Observer, MouseListener {
	int x, y, width, heigth;
	Container c = getContentPane();
	BPanel p;
	Image deck, backgroundImage, fichas[] = new Image[6];
	JLabel valor = new JLabel("0");
	ArrayList<Image> imagens = new ArrayList<Image>();
	Controller ctrl = Controller.getController();
	
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
		x = ScreenSize.getWidth() / 2 - width + 250;
		
		// Define o painel da imagem de fundo
		p = new BPanel(imagens);
		p.setBackground(Color.BLACK);
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
		
		addMouseListener(this);
		
		setBounds(x, 70, width, heigth);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	///////////////////////////////////////
	// Padrao Observer
	public void notify(Observable o) {
		int estado = o.get();
		update(estado);
	}
	
	private void update(int estado) {
		switch (estado) {
		case MUD_DEALER_MAO:
			atualizaCartas();
			break;
		}
	}
	
	///////////////////////////////////////
	// Gets
	
	public JButton getBtnSave() {
		return saveButton;
	}
	
	///////////////////////////////////////
	// Manipulacao de imagens
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
		
		if (valor == "10")
			valor = "t";

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
	
	private void atualizaCartas() {
		atualizaImagemCartas();
		atualizaValorCartas();
	}
	
//	private void atualizaImagemCartas() {
//		ArrayList<ArrayList<String>> cs = ctrl.getDealerCartas();
//		ArrayList<Image> result = new ArrayList<>();
//		for (ArrayList<String> c: cs) {
//			result.add(pegaCarta(c.get(0), c.get(1)));
//		}
//		p.adicionaImagem(result);
//	}
	
	private void atualizaImagemCartas() {
		ArrayList<ArrayList<String>> cs = ctrl.getDealerCartas();
		ProcessadorImagem.atualizaImagemCartas(p, cs);
	}
	
	private void atualizaValorCartas() {
		valor.setText(Integer.toString(ctrl.getDealerPontos()));
	}
	
	///////////////////////////////////////
	// MouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
		final int buttonHeight = 50, buttonWidth = 142, buttonY = 730;
		final int buttonOffset = 151;
		
		final int chipNum = 6;
		final int chipSide = 60, chipOffset = 70, chipX0 = 939;
		int chipPos[] = new int[chipNum];
		
		int x = e.getX(), y = e.getY();
		
		final int firstButton = 218;
		final int secondButton = 218 + buttonOffset;
		final int thirdButton = 218 + buttonOffset*2;
		final int fourthButton = 218 + buttonOffset*3;
		
		for (int i = 0; i < chipPos.length; i++) {
			chipPos[i] = 165 + i * chipOffset;
		}
		
		final int hitButtonX = 853, hitButtonY = 615, hitButtonWidth = 140;
		
		System.out.printf("x: %d y: %d\n", x, y);
		if ((x >= firstButton && x <= firstButton + buttonWidth) && (y >= buttonY && y <= buttonY + buttonHeight))
			doubleButton();
		if ((x >= secondButton && x <= secondButton + buttonWidth) && (y >= buttonY && y <= buttonY + buttonHeight))
			splitButton();
		if ((x >= thirdButton && x <= thirdButton + buttonWidth) && (y >= buttonY && y <= buttonY + buttonHeight))
			clearButton();
		if ((x >= fourthButton && x <= fourthButton + buttonWidth) && (y >= buttonY && y <= buttonY + buttonHeight))
			dealButton();
		
		if (x >= chipX0 && x <= chipX0 + chipSide) {
			if (y >= chipPos[0] && y <= chipPos[chipNum - 1] + chipSide) {
				y = y - chipPos[0];
				int indexY = y / ( (chipPos[chipNum - 1] - chipPos[0] + chipSide) / chipNum);
				chipPressed(indexY);
			}
		}
	}
	
	void chipPressed(int index) {
		int valores[] = {1, 5, 10, 20, 50, 100};
		if (index >=0 && index <= valores.length - 1) {
			ctrl.jogadorIncrementaApostaInicial(valores[index]);
		}
		
	}
	
	void doubleButton() {
		System.out.println("apertou o botão de double");
		ctrl.jogadorDoubleCond();
	}
	
	void splitButton() {
		System.out.println("apertou o botão de split");
	}

	void clearButton() {
		System.out.println("apertou o botão de clear");
	}

	void dealButton() {
		System.out.println("apertou o botão de deal");
	}
	
	void hitButton() {
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
