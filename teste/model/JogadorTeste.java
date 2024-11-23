package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class JogadorTeste {
	/**
	 * APOSTA MINIMA = 50; BALANCO INICIAL = 2400;
	 */

	/**
	 * validaAposta valida quanto a aposta minima. aposta valida quanto a balanco
	 * apenas.
	 */

	/**
	 * Caso 1: min <= valor && valor <= balanco. Esperado: validaAposta -> true;
	 * aposta -> true.
	 */
	@Test
	public void testAposta_RefOK_BalancoOK() {
		Jogador jogador = new Jogador();
		int balancoInicial = jogador.getBalanco(), valor = 2300;

		assertTrue(Participante.validaAposta(valor));
		assertTrue(jogador.aposta(0, valor));
		assertEquals(balancoInicial - valor, jogador.getBalanco());
	}

	/**
	 * Caso 2: min > valor && valor <= balanco. Esperado: validaAposta -> false;
	 * aposta -> true.
	 */
	@Test
	public void testAposta_RefNOK_BalancoOK() {
		Jogador jogador = new Jogador();
		int valor = 10;

		assertFalse(Participante.validaAposta(valor));
		assertTrue(jogador.verificaBalanco(valor));
	}

	/**
	 * Caso 3: min <= valor && valor > balanco. Esperado: validaAposta -> true;
	 * aposta -> false.
	 */
	@Test
	public void testAposta_RefOK_BalancoNOK() {
		Jogador jogador = new Jogador();
		int valor = 2500;

		assertTrue(Participante.validaAposta(valor));
		assertFalse(jogador.aposta(0, valor));
	}

	/**
	 * Caso 4: min > valor && valor > balanco. Esperado: validaAposta -> false;
	 * aposta -> false.
	 */
	@Test
	public void testAposta_RefNOK_BalancoNOK() {
		Jogador jogador = new Jogador();
		int valor1 = 2370, valor2 = 40;

		assertTrue(Participante.validaAposta(valor1));
		assertTrue(jogador.aposta(0, valor1)); // Faz aposta auxiliar para preparar caso 4
		assertFalse(Participante.validaAposta(valor2));
		assertFalse("balanco < valor2 < min", jogador.aposta(0, valor2)); // Agora, temos balanco < valor2 < min
	}

	/**
	 * Caso 1: Jogador faz surrender de 2N fichas, com N natural. Esperado: Jogador
	 * deve recuperar N fichas.
	 */
	@Test
	public void testSurrender2N() {
		Jogador jogador = new Jogador();
		int balancoInicial = jogador.getBalanco(), valor = 1200, esperado = balancoInicial - valor + (valor / 2);

		jogador.aposta(0, valor);
		jogador.surrender();
		assertEquals(esperado, jogador.getBalanco());
	}

	/**
	 * Caso 2: Jogador faz surrender de 2N + 1 fichas, com N natural. Esperado:
	 * Jogador deve recuperar N fichas.
	 */
	@Test
	public void testSurrender2N_1() {
		Jogador jogador = new Jogador();
		int balancoInicial = jogador.getBalanco(), valor = 1201, esperado = balancoInicial - valor + ((valor - 1) / 2);

		jogador.aposta(0, valor);
		jogador.surrender();
		assertEquals(esperado, jogador.getBalanco());
	}

	/**
	 * double_ decai para os casos de teste de aposta. Por isso, para simplificar,
	 * faremos apenas dois casos.
	 */

	/**
	 * Caso 1: jogador consegue fazer double, porque possui saldo suficiente.
	 */
	@Test
	public void testDoubleConsegue() {
		Jogador jogador = new Jogador();
		int valor = 1200;

		assertTrue(jogador.aposta(0, valor));
		assertTrue(jogador.double_(0));
	}

	/**
	 * Caso 2: jogador nao consegue fazer double, porque nao possui saldo
	 * suficiente.
	 */
	@Test
	public void testDoubleNaoConsegue() {
		Jogador jogador = new Jogador();
		int valor = 1400;

		assertTrue(jogador.aposta(0, valor));
		assertFalse(jogador.double_(0));
	}

	/*
	 * Caso 1: Apostas sao limpas.
	 */
	@Test
	public void testLimpaApostas() {
		int[] apostas = { 100, 200 };
		Jogador jogador = new Jogador();

		jogador.aposta(0, apostas[0]);
		jogador.aposta(1, apostas[1]);
		assertEquals(apostas[0], jogador.getAposta(0));
		assertEquals(apostas[1], jogador.getAposta(1));

		jogador.limpaApostas();
		for (int i = 0; i < apostas.length; i++)
			assertEquals(0, jogador.getAposta(i));
	}
}
