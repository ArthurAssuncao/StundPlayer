package com.arthurassuncao.stundplayer.servidor.ftp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.arthurassuncao.stundplayer.classes.LogSistema;

/** Classe para manipular o servidor FTP
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see Runnable
 * @see ServidorFTPRunnable
 */
public class ServidorFTP implements Runnable{
	private final int PORTA = 5678;
	private ServerSocket serverSocket = null;
	private boolean parado = false;
	@SuppressWarnings("unused")
	private Thread threadEmExecucao = null;
	
	/** Inicia o servidor
	 * 
	 */
	public void iniciar(){
		synchronized(this){
			this.threadEmExecucao = Thread.currentThread();
		}
		LogSistema.mostraMensagem("Servidor FTP Iniciado");
		this.abrirServerSocket();
		while(! isParado()){
			Socket clienteSocket = null;
			try {
				clienteSocket = this.serverSocket.accept();
			}
			catch (IOException e) {
				if(isParado()) {
					LogSistema.mostraMensagem("ServerFTP Parado.") ;
					return;
				}
				throw new RuntimeException("Error ao aceitar conexao do cliente", e);
			}
			new Thread(new ServidorFTPRunnable(clienteSocket, "Multithread ServerFTP")).start();
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("ServerFTP Parado.") ;
	}
	
	/** Verifica se o servidor esta parado
	 * @return <code>boolean</code> com <code>true</code> se o servidor esta parado e <code>false</code> senao
	 */
	private synchronized boolean isParado() {
		return this.parado;
	}

	/** Para o servidor
	 * 
	 */
	public synchronized void stop(){
		this.parado = true;
		try {
			this.serverSocket.close();
		}
		catch (IOException e) {
			throw new RuntimeException("Error ao fechar serverFTP", e);
		}
	}

	/** Abre o soquete do servidor
	 * 
	 */
	private void abrirServerSocket() {
		try {
			this.serverSocket = new ServerSocket(PORTA);
		}
		catch (IOException e) {
			throw new RuntimeException("Nao pode abrir porta " + PORTA, e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		iniciar();
	}
	
	/** Metodo main para testar o servidor FTP
	 * @param args <code>String[]</code> com argumentos de linha de comando
	 */
    public static void main(String[] args){  
    	ServidorFTP servidorFTP = new ServidorFTP();
    	servidorFTP.iniciar();
    }//main 
}
