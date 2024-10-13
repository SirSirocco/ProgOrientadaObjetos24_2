package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParticipanteTeste {
	/* validaAposta */
	@Test
	public void testValidaApostaIgual() {
		// Caso 1: valor igual a referencia
		// Esperado: true
		// ref = 50
		
		int val = 50;
		assertTrue(Participante.validaAposta(val));
	}
	
	@Test
	public void testValidaApostaMaior() {
		// Caso 2: valor maior que referencia
		// Esperado: true
		// ref = 50
		
		int val = 60;
		assertTrue(Participante.validaAposta(val));
	}
	
	@Test
	public void testValidaApostaMenor() {
		// Caso 3: valor menor que referencia
		// Esperado: false
		// ref = 49
		
		int val = 1;
		assertFalse(Participante.validaAposta(val));
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
