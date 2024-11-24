package view;

import java.awt.Toolkit;
import java.awt.Dimension;

/**
 * Classe auxiliar para obter dimensoes da tela.
 * 
 * @since 23-11-2024
 * @author Joao Marcello Amaral
 */
class ScreenSize {
	static Toolkit tk;
	static Dimension screenDimensions;

	static {
		tk = Toolkit.getDefaultToolkit();
		screenDimensions = tk.getScreenSize();
	}

	static int getHeight() {
		return (int) screenDimensions.getHeight();
	}

	static int getWidth() {
		return (int) screenDimensions.getWidth();
	}
}
