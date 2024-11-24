package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class FonteCartaTeste {
	FonteCarta fonte = new FonteCarta();

	/**
	 * Testa se eh possivel comprar cartas no topo do baralho.
	 */
	@Test
	public void testCompraCarta() {
		Carta retorno = FonteCarta.compraCarta();
		assertNotNull(retorno.getNaipe());
	}

	/**
	 * Checa se a funcao "checaEmbaralha" esta retornando devidamente.
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
