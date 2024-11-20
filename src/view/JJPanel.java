package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class JJPanel extends JPanel {
	Image backgroundImage = ProcessadorImagem.pegaImagem("Imagens/blackjack.png");
    int width = backgroundImage.getWidth(null);
    int height = backgroundImage.getHeight(null);
    final int offset = 135;
	final int padding = 10;
	private final int 	altCarta = 100,
						largCarta = 70,
						cartasPorLinha = 5,
						largTotalJJ = ScreenSize.getWidth() - width,
						largTotalCartas = (cartasPorLinha - 1) * padding + largCarta * cartasPorLinha,
						offsetVerticalIni = 100,
						offsetLateral = (largTotalJJ - largTotalCartas) / 2;
			
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image img;
	Image deck;
	ArrayList<Image> fichas = new ArrayList<>();
	ArrayList<Image> cartas = new ArrayList<>();
	
	public JJPanel() {
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (cartas != null) {
			for (int i = 0; i < cartas.size(); i++) {
				g.drawImage(cartas.get(i), offsetLateral + (largCarta + padding) * (i % cartasPorLinha), offsetVerticalIni + (altCarta + padding) * (i / cartasPorLinha), null);
			}
		}
	}
	
	public void adicionaImagem(ArrayList<Image> novas_cartas) {
		cartas = novas_cartas;
		revalidate();
		repaint();
	}
}
