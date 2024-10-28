package model;

import org.junit.*;
import static org.junit.Assert.*;

public class ParticipanteTeste {
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
	
	/**
	 * Caso 1: Participante possui blackjack.
	 * Esperado: 1.
	 */
	@Test
	public void testPossuiBlackjackTrue() {
		Participante dealer = Dealer.getDealer();
		dealer.mao.get(0).limpaMao();
		dealer.mao.get(0).insere(new Carta("Paus", "A"));
		dealer.mao.get(0).insere(new Carta("Paus", "10"));
		
		assertEquals(1, dealer.possuiBlackjack(0, false));
	}
	
	/**
	 * Caso 2: Participante nao possui blackjack nem 21 pontos.
	 * Esperado: 0.
	 */
	@Test
	public void testPossuiBlackjackFalseCom21Pontos() {
		Participante dealer = Dealer.getDealer();
		dealer.mao.get(0).limpaMao();
		dealer.mao.get(0).insere(new Carta("Paus", "A"));
		dealer.mao.get(0).insere(new Carta("Paus", "5"));
		dealer.mao.get(0).insere(new Carta("Copas", "5"));
		
		assertEquals(0, dealer.possuiBlackjack(0, false));
	}
	
	/**
	 * Caso 3: Jogador possui 21 pontos mas nao tem Blackjack.
	 * Esperado: 0.
	 */
	@Test
	public void testPossuiBlackjackFalseSem21Pontos() {
		Participante jogador = new Jogador();
		jogador.mao.get(0).insere(new Carta("Paus", "A"));
		jogador.mao.get(0).insere(new Carta("Paus", "K"));
		
		assertEquals(0, jogador.possuiBlackjack(0, true));
	}
	
    private Participante participante;
    @Before
    public void setUp() {
        participante = new Jogador();
        
    }
    
    /**
     * Caso 1: Jogador possui dois ases de naipes diferentes.
     * Esperado: 1.
     */
    @Test
    public void testVerificaCartasMesmoValorAs() {
        Carta carta1 = new Carta("Paus", "A");
        Carta carta2 = new Carta("Copas", "A");
        assertEquals(1, Participante.verificaCartasMesmoValor(carta1, carta2));
    }
    
    /**
     * Caso 2: Jogador possui um rei e um valete.
     * Esperado: 10.
     */
    @Test
    public void testVerificaCartasMesmoValorJR() {
        Carta carta1 = new Carta("Paus", "J");
        Carta carta2 = new Carta("Copas", "K");
        assertEquals(10, Participante.verificaCartasMesmoValor(carta1, carta2));
    }
    
    /**
     * Caso 3: Jogador possui duas cartas numericas com mesmo valor.
     * Esperado: Valor das cartas.
     */
    @Test
    public void testVerificaCartasMesmoValorNum() {
        Carta carta1 = new Carta("Paus", "7");
        Carta carta2 = new Carta("Copas", "7");
        assertEquals(7, Participante.verificaCartasMesmoValor(carta1, carta2));
    }
    
    /**
     * Caso 4: Jogador possui duas cartas com valores diferentes.
     * Esperado: -1.
     */
    @Test
    public void testVerificaCartasMesmoValorDiff() {
        Carta carta1 = new Carta("Paus", "7");
        Carta carta2 = new Carta("Copas", "A");
        assertEquals(-1, Participante.verificaCartasMesmoValor(carta1, carta2));
    }
    
    /**
     * Caso 1: Jogador soh possui valores numericos.
     * Esperado: Soma dos valores.
     */
    @Test
    public void testCalculaPontosNum() {
    	participante.mao.get(0).insere(new Carta("Copas", "8"));
    	participante.mao.get(0).insere(new Carta("Espadas", "7"));
        int pontos = participante.calculaPontos(0);
        assertEquals(15, pontos);
    }
    
    /**
     * Caso 2: Jogador soh possui As e 10.
     * Esperado: 21 pontos.
     */
    @Test
    public void testCalculaPontosBJ() {
    	participante.mao.get(0).insere(new Carta("Copas", "A"));
    	participante.mao.get(0).insere(new Carta("Espadas", "10"));
        int pontos = participante.calculaPontos(0);
        assertEquals(21, pontos);
    }
    
    /**
     * Caso 2: Jogador possui 2 Ases e 7.
     * Esperado: 19 pontos.
     */
    @Test
    public void testCalculaPontos2As() {
    	participante.mao.get(0).insere(new Carta("Copas", "A"));
    	participante.mao.get(0).insere(new Carta("Espadas", "A"));
    	participante.mao.get(0).insere(new Carta("Espadas", "7"));
        int pontos = participante.calculaPontos(0);
        assertEquals(19, pontos);
    }
    
    /**
     * Caso 1: Jogador quebra.
     * Esperado: true.
     */
    @Test
    public void testChecaQuebraTrue() {
    	participante.mao.get(0).insere(new Carta("Paus", "K"));
    	participante.mao.get(0).insere(new Carta("Ouros", "Q"));
    	participante.mao.get(0).insere(new Carta("Copas", "2"));
        assertTrue(participante.checaQuebra(0));
    }
    
    /**
     * Caso 2: Jogador nao quebra.
     * Esperado: false.
     */
    @Test
    public void testChecaQuebraFalse() {
    	participante.mao.get(0).insere(new Carta("Paus", "A"));
    	participante.mao.get(0).insere(new Carta("Ouros", "A"));
    	participante.mao.get(0).insere(new Carta("Copas", "2"));
        assertFalse(participante.checaQuebra(0));
    }

    /**
     * Caso 1: Participante ativa mao 1, antes inativa.
     * Esperado: checaMaoInativa(1) retorna false.
     */
    @Test
    public void testAtivaMao() {
        participante.ativaMao(1);
        assertTrue(participante.checaMaoAtiva(1));
    }

    /**
     * Caso 1: Participante ativa mao 1 e depois a desativa com stand.
     * Esperado: checaMaoInativa(1) retorna true.
     */
    @Test
    public void testChecaMaoInativa() {
        participante.ativaMao(1);
        participante.stand(1);
        assertFalse(participante.checaMaoAtiva(1));
    }
    
    /**
     * Caso 1: Participante compra duas cartas.
     * Esperado: numero de cartas igual a 2.
     */
    @Test
    public void testHit() {
        participante.hit(0);
        participante.hit(0);
        assertEquals(2, participante.mao.get(0).getNumCartas());
    }
    
    /**
     * Caso 1: Participante inativa mao 0, unica mao antes ativa.
     * Esperado: numMaosAtivas == 0 e checaMaoInativa(0) retorna true.
     */
    @Test
    public void testStand() {
        participante.stand(0);
        assertEquals(0, participante.numMaosAtivas);
        assertFalse(participante.checaMaoAtiva(0));
    }
}
