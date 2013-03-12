package com.arthurassuncao.stundplayer.classes;

import java.io.File;
import java.sql.SQLException;

import com.arthurassuncao.stundplayer.persistencia.BDUsuario;
import com.arthurassuncao.stundplayer.persistencia.BancoDeDados;

/** Classe para manipular os usuarios do sistema
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see BDUsuario
 */
public class Usuario extends BDUsuario{
	private String username;
	private String senha;
	
	/** <code>String</code> com o diretorio padrao dos usuarios*/
	public static final String DIRETORIO_USUARIOS = "usuarios/";
	
	/** Cria um usuario com nome de usuario e senha especificos
	 * @param username <code>String</code> com o nome de usuario
	 * @param senha <code>String</code> com a senha do usuario
	 */
	public Usuario(String username, String senha){
		this.username = username;
		this.senha = senha;
	}
	
	/** Pesquisa por um usuario no sistema
	 * @param username <code>String</code> com o nome de usuario do usuario
	 * @param senha <code>String</code> com a senha do usuario
	 * @return <code>Usuario</code> com o usuario encontrado, caso nao seja encotrado retorna <code>null</code>
	 */
	public static Usuario pesquisa(String username, String senha){
		Usuario usuario = null;
		try{
			usuario = BDUsuario.pesquisa(username, senha);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return usuario;
	}

	/** Retorna o nome de usuario do usuario
	 * @return <code>String</code> com o nome de usuario
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

	/** Insere este usuario no sistema
	 * @return <code>int</code> com o resultado da insercao
	 * @see BancoDeDados#RESULTADO_ERRO_DESCONHECIDO
	 * @see BancoDeDados#RESULTADO_SUCESSO
	 * @see BancoDeDados#RESULTADO_ERRO_REGISTRO_DUPLICADO
	 * @see BancoDeDados#RESULTADO_ERRO_BANCO_DADOS
	 */
	public int inserir(){
		int resultadoInsercao = BancoDeDados.RESULTADO_ERRO_DESCONHECIDO;
		
		resultadoInsercao = super.inserir(this);
		
		if(resultadoInsercao == BancoDeDados.RESULTADO_SUCESSO){
			System.out.println("Classe Usuario do Servidor: Diretorio do usuario criado");
			File diretorioUsuario = new File(Usuario.DIRETORIO_USUARIOS + username);
			diretorioUsuario.mkdirs();
		}
		
		return resultadoInsercao;
	}
	
	/** Exclui este usuario do sistema
	 * @return <code>boolean</code> com <code>true</code> se o usuario foi excluido e <code>false</code> senao
	 */
	public boolean excluir(){
		boolean excluiu = false;
		
		try{
			excluiu = super.excluir(this.username, this.senha);
			if(excluiu){ //exclui diretorio do usuario
				this.removerTodosArquivos(new File(Usuario.DIRETORIO_USUARIOS + this.username + "/"));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return excluiu;		
	}
	
	/** Verifica se um nome de usuario ja existe no banco de dados
	 * @return <code>boolean</code> com <code>true</code> se o usuario existe e <code>false</code> senao
	 */
	public static boolean exists(String username){
		boolean existe = false;
		
		try {
			existe = BDUsuario.exists(username);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}
	
	/** Verifica se um nome de usuario e senha ja existem no banco de dados
	 * @return <code>boolean</code> com <code>true</code> se o usuario existe e <code>false</code> senao
	 */
	public static boolean exists(String username, String senha){
		boolean existe = false;
		
		try {
			existe = BDUsuario.exists(username, senha);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}
	
	/** Remove todos arquivos do usuario
	 * @param diretorio <code>File</code> com o diretorio do usuario
	 * @see File
	 */
	public void removerTodosArquivos(File diretorio) {
	     if (diretorio.exists() && diretorio.isDirectory()) {
	         File[] arquivosDiretorios = diretorio.listFiles();
	         for (File arquivo : arquivosDiretorios) {
	             removerTodosArquivos(arquivo);
	         }
	     }
	     diretorio.delete();
	  }
	
}
