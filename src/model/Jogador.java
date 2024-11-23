package model;

/**
 * Classe que implementa os jogadores que jogarao contra o dealer.
 * 
 * @since 23-11-2024
 * @author Pedro Barizon
 */
class Jogador extends Participante {
	// CONSTANTES DE CLASSE
	static final int fatorGanhoAposta = 2; 			// Se jogador vence, tera de volta o dobro do que apostou
	static final int maosMaxJogador = 2; 			// Numero maximo de maos do jogador
	private static final int balancoInicial = 2400;	// em creditos

	// VARIAVEIS DE INSTANCIA
	private int balanco = balancoInicial;
	private int[] apostaMao; 				// Apostas em cada mao
	private boolean flagSplitAses = false;	// Flag que indica que jogador fez split de ases

	// CONSTRUTOR
	Jogador() {
		super(maosMaxJogador);
		apostaMao = new int[maosMaxJogador];
		limpaApostas();
	}

	///////////////////////
	// METODOS DE INSTANCIA

	//////////
	/* GETS */
	int getAposta(int indiceMao) {
		return apostaMao[indiceMao];
	}

	int getBalanco() {
		return balanco;
	}

	//////////
	/* SETS */

	/**
	 * Seta aposta da mao de indiceMao como valor.
	 */
	void setApostaMao(int indiceMao, int valor) {
		apostaMao[indiceMao] = valor;
	}

	void setBalanco(int valor) {
		balanco = valor;
	}

	/**
	 * Se flag for true, define que jogador fez split de ases. Do contrario, define
	 * que nao o fez.
	 */
	void setSplitAses(boolean flag) {
		flagSplitAses = flag;
	}

	//////////////////
	/* VERIFICACOES */

	//////////////////////////////
	/** VERIFICACOES DE APOSTA **/

	boolean verificaApostaInicial() {
		return Participante.validaAposta(apostaMao[0]);
	}

	boolean verificaDobraAposta() {
		return verificaBalanco(apostaMao[0]);
	}

	///////////////////////////////
	/** VERIFICACOES DE BALANCO **/

	/**
	 * @return Retorna true, se valor menor ou igual ao balanco. Do contrario,
	 *         false.
	 */
	boolean verificaBalanco(int valor) {
		return valor <= balanco;
	}

	boolean verificaBalancoMinimo() {
		return Participante.validaAposta(balanco);
	}

	///////////////////////////////
	/** VERIFICACOES DE CARTAS **/

	@Override
	int verificaBlackjack(int indiceMao) {
		// Se jogador tiver feito split de ases, nao podera mais fazer blackjack
		
		if (mao.get(indiceMao).getNumCartas() == 2 && mao.get(indiceMao).calculaPontosMao() == 21 && !flagSplitAses)
			return 1;
		return 0;
	}

	/**
	 * Verifica se jogador fez split de ases, mas nao atualiza a flag indicadora de
	 * split de ases.
	 * 
	 * @return true, se jogador fez split de ases; false, do contrario.
	 */
	boolean verificaSplitAses() {
		if (numMaosAtivas < 2)
			return false;

		return mao.get(0).cartas.get(0).getValor().contentEquals("A")
				&& mao.get(1).cartas.get(0).getValor().contentEquals("A");
	}

	///////////
	/* ACOES */

	///////////////////////
	/** MANIPULACAO DE APOSTA **/

	/**
	 * Verifica se ha saldo suficiente e, se houver, incrementa aposta da mao
	 * indiceMao com valor.
	 * 
	 * @return true, se aposta realizada; false, do contrario.
	 */
	boolean aposta(int indiceMao, int valor) {
		boolean valido = verificaBalanco(valor);

		if (valido) {
			balanco -= valor;
			apostaMao[indiceMao] += valor; // Fazemos += para reutilizarmos com double_
		}

		return valido;
	}

	void recuperaAposta(int indiceMao) {
		balanco += apostaMao[indiceMao];
	}

	void venceAposta(int indiceMao) {
		balanco += fatorGanhoAposta * apostaMao[indiceMao];
	}

	//////////////////////
	/** ACOES DE JOGO **/

	/**
	 * Limpa a aposta inicial, isto eh, a da mao de indice 0.
	 */
	void clear() {
		balanco += apostaMao[0];
		apostaMao[0] = 0;
	}

	/**
	 * Verifica se ha saldo suficiente e, se houver, dobra aposta da mao de
	 * indiceMao. Aciona hit seguido de stand.
	 * 
	 * @return Se aposta valida, retorna true. Do contrario, false.
	 */
	boolean double_(int indiceMao) {
		boolean status = aposta(indiceMao, apostaMao[indiceMao]);

		if (status) {
			hit(indiceMao);
			stand(indiceMao);
		}

		return status;
	}

	/**
	 * Supoe que dealer nao possua Blackjack. Recupera metade do valor apostado.
	 * Valido apenas para mao de indice 0.
	 * 
	 * @return Retorna metade de valor (arredonda para baixo).
	 */
	void surrender() {
		balanco += apostaMao[0] / 2;
		numMaosFinalizadas = numMaosAtivas;
	}

	/**
	 * Realiza o split da mao, caso as duas primeiras cartas tenham o mesmo valor.
	 */

	void split() {
		int flagMesmoValor = Participante.verificaCartasMesmoValor(mao.get(0).cartas.get(0), mao.get(0).cartas.get(1));

		if (mao.get(0).getNumCartas() == 2 && flagMesmoValor != -1) {
			// Obtem cartas da mao inicial
			Carta carta1 = mao.get(0).cartas.get(0);
			Carta carta2 = mao.get(0).cartas.get(1);

			// Mao 0
			mao.get(0).limpaMao();
			mao.get(0).insere(carta1);
			hit(0);

			// Mao 1
			ativaMao(1);
			mao.get(1).insere(carta2);
			hit(1);
			aposta(1, apostaMao[0]);

			// Caso sejam dois ases, nao eh possivel mais fazer blackjack
			if (flagMesmoValor == 1) {
				flagSplitAses = true; // Marcar que o blackjack nao eh mais possivel
			}
		}
	}

	/////////////////////
	/* REINICIALIZACAO */
	@Override
	void limpa() {
		super.limpa();
		limpaApostas();
		flagSplitAses = false;
	}

	/**
	 * Reinicializa com zero vetor de apostas.
	 */
	void limpaApostas() {
		for (int i = 0; i < maosMaxJogador; i++) {
			apostaMao[i] = 0;
		}
	}
}
