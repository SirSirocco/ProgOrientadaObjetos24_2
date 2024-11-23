package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

class ProcessadorImagem {
	static ArrayList<Image> imagens = new ArrayList<Image>();
	
	///////////////////////////////////////
	// Manipulacao de imagens
	static Image pegaImagem(String caminho) {
		Image result = null;
		try {
			result = ImageIO.read(new File(caminho));
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	
		imagens.add(result);
		return result;
	}

	/**
	* Função para pegar a imagem da carta de acordo com os naipes e valores
	* @param naipe
	* @param valor
	* @return imagem da carta
	*/
	static Image pegaCarta(String naipe, String valor) {
		try {
			Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			valor = valor.toLowerCase();
		}
		
		// System.out.println("< " + naipe + " >");
		// System.out.println("<" + valor + ">");
		
		if (valor.contentEquals("10"))
			valor = "t";

		switch (naipe) {
		case "Paus":
			naipe = "c";
			break;
		case "Ouros":
			naipe = "d";
			break;
		case "Espadas":
			naipe = "s";
			break;
		case "Copas":
			naipe = "h";
			break;
		default:
			System.out.println("naipe inválido");
			break;
		}

	String caminho = "Imagens/" + valor + naipe + ".gif";
	Image result = null;
	try {
		result = ImageIO.read(new File(caminho));
	} catch (IOException e) {
		System.out.println("ProcessadorImagem");
		System.out.println(e);
		System.exit(1);
	}

	return result;
	}
	
	static void atualizaImagemCartas(BPanel painel, ArrayList<ArrayList<String>> cs) {
		ArrayList<Image> result = new ArrayList<>();
		for (ArrayList<String> c: cs) {
			result.add(pegaCarta(c.get(0), c.get(1)));
		}
		painel.adicionaImagem(result);
	}
	
	static void atualizaImagemCartas(JJPanel painel, ArrayList<ArrayList<String>> cs) {
		ArrayList<Image> result = new ArrayList<>();
		for (ArrayList<String> c: cs) {
			result.add(pegaCarta(c.get(0), c.get(1)));
		}
		painel.adicionaImagem(result);
	}
}
