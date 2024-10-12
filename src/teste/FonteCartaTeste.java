package teste;

import static org.junit.Assert.*;

import model.*;

import org.junit.Test;

public class FonteCartaTeste {
	FonteCarta fonte = new FonteCarta();

	@Test
	public void testCompraCarta() {
		Carta teste = new Carta("Paus", "A");
		Carta retorno = fonte.compraCarta();
		assertEquals(teste.GetNaipe(), retorno.GetNaipe());
		assertEquals(teste.GetValor(), retorno.GetValor());
	}

	@Test
	public void testEmbaralha() {
		fonte.embaralha();
		assertTrue(fonte.checaEmbaralha());
	}

}
