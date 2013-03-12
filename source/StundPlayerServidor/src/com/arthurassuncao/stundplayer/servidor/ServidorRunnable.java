package com.arthurassuncao.stundplayer.servidor;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.arthurassuncao.stundplayer.classes.LogSistema;
import com.arthurassuncao.stundplayer.classes.MP3Dados;
import com.arthurassuncao.stundplayer.classes.MusicaDados;
import com.arthurassuncao.stundplayer.classes.Usuario;

/** Classe para manipular os servicos servidor principal
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see Runnable
 * @see Servidor
 */
public class ServidorRunnable implements Runnable{

    private Socket clienteSocket;
    @SuppressWarnings("unused")
	private String nomeServer;

    /** Cria uma instancia para tratar da conexao com um cliente
     * @param clienteSocket <code>Socket</code> com o soquete com a conexao com o cliente
     * @param nomeServer <code>String</code> com o nome do servidor
     */
    public ServidorRunnable(Socket clienteSocket, String nomeServer) {
        this.clienteSocket = clienteSocket;
        this.nomeServer   = nomeServer;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
    	try{
			ServidorRunnable.executaRequisicao(clienteSocket);
			clienteSocket.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		System.out.flush(); //libera os recursos da Thread
    }
    
    /** Executa uma requisicao vinda do cliente
     * @param clienteSocket <code>Socket</code> com o soquete com a conexao com o cliente
     * @throws IOException erro ao ler ou enviar informacoes para o cliente
     */
    private static void executaRequisicao(Socket clienteSocket) throws IOException {

		//envia DEVE ser iniciado antes do recebe
		ObjectOutputStream enviaStream = new ObjectOutputStream(clienteSocket.getOutputStream());
		ObjectInputStream recebeStream = new ObjectInputStream(clienteSocket.getInputStream());

		String comando = null;
		try {
			comando = (String)recebeStream.readObject();
		}
		catch (ClassNotFoundException e) {
			//e.printStackTrace();
			LogSistema.mostraMensagemErroServidor("Tipo String nao existe: " + e.getMessage()); //sempre vai existir
		}

		if(comando == null){
			LogSistema.mostraMensagem("Nenhum comando");
		}
		else{
			//envia resposta para cliente
			StringTokenizer tokens = new StringTokenizer(comando);
			String status = null;
			String username = null;
			String senha = null;
			String musica = null;
			status = tokens.nextToken(" ");
			username = tokens.nextToken(" ");
			if(status.equalsIgnoreCase("LOGIN") || status.equalsIgnoreCase("CADASTRAR") || status.equalsIgnoreCase("REMOVER") || status.equalsIgnoreCase("EXISTS")){
				senha = tokens.nextToken(" ");

				LogSistema.mostraMensagem("\n" + status);
				LogSistema.mostraMensagem(username);
				LogSistema.mostraMensagem(senha);
			}
			else if(status.equalsIgnoreCase("REMOVER_MUSICA")){
				musica = tokens.nextToken("\n");
				musica = musica.substring(1, musica.length()); //o token ta retornando a musica com um espaco no inicio

				LogSistema.mostraMensagem("\n" + status);
				LogSistema.mostraMensagem(username);
				LogSistema.mostraMensagem(musica);
			}


			if(status.equalsIgnoreCase("LOGIN")){
				ServidorRunnable.login(username, senha, recebeStream, enviaStream );
			}//if Status LOGIN
			else if(status.equalsIgnoreCase("CADASTRAR")){
				ServidorRunnable.cadastrar(username, senha, recebeStream, enviaStream );
			}//if Status CADASTRAR
			else if(status.equalsIgnoreCase("REMOVER")){
				ServidorRunnable.remover(username, senha, recebeStream, enviaStream );
			}
			else if(status.equalsIgnoreCase("EXISTS")){
				ServidorRunnable.exists(username, senha, recebeStream, enviaStream );
			}
			else if(status.equalsIgnoreCase("REMOVER_MUSICA")){
				ServidorRunnable.excluirMusica(username, musica, recebeStream, enviaStream );
			}
		}
	}//executaComandos

	/** Faz o login
	 * @param username <code>String</code> com o nome de usuario do cliente
	 * @param senha <code>String</code> com a senha de usuario do cliente
	 * @param recebeStream <code>ObjectInputStream</code> com a entrada de dados
	 * @param enviaStream <code>ObjectInputStream</code> com a saida de dados
	 */
	public static void login(String username, String senha, ObjectInputStream recebeStream, ObjectOutputStream enviaStream){
		Usuario usuario = Usuario.pesquisa(username, senha);
		if(usuario == null){
			try{
				String mensagem = ServidorConstants.RESULTADO_NAO_EXISTE;
				//ponto de conexão com o servidor 
				enviaStream.writeObject(mensagem);
				enviaStream.flush();
				//socket.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			try{
				String mensagem = ServidorConstants.RESULTADO_SUCESSO;
				//ponto de conexão com o servidor  

				LogSistema.mostraMensagem(mensagem);
				enviaStream.writeObject(mensagem);
				enviaStream.flush();
				String enderecoDiretorioUsuario = Usuario.DIRETORIO_USUARIOS + username;
				File diretorioUsuario = new File(enderecoDiretorioUsuario);
				FilenameFilter filtro = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						if(name.endsWith(".mp3")){
							return true;
						}
						else
							return false;
					}
				};
				//oos.flush();
				String musicas[] = diretorioUsuario.list(filtro);

				//ObjectOutputStream oos = null;
				MusicaDados musicaDados = null;
				//oos = new ObjectOutputStream(out);

				String enderecoMusica = null;
				if(musicas != null){
					for(String musica : musicas){
						String album = null;
						String autor = null;
						String data = null;
						int duracao = 0;
						String titulo = null;
						long tamanhoBytes = 0;
						try {
							enderecoMusica = enderecoDiretorioUsuario + "/" + musica;
							album = MP3Dados.getInformacaoMusicaComMp3Spi(enderecoMusica, MP3Dados.ALBUM);
							autor = MP3Dados.getInformacaoMusicaComMp3Spi(enderecoMusica, MP3Dados.AUTOR);
							data = MP3Dados.getInformacaoMusicaComMp3Spi(enderecoMusica, MP3Dados.DATA);
							duracao = MP3Dados.getDuracaoComMp3Spi(enderecoMusica);
							titulo = MP3Dados.getInformacaoMusicaComMp3Spi(enderecoMusica, MP3Dados.TITULO);
							tamanhoBytes = MP3Dados.getLengthMusica(enderecoMusica);
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						catch (UnsupportedAudioFileException e) {
							e.printStackTrace();
						}
						musicaDados = new MusicaDados(musica, duracao, titulo, autor, album, data, tamanhoBytes);

						//oos = new ObjectOutputStream(out);
						//oos.flush();
						try{
							enviaStream.writeObject(musicaDados);
							enviaStream.flush();
						}
						catch(SocketException e){
							e.printStackTrace();
						}
						//oos.flush();
						//break;
					}
				}
				enviaStream.close();
				//writer.flush();
				//writer.close();
				//socket.close();
				LogSistema.mostraMensagem("Lista de musicas enviada");
			}
			catch(Exception e){
				e.printStackTrace();
			}					
		}
	}//Login

	/** Cadastra um usuario
	 * @param username <code>String</code> com o nome de usuario do cliente
	 * @param senha <code>String</code> com a senha de usuario do cliente
	 * @param recebeStream <code>ObjectInputStream</code> com a entrada de dados
	 * @param enviaStream <code>ObjectInputStream</code> com a saida de dados
	 */
	public static void cadastrar(String username, String senha, ObjectInputStream recebeStream, ObjectOutputStream enviaStream){
		Usuario usuario = new Usuario(username, senha);
		String mensagem = ServidorConstants.RESULTADO_ERRO_DESCONHECIDO; 
		switch(usuario.inserir()){
		case 0:
			mensagem = ServidorConstants.RESULTADO_SUCESSO;
			break;
		case 1:
			mensagem = ServidorConstants.RESULTADO_ERRO_SERVIDOR;
			break;
		case 2:
			mensagem = ServidorConstants.RESULTADO_EXISTE;
			break;
		}

		try{
			//ponto de conexão com o servidor 
			enviaStream.writeObject(mensagem);
			enviaStream.flush();
			enviaStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}//Cadastrar

	/** Remove um usuario
	 * @param username <code>String</code> com o nome de usuario do cliente
	 * @param senha <code>String</code> com a senha de usuario do cliente
	 * @param recebeStream <code>ObjectInputStream</code> com a entrada de dados
	 * @param enviaStream <code>ObjectInputStream</code> com a saida de dados
	 */
	public static void remover(String username, String senha, ObjectInputStream recebeStream, ObjectOutputStream enviaStream){
		Usuario usuario = new Usuario(username, senha);
		String mensagem = ServidorConstants.RESULTADO_ERRO_DESCONHECIDO;

		boolean resultado = usuario.excluir();

		if (resultado){
			mensagem = ServidorConstants.RESULTADO_SUCESSO;
		}
		else{
			mensagem = ServidorConstants.RESULTADO_NAO_EXISTE;
		}

		try{
			//ponto de conexão com o servidor 
			enviaStream.writeObject(mensagem);
			enviaStream.flush();
			enviaStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/** Verifica se o usuario existe
	 * @param username <code>String</code> com o nome de usuario do cliente
	 * @param senha <code>String</code> com a senha de usuario do cliente
	 * @param recebeStream <code>ObjectInputStream</code> com a entrada de dados
	 * @param enviaStream <code>ObjectInputStream</code> com a saida de dados
	 */
	public static void exists(String username, String senha, ObjectInputStream recebeStream, ObjectOutputStream enviaStream){
		String mensagem = ServidorConstants.RESULTADO_ERRO_DESCONHECIDO;

		boolean resultado = Usuario.exists(username, senha);

		if (resultado){
			mensagem = ServidorConstants.RESULTADO_SUCESSO;
		}
		else{
			mensagem = ServidorConstants.RESULTADO_NAO_EXISTE;
		}

		try{
			//ponto de conexão com o servidor 
			enviaStream.writeObject(mensagem);
			enviaStream.flush();
			enviaStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/** Exclui uma musica do cliente
	 * @param username <code>String</code> com o nome de usuario do cliente
	 * @param musica <code>String</code> com a musica do usuario que sera excluida
	 * @param recebeStream <code>ObjectInputStream</code> com a entrada de dados
	 * @param enviaStream <code>ObjectInputStream</code> com a saida de dados
	 */
	public static void excluirMusica(String username, String musica, ObjectInputStream recebeStream, ObjectOutputStream enviaStream){
		String mensagem = ServidorConstants.RESULTADO_ERRO_DESCONHECIDO;

		LogSistema.mostraMensagem(Usuario.DIRETORIO_USUARIOS + username + "/" + musica);

		File arquivoMusica = new File(Usuario.DIRETORIO_USUARIOS + username + "/" + musica);
		if(arquivoMusica.exists() && arquivoMusica.getName().endsWith(".mp3")){ //So exclui mp3
			if(arquivoMusica.delete()){
				mensagem = ServidorConstants.RESULTADO_SUCESSO;
				LogSistema.mostraMensagem("Musica " + musica + " foi excluida do servidor");
			}
			else{
				mensagem = ServidorConstants.RESULTADO_ERRO_SERVIDOR;
			}
		}
		else{
			mensagem = ServidorConstants.RESULTADO_NAO_EXISTE;
		}

		try{
			//ponto de conexão com o servidor 
			enviaStream.writeObject(mensagem);
			enviaStream.flush();
			enviaStream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
    
}