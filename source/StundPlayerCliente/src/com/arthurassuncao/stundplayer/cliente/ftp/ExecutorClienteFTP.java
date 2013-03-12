package com.arthurassuncao.stundplayer.cliente.ftp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.arthurassuncao.stundplayer.gui.player.JanelaPlayer;

/** Classe para manipular a execucao do cliente FTP
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see JanelaPlayer
 * @see Executors
 * @see ExecutorService
 */
public class ExecutorClienteFTP {
	private JanelaPlayer janela;
	private String usuario;
	private ExecutorService executor;
	
	/** Cria uma instancia com a janela do player e usuario especificos
	 * @param janela <code>JanelaPlayer</code> com a janela do player
	 * @param usuario <code>String</code> com o usuario
	 * @see JanelaPlayer
	 * @see Executors
	 */
	public ExecutorClienteFTP(JanelaPlayer janela, String usuario){
		 this.executor = Executors.newCachedThreadPool();
		 this.janela = janela;
		 this.usuario = usuario;
	}
	
	/** Adiciona um arquivo a fila do cliente FTP
	 * @param arquivo <code>String</code> com o arquivo que sera enviado
	 */
	public void addTransferenciaClienteFTP(String arquivo){
		this.executor.execute(new ClienteFTP(this.janela, this.usuario, arquivo));
	}
}
