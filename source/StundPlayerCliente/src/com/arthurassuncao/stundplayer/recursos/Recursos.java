package com.arthurassuncao.stundplayer.recursos;

import java.io.InputStream;
import java.net.URL;

/** Classe para carregar recursos como imagens, icones, fontes etc
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 */
public abstract class Recursos {
	
	private static final String PACOTE = String.format("/%s/", Recursos.class.getPackage().getName().replace(".", "/"));
	
	/** Encontra o recurso no diretorio de recursos do jar
	 * @param enderecoArquivo <code>String</code> caminho do recurso
	 * @return <code>URL</code> com o endereco do recurso
	 */
	public static URL getResource(String enderecoArquivo){
		return Recursos.class.getResource(PACOTE + enderecoArquivo);
	}
	/** Encontra o recurso no diretorio de recursos do jar
	 * @param enderecoArquivo <code>String</code> caminho do recurso
	 * @return <code>InputStream</code> com o stream do recurso
	 */
	public static InputStream getResourceAsStream(String enderecoArquivo){
		return Recursos.class.getResourceAsStream(PACOTE + enderecoArquivo);
	}
	/*public static String getResourceAsString(String enderecoArquivo){
		return Recursos.class.getResource(PACOTE + enderecoArquivo).getFile();
	}*/
	/*public static File getResourceAsFile(String enderecoArquivo){
		return new File(Recursos.class.getResource(PACOTE + enderecoArquivo).getFile());
	}*/
}
