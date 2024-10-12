package teste;

import static org.junit.Assert.*;

import model.*;

import org.junit.Test;

public class FonteCartaTeste {
	FonteCarta fonte = new FonteCarta();

	@Test
	public void testCompraCarta() {
		Carta retorno = fonte.compraCarta();
		assertEquals("Paus", retorno.GetNaipe());
		assertEquals("A", retorno.GetValor());
	}

	@Test
	public void testEmbaralha() {
		fonte.embaralha();
		assertTrue(fonte.checaEmbaralha());
	}

}
