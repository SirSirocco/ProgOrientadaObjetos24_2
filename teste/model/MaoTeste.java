package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class MaoTeste {
	Mao teste = new Mao();
	Carta carta1 = new Carta("Paus", "A");
	Carta carta2 = new Carta("Paus", "K");
	Carta carta3 = new Carta("Paus", "Q");
	Carta carta4 = new Carta("Paus", "J");
	Carta carta5 = new Carta("Paus", "3");

	/**
	 * Testa se as cartas K e A são devidamente contabilizadas
	 */
	@Test
	public void testMaoCartaAeK() {
		
		teste.insere(carta1);
		teste.insere(carta2);
		assertEquals("Cartas K e A não estão sendo devidamente contabilizadas", 21, teste.calculaPontosMao());
	}

	/**
	 * Testa se as cartas Q e A são devidamente contabilizadas
	 */
	@Test
	public void testMaoCartaAeQ() {
		
		teste.insere(carta1);
		teste.insere(carta2);
		assertEquals("Cartas K e A não estão sendo devidamente contabilizadas", 21, teste.calculaPontosMao());
	}

	/**
	 * Testa se as cartas J e A são devidamente contabilizadas
	 */
	@Test
	public void testMaoCartaAeJ() {
		
		teste.insere(carta1);
		teste.insere(carta2);
		assertEquals("Cartas K e A não estão sendo devidamente contabilizadas", 21, teste.calculaPontosMao());
	}

	/**
	 * Testa se a carta A está sendo contablizidada como 1 a depender da mão
	 */
	@Test
	public void testCartaAValor1() {
		teste.insere(carta1);
		teste.insere(carta1);
		teste.insere(carta2);
		assertEquals("Carta A contabilizando um valor errado", 12, teste.calculaPontosMao());
	}
	
	/**
	 * Testa se carta com numero é devidamente contabilizada
	 */
	
	@Test
	public void testeValorCartaComNumero() {
		teste.insere(carta1);
		teste.insere(carta1);
		teste.insere(carta2);
		teste.insere(carta5);
		assertEquals("Carta com número não sendo devidamente contablizada", 15, teste.calculaPontosMao());
	}
}
