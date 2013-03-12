package com.arthurassuncao.stundplayer.gui;

import java.awt.Color;

/** Classe para tratar os eventos do mouse da janela do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see Color
 */
public abstract class Cor {	
	/** <code>int</code> representando o alpha*/
	public static final int ALPHA = 0;
	/** <code>int</code> representando o vermelhor*/
	public static final int RED = 1;
	/** <code>int</code> representando o verde*/
	public static final int GREEN = 2;
	/** <code>int</code> representando o azul*/
	public static final int BLUE = 3;

	/** <code>int</code> representando a matiz*/
	public static final int HUE = 0;
	/** <code>int</code> representando a saturacao*/
	public static final int SATURATION = 1;
	/** <code>int</code> representando o brilho*/
	public static final int BRIGHTNESS = 2;

	/** <code>int</code> representando a transparencia*/
	public static final int TRANSPARENT = 0;

	/** Retona o brilho da cor
	 * @param cor <code>Color</code> com a cor que deseja saber o brilho
	 * @return <code>int</code> com o valor do brilho de 0 a 255
	 */
	public static int getBrilho(Color cor) {
		return (int) Math.sqrt(
				cor.getRed() * cor.getRed() * 0.241 +
				cor.getGreen() * cor.getGreen() * 0.691 +
				cor.getBlue() * cor.getBlue() * 0.068);
	}

	/** Retorna um inteiro representando a cor RGB do argumento RGBSubstituto com base no RGBDestino
	 * @param RGBSubstituto <code>int</code> com o valor da cor substituta
	 * @param RGBDestino <code>int</code> com o valor da cor que sera substituida
	 * @return <code>int</code> representando a cor RGB do argumento RGBSubstituto com base no RGBDestino
	 */
	//esse fica mais natural, como o modo de sobreposicao Cor do Photoshop
	public static int getNovoPixelRGBBaseadoNoDestino(int RGBSubstituto, int RGBDestino) {
		float[] HSBDestino = Cor.getHSBComoVetor(RGBDestino);
		float[] HSBSubstituto = Cor.getHSBComoVetor(RGBSubstituto);

		int novoRGB = Color.HSBtoRGB(HSBSubstituto[HUE], HSBSubstituto[SATURATION], HSBDestino[BRIGHTNESS]);
		return novoRGB;
	}

	/** Retorna um inteiro representando a cor RGB do argumento RGBSubstituto
	 * @param RGBSubstituto <code>int</code> com o valor da cor substituta
	 * @return <code>int</code> representando a cor RGB do argumento RGBSubstituto
	 */
	public static int getNovoPixelRGB(int RGBSubstituto) {
		float[] HSBSubstituto = Cor.getHSBComoVetor(RGBSubstituto);

		int novoRGB = Color.HSBtoRGB(HSBSubstituto[HUE], HSBSubstituto[SATURATION], HSBSubstituto[BRIGHTNESS]);
		return novoRGB;
	}

	/** Verifica se duas cores sao compativeis(matches)
	 * @param RGB1 <code>int</code> com um valor de uma cor
	 * @param RGB2 <code>int</code> com um valor de uma cor
	 * @return <code>boolean</code> com <code>true</code> se as cores forem iguais e <code>false</code> senao
	 */
	public static boolean coresCompativeis(int RGB1, int RGB2) {
		float[] HSBSubstituido = Cor.getHSBComoVetor(RGB1);
		float[] HSBDestino = Cor.getHSBComoVetor(RGB2);

		if (HSBSubstituido[HUE] == HSBDestino[HUE] && HSBSubstituido[SATURATION] == HSBDestino[SATURATION] && getRGBComoVetor(RGB2)[ALPHA] != TRANSPARENT) {
			return true;
		}
		return false;
	}

	/** Retorna um vetor com os canais alpha, red, green e blue do valor fornecido.<br>
	 * Os bits de 25 a 32 representam o alpha<br>
	 * Os bits de 17 a 24 representam o red<br>
	 * Os bits de 9 a 16 representam o green<br>
	 * Os bits de 0 a 8 representam o blue<br>
	 * @param RGB <code>int</code> com o valor da cor RGB
	 * @return <code>int[]</code> com os canais ARGB da cor
	 */
	public static int[] getRGBComoVetor(int RGB) {
		//return new int[] {(rgb >> 24) & 0xff, (rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff };
		int alpha = (RGB >>> 24) & 0xFF;
		int red   = (RGB >>> 16) & 0xFF;
		int green = (RGB >>>  8) & 0xFF;
		int blue  = (RGB >>>  0) & 0xFF;
		return new int[]{alpha, red, green, blue};
	}

	/** Retorna um vetor com a matiz, saturacao e brilho do valor fornecido
	 * @param RGB <code>int</code> com o valor da cor RGB
	 * @return <code>float[]</code> com os canais HSB da cor
	 */
	public static float[] getHSBComoVetor(int RGB) {
		int[] rgbArray = Cor.getRGBComoVetor(RGB);
		return Color.RGBtoHSB(rgbArray[RED], rgbArray[GREEN], rgbArray[BLUE], null);
	}


}
