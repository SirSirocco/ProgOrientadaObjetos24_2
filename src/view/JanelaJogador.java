package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.Jogador;

public class JanelaJogador extends JFrame {

    private Jogador jogador; // Referência ao modelo do jogador
    private JLabel lblCreditos;
    private JLabel lblValorAposta;
    private JLabel lblCartas;
    private JButton btnHit, btnStand, btnDouble, btnSurrender;

    public JanelaJogador(Jogador jogador) {
        this.jogador = jogador;
        setTitle("Janela do Jogador");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(null);

        // Créditos do jogador
        lblCreditos = new JLabel("Créditos: $" + jogador.getBalanco());
        lblCreditos.setBounds(20, 20, 200, 30);
        add(lblCreditos);

        // Valor da Aposta
        lblValorAposta = new JLabel("Aposta: $0");
        lblValorAposta.setBounds(20, 60, 200, 30);
        add(lblValorAposta);

        // Cartas do Jogador
        lblCartas = new JLabel("Cartas: Nenhuma");
        lblCartas.setBounds(20, 100, 400, 30);
        add(lblCartas);

        // Botões de Ação
        btnHit = new JButton("Hit");
        btnHit.setBounds(20, 150, 100, 30);
        btnHit.addActionListener(e -> realizarAcaoHit());
        add(btnHit);

        btnStand = new JButton("Stand");
        btnStand.setBounds(130, 150, 100, 30);
        btnStand.addActionListener(e -> realizarAcaoStand());
        add(btnStand);

        btnDouble = new JButton("Double");
        btnDouble.setBounds(240, 150, 100, 30);
        btnDouble.addActionListener(e -> realizarAcaoDouble());
        add(btnDouble);

        btnSurrender = new JButton("Surrender");
        btnSurrender.setBounds(350, 150, 100, 30);
        btnSurrender.addActionListener(e -> realizarAcaoSurrender());
        add(btnSurrender);
    }

    private void realizarAcaoHit() {
        // Logica para o jogador pegar uma nova carta
        // Atualizar a interface gráfica, exibindo as cartas novas
        lblCartas.setText("Cartas: Atualizado (simulando nova carta)");
    }

    private void realizarAcaoStand() {
        // Logica para o jogador "parar"
        JOptionPane.showMessageDialog(this, "Jogador parou.");
    }

    private void realizarAcaoDouble() {
        int apostaAtual = jogador.getApostas()[0];
        if (jogador.double_(0)) {
            lblValorAposta.setText("Aposta: $" + (apostaAtual * 2));
            lblCreditos.setText("Créditos: $" + jogador.getBalanco());
            JOptionPane.showMessageDialog(this, "Aposta dobrada!");
        } else {
            JOptionPane.showMessageDialog(this, "Saldo insuficiente para dobrar a aposta.");
        }
    }

    private void realizarAcaoSurrender() {
        jogador.surrender();
        lblCreditos.setText("Créditos: $" + jogador.getBalanco());
        JOptionPane.showMessageDialog(this, "Você se rendeu e perdeu metade da aposta.");
    }

    public static void main(String[] args) {
        Jogador jogador = new Jogador();
        JanelaJogador janela = new JanelaJogador(jogador);
        janela.setVisible(true);
    }
}