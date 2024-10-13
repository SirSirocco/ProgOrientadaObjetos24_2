package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class FonteCartaTeste {
	FonteCarta fonte = new FonteCarta();

	@Test
	public void testCompraCarta() {
		Carta retorno = fonte.compraCarta();
		assertEquals("Paus", retorno.getNaipe());
		assertEquals("A", retorno.getValor());
	}
	
	@Test
	public void testCartaNoFinalDaFila() {
		Carta retorno = fonte.compraCarta();
		assertEquals(retorno.getNaipe(), fonte.fonte.getLast().getNaipe());
		assertEquals(retorno.getValor(), fonte.fonte.getLast().getValor());
	}

	@Test
	public void testChecaEmbaralha() {
		fonte.contCartas = 10;
		assertFalse(fonte.checaEmbaralha());
		fonte.contCartas = 300;
		assertTrue(fonte.checaEmbaralha());
	}
}
