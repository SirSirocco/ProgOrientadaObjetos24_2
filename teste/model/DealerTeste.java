package model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DealerTeste {
	/**
	 * Caso 1: testa se numero de maos ativas e de maos maximo respeitam
	 * as restricoes da subclasse Dealer.
	 * Esperado: 1 mao ativa e 1 mao no maximo.
	 */
	@Test
	public void testConstrutor() {
		Dealer dealer = Dealer.getDealer();
		assertEquals(1, dealer.numMaosAtivas);
		assertEquals(1, dealer.numMaosMax);
	}
	
	// Demais testes serao feitos em ParticipanteTeste
}
