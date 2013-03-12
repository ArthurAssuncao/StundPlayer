package com.arthurassuncao.stundplayer.classes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arthurassuncao.stundplayer.cliente.Cliente;
import com.arthurassuncao.stundplayer.cliente.usuario.ClienteUsuario;
import com.arthurassuncao.stundplayer.gui.JanelaMensagem;

/** Classe para manipular os usuarios do sistema no lado cliente da aplicacao
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see ClienteUsuario
 * @see Serializable
 */
public class Usuario extends ClienteUsuario implements Serializable{
	private static final long serialVersionUID = 4171546120139140514L;
	private String username;
	private transient String senha;
	private transient List<MusicaDados> listaMusicas;

	/** Cria um instancia com username e senha especificos
	 * @param username <code>String</code> com o nome de usuario
	 * @param senha <code>String</code> com a senha do usuario
	 */
	public Usuario(String username, String senha){
		this.username = username;
		this.senha = senha;
		this.listaMusicas = new ArrayList<MusicaDados>();
	}

	/** Cria um instancia com username, senha e lista de musicas especificos
	 * @param username <code>String</code> com o nome de usuario
	 * @param senha <code>String</code> com a senha do usuario
	 * @param listaMusicas {@code List<MusicaDados>} com a lista de musicas do usuario
	 * @see List
	 * @see MusicaDados
	 */
	public Usuario(String username, String senha, List<MusicaDados> listaMusicas){
		this.username = username;
		this.senha = senha;
		this.listaMusicas = listaMusicas;
	}

	/** Retorna a lista de musicas do usuario
	 * @return {@code List<MusicaDados>} com a lista de musicas do usuario
	 * @see List
	 * @see MusicaDados
	 */
	public List<MusicaDados> getListaMusicas(){
		return this.listaMusicas;
	}

	/** Seta a lista de musicas do usuario
	 * @param listaMusicas {@code List<MusicaDados>} com a nova lista de musicas do usuario
	 * @see List
	 * @see MusicaDados
	 */
	public void setListaMusicas(List<MusicaDados> listaMusicas){
		this.listaMusicas = listaMusicas;
	}

	/** Remove uma musica do usuario, é possivel remover da playlist ou do sistema
	 * @param musicaRemovida <code>MusicaDados</code> com a musica que podera ser removida
	 * @return <code>int</code> com 0 se a musica foi excluida do servidor e da playlist, 1 se foi excluida apenas da playlist e 2 se nao foi excluida nem da playlist nem do servidor
	 */
	public int removeMusica(MusicaDados musicaRemovida){
		int removeu = 2;

		int removerDoServidor = 2;
		removerDoServidor = JanelaMensagem.mostraMensagemConfirmaYesNoCancel(null, "" +
				"Deseja remover a musica tambem do servidor?\n" +
				"Ao excluir a musica do servidor se exclui a musica definitivamente.");

		if(removerDoServidor != 2){ //diferente de Cancelar
			removeu = this.listaMusicas.remove(musicaRemovida) ? 1 : 2;
			if(removeu == 1){ //removeu da lista
				if(removerDoServidor == 1){ //se clicou em Nao
					removeu = 1;
					//System.out.println("Classe Usuario: So removeu da lista");
				}
				else{ //se cliclou em Sim
					removeu = 0;
					if(super.excluirMusica(this.username, musicaRemovida.getNomeMusica())){
						removeu = 0;
					}
					else{
						removeu = 1;
					}
					//System.out.println("Classe Usuario: Falta remover a musica do servidor");
				}
			}
		}
		return removeu;
	}

	/** Retorna o username do usuario
	 * @return <code>String</code> com o nome de usuario do usuario
	 */
	public String getUsername() {
		return username;
	}

	/** Retorna a senha do usuario
	 * @return <code>String</code> com a senha do usuario
	 */
	public String getSenha() {
		return senha;
	}

	/** Pesquisa por um usuario no sistema
	 * @return <code>Usuario</code> com o usuario encontrado, caso nao encontre retorna <code>null</code>
	 * @param username <code>String</code> com o nome de usuario
	 * @param senha <code>String</code> com a senha do usuario
	 */
	public static Usuario pesquisa(String username, String senha){
		Usuario usuario = null;
		//try{
		usuario = ClienteUsuario.pesquisa(username, senha);
		/*}
		catch(SQLException e){
			e.printStackTrace();
		}*/
		return usuario;
	}

	/** Insere este usuario no sistema
	 * @return <code>String</code> com o resultado da insercao
	 * @see Cliente
	 */
	public String inserir(){
		return super.inserir(this.username, this.senha);
	}

	/** Exclui este usuario do sistema
	 * @return <code>boolean</code> com <code>true</code> se o usuario foi excluido e <code>false</code> senao.
	 */
	public boolean excluir(){
		boolean excluiu = false;

		if(ClienteUsuario.exists(this.username, this.senha)){
			excluiu = super.excluir(this.username, this.senha);
		}

		return excluiu;
	}

	/** Adiciona uma musica a lista de musicas do usuario
	 * @param musica <code>MusicaDados</code> com a musica
	 * @see MusicaDados
	 */
	public void addMusica(MusicaDados musica){
		this.listaMusicas.add(musica);
	}

	/** Salva em arquivo o ultimo usuario logado
	 * @param usuario <code>Usuario</code> com o ultimo usuario logado
	 * @throws IOException caso ocorra um erro ao salvar o arquivo
	 */
	public static void salvaUltimoUsuario(Usuario usuario) throws IOException{ //salva o usuario para saber qual foi o ultimo logado
		//File arquivo = new File("ultimoUsuario.dat");
		FileOutputStream arquivoGrava = new FileOutputStream("ultimoUsuario.dat");

		ObjectOutputStream objetoGrava = new ObjectOutputStream(arquivoGrava);

		//grava o objeto no arquivo
		objetoGrava.writeObject(usuario);
		objetoGrava.flush();
		objetoGrava.close();
		arquivoGrava.flush();
		arquivoGrava.close();
	}
	
	/** Recupera de arquivo o ultimo usuario logado
	 * @return <code>Usuario</code> com o ultimo usuario logado
	 * @throws IOException caso ocorra um erro ao ler o arquivo
	 */
	public static Usuario recuperaUltimoUsuario() throws IOException{
        FileInputStream arquivoLeitura = new FileInputStream("ultimoUsuario.dat");

        ObjectInputStream objetoLeitura = new ObjectInputStream(arquivoLeitura);
        Usuario usuario = null;
        try {
			usuario = (Usuario)objetoLeitura.readObject();
		}
        catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        objetoLeitura.close();
        arquivoLeitura.close();
        
        return usuario;
	}
	
}
