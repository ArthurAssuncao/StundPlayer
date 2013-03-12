package com.arthurassuncao.stundplayer.persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.arthurassuncao.stundplayer.classes.LogSistema;
import com.arthurassuncao.stundplayer.classes.Usuario;

/** Classe para manipular dados dos Usuarios no banco de dados
 * 
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * 
 * @see Usuario
 * @see BDPlayer
 *
 */
public abstract class BDUsuario extends BDPlayer {

	/** Insere um usuario no banco de dados
	 * @param usuario <code>Usuario</code> que será inserido no banco de dados
	 * @return <code>int</code> com o resultado da inserção. Este valor é uma constante definida na classe <code>BancoDeDados</code>
	 * 
	 * @see Usuario
	 * @see BancoDeDados#RESULTADO_SUCESSO
	 * @see BancoDeDados#RESULTADO_ERRO_REGISTRO_DUPLICADO
	 * @see BancoDeDados#RESULTADO_ERRO_BANCO_DADOS
	 * @see BancoDeDados#RESULTADO_ERRO_DESCONHECIDO
	 */
	protected int inserir(Usuario usuario){
		String username;
		String senha;

		username = substituiAspasSimplesPorUmaValidaNoBD( usuario.getUsername() );
		senha = substituiAspasSimplesPorUmaValidaNoBD( usuario.getSenha() );

		String comandoInsercao = "INSERT INTO usuario(idUsuario, username, senha) VALUES ";

		try{
			if(!BDUsuario.exists(username)){ //se nao existe, entao insere
				String comandoSQL = comandoInsercao + "(" + "NEXT VALUE FOR seq_usuario" + "," +
						"\'" + username + "\'" + "," +
						"\'" + senha + "\'" + 
						")";
				try{
					this.executaUpdate(comandoSQL);
				}
				catch(SQLException e){
					e.printStackTrace();
					return BancoDeDados.RESULTADO_ERRO_BANCO_DADOS;
				}
			}
			else{
				return BancoDeDados.RESULTADO_ERRO_REGISTRO_DUPLICADO;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			LogSistema.mostraMensagemErroBD(e.getMessage());
			return BancoDeDados.RESULTADO_ERRO_BANCO_DADOS;
		}

		return BancoDeDados.RESULTADO_SUCESSO;
	}

	/** Verifica se existe um usuario com username especificados
	 * @param username <code>String</code> com o username do usuario
	 * @return <code>boolean</code> com <code>true</code> se o usuario existe e <code>false</code> senão.
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	protected static boolean exists(String username) throws SQLException{
		int contagem = 0;

		BANCO_DE_DADOS_PLAYER.abreConexao();

		username =  substituiAspasSimplesPorUmaValidaNoBD( username );
		
		ResultSet resultadoQuery = BANCO_DE_DADOS_PLAYER.executaQuery("SELECT COUNT(*) AS contagem FROM usuario WHERE username=\'" + username + "\'");
		resultadoQuery.next();

		String contagemUsuario = resultadoQuery.getString("contagem");

		if(contagemUsuario != null){
			contagem = Integer.parseInt(contagemUsuario);
		}

		BANCO_DE_DADOS_PLAYER.fechaConexao();

		return contagem > 0 ? true : false;		
	}

	/** Verifica se existe um usuario com username e senha especificados
	 * @param username <code>String</code> com o username do usuario
	 * @param senha <code>String</code> com a senha do usuario
	 * @return <code>boolean</code> com <code>true</code> se o usuario existe e <code>false</code> senão.
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	protected static boolean exists(String username, String senha) throws SQLException{
		int contagem = 0;

		BANCO_DE_DADOS_PLAYER.abreConexao();

		username =  substituiAspasSimplesPorUmaValidaNoBD( username );
		senha =  substituiAspasSimplesPorUmaValidaNoBD( senha );

		ResultSet resultadoQuery = BANCO_DE_DADOS_PLAYER.executaQuery("SELECT COUNT(*) AS contagem FROM usuario WHERE username=\'" + username + "\' AND senha=\'" + senha + "\'");
		resultadoQuery.next();

		String contagemUsuario = resultadoQuery.getString("contagem");

		if(contagemUsuario != null){
			contagem = Integer.parseInt(contagemUsuario);
		}

		BANCO_DE_DADOS_PLAYER.fechaConexao();

		return contagem > 0 ? true : false;		
	}

	/** Exclui um usuario do banco de dados
	 * @param username <code>String</code> com o username do usuario
	 * @param senha <code>String</code> com a senha do usuario
	 * @return <code>boolean</code> com <code>true</code> se o usuario foi excluido e <code>false</code> senão.
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	protected boolean excluir(String username, String senha) throws SQLException{
		this.abreConexao();

		username =  substituiAspasSimplesPorUmaValidaNoBD( username );
		senha =  substituiAspasSimplesPorUmaValidaNoBD( senha );

		String comandoSQL = "DELETE FROM usuario WHERE username =\'" + username + "\' AND senha =\'" + senha + "\'";
		this.executaUpdate(comandoSQL);

		this.fechaConexao();

		return true;
	}

	/** Pesquisa por um usuario no sistema
	 * @param loginUsuario <code>String</code> com o nome de usuario do usuario
	 * @param senhaUsuario <code>String</code> com a senha do usuario
	 * @return <code>Usuario</code> com o usuario encontrado, caso nao seja encotrado retorna <code>null</code>
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	protected static Usuario pesquisa(String loginUsuario, String senhaUsuario) throws SQLException{
		Usuario usuario = null;
		String comandoPesquisa = null;
		String username = null;
		String senha = null;

		loginUsuario =  substituiAspasSimplesPorUmaValidaNoBD( loginUsuario );
		senhaUsuario =  substituiAspasSimplesPorUmaValidaNoBD( senhaUsuario );

		comandoPesquisa = "SELECT username, senha FROM usuario WHERE username=\'" + loginUsuario + "\' AND senha=\'" + senhaUsuario + "\'";

		BANCO_DE_DADOS_PLAYER.abreConexao();
		ResultSet resultadoQuery = BANCO_DE_DADOS_PLAYER.executaQuery(comandoPesquisa);

		if(resultadoQuery.next()){
			username = resultadoQuery.getString("username");
			senha = resultadoQuery.getString("senha");
		}

		if(username != null && senha != null){
			usuario = new Usuario(username, senha);
		}

		BANCO_DE_DADOS_PLAYER.fechaConexao();

		return usuario;
	}
}
