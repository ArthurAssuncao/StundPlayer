package com.arthurassuncao.stundplayer.app;

import com.arthurassuncao.stundplayer.persistencia.BDPlayer;
import com.arthurassuncao.stundplayer.servidor.Servidor;
import com.arthurassuncao.stundplayer.servidor.ftp.ServidorFTP;
import com.arthurassuncao.stundplayer.servidor.rtsp.ServidorRTSP;


/** Inicia os servicos do servidor
 * @author Arthur Assuncao
 * @author Paulo Vitor
 */
public class StundPlayerServidor {
	
	/** Metodo main
	 * @param args <code>String[]</code> com argumentos de linha de comando
	 */
	public static void main(String[] args) {
		BDPlayer bancoDeDados = BDPlayer.getInstance();
		Thread threadBancoDeDados = new Thread(bancoDeDados);
		Servidor servidor = new Servidor();
		Thread threadServidor = new Thread(servidor);
		ServidorFTP servidorFTP = new ServidorFTP();
		Thread threadServidorFTP = new Thread(servidorFTP);
		ServidorRTSP servidorRTSP = new ServidorRTSP();
		Thread threadServidorRTSP = new Thread(servidorRTSP);
		
		//Inicia as threads
		threadServidor.start();
		threadServidorFTP.start();
		threadServidorRTSP.start();
		threadBancoDeDados.start();
	}

}
