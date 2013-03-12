package com.arthurassuncao.stundplayer.cliente.usuario;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.arthurassuncao.stundplayer.classes.Configuracoes;
import com.arthurassuncao.stundplayer.classes.MusicaDados;
import com.arthurassuncao.stundplayer.classes.Usuario;
import com.arthurassuncao.stundplayer.cliente.Cliente;
import com.arthurassuncao.stundplayer.gui.JanelaMensagem;

/** Classe para manipular a troca de dados de usuarios com o servidor
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see Usuario
 * @see Cliente
 */
public class ClienteUsuario extends Cliente {

	private static String ipServidor = Configuracoes.getInstance().getIpServidor();
	private static int portaServidor = Configuracoes.getInstance().getPortaServidor();
	private static int tempoTimeOut = 4000;

	/* Cria uma instancia
	 *
	 */
	/*private ClienteUsuario(){

	}*/
	
	/**
	 *  Atualiza o ip e a porta do servidor de acordo com as configuracoes
	 */
	private static void atualizaDadosServidor(){
		ClienteUsuario.ipServidor = Configuracoes.getInstance().getIpServidor();
		ClienteUsuario.portaServidor = Configuracoes.getInstance().getPortaServidor();
	}

	/** Insere um usuario no sistema
	 * @param username <code>String</code> com o nome de usuario do usuario
	 * @param senha <code>String</code> com o senha do usuario
	 * @return <code>String</code> com o resultado da insercao
	 * @see Cliente
	 */
	protected String inserir(String username, String senha){
		ClienteUsuario.atualizaDadosServidor();
		StringBuffer comando = new StringBuffer("CADASTRAR");

		comando.append(" " + username);
		comando.append(" " + senha);

		Socket clienteSocket = null;
		ObjectInputStream recebeStream = null;
		ObjectOutputStream enviaStream = null; 
		try {
			clienteSocket = new Socket(InetAddress.getByName(ipServidor), portaServidor);
			clienteSocket.setSoTimeout(tempoTimeOut);
			enviaStream = new ObjectOutputStream(clienteSocket.getOutputStream());
			recebeStream = new ObjectInputStream(clienteSocket.getInputStream());
			System.out.println("Carregou o clienteSocket, envia e recebe");
		}
		catch(ConnectException e){
			System.err.println("Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
			JanelaMensagem.mostraMensagemErro(null, "Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if(clienteSocket == null){
			System.out.println("clienteSocket esta null");
			return null;
		}
		else if(recebeStream == null){
			System.out.println("recebe esta null");
			return null;
		}
		else if(enviaStream == null){
			System.out.println("envia esta null");
			return null;
		}
		ClienteUsuario.enviaComando(enviaStream, comando.toString());
		String mensagemServidor = "";
		mensagemServidor = recebeMensagemObjetoServidor(recebeStream);

		return mensagemServidor;
	}

	/** Verifica se um usuario existe no sistema
	 * @param username <code>String</code> com o nome de usuario do usuario
	 * @param senha <code>String</code> com o senha do usuario
	 * @return <code>boolean</code> com <code>true</code> se o usuario existe e <code>false</code> senao
	 */
	protected static boolean exists(String username, String senha){
		ClienteUsuario.atualizaDadosServidor();
		StringBuffer comando = new StringBuffer("EXISTS");
		
		comando.append(" " + username);
		comando.append(" " + senha);

		Socket clienteSocket = null;
		ObjectInputStream recebeStream = null;
		ObjectOutputStream enviaStream = null; 
		try {
			clienteSocket = new Socket(InetAddress.getByName(ipServidor), portaServidor);
			clienteSocket.setSoTimeout(tempoTimeOut);
			enviaStream = new ObjectOutputStream(clienteSocket.getOutputStream());
			recebeStream = new ObjectInputStream(clienteSocket.getInputStream());
			System.out.println("Carregou o clienteSocket, envia e recebe");
		}
		catch(ConnectException e){
			System.err.println("Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
			JanelaMensagem.mostraMensagemErro(null, "Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if(clienteSocket == null){
			System.out.println("clienteSocket esta null");
			return false;
		}
		else if(recebeStream == null){
			System.out.println("recebe esta null");
			return false;
		}
		else if(enviaStream == null){
			System.out.println("envia esta null");
			return false;
		}
		ClienteUsuario.enviaComando(enviaStream, comando.toString());
		String mensagemServidor = "";
		mensagemServidor = recebeMensagemObjetoServidor(recebeStream);
		
		return mensagemServidor.equals(Cliente.RESULTADO_SUCESSO) ? true : false;
	}

	/** Exclui um usuario do sistema
	 * @param username <code>String</code> com o nome de usuario do usuario
	 * @param senha <code>String</code> com o senha do usuario
	 * @return <code>boolean</code> com <code>true</code> se o usuario foi excluido e <code>false</code> senao
	 */
	protected boolean excluir(String username, String senha){
		ClienteUsuario.atualizaDadosServidor();
		StringBuffer comando = new StringBuffer("REMOVER");
		comando.append(" " + username);
		comando.append(" " + senha);

		Socket clienteSocket = null;
		ObjectInputStream recebeStream = null;
		ObjectOutputStream enviaStream = null; 
		try {
			clienteSocket = new Socket(InetAddress.getByName(ipServidor), portaServidor);
			clienteSocket.setSoTimeout(tempoTimeOut);
			enviaStream = new ObjectOutputStream(clienteSocket.getOutputStream());
			recebeStream = new ObjectInputStream(clienteSocket.getInputStream());
			System.out.println("Carregou o clienteSocket, envia e recebe");
		}
		catch(ConnectException e){
			System.err.println("Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
			JanelaMensagem.mostraMensagemErro(null, "Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if(clienteSocket == null){
			System.out.println("clienteSocket esta null");
			return false;
		}
		else if(recebeStream == null){
			System.out.println("recebe esta null");
			return false;
		}
		else if(enviaStream == null){
			System.out.println("envia esta null");
			return false;
		}
		ClienteUsuario.enviaComando(enviaStream, comando.toString());
		String mensagemServidor = "";
		mensagemServidor = recebeMensagemObjetoServidor(recebeStream);

		return mensagemServidor.equals(Cliente.RESULTADO_SUCESSO) ? true : false;
	} 

	/** Pesquisa por um usuario no sistema
	 * @return <code>Usuario</code> com o usuario encontrado, caso nao encontre retorna <code>null</code>
	 * @param loginUsuario <code>String</code> com o nome de usuario
	 * @param senhaUsuario <code>String</code> com a senha do usuario
	 */
	protected static Usuario pesquisa(String loginUsuario, String senhaUsuario){
		ClienteUsuario.atualizaDadosServidor();
		StringBuffer comando = new StringBuffer("LOGIN");
		comando.append(" " + loginUsuario);
		comando.append(" " + senhaUsuario);

		Socket clienteSocket = null;
		ObjectInputStream recebeStream = null;
		ObjectOutputStream enviaStream = null; 
		try {
			clienteSocket = new Socket(InetAddress.getByName(ipServidor), portaServidor);
			clienteSocket.setSoTimeout(tempoTimeOut);
			enviaStream = new ObjectOutputStream(clienteSocket.getOutputStream());
			recebeStream = new ObjectInputStream(clienteSocket.getInputStream());
			System.out.println("Carregou o clienteSocket, envia e recebe");
		}
		catch(ConnectException e){
			System.err.println("Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
			JanelaMensagem.mostraMensagemErro(null, "Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
		}
		catch (UnknownHostException e) {
			//e.printStackTrace();
			System.err.println("Servidor desconhecido: " + e.getMessage());
		}
		catch (IOException e) {
			if(e.getMessage().equalsIgnoreCase("Connection refused: connect")){
				System.err.println("Servidor indisponivel: " + e.getMessage());
			}
			else{
				e.printStackTrace();
			}
		}

		if(clienteSocket == null){
			System.out.println("clienteSocket esta null");
			return null;
		}
		else if(recebeStream == null){
			System.out.println("recebe esta null");
			return null;
		}
		else if(enviaStream == null){
			System.out.println("envia esta null");
			return null;
		}
		ClienteUsuario.enviaComando(enviaStream, comando.toString());

		String mensagemServidor = null;
		mensagemServidor = recebeMensagemObjetoServidor(recebeStream);

		if(mensagemServidor != null){
			if(mensagemServidor.equals(Cliente.RESULTADO_SUCESSO)){
				Usuario usuario = new Usuario(loginUsuario, senhaUsuario);
				List<MusicaDados> listaDeMusicas = null;

				listaDeMusicas = recebeMusicasDadosServidor(recebeStream);

				if(listaDeMusicas != null)
				{
					System.out.println("Numero de musicas " + listaDeMusicas.size());
					for(MusicaDados musica : listaDeMusicas){
						System.out.println("Musica :" + musica.getNomeMusica());
						System.out.println();
					}
				}
				usuario.setListaMusicas(listaDeMusicas);
				return usuario;
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
	}
	
	/** Exclui uma musica do usuario
	 * @param username <code>String</code> com o nome de usuario do usuario
	 * @param nomeMusica <code>String</code> com o nome do arquivo que sera excluido
	 * @return <code>boolean</code> com <code>true</code> se a musica foi excluida e <code>false</code> senao
	 */
	protected boolean excluirMusica(String username, String nomeMusica){
		ClienteUsuario.atualizaDadosServidor();
		StringBuffer comando = new StringBuffer("REMOVER_MUSICA");
		comando.append(" " + username);
		comando.append(" " + nomeMusica);

		Socket clienteSocket = null;
		ObjectInputStream recebeStream = null;
		ObjectOutputStream enviaStream = null; 
		try {
			clienteSocket = new Socket(InetAddress.getByName(ipServidor), portaServidor);
			clienteSocket.setSoTimeout(tempoTimeOut);
			enviaStream = new ObjectOutputStream(clienteSocket.getOutputStream());
			recebeStream = new ObjectInputStream(clienteSocket.getInputStream());
			System.out.println("Carregou o clienteSocket, envia e recebe");
		}
		catch(ConnectException e){
			System.err.println("Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
			JanelaMensagem.mostraMensagemErro(null, "Nao foi possivel conectar ao servidor, erro: " + e.getMessage());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		if(clienteSocket == null){
			System.out.println("clienteSocket esta null");
			return false;
		}
		else if(recebeStream == null){
			System.out.println("recebe esta null");
			return false;
		}
		else if(enviaStream == null){
			System.out.println("envia esta null");
			return false;
		}
		ClienteUsuario.enviaComando(enviaStream, comando.toString());
		String mensagemServidor = "";
		mensagemServidor = recebeMensagemObjetoServidor(recebeStream);

		return mensagemServidor.equals(Cliente.RESULTADO_SUCESSO) ? true : false;
	} 

	/** Envia um comando para o servidor
	 * @param envia <code>ObjectOutputStream</code> com o stream de saida
	 * @param comando <code>String</code> com o comando
	 * @see ObjectOutputStream
	 */
	private static void enviaComando(ObjectOutputStream envia, String comando){
		System.out.println("Comando: " + comando);
		try {
			envia.writeObject(comando);
			//socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Mensagem enviada " + comando.toString());
	}

	/** Recebe uma mensagem do servidor
	 * @param recebe <code>ObjectOutputStream</code> com o stream de entrada
	 * @return <code>String</code> com a mensagem recebida, retorna <code>null</code> se o servidor estiver indisponivel
	 * @see ObjectInputStream
	 */
	private static String recebeMensagemObjetoServidor(ObjectInputStream recebe){

		//Socket clienteSocket = null;
		//ObjectInputStream input = null;
		String mensagem = null;
		try {
			//clienteSocket = new Socket(ipServidor, portaServidor);
			//recebe = new ObjectInputStream(clienteSocket.getInputStream());
			try {
				mensagem = (String)recebe.readObject();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//clienteSocket.close();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Status recebido " + mensagem);
		if(mensagem == null){
			System.err.println("Servidor indisponivel");
		}

		return mensagem;
	}

	/** Recebe uma lista de musicas do servidor
	 * @param recebe <code>ObjectOutputStream</code> com o stream de entrada
	 * @return {@code List<MusicaDados>} com a lista de musicas
	 * @see ObjectInputStream
	 * @see List
	 * @see MusicaDados
	 */
	private static List<MusicaDados> recebeMusicasDadosServidor(ObjectInputStream recebe){
		List<MusicaDados> listaMusicas = new ArrayList<MusicaDados>();

		//Socket clienteSocket = null;
		MusicaDados musica = null;
		//ObjectInputStream input = null;
		try {
			//clienteSocket = new Socket(ipServidor, portaServidor);
			//System.out.println("teste");
			//input = new ObjectInputStream(clienteSocket.getInputStream());
			try {
				while(true){
					musica = (MusicaDados)recebe.readObject();
					System.out.println(musica.getNomeMusica());
					listaMusicas.add(musica);
				}
			} 
			catch(EOFException e){
				//terminou a leitura
				System.out.println("Terminou a leitura das musicas");
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//clienteSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//MusicaDados[] musicas = listaMusicas.toArray(new MusicaDados[listaMusicas.size()]);
		return listaMusicas;
	}
}
