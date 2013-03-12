package com.arthurassuncao.stundplayer.app;

import com.arthurassuncao.stundplayer.gui.sistema.JanelaLogin;
import com.arthurassuncao.stundplayer.gui.temas.Temas;

/** Classe para iniciar o programa StundPlayer
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 */
public abstract class StundPlayer {
	
	/** Metodo Main
	 * @param args <code>String[]</code> com argumentos de linha de comando
	 */
	public static void main(String[] args) {
		Temas.mudaTema(Temas.NIMBUS_DARK);
		
		new JanelaLogin();
	}
}
