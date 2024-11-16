package view;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;

import javax.swing.*;
import control.Controller;
import control.Observer;
import control.Observable;


/**
 * Janela responsavel pela exibicao da mao de indice indexMao
 * do jogador indexJ.
 * Implementa o padrao Observer.
 * Conecta-se ao pacote control via chamadas ao Controller.
 */
public class JanelaJogador extends JFrame implements Observer {
	// Identificador serial da classe
	private static final long	serialVersionUID = 1L;
	
	// Constantes auxiliares
	private final int	LARG_DFL = 768,
					  	ALT_DFL = 546;
	
	private final String	msgCreditos = "Créditos: $",
							msgAposta = "Aposta: $",
							msgPontos = "Pontos: ";
	
	// Interface com o Controller
	private int	indexJ,		// Indice do jogador respectivo a janela
				indexMao; 	// Indice da mao
	
	private Controller ctrl = Controller.getController();
	
	// Elementos de tela
    private JLabel 	lblCreditos,
    				lblValorAposta,
    				lblPontos;
    
    private JJPanel painel;
    Container c = getContentPane();
	
    ///////////////////////////////////////
    // Construtor
    public JanelaJogador(int indexJ, int indexMao) {
        super("Janela do Jogador");
        int x, y;
        
    	this.indexJ = indexJ;
        this.indexMao = indexMao;
        
        // Centraliza janela
        x = ScreenSize.getWidth() / 2 - LARG_DFL / 2;
		y = ScreenSize.getHeight() / 2 - ALT_DFL / 2;
		setBounds(x, y, LARG_DFL, ALT_DFL);
		
		// Define o painel da imagem de fundo
		painel = new JJPanel();
		painel.setBackground(Color.WHITE);
		painel.setBounds(0, 0, LARG_DFL, ALT_DFL);
		
		// Adiciona os componentes ao painel
		c.add(painel);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        inicializarComponentes();
    }
    
    ///////////////////////////////////////
    // Metodos de instancia
    private void inicializarComponentes() {
        setLayout(null);

        // Créditos do jogador
        lblCreditos = new JLabel(msgCreditos + ctrl.getJogadorBalanco(indexJ));
        lblCreditos.setBounds(20, 20, 200, 30);
        painel.add(lblCreditos);

        // Valor da Aposta
        lblValorAposta = new JLabel("Aposta: $0");
        lblValorAposta.setBounds(20, 60, 200, 30);
        painel.add(lblValorAposta);
        
        // Pontos do jogador
        lblPontos = new JLabel(msgPontos + ctrl.getJogadorPontos(indexJ, indexMao));
        lblPontos.setBounds(20, 100, 200, 30);
        painel.add(lblPontos);
        
        // Completar com exibicao das cartas
    }
    
    public void notify(Observable o) {
    	int estado = o.get();
    	update(estado);
    }
    
    private void update(int estado) {
    	if (estado % 10 != indexMao)
    		return;
    	
    	estado -= indexMao;
    	
    	switch (estado) {
    	case MUD_JOGADOR_APOSTA:
    		atualizaAposta();
    		break;
    	
    	case MUD_JOGADOR_BALANCO:
    		atualizaBalanco();
    		break;
    		
    	case MUD_JOGADOR_MAO:
    		atualizaCartas();
    		break;
    	}
    }
    
    private void atualizaAposta() {
    	lblValorAposta.setText(msgAposta + ctrl.getJogadorAposta(indexJ, indexMao));
    }
    
    private void atualizaBalanco() {
    	lblValorAposta.setText(msgAposta + ctrl.getJogadorBalanco(indexJ));
    }
    
    private void atualizaCartas() {
    	lblPontos.setText(msgPontos + ctrl.getJogadorPontos(indexJ, indexMao));
    	atualizaImagemCartas();
    	// Completar com exibicao das cartas
    }
    
    private void atualizaImagemCartas() {
		ArrayList<ArrayList<String>> jogadorCartas = ctrl.getJogadorCartas(indexJ, indexMao);
		ProcessadorImagem.atualizaImagemCartas(painel, jogadorCartas);
	}
}
