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
		c.add(valor);
		c.add(saveButton);
		c.add(p);
		
		
		addMouseListener(this);
		
		setBounds(x, 0, width, heigth);
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
		
		System.out.println("< " + naipe + " >");
		System.out.println("< " + valor + " >");
		
		if (valor.contentEquals("10"))
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
			System.out.println("perror: pegaCarta");
			System.out.println(e);
			System.exit(1);
		}
		
		return result;
	}
	
	private void atualizaCartas() {
		atualizaImagemCartas();
		atualizaValorCartas();
	}

	private void atualizaImagemCartas() {
		ArrayList<ArrayList<String>> cs = ctrl.getDealerCartas();
		ProcessadorImagem.atualizaImagemCartas(p, cs);
	}
	
	private void atualizaValorCartas() {
		valor.setText(Integer.toString(ctrl.getDealerPontos()));
		c.repaint(); // Necessario para a correta exibição da label na tela.
	}
	
	///////////////////////////////////////
	// MouseListener
	@Override
	public void mouseClicked(MouseEvent e) {
		final int buttonWidth = 142, buttonHeigth = 50;

		final int buttonNum1 = 4;
		final int buttonY1 = 730;
		final int buttonOffset1 = 151;
		int buttonPos1[] = new int[buttonNum1];

		final int buttonNum2 = 3;
		final int buttonX2 = 853;
		final int buttonOffset2 = 60;
		int buttonPos2[] = new int[buttonNum2];
		
		final int chipNum = 6;
		final int chipSide = 60, chipOffset = 70, chipX0 = 939;
		int chipPos[] = new int[chipNum];

		final int exitButtonX = 10, exitButtonY = 608, exitButtonHeight = 68, exitButtonWidth = 190;
		
		int x = e.getX(), y = e.getY();
		
		for (int i = 0; i < buttonPos1.length; i++) {
			buttonPos1[i] = 218 + i * buttonOffset1;
		}

		for (int i = 0; i < buttonPos2.length; i++) {
			buttonPos2[i] = 615 + i * buttonOffset2;
		}
		
		for (int i = 0; i < chipPos.length; i++) {
			chipPos[i] = 165 + i * chipOffset;
		}
		
		
		System.out.printf("x: %d y: %d\n", x, y);
		if (y >= buttonY1 && y <= buttonY1 + buttonHeigth) {

			if (x >= buttonPos1[0] && x <= buttonPos1[0] + buttonWidth) {
				doubleButton();
			}
			if (x >= buttonPos1[1] && x <= buttonPos1[1] + buttonWidth) {
				splitButton();
			}
			if (x >= buttonPos1[2] && x <= buttonPos1[2] + buttonWidth) {
				clearButton();
			}
			if (x >= buttonPos1[3] && x <= buttonPos1[3] + buttonWidth) {
				dealButton();
			}
		}
		
		if (x >= buttonX2 && x <= buttonX2 + buttonWidth) {
			if (y >= buttonPos2[0] && y <= buttonPos2[0] + buttonHeigth) {
				hitButton();
			}
			if (y >= buttonPos2[1] && y <= buttonPos2[1] + buttonHeigth) {
				standButton();
			}
			if (y >= buttonPos2[2] && y <= buttonPos2[2] + buttonHeigth) {
				surrenderButton();
			}
		}
		
		if (x >= chipX0 && x <= chipX0 + chipSide) {

			if (y >= chipPos[0] && y <= chipPos[chipNum - 1] + chipSide) {
				y = y - chipPos[0];
				int indexY = y / ( (chipPos[chipNum - 1] - chipPos[0] + chipSide) / chipNum);
				chipPressed(indexY);
			}
		}
		
		if ((x >= exitButtonX && x <= exitButtonX + exitButtonWidth)  && (y >= exitButtonY && y <= exitButtonY + exitButtonHeight))
		{
			exitButton();
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
		ctrl.jogadorSplitCond();
	}

	void clearButton() {
		System.out.println("apertou o botão de clear");
		ctrl.jogadorClearCond();
	}

	void dealButton() {
		System.out.println("apertou o botão de deal");
		ctrl.jogadorDealCond();
	}
	
	void hitButton() {
		System.out.println("apertou o botão de hit");
		ctrl.jogadorHitCond();
	}
	
	void standButton() {
		System.out.println("apertou o botão de stand");
		ctrl.jogadorStandCond();
	}
	
	void surrenderButton() {
		System.out.println("apertou o botão de surrender");
		ctrl.jogadorSurrenderCond();
	}
	
	void exitButton() {
		System.out.println("apertou o botão de exit");
		ctrl.exit();
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
