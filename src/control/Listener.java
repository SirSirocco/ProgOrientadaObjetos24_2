package control;

import java.awt.event.*;

/**
 * Classe abstrata auxiliar.
 */
abstract class Listener {
	Controller ctrl;
	
	Listener() {
		ctrl = Controller.getController();
	}
	
	void saiMenuEntraBanca() {
		ctrl.getMenu().setVisible(false);
		ctrl.getJBanca().setVisible(true);
	}
}

/**
 * Listener para jogo novo.
 */
class JogoNovo extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		saiMenuEntraBanca();
	}
}

/**
 * Listener para continuacao de jogo salvo.
 */
class JogoSalvo extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.recuperaJogo();
		saiMenuEntraBanca();
	}
}

class DealerCompraCarta extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.dealerCompraCarta();
	}
}
