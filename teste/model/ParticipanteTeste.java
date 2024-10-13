package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParticipanteTeste {
	/* validaAposta */
	@Test
	public void testValidaApostaIgual() {
		// Caso 1: valor igual a referencia
		// Esperado: true
		
		int ref = 10, val = 10;
		assertTrue(Participante.validaAposta(ref, val));
	}
	
	@Test
	public void testValidaApostaMaior() {
		// Caso 2: valor maior que referencia
		// Esperado: true
		
		int ref = 10, val = 20;
		assertTrue(Participante.validaAposta(ref, val));
	}
	
	@Test
	public void testValidaApostaMenor() {
		// Caso 3: valor menor que referencia
		// Esperado: false
		
		int ref = 10, val = 1;
		assertFalse(Participante.validaAposta(ref, val));
	}
	
	
	/* possuiBlackjack */
	@Test
	public void testPossuiBlackjackTrue() {
	// Caso 1: Participante possui blackjack
	// Esperado: true
		
		
	}
	
	@Test
	public void testPossuiBlackjackFalseCom21Pontos() {
		// Caso 2: Participante nao possui blackjack nem 21 pontos
		// Esperado: false
		
		
	}
	
	@Test
	public void testPossuiBlackjackFalseSem21Pontos() {
		// Caso 3: jogador possui 21 pontos mas nao testaBlackjack
		// Esperado: false
		
	}

}
