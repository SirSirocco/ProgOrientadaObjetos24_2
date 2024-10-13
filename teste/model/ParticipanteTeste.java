package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParticipanteTeste {
	/* validaAposta */
	// APOSTA MINIMA = 50
	/**
	 * Caso 1: valor igual a referencia.
	 * Esperado: true.
	 */
	@Test
	public void testValidaApostaIgual() {
		int val = 50;
		assertTrue(Participante.validaAposta(val));
	}
	
	/**
	 * Caso 2: valor maior que referencia.
	 * Esperado: true.
	 */
	@Test
	public void testValidaApostaMaior() {
		int val = 60;
		assertTrue(Participante.validaAposta(val));
	}
	
	/**
	 * Caso 3: valor menor que referencia.
	 * Esperado: false.
	 */
	@Test
	public void testValidaApostaMenor() {
		int val = 1;
		assertFalse(Participante.validaAposta(val));
	}
	
	/* possuiBlackjack */
	/**
	 * Caso 1: Participante possui blackjack.
	 * Esperado: true.
	 */
	@Test
	public void testPossuiBlackjackTrue() {
		
	}
	
	/**
	 * Caso 2: Participante nao possui blackjack nem 21 pontos.
	 * Esperado: false.
	 */
	@Test
	public void testPossuiBlackjackFalseCom21Pontos() {
		
	}
	
	/**
	 * Caso 3: jogador possui 21 pontos mas nao testaBlackjack.
	 * Esperado: false.
	 */
	@Test
	public void testPossuiBlackjackFalseSem21Pontos() {
		
	}

}
