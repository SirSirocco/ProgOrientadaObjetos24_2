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
		if (ctrl.buttonsSwitch == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		ctrl.salvaJogo();
	}
}

///////////////////////////////////////
// LISTENERS PARA INTERACAO COM JOGADOR

class JogadorDouble extends Listener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorDoubleCond();
	}
}

class JogadorHit extends Listener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorHitCond();
	}
}

/**
 * Listener para o split do jogador.
 */
class JogadorSplit extends Listener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorSplitCond(); // Realiza a lógica do split no modelo
	}
}

class JogadorStand extends Listener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorStandCond();
	}
}

class JogadorSurrender extends Listener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorSurrenderCond();
	}
}
