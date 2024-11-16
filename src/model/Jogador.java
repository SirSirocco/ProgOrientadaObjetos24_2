package model;

class Jogador extends Participante {
	////////////////////////////////////
	// VARIAVEIS DE CLASSE
	public static final int maosMaxJogador = 2; // Numero maximo de maos do jogador
	private static final int balancoInicial = 2400;
	
	////////////////////////////////////
	// VARIAVEIS DE INSTANCIA
	private int balanco = balancoInicial;
	private int[] apostaMao; // Apostas em cada mao
	private boolean asesSplitFlag = false;
	
	////////////////////////////////////
	// CONSTRUTOR
	Jogador() {
		super(maosMaxJogador);
		apostaMao = new int[maosMaxJogador];
		limpaApostas();
	}
	
	////////////////////////////////////
	// METODOS DE INSTANCIA
	
	/* Limpeza */
	@Override
	void limpa() {
		super.limpa();
		
		limpaApostas();
	
		asesSplitFlag = false;
	}
	
	/* Gets */
	int getBalanco() {
		return balanco;
	}
	
	int getApostas(int indexMao) {
		return apostaMao[indexMao];
	}
	
	/* Validacoes */
	@Override
	int possuiBlackjack(int indMao) {
		if (mao.get(indMao).getNumCartas() == 2 && mao.get(indMao).calculaPontosMao() == 21 && !asesSplitFlag)
			return 1;
		return 0;
	}
	
	boolean validaApostaInicial() {
		return Participante.validaAposta(apostaMao[0]);
	}
	
	boolean verificaBalancoMinimo() {
		return Participante.validaAposta(balanco);
	}
	
	boolean validaDobraAposta() {
		return validaBalanco(apostaMao[0]);
	}

	/**
	 * @return Retorna true, se valor menor ou igual ao balanco. Do contrario, false.
	 */
	boolean validaBalanco(int valor) {
		return valor <= balanco;
	}

	/**
	 * Supoe que dealer nao possua Blackjack. Recupera metade do valor apostado.
	 * Valido apenas para mao de indice 0.
	 * @return Retorna metade de valor (arredonda para baixo).
	 */
	void surrender() {
		balanco += apostaMao[0] / 2;
	}

	/**
	 * Reinicializa com zero vetor de apostas.
	 */
	void limpaApostas() {
		for (int i = 0; i < maosMaxJogador; i++) {
			apostaMao[i] = 0;
		}
	}
	
	/* Acoes */
	
	/**
	 * Dobra aposta da mao indMao.
	 * @return Se aposta valida, retorna true. Do contrario, false.
	 */
	boolean double_(int indMao) {
		return aposta(apostaMao[indMao], indMao);
	}
	
	/**
	 * Incrementa aposta da mao indMao com valor.
	 * 
	 * @return Se aposta valida, retorna true. Do contrario, false.
	 */
	boolean aposta(int valor, int indMao) {
		boolean valido = /* Participante.validaAposta(valor) && */ validaBalanco(valor);

		if (valido) {
			balanco -= valor;
			apostaMao[indMao] += valor; // Fazemos += para reutilizarmos com double_
		}

		return valido;
	}
	
	void venceAposta(int indMao) {
		balanco += 2 * apostaMao[indMao];
	}

	/**
     * Realiza o split da mão, caso as duas primeiras cartas tenham o mesmo valor.
     */
	
    void split() {
    	int flagMesmoValor = Participante.verificaCartasMesmoValor(mao.get(0).cartas.get(0), mao.get(0).cartas.get(1));
    	
        if (mao.get(0).getNumCartas() == 2 && flagMesmoValor != -1) {
            Carta carta1 = mao.get(0).cartas.get(0);
            Carta carta2 = mao.get(0).cartas.get(1);

            mao.get(0).cartas.clear(); // Limpar mão original
            mao.get(0).insere(carta1);
            mao.get(1).insere(carta2);
            ativaMao(1); // Ativar segunda mão

            // Caso sejam dois Ases, não é possível mais fazer BlackJack
            if (flagMesmoValor == 1) {
                // Marcar que o blackjack não é possível
            	asesSplitFlag = true;
            }
        }
    }
}