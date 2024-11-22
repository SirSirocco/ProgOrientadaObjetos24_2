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
		ctrl.jogoSalvoRecupera();
	}
}

class SalvarJogo extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		if (ctrl.buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			System.out.println("Não está na vez do jogador.");
			return;
		}
		ctrl.salvaJogo();
	}
}

// LISTENERS PARA CONTROLE DO JOGADOR
class HitJogador extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorHitCond();
	}
}

class StandJogador extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorStandCond();
	}
}

class DoubleJogador extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorDoubleCond();
	}
}

class SurrenderJogador extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		ctrl.jogadorSurrenderCond();
	}
}

/**
 * Listener para o split do jogador.
 */
class SplitJogador extends Listener implements ActionListener {
	@Override
    public void actionPerformed(ActionEvent e) {
        ctrl.jogadorSplitCond(); // Realiza a lógica do split no modelo
    }
}
