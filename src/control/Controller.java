package control;

import model.FachadaModel;
import view.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class MecanicaJogo implements Runnable {
	@Override
	public void run() {
		Controller ctrl = Controller.getController();
		ctrl.painelJogo();
	}
}

public class Controller implements Observable {
	// DEFINICOES AUXILIARES SALVAMENTO
	private final int 	FALHA = -1,
						SUCESSO = 0,
						ARQS_INEXIST = 1,
						DIRETORIO_INEXIST = 2;
	
	// SINGLETON
	private static Controller ctrl = null;

	// FACADE
	private FachadaModel model = FachadaModel.getFachada();

	// OBSERVERS
	private List<Observer> lObserverBanca = new ArrayList<Observer>();
	private List<Observer> lObserverJogador = new ArrayList<Observer>();

	// JANELAS
	private JanelaInicial menu;
	private JanelaBanca janelaBanca;
	private List<JanelaJogador> janelaJogador = new ArrayList<JanelaJogador>(); // Adicionar indice de janela
	
	// SALVAMENTO
	private final String 	pastaSalvamento = "salvamento",
							nomeFiltro = "Arquivos de Salvamento",
							extensaoSalvamento = "txt";
	private final File		diretorioSalvamento = new File(System.getProperty("user.di"), pastaSalvamento);
	
	// Tabela de estados do fluxo de jogo
	private int estadoJogo; // -1 para banca e index para jogadores

	private final int JOGADOR = 0;
	private final int DEALER = -1;
	private final int DEALER_QUEBRA = -2;
	private final int CHECA_VENCEDOR = -3;
	private final int NOVA_RODADA = -4;

	// Tabela de estados do observable
	private int estadoObservable;

	public final int MUD_DEALER_MAO = -1;
	public final int MUD_JOGADOR_MAO = 100;
	public final int MUD_JOGADOR_APOSTA = 200;
	public final int MUD_JOGADOR_BALANCO = 300;

	// VARIAVEIS DE CONTROLE
	private int maoCorrente;
	private int numJogadores;
	private int maosMax;
	private int hitUntil;
	private int cartasNovaRodada;

	private int jogadorAtivo = 0;
	private boolean apostaOK = false;
	private boolean dealerBlackjack = false;
	protected volatile boolean buttonsSwitch = true; // A flag compartilhada
	
	// INSTANCIACAO
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

	///////////////////////////////
	// PADRAO OBSERVER
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

	///////////////////////////////
	// MANIPULACAO DE JANELAS
	JanelaInicial criaMenu() {
		JanelaInicial janela = new JanelaInicial();

		janela.getBtnJogoNovo().addActionListener(new JogoNovo());
		janela.getBtnJogoSalvo().addActionListener(new JogoSalvo());

		return janela;
	}

	JanelaBanca criaJBanca() {
		JanelaBanca janela = new JanelaBanca();

		janela.getBtnSave().addActionListener(new SalvarJogo());

		return janela;
	}

	JanelaJogador criaJJogador(int idxMao) {
		JanelaJogador janela = new JanelaJogador(0, idxMao);
		return janela;
	}
	
	public void defineMaoCorrente(int indexMao) {
		maoCorrente = indexMao;
		System.out.println("MAO COORENTE " + maoCorrente);
		
		for (JanelaJogador j : janelaJogador) {
			j.mudaJanelaCor(maoCorrente);
		}
	}
	
	///////////////////////////////
	// RECUPERACAO DE JOGO SALVO
	
	/*** Sets (Ponte entre Control e Model) ***/
	
	/** Dealer **/
	/**
	 * Define mao do dealer com base nas cartas passadas como argumento.
	 * Notifica observadores de Eventos da Banca.
	 * 
	 * @param cartas Lista de listas com dois elementos da forma (naipe, valor).
	 */
	void setDealerMao(ArrayList<ArrayList<String>> cartas) {
		model.setDealerMao(cartas);
		mudancaDealerMao();
		
		// Se dealer tiver blackjack, atualiza flag correspondente do ctrl
		dealerBlackjack = model.dealerPossuiBlackjack();
	}
	
	/** Jogador **/
	/**
	 * Define a aposta da mao indiceMao do jogador como o valor passado como argumento.
	 * Notifica observadores de Eventos do Jogador.
	 * 
	 * @param indiceMao Indice da mao respectiva a aposta.
	 * @param valor Valor da aposta.
	 */
	void setJogadorApostaMao(int indiceMao, int valor) {
		model.setApostaMaoJogador(jogadorAtivo, indiceMao, valor);
		mudancaJogadorAposta(indiceMao);
	}
	
	/**
	 * Define o balanco do jogador como o valor passado como argumento.
	 * Notifica observadores de Eventos do Jogador.
	 * 
	 * @param valor Valor do balanco.
	 */
	void setJogadorBalanco(int valor) {
		model.setBalancoJogador(jogadorAtivo, valor);
		mudancaJogadorBalanco();
	}
	
	/**
	 * Define mao indiceMao do jogador com base nas cartas passadas como argumento.
	 * Notifica observadores de Eventos do Jogador.
	 * 
	 * @param indiceMao Indice da mao a receber as cartas.
	 * @param cartas Lista de listas com dois elementos da forma (naipe, valor).
	 */
	void setJogadorMao(int indiceMao, ArrayList<ArrayList<String>> cartas) {
		model.setMaoJogador(jogadorAtivo, indiceMao, cartas);
		mudancaJogadorMao(indiceMao);
	}
	
	/**
	 * Define numero de maos ativas do jogador como numMaos.
	 */
	void setJogadorNumMaosAtivas(int numMaos) {
		model.setJogadorNumMaosAtivas(jogadorAtivo, numMaos);
	}
	
	/**
	 * Define como quebrada a mao indiceMao do jogador. Isso tambem finaliza
	 * o turno dessa mao (estado finalizada == true).
	 * 
	 * @param indiceMao Indice da mao a receber o estado de quebrada.
	 */
	void setJogadorQuebraMao(int indiceMao) {
		model.jogadorQuebra(jogadorAtivo, indiceMao);
	}
	
	/*** Atualizacoes ***/
	
	/** Dealer **/
	/**
	 * Extrai informacoes de infoSalva e atualiza a mao do dealer no model.
	 * Faz uso das funcoes set, definidas acima.
	 * Indiretamente, notifica observadores de Eventos da Banca.
	 * 
	 * @param scannerSecao Scanner obtido de {@code atualizaSecao} com informacoes sobre
	 * o dealer.
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
	
	/** Jogador **/
	/**
	 * Extrai informacoes de infoSalva e atualiza o jogador no model.
	 * Faz uso das funcoes set, definidas acima.
	 * Indiretamente, notifica observadores de Eventos do Jogador.
	 * 
	 * @param scannerSecao Scanner obtido de {@code atualizaSecao} com informacoes sobre
	 * o jogador.
	 */
	void atualizaJogador(Scanner scannerSecao) {
		String 	infoSalva;
		Scanner scannerAux; // Scanner auxiliar usado para processar a secao em subsecoes
		int 	numMaosAtivas;
		
		// Logging para depuracao
		System.out.println("ATUALIZA JOGADOR");
		
		/* Define delimitador a ser usado (**).
		 * As barras indicam que * deve aparecer duas vezes e de forma consecutiva.
		 */
		scannerSecao.useDelimiter("\\*\\*");
		
		// Obtem secao pertinente de informacao
		infoSalva = scannerSecao.next();
		
		// Processa secao de informacao
		scannerAux = new Scanner(infoSalva);
		
		// Define se jogador ja realizou a aposta
		apostaOK = scannerAux.nextBoolean();
		
		// Atualiza balanco do jogador
		setJogadorBalanco(scannerAux.nextInt());
		
		// Atualiza numero de maos ativas
		numMaosAtivas = scannerAux.nextInt();
		setJogadorNumMaosAtivas(numMaosAtivas);
		
		for (int i = 0; i < numMaosAtivas; i++) {
			atualizaMaoJogador(i, scannerSecao.next());
			janelaJogador.get(i).setVisible(true);
			janelaJogador.get(i).mudaJanelaCor(maoCorrente);
		}
		
		// Se jogador tiver feito split de ases, model ativa flag correspondente
		model.jogadorFezSplitAses(jogadorAtivo);
		
		scannerAux.close();
		// Nao devemos fechar scannerSecao aqui, porque sera usado em outras funcoes
	}
	
	/**
	 * Extrai informacoes de infoSalva e atualiza a mao indiceMao no model.
	 * Faz uso das funcoes set, definidas acima.
	 * Indiretamente, notifica observadores de Eventos do Jogador.
	 * 
	 * @param indiceMao Indice da mao a ser atualizada.
	 * @param infoSalva String que contem informacoes a respeito da mao do jogador. Extraida
	 * da funcao atualizaJogador.
	 */
	void atualizaMaoJogador(int indiceMao, String infoSalva) {
		ArrayList<ArrayList<String>> 	cartas = new ArrayList<ArrayList<String>>();
		ArrayList<String> 				carta;
		String 							estadoMao;
		Scanner 						scanner;
		int								numCartas;
		
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
		switch(estadoMao) {
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
	
	/** Geral **/
	
	/**
	 * Identifica qual o tipo da secao e chama a funcao de atualizacao
	 * correspondente.
	 * 
	 * @param secaoInfo Secao extraida do arquivo vindo de {@code jogoSalvoRestauraContexto}.
	 */
	void atualizaSecao(String secaoInfo) {
		Scanner scannerSecao = new Scanner(secaoInfo);
		String tipoSecao = scannerSecao.next().trim();
		
		// Logging para depuracao
		System.out.println(tipoSecao);
		
		switch(tipoSecao) {
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
	 * @param arquivoSalvamento Arquivo vindo de {@code jogoSalvoRecupera}.
	 */
	int jogoSalvoRestauraContexto(File arquivoSalvamento) {
		String 	secaoInfo;
		Scanner scannerArquivo;
		int 	status = SUCESSO;
		
		// Logging para depuracao
		System.out.println("JogoSalvoRestauraContexto EXECUTADO");
		
		try {
			// Configura Scanner
			scannerArquivo  = new Scanner(arquivoSalvamento);
			scannerArquivo.useDelimiter("\\$\\$"); // Usa $$ como delimitador
			
			// Enquanto houver secoes
			while (scannerArquivo.hasNext())
			{
				// Extrai e atualiza secao
				secaoInfo = scannerArquivo.next().trim();
				atualizaSecao(secaoInfo);
			}
			
			// Libera recursos
			scannerArquivo.close();
		}
		catch(IOException e) { // Falha na abertura do scanner ou na leitura do arquivo
			System.out.println(e.getMessage());
			status = FALHA;
		}
		
		return status;
	}
	
	/**
	 * Verifica se diretorio possui arquivos com a extensao recebida.
	 * Usada como funcao auxiliar {@code jogoSalvoRecupera}, para impedir
	 * que o usuario acesse a pasta de salvamento, se esta estiver vazia.
	 * 
	 * @implNote
	 * status: <br>
	 * SUCESSO				= OK <br>
	 * ARQS_INEXIST			= Diretorio existe, mas arquivos com extensao inexistem <br>
	 * DIRETORIO_INEXIST 	= Diretorio nao existe <br>
	 * 
	 * @param diretorio Diretorio no qual se deseja procurar os arquivos.
	 * @param extensao Extensao com que devem terminar os arquivos. Escreva sem
	 * o ".". Por exemplo, escreva "txt" e nao ".txt".
	 */
	int validaDiretorioNaoVazio(File diretorio, String extensao) {
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
                System.out.println(String.format("O diretório está vazio para a extensão especificada (.%s).", extensao));
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
	 * Permite ao usuario escolher o arquivo de salvamento usado na recuperacao
	 * do jogo. Se falha na escolha, inicia novo jogo sem salvamento.
	 * Faz uso do JFileChooser e de outras classes Swing.
	 */
	void jogoSalvoRecupera() {
		JFileChooser seletorArquivo = new JFileChooser();
		File /* diretorioSalvamento, */ arquivoEscolhido;
		FileNameExtensionFilter filtro = new FileNameExtensionFilter(nomeFiltro, extensaoSalvamento);
		int resultado;
		
		// Obtem diretorio de salvamento de forma portavel
		// diretorioSalvamento = new File(System.getProperty("user.dir"), pastaSalvamento);
		
		// Logging para depuracao
		System.out.println(diretorioSalvamento.getAbsolutePath());
		
		// Verificacao Diretorio Vazio
		resultado = validaDiretorioNaoVazio(diretorioSalvamento, extensaoSalvamento);
		
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
				JOptionPane.showMessageDialog(null, "Houve um erro na escolha do arquivo de salvamento. Novo jogo iniciado sem salvamento.");
				break;
			}
		}
		
		else
			JOptionPane.showMessageDialog(null, "Ainda não há arquivos de salvamento. Novo jogo iniciado sem salvamento.");
		
		// Inicializa mecanica do jogo
		initJanelas(model.jogadorNumMaosAtivas(jogadorAtivo));
		estadoJogo = JOGADOR;
		inicializaMecanicaJogo();
	}
	
	///////////////////////////////
	// SALVAMENTO DO JOGO
	
	/** Dealer **/
	void salvaDealer(File arquivoSalvamento) {
		BufferedWriter escritor;
		ArrayList<ArrayList<String>> cartas = model.getCartasDealer();
		
		try {
			escritor = new BufferedWriter(new FileWriter(arquivoSalvamento, true));
			escritor.write("DEALER");
			escritor.newLine(); // Escreve new line de forma portavel
			
			if (apostaOK) {
				for (ArrayList<String> carta : cartas) {
					escritor.write(String.format("%s %s", carta.get(0), carta.get(1)));
					escritor.newLine();
				}
			}
			escritor.write("$$");
			escritor.newLine();
			escritor.close();
		}
		
		catch(IOException e) {
			System.out.println("Erro no salvamento do arquivo.");
		}
	}
	
	/** Jogador **/
	void salvaJogador(File arquivoSalvamento) {
		BufferedWriter escritor;
		ArrayList<ArrayList<String>> cartas;
		
		try {
			escritor = new BufferedWriter(new FileWriter(arquivoSalvamento, true));
			escritor.write("JOGADOR");
			escritor.newLine(); // Escreve new line de forma portavel
			escritor.write(Boolean.toString(apostaOK));
			escritor.newLine();
			escritor.write(Integer.toString(model.jogadorBalanco(jogadorAtivo)));
			escritor.newLine();
			
			if (apostaOK) {
				escritor.write(Integer.toString(model.jogadorNumMaosAtivas(jogadorAtivo)));
				escritor.newLine();
				
				for (int i = 0; i < model.jogadorNumMaosAtivas(jogadorAtivo); i++) {
					cartas = model.getCartasJogador(jogadorAtivo, i);
					
					escritor.write("**");
					escritor.newLine();
					
					escritor.write(Integer.toString(model.jogadorAposta(jogadorAtivo, i)));
					escritor.newLine();
					
					if (model.jogadorMaoQuebrada(jogadorAtivo, i))
						escritor.write("Q");
					else if (model.jogadorMaoFinalizada(jogadorAtivo, i))
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
		
		catch(IOException e) {
			System.out.println("Erro no salvamento do arquivo.");
		}
	}
	
	
	/** Geral **/
	
	void salvamentoEscreveArquivo(File arquivoSalvamento) {
		FileWriter limpador;
		
		try {
			limpador = new FileWriter(arquivoSalvamento, false); // Limpa conteudo do arquivo
			limpador.close();
		}
		
		catch(IOException e) {
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


	///////////////////////////////
	// ROTINAS DE CONTROLE DE JOGO
	void init() {
		/*
		 * Necessario criar janelas aqui em vez de no construtor, para evitar chamada
		 * recursiva de getController
		 */
		model = FachadaModel.getFachada();

		maosMax = model.getMaosMax();
		numJogadores = model.getNumJogadores();
		hitUntil = model.getHitUntil();
		cartasNovaRodada = model.getCartasNovaRodada();
		maoCorrente = 0;
		
		estadoJogo = NOVA_RODADA;
		apostaOK = false;

		menu = criaMenu();
		janelaBanca = criaJBanca();
		this.addObserver(janelaBanca, 'B');

		for (int i = 0; i < maosMax; i++) {
			janelaJogador.add(criaJJogador(i));
			this.addObserver(janelaJogador.get(i), 'J');
		}

		menu.setVisible(true);
		System.out.println("INIT");///////////////////////////
	}
	
	/**
	 * Inicializa as janelas da aplicacao.
	 * @param numMaosAtivas Numero de maos ativas do jogador corrente. Define o numero
	 * de janelas do jogador a serem abertas.
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
		initJanelas(model.jogadorNumMaosAtivas(jogadorAtivo));
		inicializaMecanicaJogo();
	}
	
	//////////////////////////////////////
	// PAINEL DO JOGO

	void painelJogo() {
		while (true) {
			switch (estadoJogo) {
			case NOVA_RODADA: // OK
				novaRodada();
				break;

			case DEALER: // OK
				dealerVez();
				break;

			case DEALER_QUEBRA: // OK
				dealerQuebra();
				break;

			case CHECA_VENCEDOR:
				checaVencedor();
				break;

			case JOGADOR:
			default:
				jogadorVez(estadoJogo);
				break;
			}
		}
	}

	///////////////////////////////
	// ETAPAS DO JOGO

	void novaRodada() { // OK
		System.out.println("NOVA RODADA EXECUTADA");
		if (model.jogadorVerificaBalancoMinimo(0) == false) // Se houver mais participantes, faca um for
		{
			JOptionPane.showMessageDialog(null, "Saldo insuficiente. Fim do Jogo");
			System.exit(0);
		}

		limpaParticipantes();
		model.embaralhaFonte();
		apostaOK = false;

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

		dealerBlackjack = model.dealerPossuiBlackjack();

		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < cartasNovaRodada; j++)
				jogadorHit(i, 0); // Jogadores compram duas cartas na mao de indice 0
		}
	}

	void jogadorVez(int indexJ) {
		buttonsSwitch = true;
		System.out.println("JOGADOR VEZ EXECUTADO");
		while (buttonsSwitch);
		System.out.println("FIM DE JOGADOR VEZ");
	}

	void dealerVez() // OK
	{
		System.out.println("DEALER VEZ EXECUTADO");
		if (dealerBlackjack) {
			estadoJogo = CHECA_VENCEDOR;
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("DEALER BLACKJACK RETURN"); // TODO JOptionPane
			return;
		}
		
		else if (model.dealerCalculaPontos() >= hitUntil) {
			System.out.println("BANCA NAO FARÁ HIT"); // TODO JOptionPane
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		while (model.dealerCalculaPontos() < hitUntil) {
			System.out.println("BANCA FEZ HIT"); // TODO JOptionPane
			dealerHit();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (model.dealerQuebra() == true) {
				estadoJogo = DEALER_QUEBRA;
				JOptionPane.showMessageDialog(null, "DEALER QUEBROU!!!"); // TODO JOptionPane
				return;
			}
		}
		
		estadoJogo = CHECA_VENCEDOR;
		System.out.println("FIM DE DEALER VEZ");
	}

	void dealerQuebra() // OK
	{
		String msg;
		System.out.println("DEALER QUEBRA EXECUTADO");
		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < maosMax; j++) {
				if (model.jogadorMaoAtiva(i, j) == true) {
					msg = String.format("Jogador %d | Mão %d: " , i + 1, j + 1);
					
					if (model.jogadorMaoQuebrada(i, j)) {
						jogadorRecuperaAposta(i, j);
						msg += "EMPATE\n";
					}
					else {
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

	void checaVencedor() { // OK
		String msg;
		int jogadorVencedorFlag;
		/* 
		 * maior que 0	-> Jogador vence
		 * igual a 0 	-> Empate
		 * menor que 0 	-> Dealer vence
		 */
		System.out.println("CHECA VENCEDOR EXECUTADO");
		for (int i = 0; i < numJogadores; i++) {
			for (int j = 0; j < maosMax; j++) {
				if (model.jogadorMaoAtiva(i, j) == true) {
					msg = String.format("Jogador %d | Mão %d: " , i + 1, j + 1);
					
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

	/////////////////////////////////////////////
	// GERAL

	void mudancaNovaRodada() {
		mudancaDealerMao();
		mudancaJogadorBalanco();

		for (int i = 0; i < maosMax; i++) {
			mudancaJogadorMao(i);
			mudancaJogadorAposta(i);
		}
	}

	void limpaParticipantes() {
		model.limpaParticipantes();
		mudancaNovaRodada();
	}

	//////////////////////////////////////////////
	// DEALER

	/* Gets */

	public int getDealerPontos() {
		return model.dealerCalculaPontos();
	}

	public ArrayList<ArrayList<String>> getDealerCartas() {
		return model.getCartasDealer();
	}

	void mudancaDealerMao() {
		estadoObservable = MUD_DEALER_MAO;
		notificaEventoBanca();
	}

	void dealerHit() {
		model.dealerHit();
		mudancaDealerMao();
	}

	//////////////////////////////////////////////
	// JOGADOR

	/* Gets */
	public int getJogadorAposta(int indexJ, int indexMao) {
		return model.jogadorAposta(indexJ, indexMao);
	}

	public int getJogadorBalanco(int indexJ) {
		return model.jogadorBalanco(indexJ);
	}

	public ArrayList<ArrayList<String>> getJogadorCartas(int indexJ, int indexMao) {
		return model.getCartasJogador(indexJ, indexMao);
	}

	public int getJogadorPontos(int indexJ, int indexMao) {
		return model.jogadorCalculaPontos(indexJ, indexMao);
	}

	/* MUDANCA */
	void mudancaJogadorBalanco() {
		estadoObservable = MUD_JOGADOR_BALANCO;
		notificaEventoJogador();
	}

	void mudancaJogadorAposta(int indexMao) {
		estadoObservable = MUD_JOGADOR_APOSTA + indexMao;
		notificaEventoJogador();
		estadoObservable = MUD_JOGADOR_BALANCO;
		notificaEventoJogador();
	}

	void mudancaJogadorMao(int indexMao) {
		estadoObservable = MUD_JOGADOR_MAO + indexMao;
		notificaEventoJogador();
	}

	/* APOSTA */
	public void jogadorIncrementaApostaInicial(int valor) { // OK
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		int status = 0;

		if (model.jogadorIncrementaAposta(jogadorAtivo, 0, valor) == false)
			status = 1;

		else if (apostaOK)
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

	void jogadorDeal() { // OK
		apostaOK = true;
		compraInicialCartas();
		mudancaNovaRodada();
	}

	/**
	 * Deal eh sempre feito na mao de indice 0.
	 * 
	 * @param indexJ
	 */
	public void jogadorDealCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			System.out.println("Não está na vez do jogador.");
			return;
		}
		int status = 0;

		if (model.jogadorValidaApostaInicial(jogadorAtivo) == false)
			status = 1;

		else if (apostaOK)
			status = 2;

		switch (status) {
		case 0:
			jogadorDeal();
			System.out.println("Aposta realizada com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Valor abaixo da aposta minima."); // Depois gerar uma tela com a mensagem
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Aposta já realizada."); // Depois gerar uma tela com a mensagem
			break;
		}
	}

	void jogadorClear() { // OK
		model.jogadorClear(jogadorAtivo);
		mudancaJogadorAposta(0);
		mudancaJogadorBalanco();
	}

	public void jogadorClearCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		int status = 0;
		
		if (apostaOK)
			status = 1;
		
		switch(status) {
		case 0:
			jogadorClear();
			System.out.println("Clear realizado com sucesso.");
			break;
			
		case 1:
			JOptionPane.showMessageDialog(null, "Clear não permitido depois de aposta já realizada.");
			break;
		}
	}

	void jogadorVenceAposta(int indexJ, int indexMao) {
		model.jogadorVenceAposta(indexJ, indexMao);
		mudancaJogadorBalanco();
	}
			
	void jogadorRecuperaAposta(int indexJ, int indexMao) {
		model.jogadorRecuperaAposta(indexJ, indexMao);
		mudancaJogadorBalanco();
	}

	/* SURRENDER */
	void jogadorSurrender(int indexJ) { // OK
		model.jogadorSurrender(indexJ);
		mudancaJogadorBalanco();
	}

	public void jogadorSurrenderCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		
		int status = 0;

		if (apostaOK == false)
			status = 1;

		else if (model.jogadorNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorGetNumCartas(jogadorAtivo, 0) > 2)
			status = 3;

		else if (dealerBlackjack)
			status = 4;

		switch (status) {
		case 0:
			estadoJogo = NOVA_RODADA;
			jogadorSurrender(jogadorAtivo);
			buttonsSwitch = false;
			System.out.println("Surrender realizado com sucesso.");
			break;

		case 1:
			JOptionPane.showMessageDialog(null, "Surrender permitido depois da aposta inicial."); // Depois gerar uma tela com a mensagem
			break;

		case 2:
			JOptionPane.showMessageDialog(null, "Surrender permitido apenas com uma mão."); // Depois gerar uma tela com a mensagem
			break;
		case 3:
			JOptionPane.showMessageDialog(null, "Surrender permitido apenas na mão inicial."); // Depois gerar uma tela com a mensagem
			break;

		case 4:
			JOptionPane.showMessageDialog(null, "Surrender não permitido, porque Dealer tem Blackjack.");
			break;
		}
	}

	/* HIT */
	void jogadorHit(int indexJ, int indexMao) {
		String msg;
		model.jogadorHit(indexJ, indexMao);
		mudancaJogadorMao(indexMao);

		if (model.jogadorQuebra(indexJ, indexMao) == true)
		{
			msg = "Mão " + (indexMao + 1) + " quebrou";
			JOptionPane.showMessageDialog(null, msg);
			
			System.out.println(model.jogadorNumMaosAtivas(indexJ));
			System.out.println(model.jogadorNumMaosFinalizadas(indexJ));
			
			// TODO extrair funcao
			if (model.jogadorNumMaosAtivas(indexJ) == model.jogadorNumMaosFinalizadas(indexJ))
				buttonsSwitch = false;
				estadoJogo = DEALER;
		}
	}

	public void jogadorHitCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		
		
		int status = 0;

		if (apostaOK == false)
			status = 1;

		else if (model.jogadorMaoQuebrada(jogadorAtivo, maoCorrente) == true)
			status = 2;

		else if (model.jogadorMaoFinalizada(jogadorAtivo, maoCorrente) == true)
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

	/* STAND */
	void jogadorStand(int indexJ, int indexMao) {
		model.jogadorStand(indexJ, indexMao);
		
		if (model.jogadorNumMaosAtivas(indexJ) == model.jogadorNumMaosFinalizadas(indexJ))
		{
			buttonsSwitch = false;
			estadoJogo = DEALER;
		}
	}

	public void jogadorStandCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		String msg;
		int status = 0;
		
		if (apostaOK == false)
			status = 1;

		else if (model.jogadorMaoQuebrada(jogadorAtivo, maoCorrente) == true)
			status = 2;

		else if (model.jogadorMaoFinalizada(jogadorAtivo, maoCorrente) == true)
			status = 3;

		switch (status) {
		case 0:
			msg = "Stand na mao " + (maoCorrente + 1) + " realizado com sucesso.\n";
			JOptionPane.showMessageDialog(null,  msg);
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

	/* DOUBLE */
	void jogadorDouble(int indexJ) {
		model.jogadorDouble(indexJ);
		mudancaJogadorBalanco();
		mudancaJogadorAposta(0);
		mudancaJogadorMao(0);
		
		if (model.jogadorNumMaosAtivas(indexJ) == model.jogadorNumMaosFinalizadas(indexJ))
		{
			buttonsSwitch = false;
			estadoJogo = DEALER;
		}
	}

	public void jogadorDoubleCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		int status = 0;

		if (apostaOK == false)
			status = 1;

		else if (model.jogadorNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorMaoQuebrada(jogadorAtivo, 0) == true)
			status = 3;

		else if (model.jogadorMaoFinalizada(jogadorAtivo, 0) == true)
			status = 4;

		else if (model.jogadorSaldoSuficienteDobra(jogadorAtivo) == false)
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

	/* SPLIT */
	void jogadorSplit(int indexJ) {
		model.jogadorSplit(indexJ);
		mudancaJogadorBalanco();
		for (int i = 0; i < maosMax; i++) {
			mudancaJogadorMao(i);
			mudancaJogadorAposta(i);
		}
		
		defineMaoCorrente(0);
		janelaJogador.get(1).setVisible(true);
	}

	public void jogadorSplitCond() {
		if (buttonsSwitch == false)
		{
			// TODO implementar em todos os botões
			JOptionPane.showMessageDialog(null, "Não está na vez do jogador.");
			return;
		}
		int status = 0;

		if (apostaOK == false)
			status = 1;

		else if (model.jogadorNumMaosAtivas(jogadorAtivo) > 1)
			status = 2;

		else if (model.jogadorMaoQuebrada(jogadorAtivo, 0) == true)
			status = 3;

		else if (model.jogadorMaoFinalizada(jogadorAtivo, 0) == true)
			status = 4;

		else if (model.jogadorSaldoSuficienteDobra(jogadorAtivo) == false)
			status = 5;

		else if (model.jogadorPrimCartasMesmoValor(jogadorAtivo) == false)
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
	
	public void exit()
	{
		System.exit(0);
	}
}
