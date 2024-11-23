package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class MaoTeste {
	Mao teste = new Mao();
	Carta carta1 = new Carta("Paus", "A");
	Carta carta2 = new Carta("Paus", "K");
	Carta carta3 = new Carta("Paus", "Q");
	Carta carta4 = new Carta("Paus", "J");
	Carta carta5 = new Carta("Paus", "3");

	/**
	 * Testa se as cartas A e K são devidamente contabilizadas.
	 */
	@Test
	public void testMaoCartaAeK() {

		teste.insere(carta1);
		teste.insere(carta2);
		assertEquals("Cartas A e K não estão sendo devidamente contabilizadas", 21, teste.calculaPontosMao());
	}

	/**
	 * Testa se as cartas A e Q são devidamente contabilizadas.
	 */
	@Test
	public void testMaoCartaAeQ() {

		teste.insere(carta1);
		teste.insere(carta3);
		assertEquals("Cartas A e Q não estão sendo devidamente contabilizadas", 21, teste.calculaPontosMao());
	}

	/**
	 * Testa se as cartas A e J são devidamente contabilizadas.
	 */
	@Test
	public void testMaoCartaAeJ() {

		teste.insere(carta1);
		teste.insere(carta4);
		assertEquals("Cartas A e J não estão sendo devidamente contabilizadas", 21, teste.calculaPontosMao());
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
