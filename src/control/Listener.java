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
		ctrl.jogoNovo();
	}
}

/**
 * Listener para continuacao de jogo salvo.
 */
class JogoSalvo extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.recuperaJogo();
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

/**
 * Listener para o split do jogador.
 */
class SplitListener extends Listener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ctrl.jogadorSplitCond(); // Realiza a lógica do split no modelo
    }
}


