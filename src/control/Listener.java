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

/**
 * Listener para o split do jogador.
 */
class SplitListener extends Listener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ctrl.jogadorSplitCond(); // Realiza a lógica do split no modelo
    }
}


// LISTENERS PARA CONTROLE DO JOGADOR
class HitJogador extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		
	}
}

class StandJogador extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		
	}
}

class DoubleJogador extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		
	}
}

class SurrenderJogador extends Listener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		
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

