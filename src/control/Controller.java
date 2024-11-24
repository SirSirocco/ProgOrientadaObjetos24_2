package control;

import model.FachadaModel;
import view.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Classe singleton que implementa o controlador que realiza a comunicacao entre
 * a parte grafica da aplicacao (View) e a parte abstrata das regras (Model).
 * 
 * @since 23-11-2024
 * @author Pedro Barizon
 */
public class Controller implements Observable {
	// SINGLETON
	private static Controller ctrl = null;

	private Controller() {
	}

	/**
	 * Retorna referencia para o singleton. Cria instancia de controller se esta nao
	 * existir.
	 * 
	 * @return
	 */
	public static Controller getController() {
		if (ctrl == null)
			ctrl = new Controller();

		return ctrl;
	}

	//////////////////
	// FACADE DO MODEL
	private FachadaModel model = FachadaModel.getFachada();

	/////////////////////////
	// MANIPULACAO DE JANELAS

	/////////////
	/* JANELAS */
	private JanelaInicial menu;
	private JanelaBanca janelaBanca;
	private List<JanelaJogador> janelaJogador = new ArrayList<JanelaJogador>(); // Adicionar indice de janela

	////////////////////////
	/* CRIACAO DE JANELAS */
	JanelaInicial criaMenu() {
		JanelaInicial janela = new JanelaInicial();

		janela.getBtnJogoNovo().addActionListener(new JogoNovo());
		janela.getBtnJogoSalvo().addActionListener(new JogoSalvo());

		return janela;
	}

	JanelaBanca criaJBanca() {
		JanelaBanca janela = new JanelaBanca();

		janela.getBtnSave().addActionListener(new JogoSalvar());

		return janela;
	}

	JanelaJogador criaJJogador(int indiceMao) {
		JanelaJogador janela = new JanelaJogador(0, indiceMao);
		return janela;
	}

	//////////////////////////
	/* ALTERACAO DE JANELAS */

	public void defineMaoCorrente(int indiceMao) {
		// Logging para depuracao
		System.out.println("MAO COORENTE " + maoCorrente);

		maoCorrente = indiceMao;

		for (JanelaJogador j : janelaJogador) {
			j.mudaJanelaCor(maoCorrente);
		}
	}

	//////////////////
	// PADRAO OBSERVER
	private int estadoObservable;

	///////////////
	/* OBSERVERS */
	private List<Observer> lObserverBanca = new ArrayList<Observer>();
	private List<Observer> lObserverJogador = new ArrayList<Observer>();

	/////////////
	/* METODOS */

	public void addObserver(Observer o, char tipoEvento) {
		switch (tipoEvento) {
		case 'B':
			lObserverBanca.add(o);
			break;

		case 'J':
			lObserverJogador.add(o);
			break;
		}
	}

	public void removeObserver(Observer o, char tipoEvento) {
		switch (tipoEvento) {
		case 'B':
			lObserverBanca.remove(o);

		case 'J':
			lObserverJogador.remove(o);
			break;
		}
	}

	public int get() {
		return estadoObservable;
	}

	public void notificaEventoBanca() {
		for (Observer o : lObserverBanca)
			o.notify(this);
	}

	public void notificaEventoJogador() {
		for (Observer o : lObserverJogador)
			o.notify(this);
	}

	/////////////
	// SALVAMENTO

	//////////////////////////////////////
	/* DEFINICOES AUXILIARES SALVAMENTO */
	private final int FALHA = -1, SUCESSO = 0, ARQS_INEXIST = 1, DIRETORIO_INEXIST = 2;

	private final String pastaSalvamento = "salvamento";
	private final String extensaoSalvamento = "txt";
	private final String nomeFiltro = "Arquivos de Salvamento";

	// Diretorio Portavel
	private final File diretorioSalvamento = new File(System.getProperty("user.di"), pastaSalvamento);

	///////////////////////////////
	/* RECUPERACAO DE JOGO SALVO */

	//////////////////////////////////////////
	/** SETS (Ponte entre Control e Model) **/

	////////////////
	/*** DEALER ***/

	/**
	 * Define mao do dealer com base nas cartas passadas como argumento. Notifica
	 * observadores de Eventos da Banca.
	 * 
	 * @param cartas Lista de listas com dois elementos da forma (naipe, valor).
	 */
	void setDealerMao(ArrayList<ArrayList<String>> cartas) {
		model.dealerSetMao(cartas);
		mudancaDealerMao();

		// Se dealer tiver blackjack, atualiza flag correspondente do ctrl
		dealerBlackjack = model.dealerVerificaBlackjack();
	}

	///////////////
	/** JOGADOR **/

	/**
	 * Define a aposta da mao indiceMao do jogador como o valor passado como
	 * argumento. Notifica observadores de Eventos do Jogador.
	 * 
	 * @param indiceMao Indice da mao respectiva a aposta.
	 * @param valor     Valor da aposta.
	 */
	void setJogadorApostaMao(int indiceMao, int valor) {
		model.jogadorSetApostaMao(jogadorAtivo, indiceMao, valor);
		mudancaJogadorAposta(indiceMao);
	}

	/**
	 * Define o balanco do jogador como o valor passado como argumento. Notifica
	 * observadores de Eventos do Jogador.
	 * 
	 * @param valor Valor do balanco.
	 */
	void setJogadorBalanco(int valor) {
		model.jogadorSetBalanco(jogadorAtivo, valor);
		mudancaJogadorBalanco();
	}

	/**
	 * Define mao indiceMao do jogador com base nas cartas passadas como argumento.
	 * Notifica observadores de Eventos do Jogador.
	 * 
	 * @param indiceMao Indice da mao a receber as cartas.
	 * @param cartas    Lista de listas com dois elementos da forma (naipe, valor).
	 */
	void setJogadorMao(int indiceMao, ArrayList<ArrayList<String>> cartas) {
		model.jogadorSetMao(jogadorAtivo, indiceMao, cartas);
		mudancaJogadorMao(indiceMao);
	}

	/**
	 * Define numero de maos ativas do jogador como numMaos.
	 */
	void setJogadorNumMaosAtivas(int numMaos) {
		model.jogadorSetNumMaosAtivas(jogadorAtivo, numMaos);
	}

	/**
	 * Define como quebrada a mao indiceMao do jogador. Isso tambem finaliza o turno
	 * dessa mao (estado finalizada == true).
	 * 
	 * @param indiceMao Indice da mao a receber o estado de quebrada.
	 */
	void setJogadorQuebraMao(int indiceMao) {
		model.jogadorQuebra(jogadorAtivo, indiceMao);
	}

	/////////////////////////////////////////////////////
	/** ATUALIZACOES (chamadas a metodos do tipo set) **/

	////////////////
	/*** DEALER ***/

	/**
	 * Extrai informacoes de infoSalva e atualiza a mao do dealer no model. Faz uso
	 * das funcoes set, definidas acima. Indiretamente, notifica observadores de
	 * Eventos da Banca.
	 * 
	 * @param scannerSecao Scanner obtido de {@code atualizaSecao} com informacoes
	 *                     sobre o dealer.
	 */
	void atualizaDealer(Scanner scannerSecao) {
		ArrayList<ArrayList<String>> cartas = new ArrayList<>();
		ArrayList<String> carta;

		// Logging para depuracao
		System.out.println("ATUALIZA DEALER");

		// Enquanto houver cartas a serem lidas, adiciona nova carta
		while (scannerSecao.hasNext()) {
			carta = new ArrayList<String>();
			carta.add(scannerSecao.next().trim()); // Adiciona naipe
			carta.add(scannerSecao.next().trim()); // Adiciona valor (A, J, K, Q, 1, 2, ...)
			cartas.add(carta);
		}

		// Atualiza cartas da mao no model
		setDealerMao(cartas);

		// Nao devemos fechar scannerSecao aqui, porque sera usado em outras funcoes
	}

	/////////////////
	/*** JOGADOR ***/

	/**
	 * Extrai informacoes de infoSalva e atualiza o jogador no model. Faz uso das
	 * funcoes set, definidas acima. Indiretamente, notifica observadores de Eventos
	 * do Jogador.
	 * 
	 * @param scannerSecao Scanner obtido de {@code atualizaSecao} com informacoes
	 *                     sobre o jogador.
	 */
	void atualizaJogador(Scanner scannerSecao) {
		String infoSalva;
		Scanner scannerAux; // Scanner auxiliar usado para processar a secao em subsecoes
		int numMaosAtivas;

		// Logging para depuracao
		System.out.println("ATUALIZA JOGADOR");

		/*
		 * Define delimitador a ser usado (**). As barras indicam que * deve aparecer
		 * duas vezes e de forma consecutiva.
		 */
		scannerSecao.useDelimiter("\\*\\*");

		// Obtem secao pertinente de informacao
		infoSalva = scannerSecao.next();

		// Processa secao de informacao
		scannerAux = new Scanner(infoSalva);

		// Define se jogador ja realizou a aposta
		apostaInicialFeita = scannerAux.nextBoolean();

		// Atualiza balanco do jogador
		setJogadorBalanco(scannerAux.nextInt());

		// Atualiza numero de maos ativas
		numMaosAtivas = scannerAux.nextInt();
		setJogadorNumMaosAtivas(numMaosAtivas);

		for (int i = 0; i < numMaosAtivas; i++) {
			atualizaJogadorMao(i, scannerSecao.next());
			janelaJogador.get(i).setVisible(true);
			janelaJogador.get(i).mudaJanelaCor(maoCorrente);
		}

		// Se jogador tiver feito split de ases, model ativa flag correspondente
		model.jogadorVerificaSplitAses(jogadorAtivo);

		scannerAux.close();
		// Nao devemos fechar scannerSecao aqui, porque sera usado em outras funcoes
	}

	/**
	 * Extrai informacoes de infoSalva e atualiza a mao indiceMao no model. Faz uso
	 * das funcoes set, definidas acima. Indiretamente, notifica observadores de
	 * Eventos do Jogador.
	 * 
	 * @param indiceMao Indice da mao a ser atualizada.
	 * @param infoSalva String que contem informacoes a respeito da mao do jogador.
	 *                  Extraida da funcao atualizaJogador.
	 */
	void atualizaJogadorMao(int indiceMao, String infoSalva) {
		ArrayList<ArrayList<String>> cartas = new ArrayList<ArrayList<String>>();
		ArrayList<String> carta;
		String estadoMao;
		Scanner scanner;
		int numCartas;

		// Define fonte de informacoes
		scanner = new Scanner(infoSalva);

		// Recupera valor da aposta
		setJogadorApostaMao(indiceMao, scanner.nextInt());

		// Recupera estado da mao (ativa, quebrada ou finalizada)
		estadoMao = scanner.next().trim(); // Fazemos trim por precaucao

		// Recupera numero de cartas da mao
		numCartas = scanner.nextInt();

		// Recupera cartas da mao
		for (int i = 0; i < numCartas; i++) {
			carta = new ArrayList<String>();
			carta.add(scanner.next().trim()); // Adiciona naipe
			carta.add(scanner.next().trim()); // Adiciona valor (A, J, K, Q, 1, 2, ...)
			cartas.add(carta);
		}

		// Atualiza cartas da mao no model
		setJogadorMao(indiceMao, cartas);

		// Atualiza estado da mao no model
		switch (estadoMao) {
		case "T": // Mao ainda em turno
			break;

		case "Q": // Mao quebrada
			setJogadorQuebraMao(indiceMao);
			break;

		case "F": // Mao com turno finalizado
			jogadorStand(jogadorAtivo, indiceMao);
			break;
		}

		// Libera recursos
		scanner.close();
	}

	/////////////
	/** GERAL **/

	/**
	 * Identifica qual o tipo da secao e chama a funcao de atualizacao
	 * correspondente.
	 * 
	 * @param secaoInfo Secao extraida do arquivo vindo de
	 *                  {@code jogoSalvoRestauraContexto}.
	 */
	void atualizaSecao(String secaoInfo) {
		Scanner scannerSecao = new Scanner(secaoInfo);
		String tipoSecao = scannerSecao.next().trim();

		// Logging para depuracao
		System.out.println(tipoSecao);

		switch (tipoSecao) {
		case "DEALER":
			atualizaDealer(scannerSecao);
			break;
		case "JOGADOR":
			atualizaJogador(scannerSecao);
			break;
		case "JOGO":
		}

		// Libera recursos
		scannerSecao.close();
	}

	/**
	 * Le arquivo de salvamento e restaura o contexto do jogo.
	 * 
	 * @param arquivoSalvamento Arquivo vindo de {@code jogoSalvoRecupera}.
	 */
	int jogoSalvoRestauraContexto(File arquivoSalvamento) {
		String secaoInfo;
		Scanner scannerArquivo;
		int status = SUCESSO;

		// Logging para depuracao
		System.out.println("JogoSalvoRestauraContexto EXECUTADO");

		try {
			// Configura Scanner
			scannerArquivo = new Scanner(arquivoSalvamento);
			scannerArquivo.useDelimiter("\\$\\$"); // Usa $$ como delimitador

			// Enquanto houver secoes
			while (scannerArquivo.hasNext()) {
				// Extrai e atualiza secao
				secaoInfo = scannerArquivo.next().trim();
				atualizaSecao(secaoInfo);
			}

			// Libera recursos
			scannerArquivo.close();
		} catch (IOException e) { // Falha na abertura do scanner ou na leitura do arquivo
			System.out.println(e.getMessage());
			status = FALHA;
		}

		return status;
	}

	/**
	 * Verifica se diretorio possui arquivos com a extensao recebida. Usada como
	 * funcao auxiliar {@code jogoSalvoRecupera}, para impedir que o usuario acesse
	 * a pasta de salvamento, se esta estiver vazia.
	 * 
	 * @implNote status: <br>
	 *           SUCESSO = OK <br>
	 *           ARQS_INEXIST = Diretorio existe, mas arquivos com extensao
	 *           inexistem <br>
	 *           DIRETORIO_INEXIST = Diretorio nao existe <br>
	 * 
	 * @param diretorio Diretorio no qual se deseja procurar os arquivos.
	 * @param extensao  Extensao com que devem terminar os arquivos. Escreva sem o
	 *                  ".". Por exemplo, escreva "txt" e nao ".txt".
	 */
	int verificaDiretorioNaoVazio(File diretorio, String extensao) {
		String[] arquivosFiltrados;
		FilenameFilter filtro;
		int status = 0;

		// Verifica se o diretorio existe e eh realmente um diretorio
		if (diretorio.exists() && diretorio.isDirectory()) {
			// Filtro para arquivos com extensao desejada
			filtro = (dir, nome) -> nome.endsWith(extensao);

			// Lista de arquivos que correspondem ao filtro
			arquivosFiltrados = diretorio.list(filtro);

			if (arquivosFiltrados != null && arquivosFiltrados.length > 0)
				System.out.println(String.format("O diretório contém arquivos com a extensão .%s.", extensao));

			else {
				System.out
						.println(String.format("O diretório está vazio para a extensão especificada (.%s).", extensao));
				status = ARQS_INEXIST;
			}
		}

		else {
			System.out.println("O caminho especificado não é um diretório ou não existe.");
			status = DIRETORIO_INEXIST;
		}

		return status;
	}

	/**
	 * Permite ao usuario escolher o arquivo de salvamento usado na recuperacao do
	 * jogo. Se falha na escolha, inicia novo jogo sem salvamento. Faz uso do
	 * JFileChooser e de outras classes Swing.
	 */
	void jogoSalvoRecupera() {
		JFileChooser seletorArquivo = new JFileChooser();
		File /* diretorioSalvamento, */ arquivoEscolhido;
		FileNameExtensionFilter filtro = new FileNameExtensionFilter(nomeFiltro, extensaoSalvamento);
		int resultado;

		// Obtem diretorio de salvamento de forma portavel
		// diretorioSalvamento = new File(System.getProperty("user.dir"),
		// pastaSalvamento);

		// Logging para depuracao
		System.out.println(diretorioSalvamento.getAbsolutePath());

		// Verificacao Diretorio Vazio
		resultado = verificaDiretorioNaoVazio(diretorioSalvamento, extensaoSalvamento);

		if (resultado == SUCESSO) {
			// Configuracao do seletor de arquivos
			seletorArquivo.setCurrentDirectory(diretorioSalvamento);
			seletorArquivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
			seletorArquivo.setFileFilter(filtro);
			seletorArquivo.setApproveButtonText("Abrir");

			// Interacao com usuario
			resultado = seletorArquivo.showOpenDialog(null); // Exibe dialogo de abertura de arquivo

			switch (resultado) {
			case JFileChooser.APPROVE_OPTION:
				// Escolha de arquivo
				arquivoEscolhido = seletorArquivo.getSelectedFile();
				if (jogoSalvoRestauraContexto(arquivoEscolhido) == SUCESSO) // Escolha bem-sucedida
					break;

				// Escolha mal-sucedida
			case JFileChooser.CANCEL_OPTION:
			case JFileChooser.ERROR_OPTION:
				JOptionPane.showMessageDialog(null,
						"Houve um erro na escolha do arquivo de salvamento. Novo jogo iniciado sem salvamento.");
				break;
			}
		}

		else
			JOptionPane.showMessageDialog(null,
					"Ainda não há arquivos de salvamento. Novo jogo iniciado sem salvamento.");

		// Inicializa mecanica do jogo
		initJanelas(model.jogadorGetNumMaosAtivas(jogadorAtivo));
		estadoJogo = JOGADOR;
		inicializaMecanicaJogo();
	}

	////////////////////////
	/* SALVAMENTO DO JOGO */

	/** DEALER **/
	void salvaDealer(File arquivoSalvamento) {
		BufferedWriter escritor;
		ArrayList<ArrayList<String>> cartas = model.dealerGetCartas();

		try {
			escritor = new BufferedWriter(new FileWriter(arquivoSalvamento, true));
			escritor.write("DEALER");
			escritor.newLine(); // Escreve new line de forma portavel

			if (apostaInicialFeita) {
				for (ArrayList<String> carta : cartas) {
					escritor.write(String.format("%s %s", carta.get(0), carta.get(1)));
					escritor.newLine();
				}
			}
			escritor.write("$$");
			escritor.newLine();
			escritor.close();
		}

		catch (IOException e) {
			System.out.println("Erro no salvamento do arquivo.");
		}
	}

	///////////////
	/** JOGADOR **/
	void salvaJogador(File arquivoSalvamento) {
		BufferedWriter escritor;
		ArrayList<ArrayList<String>> cartas;

		try {
			escritor = new BufferedWriter(new FileWriter(arquivoSalvamento, true));
			escritor.write("JOGADOR");
			escritor.newLine(); // Escreve new line de forma portavel
			escritor.write(Boolean.toString(apostaInicialFeita));
			escritor.newLine();
			escritor.write(Integer.toString(model.jogadorGetBalanco(jogadorAtivo)));
			escritor.newLine();

			if (apostaInicialFeita) {
				escritor.write(Integer.toString(model.jogadorGetNumMaosAtivas(jogadorAtivo)));
				escritor.newLine();

				for (int i = 0; i < model.jogadorGetNumMaosAtivas(jogadorAtivo); i++) {
					cartas = model.getCartasJogador(jogadorAtivo, i);

					escritor.write("**");
					escritor.newLine();

					escritor.write(Integer.toString(model.jogadorGetAposta(jogadorAtivo, i)));
					escritor.newLine();

					if (model.jogadorVerificaMaoQuebrada(jogadorAtivo, i))
						escritor.write("Q");
					else if (model.jogadorVerificaMaoFinalizada(jogadorAtivo, i))
						escritor.write("F");
					else
						escritor.write("T");
					escritor.newLine();

					escritor.write(Integer.toString(cartas.size()));
					escritor.newLine(); // Escreve new line de forma portavel

					for (ArrayList<String> carta : cartas) {
						escritor.write(String.format("%s %s", carta.get(0), carta.get(1)));
						escritor.newLine();
					}
				}
			}

			else {
				escritor.write("0");
				escritor.newLine();
			}

			escritor.close();
		}

		catch (IOException e) {
			System.out.println("Erro no salvamento do arquivo.");
		}
	}

	/////////////
	/** GERAL **/

	void salvamentoEscreveArquivo(File arquivoSalvamento) {
		FileWriter limpador;

		try {
			limpador = new FileWriter(arquivoSalvamento, false); // Limpa conteudo do arquivo
			limpador.close();
		}

		catch (IOException e) {
			System.out.println("Erro no salvamento do arquivo.");
		}

		salvaDealer(arquivoSalvamento);
		salvaJogador(arquivoSalvamento);
	}

	void salvaJogo() {
		String teminacaoSalvamento = "." + extensaoSalvamento;
		JFileChooser seletorArquivo = new JFileChooser();
		File arquivoEscolhido;
		FileNameExtensionFilter filtro = new FileNameExtensionFilter(nomeFiltro, extensaoSalvamento);
		int resultado;

		// Logging para depuracao
		System.out.println(diretorioSalvamento.getAbsolutePath());

		// Configuracao do seletor de arquivos
		seletorArquivo.setCurrentDirectory(diretorioSalvamento);
		seletorArquivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
		seletorArquivo.setFileFilter(filtro);
		seletorArquivo.setApproveButtonText("Salvar (.txt)");

		// Interacao com usuario
		resultado = seletorArquivo.showSaveDialog(null); // Exibe dialogo de abertura de arquivo

		switch (resultado) {
		case JFileChooser.APPROVE_OPTION:
			// Escolha de arquivo
			arquivoEscolhido = seletorArquivo.getSelectedFile();

			if (!arquivoEscolhido.getName().endsWith(teminacaoSalvamento))
				arquivoEscolhido = new File(diretorioSalvamento, arquivoEscolhido.getName() + teminacaoSalvamento);

			salvamentoEscreveArquivo(arquivoEscolhido);
			JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso. Pressione OK para encerrar.");
			exit();
			break;

		// Escolha mal-sucedida
		case JFileChooser.CANCEL_OPTION:
		case JFileChooser.ERROR_OPTION:
			JOptionPane.showMessageDialog(null, "Houve um erro no salvamento. Processo cancelado.");
			break;
		}
	}

	///////////////////
	// CONTROLE DE JOGO
	/**
	 * Armazena estado corrente do jogo.
	 */
	private int estadoJogo;

	///////////////////////
	/* TABELA DE ESTADOS */
	private final int JOGADOR = 0;			// Vez do jogador
	private final int DEALER = -1;			// Vez do dealer
	private final int DEALER_QUEBRA = -2;	// Dealer quebrou
	private final int CHECA_VENCEDOR = -3;	// Dealer nao quebrou, e obtem-se as maos vencedoras
	private final int NOVA_RODADA = -4;		// Nova rodada do jogo (reinicializacao de variaveis)

	////////////////////////////////////////////
	/* CONSTANTES DE CONTROLE VINDAS DO MODEL */
	private int numJogadores;			// Numero de jogadores (default = 1)
	private int maosMax;				// Numero maximo de maos por jogador (default = 2)
	private int hitUntil;				// Numero de pontos a partir do qual o dealer para de comprar cartas
	private int cartasNovaRodada;		// Numero de cartas compradas a cada nova rodada (default

	///////////////////////////
	/* VARIAVEIS DE CONTROLE */
	private int jogadorAtivo = 0;							// So realmente util para mais de um jogador
	private int maoCorrente;								// Mao sobre a qual incidirao as acoes acionadas no GUI

	private boolean apostaInicialFeita = false;				// Indica se jogador ja realizou aposta inicial
	private boolean dealerBlackjack = false;				// Indica se dealer possui blackjack
	protected volatile boolean botoesJogadorAtivos = true; 	// Switch que ora ativa botoes da GUI ora os desativa
															 	// (exceto EXIT)

	/////////////////////////////////
	/* ROTINAS DE CONTROLE DE JOGO */

	/**
	 * Encerra imediatamente a aplicacao.
	 */
	public void exit() {
		System.exit(0);
	}

	/**
	 * Inicializa a aplicacao.
	 */
	void init() {
		// Logging para depuracao
		System.out.println("INIT");

		/*
		 * Necessario criar janelas aqui em vez de no construtor, para evitar chamada
		 * recursiva de getController
		 */

		// Obtem referencia para a fachada do model
		model = FachadaModel.getFachada();

		// Obtem constantes do model
		maosMax = model.getJogadoresMaosMax();
		numJogadores = model.getJogadoresNum();
		hitUntil = model.getHitUntil();
		cartasNovaRodada = model.getNumCartasNovaRodada();

		// Atualiza variaveis de controle
		maoCorrente = 0;
		apostaInicialFeita = false;
		estadoJogo = NOVA_RODADA;

		// Cria menu
		menu = criaMenu();

		// Cria janela da banca e a adiciona como observador de eventos da banca
		janelaBanca = criaJBanca();
		this.addObserver(janelaBanca, 'B');

		// Cria janelas do jogador e as adiciona como observadores de eventos do jogador
		for (int i = 0; i < maosMax; i++) {
			janelaJogador.add(criaJJogador(i));
			this.addObserver(janelaJogador.get(i), 'J');
		}

		menu.setVisible(true);
	}

	/**
	 * Inicializa as janelas da aplicacao.
	 * 
	 * @param numMaosAtivas Numero de maos ativas do jogador corrente. Define o
	 *                      numero de janelas do jogador a serem abertas.
	 */
	void initJanelas(int numMaosAtivas) {
		menu.setVisible(false);
		janelaBanca.setVisible(true);

		for (int i = 0; i < numMaosAtivas; i++)
			janelaJogador.get(0).setVisible(true);
	}

	void inicializaMecanicaJogo() {
		Thread tarefa = new Thread(new MecanicaJogo());
		tarefa.start();
	}

	void jogoNovo() {
		estadoJogo = NOVA_RODADA;
		initJanelas(model.jogadorGetNumMaosAtivas(jogadorAtivo));
		inicializaMecanicaJogo();
	}

	/////////////////
	// PAINEL DO JOGO

	void painelJogo() {
		while (true) {
			switch (estadoJogo) {
			case NOVA_RODADA:
				novaRodada();
				break;

			case DEALER:
				dealerVez();
				break;

			case DEALER_QUEBRA:
				dealerQuebra();
				break;

			case CHECA_VENCEDOR:
				checaVencedor();
				break;

			case JOGADOR:
			default:
				jogadorVez(jogadorAtivo);
				break;
			}
		}
	}

	///////////////////////////////
	// ETAPAS DO JOGO

	void novaRodada() {
		System.out.println("NOVA RODADA EXECUTADA");
		if (model.jogadorVerificaBalancoMinimo(0) == false) // Se houver mais participantes, faca um for
		{
			JOptionPane.showMessageDialog(null, "Saldo insuficiente. Fim do Jogo");
			System.exit(0);
		}

		limpaParticipantes();
		model.embaralhaFonte();
		apostaInicialFeita = false;

		jogadorAtivo = 0;
		janelaJogador.get(1).setVisible(false);
		maoCorrente = 0;

		// Restaura cor da janela
		for (int i = 0; i < maosMax; i++)
			janelaJogador.get(i).mudaJanelaCor(maoCorrente);

		mudancaNovaRodada();
		estadoJogo = JOGADOR;
		System.out.println("FIM DE NOVA RODADA");
	}

	void compraInicialCartas() {
		for (int i = 0; i < cartasNovaRodada; i++)
			dealerHit();

		if ((dealerBlackjack = model.dealerVerificaBlackjack()))
			JOptionPane.showMessageDialog(null, "DEALER TEM BLACKJACK!!!");

		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < cartasNovaRodada; j++)
				jogadorHit(i, 0); // Jogadores compram duas cartas na mao de indice 0
		}

		if (model.jogadorVerificaBlackjack(jogadorAtivo, 0))
			JOptionPane.showMessageDialog(null, "JOGADOR TEM BLACKJACK NA MAO 1!!!");
	}

	void jogadorVez(int indiceJ) {
		botoesJogadorAtivos = true;
		System.out.println("JOGADOR VEZ EXECUTADO");
		while (botoesJogadorAtivos)
			;
		System.out.println("FIM DE JOGADOR VEZ");
	}

	void dealerVez() {
		System.out.println("DEALER VEZ EXECUTADO");
		if (dealerBlackjack) {
			estadoJogo = CHECA_VENCEDOR;

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("DEALER BLACKJACK RETURN");
			return;
		}

		else if (model.dealerGetPontos() >= hitUntil) {
			System.out.println("BANCA NAO FARÁ HIT");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while (model.dealerGetPontos() < hitUntil) {
			System.out.println("BANCA FEZ HIT");
			dealerHit();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (model.dealerQuebra() == true) {
				estadoJogo = DEALER_QUEBRA;
				JOptionPane.showMessageDialog(null, "DEALER QUEBROU!!!");
				return;
			}
		}

		estadoJogo = CHECA_VENCEDOR;
		System.out.println("FIM DE DEALER VEZ");
	}

	void dealerQuebra() {
		String msg;
		System.out.println("DEALER QUEBRA EXECUTADO");
		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < maosMax; j++) {
				if (model.jogadorVerificaMaoAtiva(i, j) == true) {
					msg = String.format("Jogador %d | Mão %d: ", i + 1, j + 1);

					if (model.jogadorVerificaMaoQuebrada(i, j)) {
						jogadorRecuperaAposta(i, j);
						msg += "EMPATE\n";
					} else {
						jogadorVenceAposta(i, j);
						msg += "JOGADOR VENCE\n";
					}

					JOptionPane.showMessageDialog(null, msg);
				}
			}
		}

		estadoJogo = NOVA_RODADA;
		System.out.println("FIM DE DEALER QUEBRA");
	}

	void checaVencedor() {
		String msg;
		int jogadorVencedorFlag;
		/*
		 * maior que 0 -> Jogador vence igual a 0 -> Empate menor que 0 -> Dealer vence
		 */
		System.out.println("CHECA VENCEDOR EXECUTADO");
		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < maosMax; j++) {
				if (model.jogadorVerificaMaoAtiva(i, j) == true) {
					msg = String.format("Jogador %d | Mão %d: ", i + 1, j + 1);

					if ((jogadorVencedorFlag = model.jogadorVerificaVitoria(i, j)) > 0) {
						jogadorVenceAposta(i, j);
						msg += "JOGADOR VENCE\n";
					}

					else if (jogadorVencedorFlag == 0) {
						jogadorRecuperaAposta(i, j);
						msg += "EMPATE\n";
					}

					else {
						msg += "BANCA VENCE\n";
					}
					JOptionPane.showMessageDialog(null, msg);
				}
			}
		}

		estadoJogo = NOVA_RODADA;
		System.out.println("FIM DE CHECA VENCEDOR");
	}

	////////
	// GERAL

	void mudancaNovaRodada() {
		mudancaDealerMao();
		mudancaJogadorBalanco();

		for (int i = 0; i < maosMax; i++) {
			mudancaJogadorMao(i);
			mudancaJogadorAposta(i);
		}
	}

	//////////
	// DEALER

	//////////
	/* GETS */

	public ArrayList<ArrayList<String>> getDealerCartas() {
		return model.dealerGetCartas();
	}

	public int getDealerPontos() {
		return model.dealerGetPontos();
	}

	/////////////
	/* MUDANCA */
	void mudancaDealerMao() {
		estadoObservable = MUD_DEALER_MAO;
		notificaEventoBanca();
	}

	///////////
	/* ACOES */

	void dealerHit() {
		model.dealerHit();
		mudancaDealerMao();
	}

	///////////
	// JOGADOR

	//////////
	/* GETS */
	public int getJogadorAposta(int indiceJ, int indiceMao) {
		return model.jogadorGetAposta(indiceJ, indiceMao);
	}

	public int getJogadorBalanco(int indiceJ) {
		return model.jogadorGetBalanco(indiceJ);
	}

	public ArrayList<ArrayList<String>> getJogadorCartas(int indiceJ, int indiceMao) {
		return model.getCartasJogador(indiceJ, indiceMao);
	}

	public int getJogadorPontos(int indiceJ, int indiceMao) {
		return model.jogadorGetPontos(indiceJ, indiceMao);
	}

	/////////////
	/* MUDANCA */

	void mudancaJogadorAposta(int indiceMao) {
		estadoObservable = MUD_JOGADOR_APOSTA + indiceMao;
		notificaEventoJogador();
		estadoObservable = MUD_JOGADOR_BALANCO;
		notificaEventoJogador();
	}

	void mudancaJogadorBalanco() {
		estadoObservable = MUD_JOGADOR_BALANCO;
		notificaEventoJogador();
	}

	void mudancaJogadorMao(int indiceMao) {
		estadoObservable = MUD_JOGADOR_MAO + indiceMao;
		notificaEventoJogador();
	}

	///////////
	/* ACOES */

	//////////////
	/** APOSTA **/

	void jogadorRecuperaAposta(int indiceJ, int indiceMao) {
		model.jogadorRecuperaAposta(indiceJ, indiceMao);
		mudancaJogadorBalanco();
	}

	void jogadorVenceAposta(int indiceJ, int indiceMao) {
		model.jogadorVenceAposta(indiceJ, indiceMao);
		mudancaJogadorBalanco();
	}

	public void jogadorIncrementaApostaInicial(int valor) {
		if (botoesJogadorAtivos == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}

		int status = 0;

		if (model.jogadorIncrementaAposta(jogadorAtivo, 0, valor) == false)
			status = 1;

		else if (apostaInicialFeita)
			status = 2;

		switch (status) {
		case 0:
			mudancaJogadorBalanco();
			mudancaJogadorAposta(0);
			System.out.println("Aposta inicial incrementada.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Aposta já realizada.");
			break;
		}
	}

	////////////
	/** DEAL **/

	void jogadorDeal() {
		apostaInicialFeita = true;
		compraInicialCartas();
		mudancaNovaRodada();
	}

	/**
	 * Deal eh sempre feito na mao de indice 0.
	 * 
	 * @param indexJ
	 */
	public void jogadorDealCond() {
		if (botoesJogadorAtivos == false) {
			System.out.println("Não está na vez do jogador.");
			return;
		}

		int status = 0;

		if (model.jogadorVerificaApostaInicial(jogadorAtivo) == false)
			status = 1;

		else if (apostaInicialFeita)
			status = 2;

		switch (status) {
		case 0:
			jogadorDeal();
			System.out.println("Aposta realizada com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Valor abaixo da aposta minima."); // Depois gerar uma tela com a
																					 // mensagem
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Aposta já realizada."); // Depois gerar uma tela com a mensagem
			break;
		}
	}

	///////////
	/* CLEAR */
	void jogadorClear() {
		model.jogadorClear(jogadorAtivo);
		mudancaJogadorAposta(0);
		mudancaJogadorBalanco();
	}

	public void jogadorClearCond() {
		if (botoesJogadorAtivos == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}

		int status = 0;

		if (apostaInicialFeita)
			status = 1;

		switch (status) {
		case 0:
			jogadorClear();
			System.out.println("Clear realizado com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Clear não permitido depois de aposta já realizada.");
			break;
		}
	}

	////////////
	/* DOUBLE */
	void jogadorDouble(int indiceJ) {
		model.jogadorDouble(indiceJ);
		mudancaJogadorBalanco();
		mudancaJogadorAposta(0);
		mudancaJogadorMao(0);

		if (model.jogadorGetNumMaosAtivas(indiceJ) == model.jogadorGetNumMaosFinalizadas(indiceJ)) {
			botoesJogadorAtivos = false;
			estadoJogo = DEALER;
		}
	}

	public void jogadorDoubleCond() {
		if (botoesJogadorAtivos == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}

		int status = 0;

		if (apostaInicialFeita == false)
			status = 1;

		else if (model.jogadorGetNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorVerificaMaoQuebrada(jogadorAtivo, 0) == true)
			status = 3;

		else if (model.jogadorVerificaMaoFinalizada(jogadorAtivo, 0) == true)
			status = 4;

		else if (model.jogadorVerificaDobraAposta(jogadorAtivo) == false)
			status = 5;

		switch (status) {
		case 0:
			jogadorDouble(jogadorAtivo);
			System.out.println("Double realizado com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Double permitido depois da aposta inicial.");
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Double permitido apenas com uma mao.");
			break;

		case 3:
		case 4:
			JOptionPane.showMessageDialog(null, "Mão atual já finalizou o turno.");
			break;

		case 5:
			JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
			break;
		}
	}

	/////////
	/* HIT */
	void jogadorHit(int indiceJ, int indiceMao) {
		String msg;
		model.jogadorHit(indiceJ, indiceMao);
		mudancaJogadorMao(indiceMao);

		if (model.jogadorQuebra(indiceJ, indiceMao) == true) {
			msg = "Mão " + (indiceMao + 1) + " quebrou";
			JOptionPane.showMessageDialog(null, msg);

			System.out.println(model.jogadorGetNumMaosAtivas(indiceJ));
			System.out.println(model.jogadorGetNumMaosFinalizadas(indiceJ));

			if (model.jogadorGetNumMaosAtivas(indiceJ) == model.jogadorGetNumMaosFinalizadas(indiceJ))
				botoesJogadorAtivos = false;
			estadoJogo = DEALER;
		}
	}

	public void jogadorHitCond() {
		if (botoesJogadorAtivos == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}

		int status = 0;

		if (apostaInicialFeita == false)
			status = 1;

		else if (model.jogadorVerificaMaoQuebrada(jogadorAtivo, maoCorrente) == true)
			status = 2;

		else if (model.jogadorVerificaMaoFinalizada(jogadorAtivo, maoCorrente) == true)
			status = 3;

		switch (status) {
		case 0:
			jogadorHit(jogadorAtivo, maoCorrente);
			System.out.printf("Hit na mao %d realizado com sucesso.\n", maoCorrente + 1);
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Hit permitido depois da aposta inicial.");
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Hit não permitido para uma mão quebrada.");
			break;

		case 3:
			JOptionPane.showMessageDialog(null, "Hit não permitido depois do fim do turno.");
			break;
		}
	}

	///////////
	/* SPLIT */
	void jogadorSplit(int indiceJ) {
		model.jogadorSplit(indiceJ);
		mudancaJogadorBalanco();
		for (int i = 0; i < maosMax; i++) {
			mudancaJogadorMao(i);
			mudancaJogadorAposta(i);

			if (model.jogadorVerificaBlackjack(jogadorAtivo, i))
				JOptionPane.showMessageDialog(null, String.format("JOGADOR TEM BLACKJACK NA MAO %d!!!\n", i + 1));
		}

		defineMaoCorrente(0);
		janelaJogador.get(1).setVisible(true);
	}

	public void jogadorSplitCond() {
		if (botoesJogadorAtivos == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}

		int status = 0;

		if (apostaInicialFeita == false)
			status = 1;

		else if (model.jogadorGetNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorVerificaMaoQuebrada(jogadorAtivo, 0) == true)
			status = 3;

		else if (model.jogadorVerificaMaoFinalizada(jogadorAtivo, 0) == true)
			status = 4;

		else if (model.jogadorVerificaDobraAposta(jogadorAtivo) == false)
			status = 5;

		else if (model.jogadorVerificaPrimCartasMesmoValor(jogadorAtivo) == false)
			status = 6;

		switch (status) {
		case 0:
			jogadorSplit(jogadorAtivo);
			System.out.println("Split realizado com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Split permitido depois da aposta inicial.");
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Split permitido apenas com uma mao.");
			break;

		case 3:
		case 4:
			JOptionPane.showMessageDialog(null, "Mão atual já finalizou o turno.");
			break;

		case 5:
			JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
			break;

		case 6:
			JOptionPane.showMessageDialog(null, "Cartas precisam ter mesmo valor para realizar Split");
			break;
		}
	}

	///////////
	/* STAND */
	void jogadorStand(int indiceJ, int indiceMao) {
		model.jogadorStand(indiceJ, indiceMao);

		if (model.jogadorGetNumMaosAtivas(indiceJ) == model.jogadorGetNumMaosFinalizadas(indiceJ)) {
			botoesJogadorAtivos = false;
			estadoJogo = DEALER;
		}
	}

	public void jogadorStandCond() {
		if (botoesJogadorAtivos == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		String msg;
		int status = 0;

		if (apostaInicialFeita == false)
			status = 1;

		else if (model.jogadorVerificaMaoQuebrada(jogadorAtivo, maoCorrente) == true)
			status = 2;

		else if (model.jogadorVerificaMaoFinalizada(jogadorAtivo, maoCorrente) == true)
			status = 3;

		switch (status) {
		case 0:
			msg = String.format("Stand na mão %d realizado com sucesso.\n", maoCorrente + 1);
			JOptionPane.showMessageDialog(null, msg);
			jogadorStand(jogadorAtivo, maoCorrente);
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Stand permitido depois da aposta inicial.");
			break;

		case 2:
		case 3:
			JOptionPane.showMessageDialog(null, "Mão atual já finalizou o turno.");
			break;
		}
	}

	///////////////
	/* SURRENDER */
	void jogadorSurrender(int indiceJ) {
		model.jogadorSurrender(indiceJ);
		mudancaJogadorBalanco();
	}

	public void jogadorSurrenderCond() {
		if (botoesJogadorAtivos == false) {
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}

		int status = 0;

		if (apostaInicialFeita == false)
			status = 1;

		else if (model.jogadorGetNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorGetNumCartas(jogadorAtivo, 0) > 2)
			status = 3;

		else if (dealerBlackjack)
			status = 4;

		switch (status) {
		case 0:
			estadoJogo = NOVA_RODADA;
			jogadorSurrender(jogadorAtivo);
			botoesJogadorAtivos = false;
			System.out.println("Surrender realizado com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Surrender permitido depois da aposta inicial."); // Depois gerar uma
																									 // tela com a
																									 // mensagem
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Surrender permitido apenas com uma mão."); // Depois gerar uma tela com
																							 // a mensagem
			break;
		case 3:
			JOptionPane.showMessageDialog(null, "Surrender permitido apenas na mão inicial."); // Depois gerar uma tela
																								 // com a mensagem
			break;

		case 4:
			JOptionPane.showMessageDialog(null, "Surrender não permitido, porque Dealer tem Blackjack.");
			break;
		}
	}

	/////////////////////
	/* REINICIALIZACAO */

	void limpaParticipantes() {
		model.limpaParticipantes();
		mudancaNovaRodada();
	}
}
