package model;

import org.junit.*;
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
    private Participante participante;

    @Before
    public void setUp() {
        participante = new Jogador();
        
    }

    @Test
    public void testValidaAposta() {
        assertTrue(Participante.validaAposta(50));
        assertFalse(Participante.validaAposta(30));
    }

    @Test
    public void testVerificaCartasMesmoValor() {
        Carta carta1 = new Carta("Paus", "A");
        Carta carta2 = new Carta("Copas", "A");
        Carta carta3 = new Carta("Ouros", "K");
        assertEquals(1, Participante.verificaCartasMesmoValor(carta1, carta2));
        assertEquals(-1, Participante.verificaCartasMesmoValor(carta1, carta3));
    }

    @Test
    public void testCalcPontos() {
    participante.mao.get(0).insere(new Carta("Copas", "8"));
    participante.mao.get(0).insere(new Carta("Espadas", "7"));
        int pontos = participante.calculaPontos(0);
        assertEquals(15, pontos);
    }

    @Test
    public void testChecaQuebra() {
    
    participante.mao.get(0).insere(new Carta("Paus", "K"));
    participante.mao.get(0).insere(new Carta("Ouros", "Q"));
    participante.mao.get(0).insere(new Carta("Copas", "2"));
        assertTrue(participante.checaQuebra(0));
    }

    @Test
    public void testAtivaMao() {
        participante.ativaMao(0);
        assertFalse(participante.checaMaoInativa(0));
    }

    @Test
    public void testChecaMaoInativa() {
        participante.ativaMao(0);
        participante.stand(0);
        assertTrue(participante.checaMaoInativa(0));
    }
    
    @Test
    public void testHit() {
        participante.mao.get(0).insere(new Carta("Paus", "5"));
        participante.hit(0);
        assertEquals(2, participante.mao.get(0).getNumCartas());
    }

    @Test
    public void testStand() {
        participante.numMaosAtivas = 1;
        participante.stand(0);
        assertEquals(0, participante.numMaosAtivas);
        assertTrue(participante.checaMaoInativa(0));
    }
}
