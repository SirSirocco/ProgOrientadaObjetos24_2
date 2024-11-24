package control;

import java.awt.event.*;
import javax.swing.JOptionPane;

/**
 * Classe abstrata auxiliar.
 */
abstract class Listener {
	Controller ctrl;

	Listener() {
		ctrl = Controller.getController();
	}
}

///////////////////////////
// LISTENERS PARA JOGO NOVO

/**
 * Listener para jogo novo.
 */
class JogoNovo extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.jogoNovo();
	}
}

////////////////////////////
// LISTENERS PARA JOGO SALVO

/**
 * Listener para continuacao de jogo salvo.
 */
class JogoSalvo extends Listener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		ctrl.jogoSalvoRecupera();
	}
}

class JogoSalvar extends Listener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ctrl.botoesJogadorAtivos == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		ctrl.salvaJogo();
	}
}
