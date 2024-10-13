package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class FonteCartaTeste {
	FonteCarta fonte = new FonteCarta();

	/**
	 * Testa se é possível comprar cartas no topo do baralho
	 */
	@Test
	public void testCompraCarta() {
		Carta retorno = FonteCarta.compraCarta();
		assertEquals("Paus", retorno.getNaipe());
		assertEquals("A", retorno.getValor());
	}
	
	/**
	 * Checa se a função "checaEmbaralha" está retornando devidamente
	 */
	@Test
	public void testChecaEmbaralha() {
		for (int i = 0; i < 10; i++)
			FonteCarta.compraCarta();
		assertFalse(FonteCarta.checaEmbaralha());
		for (int i = 0; i < 50; i++)
			FonteCarta.compraCarta();
		assertTrue(FonteCarta.checaEmbaralha());
	}
}
