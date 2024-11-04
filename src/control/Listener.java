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
}

/**
 * Listener para jogo novo.
 */
class JogoNovo extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.saiMenuEntraBanca();
		ctrl.inicializaMecanicaJogo();

	}
}

/**
 * Listener para continuacao de jogo salvo.
 */
class JogoSalvo extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.recuperaJogoSalvo();
		ctrl.saiMenuEntraBanca();
		ctrl.inicializaMecanicaJogo();
	}
}

class SalvarJogo extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.salvaJogo();
	}
}


class DealerCompraCarta extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.dealerHit();
	}
}