package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class JogadorTeste {
	/**
	 * APOSTA MINIMA 	= 50;
	 * BALANCO INICIAL 	= 2400;
	 */
	
	/**
	 * Caso 1: min <= valor && valor <= balanco.
	 * Esperado: consegue apostar e valor eh descontado de balanco.
	 */
	@Test
	public void testAposta_RefOK_BalancoOK() {
		Jogador jogador = new Jogador();
		int balancoInicial = jogador.getBalanco(),
			valor = 2300;
		
		assertTrue(jogador.aposta(0, valor));
		assertEquals(balancoInicial - valor, jogador.getBalanco());
	}
	
	/**
	 * Caso 2: min > valor && valor <= balanco.
	 * Esperado: nao consegue apostar.
	 */
	@Test
	public void testAposta_RefNOK_BalancoOK() {
		Jogador jogador = new Jogador();
		int valor = 10;
		
		assertFalse(jogador.aposta(0, valor));
	}
	
	/**
	 * Caso 3: min <= valor && valor > balanco.
	 * Esperado: nao consegue apostar.
	 */
	@Test
	public void testAposta_RefOK_BalancoNOK() {
		Jogador jogador = new Jogador();
		int valor = 2500;
		
		assertFalse(jogador.aposta(0, valor));
	}
	/**
	 * Caso 4: min > valor && valor > balanco.
	 * Esperado: nao consegue apostar.
	 */
	@Test
	public void testAposta_RefNOK_BalancoNOK() {
		Jogador jogador = new Jogador();
		int valor1 = 2370, valor2 = 40;
		
		assertTrue(jogador.aposta(0, valor1)); // Faz aposta auxiliar para preparar caso 4
		assertFalse("balanco < valor2 < min", jogador.aposta(0, valor2)); // Agora, temos balanco < valor2 < min
	}
	
	/**
	 * Caso 1: Jogador faz surrender de 2N fichas, com N natural.
	 * Esperado: Jogador deve recuperar N fichas.
	 */
	@Test
	public void testSurrender2N() {		
		Jogador jogador = new Jogador();
		int balancoInicial = jogador.getBalanco(),
			valor = 1200,
			esperado = balancoInicial - valor + (valor / 2);
		
		jogador.aposta(0, valor);
		jogador.surrender();
		assertEquals(esperado, jogador.getBalanco());
	}
	
	/**
	 * Caso 2: Jogador faz surrender de 2N + 1 fichas, com N natural.
	 * Esperado: Jogador deve recuperar N fichas.
	 */
	@Test
	public void testSurrender2N_1() {
		Jogador jogador = new Jogador();
		int balancoInicial = jogador.getBalanco(),
			valor = 1201,
			esperado = balancoInicial - valor + ((valor - 1) / 2);
		
		jogador.aposta(0, valor);
		jogador.surrender();
		assertEquals(esperado, jogador.getBalanco());
	}
	
	/**
	 * double_ decai para os casos de teste de aposta. Por isso,
	 * para simplificar, faremos apenas dois casos.
	 */
	
	/**
	 * Caso 1: jogador consegue fazer double.
	 */
	@Test
	public void testDoubleConsegue() {
		Jogador jogador = new Jogador();
		int valor = 1200;
		
		assertTrue(jogador.aposta(0, valor));
		assertTrue(jogador.double_(0));
	}
	
	/**
	 * Caso 2: jogador nao consegue fazer double.
	 */
	@Test
	public void testDoubleNaoConsegue() {
		Jogador jogador = new Jogador();
		int valor = 1400;
		
		assertTrue(jogador.aposta(0, valor));
		assertFalse(jogador.double_(0));
	}
	
//	/*
//	 * Caso 1: Apostas sao limpas.
//	 */
//	@Test
//	public void testLimpaApostas() {
//		int[] apostas;
//		int aposta0 = 100, aposta1 = 200;
//		Jogador jogador = new Jogador();
//		jogador.aposta(aposta0, 0);
//		jogador.aposta(aposta1, 1);
//		
//		apostas = jogador.getApostas();
//		assertEquals(aposta0, apostas[0]);
//		assertEquals(aposta1, apostas[1]);
//		
//		jogador.limpaApostas();
//		
//		for (int i = 0; i < apostas.length; i++)
//			assertEquals(0, apostas[i]);
//	}
}
