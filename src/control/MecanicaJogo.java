package control;

/**
 * Implementa a mecanica do jogo em uma thread auxiliar.
 * 
 * @since 23-11-2024
 * @author Pedro Barizon
 */
class MecanicaJogo implements Runnable {
	@Override
	public void run() {
		Controller ctrl = Controller.getController();
		ctrl.painelJogo();
	}
}
