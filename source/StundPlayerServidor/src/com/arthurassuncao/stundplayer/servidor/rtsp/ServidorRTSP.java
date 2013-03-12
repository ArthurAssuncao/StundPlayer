package com.arthurassuncao.stundplayer.servidor.rtsp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.arthurassuncao.stundplayer.classes.LogSistema;

/** Classe para manipular o servidor principal
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see Runnable
 * @see ServidorRTSPRunnable
 */
public class ServidorRTSP implements Runnable{
	
	private final int PORTA_RTSP = 9000;
	//protected ServidorRTSP theServer = null;
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
		LogSistema.mostraMensagem("Servidor RTSP Iniciado");
		this.abrirServerSocket();
		while(! isParado()){
			Socket clienteSocket = null;
			try {
				clienteSocket = this.serverSocket.accept();
			}
			catch (IOException e) {
				if(isParado()) {
					LogSistema.mostraMensagem("ServerRTSP Parado.");
					return;
				}
				throw new RuntimeException("Error ao aceitar conexao do cliente", e);
			}
			LogSistema.mostraMensagem("Conectado ao Clinte: " + clienteSocket.getInetAddress());
			new Thread(new ServidorRTSPRunnable(clienteSocket, "Multithread ServerRTSP")).start();
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("ServerRTSP Parado.") ;
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
			throw new RuntimeException("Error ao fechar serverRTSP", e);
		}
	}

	/** Abre o soquete do servidor
	 * 
	 */
	private void abrirServerSocket() {
		try {
			//cria um objeto Server
			//this.theServer = new ServidorRTSP();
			
			//Iniciar uma conexão TCP com o cliente para a sessão RTSP
			this.serverSocket = new ServerSocket(PORTA_RTSP);
		}
		catch (IOException e) {
			throw new RuntimeException("Nao pode abrir porta " + PORTA_RTSP, e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		iniciar();
	}
	
}

