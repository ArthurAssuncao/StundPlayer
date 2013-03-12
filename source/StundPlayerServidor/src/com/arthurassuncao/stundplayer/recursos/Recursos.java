package com.arthurassuncao.stundplayer.recursos;

import java.io.InputStream;
import java.net.URL;

/** Classe para carregar recursos como imagens, icones, fontes etc
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 */
public abstract class Recursos {
	
	/** Encontra o recurso no diretorio de recursos do jar
	 * @param enderecoArquivo <code>String</code> caminho do recurso
	 * @return <code>URL</code> com o endereco do recurso
	 */
	public static URL getResource(String enderecoArquivo){
		return Recursos.class.getResource("/br/java/redes/recursos/" + enderecoArquivo);
	}
	/** Encontra o recurso no diretorio de recursos do jar
	 * @param enderecoArquivo <code>String</code> caminho do recurso
	 * @return <code>InputStream</code> com o stream do recurso
	 */
	public static InputStream getResourceAsStream(String enderecoArquivo){
		return Recursos.class.getResourceAsStream("/br/java/redes/recursos/" + enderecoArquivo);
	}
	/*public static String getResourceAsString(String enderecoArquivo){
		return Recursos.class.getResource("/br/java/redes/recursos/" + enderecoArquivo).getFile();
	}*/
	/*public static File getResourceAsFile(String enderecoArquivo){
		return new File(Recursos.class.getResource("/br/java/redes/recursos/" + enderecoArquivo).getFile());
	}*/
}
