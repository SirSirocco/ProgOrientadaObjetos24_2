package view;

import java.awt.Toolkit;
import java.awt.Dimension;

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
