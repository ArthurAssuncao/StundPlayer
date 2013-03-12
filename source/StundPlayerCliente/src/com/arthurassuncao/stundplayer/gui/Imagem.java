package com.arthurassuncao.stundplayer.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.arthurassuncao.stundplayer.recursos.Recursos;

/** Classe para tratar os eventos do mouse da janela do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see ImageIcon
 * @see Cor
 */
public class Imagem {

	
	/** Retorna uma imagem com dimensoes especificas 
	 * @param imagem <code>ImageIcon</code> com a imagem que sera redimensionada
	 * @param escalaLargura <code>double</code> com a porcentagem da largura que a imagem tera
	 * @param escalaAltura <code>double</code> com a porcentagem da altura que a imagem tera
	 * @return <code>Image</code> com a imagem redimensionada
	 * @see ImageIcon
	 * @see Image
	 */
	public static Image redimensionaImagem(ImageIcon imagem, double escalaLargura, double escalaAltura){
		Image imagemRedimensionada = null;
		if(imagem != null){
			if(escalaLargura > 0.0 && escalaAltura > 0.0){
				imagemRedimensionada = imagem.getImage().getScaledInstance((int)(imagem.getIconWidth() * escalaLargura), (int)(imagem.getIconHeight() * escalaAltura), 100);
			}
			else{
				imagemRedimensionada = imagem.getImage();
			}
		}
		return imagemRedimensionada;
	}

	//Codigo de mudar da imagem cor baseado no: http://forum.intern0t.org/java-ruby/3932-java-source-change-image-color.html
	/*public static void main(String[] args) {
		String filename = "imagens/logo_95_opaco.png";
		BufferedImage imagem = null;
		try {
			imagem = ImageIO.read(new File(filename));
			imagem = Imagem.mudaCor(imagem, new Color(249, 127, 16), Color.BLUE);
			JFrame frame = new JFrame();
			frame.setVisible(true);
			frame.setSize(400, 250);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(new JLabel(new ImageIcon(imagem)));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	/** Retorna um buffer com a imagem com um cor substituida
	 * @param bufferImagem <code>BufferedImage</code> com a imagem
	 * @param corAntiga <code>Color</code> com a cor que sera substituida
	 * @param novaCor <code>Color</code> com a cor que substituira a antiga
	 * @return <code>BufferedImage</code> com a imagem ja com a cor substituida
	 * @see BufferedImage
	 * @see Color
	 */
	public static BufferedImage mudaCor(BufferedImage bufferImagem, Color corAntiga, Color novaCor) {
		BufferedImage imagemModificada = new BufferedImage(bufferImagem.getWidth(), bufferImagem.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = imagemModificada.createGraphics();
		g.drawImage(bufferImagem, null, 0, 0);
		g.dispose();

		for (int i = 0; i < imagemModificada.getWidth(); i++) {
			for (int j = 0; j < imagemModificada.getHeight(); j++) {

				int RGBDestino = imagemModificada.getRGB(i, j);

				if (Cor.coresCompativeis(corAntiga.getRGB(), RGBDestino)) {
					int novoRGB = Cor.getNovoPixelRGB(novaCor.getRGB());
					imagemModificada.setRGB(i, j, novoRGB);
				}
			}
		}
		return imagemModificada;
	}

	/** Retorna um buffer com a imagem com um cor substituida
	 * @param enderecoImagem <code>String</code> com o endereco da imagem
	 * @param corSubstituida <code>Color</code> com a cor que sera substituida
	 * @param corSubstituta <code>Color</code> com a cor que substituira a antiga
	 * @return <code>ImageIcon</code> com a imagem ja com a cor substituida
	 * @throws IOException caso ocorra erro na leitura da imagem
	 * @see ImageIcon
	 * @see Color
	 */
	public static ImageIcon mudaCor(String enderecoImagem, Color corSubstituida, Color corSubstituta) throws IOException {
		BufferedImage bufferImagem = ImageIO.read(Recursos.getResourceAsStream(enderecoImagem));
		BufferedImage imagemModificada = new BufferedImage(bufferImagem.getWidth(), bufferImagem.getHeight(), BufferedImage.TYPE_INT_ARGB);
		ImageIcon imagem = null;

		Graphics2D g = imagemModificada.createGraphics();
		g.drawImage(bufferImagem, null, 0, 0);
		g.dispose();

		for (int i = 0; i < imagemModificada.getWidth(); i++) {
			for (int j = 0; j < imagemModificada.getHeight(); j++) {

				int RGBDestino = imagemModificada.getRGB(i, j);

				if (Cor.coresCompativeis(corSubstituida.getRGB(), RGBDestino)) {
					int rgbnew = Cor.getNovoPixelRGB(corSubstituta.getRGB());
					imagemModificada.setRGB(i, j, rgbnew);
				}
			}
		}
		imagem = new ImageIcon(imagemModificada);
		return imagem;
	}
}
