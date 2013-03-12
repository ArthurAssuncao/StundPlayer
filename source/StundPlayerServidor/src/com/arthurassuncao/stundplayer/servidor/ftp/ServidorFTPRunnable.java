package com.arthurassuncao.stundplayer.servidor.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.arthurassuncao.stundplayer.classes.LogSistema;
import com.arthurassuncao.stundplayer.classes.Usuario;

/** Classe para manipular os servicos servidor FTP
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see Runnable
 * @see ServidorFTP
 */
public class ServidorFTPRunnable implements Runnable{

	private Socket clienteSocket;
	@SuppressWarnings("unused")
	private String nomeServer;

	/** Cria uma instancia para tratar da conexao com um cliente
     * @param clienteSocket <code>Socket</code> com o soquete com a conexao com o cliente
     * @param nomeServer <code>String</code> com o nome do servidor
     */
	public ServidorFTPRunnable(Socket clienteSocket, String nomeServer) {
		this.clienteSocket = clienteSocket;
		this.nomeServer   = nomeServer;
	}

	/* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
	public void run() {
		try {
			InputStream in = clienteSocket.getInputStream();  
			InputStreamReader isr = new InputStreamReader(in);  
			BufferedReader reader = new BufferedReader(isr);
			String nomeUsuario = reader.readLine();
			String nomeArquivo = reader.readLine();
			LogSistema.mostraMensagem(nomeArquivo);
			String diretorioDestino = Usuario.DIRETORIO_USUARIOS + nomeUsuario;
			File diretorio = new File(diretorioDestino);
			if(!diretorio.exists()){
				diretorio.mkdirs();
			}
			File f1 = new File(diretorioDestino + "/" + nomeArquivo);  
			FileOutputStream out = new FileOutputStream(f1);  

			int tamanho = 4096; // buffer de 4KB    
			byte[] buffer = new byte[tamanho];    
			int lidos = -1;
			while ((lidos = in.read(buffer, 0, tamanho)) != -1) {
				LogSistema.mostraMensagem(lidos);  
				out.write(buffer, 0, lidos);    
			}    
			out.flush();
			out.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		System.out.flush(); //libera os recursos da Thread
	}
}
