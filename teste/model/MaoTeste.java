package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class MaoTeste {
	Mao teste = new Mao();

	@Test
	public void testCalcPontos() {
		Carta carta1 = new Carta("Paus", "A");
		Carta carta2 = new Carta("Paus", "K");
		Carta carta3 = new Carta("Paus", "Q");
		Carta carta4 = new Carta("Paus", "J");
		Carta carta5 = new Carta("Paus", "3");
		
		// Testa se a carta K e A é devidamente contabilizada
		teste.insere(carta1);
		teste.insere(carta2);
		assertEquals("Cartas K e A não estão sendo devidamente contabilizadas", 21, teste.calcPontos());
		teste.limpa();

		// Testa se a carta Q e A é devidamente contabilizada
		teste.insere(carta1);
		teste.insere(carta3);
		assertEquals("Cartas Q e A não estão sendo devidamente contabilizadas", 21, teste.calcPontos());
		teste.limpa();

		// Testa se a carta J e A é devidamente contabilizada
		teste.insere(carta1);
		teste.insere(carta4);
		assertEquals("Cartas J e A não estão sendo devidamente contabilizadas", 21, teste.calcPontos());
		teste.limpa();
		
		// Testa se a carta A contabiliza como 1 caso a mão não seja um blackjack
		teste.insere(carta1);
		teste.insere(carta1);
		teste.insere(carta2);
		assertEquals("Carta A contabilizando um valor errado", 12, teste.calcPontos());
		
		// Testa se carta com numero é devidamente contabilizada
		teste.insere(carta5);
		assertEquals("Carta com número não sendo devidamente contablizada", 15, teste.calcPontos());
	}

}
