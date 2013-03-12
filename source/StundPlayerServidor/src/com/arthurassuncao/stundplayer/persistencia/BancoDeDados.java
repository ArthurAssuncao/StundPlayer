package com.arthurassuncao.stundplayer.persistencia;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.hsqldb.lib.tar.DbBackup;
import org.hsqldb.lib.tar.TarMalformatException;
import org.hsqldb.util.DatabaseManagerSwing;

import com.arthurassuncao.stundplayer.classes.LogSistema;

/** Classe para manipular o banco de dados
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * 
 * @see Connection
 * @see Statement
 *
 */
public abstract class BancoDeDados {
	private Connection conexao;
	private Statement statement;
	private String arquivoBD;

	/** <code>int</code> com o valor para o resultado de sucesso da operacao no banco de dados*/
	public static final int RESULTADO_SUCESSO = 0;
	/** <code>int</code> com o valor para o resultado de erro de banco de dados*/
	public static final int RESULTADO_ERRO_BANCO_DADOS = 1;
	/** <code>int</code> com o valor para o resultado de registro duplicado*/
	public static final int RESULTADO_ERRO_REGISTRO_DUPLICADO = 2;
	/** <code>int</code> com o valor para o resultado de erro desconhecido*/
	public static final int RESULTADO_ERRO_DESCONHECIDO = 9;

	/**
	 * O construtor cria uma nova conexão com o banco de dados.
	 * A conexão é possibilitada pelo driver JDBC do banco de dados.
	 * 
	 */
	public BancoDeDados(){

	}
	/**
	 * O construtor cria uma nova conexão com o banco de dados contido
	 * no arquivo passado como parametro. A conexão é possibilitada pelo driver
	 * JDBC do banco de dados.
	 * 
	 * @param arquivo <code>String</code> com endereco do banco de dados.
	 */
	public BancoDeDados(String arquivo){
		setArquivoBD(arquivo);
	}

	/** Retorna um <code>String</code> com o arquivoBD
	 * @return um <code>String</code> com o arquivoBD
	 */
	protected String getArquivoBD() {
		return arquivoBD;
	}

	/** Seta o arquivo do bando de dados
	 * @param arquivoBD <code>String</code> com o endereco do arquivo do banco de dados
	 */
	protected void setArquivoBD(String arquivoBD) {
		this.arquivoBD = arquivoBD;
	}

	/** Abre a conexao com o banco de dados
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	protected void abreConexao() throws SQLException{
		if (!DriverManager.getConnection("jdbc:hsqldb:file:" + this.arquivoBD, "sa", "").isClosed()){
			this.conexao = DriverManager.getConnection("jdbc:hsqldb:file:" + this.arquivoBD + ";hsqldb.write_delay=0;shutdown=true", "sa", ""); //banco, usuario, senha
			this.statement = this.conexao.createStatement();
		}
	}

	/** Abre a conexao com o banco de dados, usando o endereco do arquivo passado como parametro
	 * @param arquivo <code>String</code> com o endereco ou nome do arquivo do banco de dados
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	protected void abreConexao(String arquivo) throws SQLException{
		if (!DriverManager.getConnection("jdbc:hsqldb:file:" + arquivo, "sa", "").isClosed()){
			this.conexao = DriverManager.getConnection("jdbc:hsqldb:file:" + arquivo + ";hsqldb.write_delay=0;shutdown=true", "sa", ""); //banco, usuario, senha
			this.statement = this.conexao.createStatement();
		}
	}

	/** Retorna uma conexao com o banco de dados
	 * @param arquivo <code>String</code> com o endereco ou nome do arquivo do banco de dados
	 * @return um <code>Connection</code> com a conexao com o banco de dados
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	protected static Connection abreQualquerConexao(String arquivo) throws SQLException{
		return DriverManager.getConnection("jdbc:hsqldb:file:" + arquivo + ";hsqldb.write_delay=0;shutdown=true", "sa", ""); //banco, usuario, senha
	}

	/** Fecha a conexao com o banco de dados
	 * 
	 */
	protected void fechaConexao(){
		try{
			//this.statement.close();
			if (!this.conexao.isClosed()){
				if(!this.statement.isClosed()){
					this.statement.close();
				}
				this.conexao.close();
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			LogSistema.mostraMensagemErroBD(e.getMessage());
		}
	}

	//Executa comandos que nao modificam o banco de dados

	/** Executa comandos SQL que nao modificam o banco de dados, como Select
	 * @param query <code>String</code> com o comando sql que será executado
	 * @return um objeto <code>ResultSet</code>  que contém os dados produzidos pela consulta, nunca <code>null</code>
	 */
	protected ResultSet executaQuery(String query){
		try{
			this.abreConexao();
			ResultSet resultadoQuery = this.statement.executeQuery(query);
			LogSistema.mostraMensagem("Comando SQL executado: " + query);
			return resultadoQuery;
		}
		catch(SQLException e){
			e.printStackTrace();
			LogSistema.mostraMensagemErroBD(e.getMessage());
			return null;
		}
		finally{
			this.fechaConexao();
		}
	}

	//Executa comandos que modificam o banco de dados
	/** Executam comandos que modificam o banco de dados, comandos estes que não retornam nada, como INSERT, UPDATE, DELETE...
	 * @param query <code>String</code> com o comando sql que será executado
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	protected void executaUpdate(String query) throws SQLException{
		this.abreConexao();

		this.statement.executeUpdate(query);
		this.conexao.commit();
		this.statement.execute("SHUTDOWN"); //forca a gravacao dos dados no disco

		this.fechaConexao();
	}

	/** Abre a interface do banco de dados sem configuração
	 * 
	 */
	public static void mostraJanelaBancoDeDados(){
		//DatabaseManagerSwing.main(new String[] {"--help", "--noexit"}); //exibe o help
		DatabaseManagerSwing.main(new String[0]);
	}

	/** Abre a interface do banco de dados com as configuracoes especificadas
	 * @param configs um <code>String[]</code> com as configuracoes do banco de dados 
	 */
	public static void mostraJanelaBancoDeDados(String[] configs){
		//DatabaseManagerSwing.main(new String[] {"--help", "--noexit"}); //exibe o help
		DatabaseManagerSwing.main(configs);
	}

	/** Abre a interface do banco de dados no modo Help
	 * 
	 */
	public static void mostraOpcoesJanelaBancoDeDados(){
		DatabaseManagerSwing.main(new String[] {"--help"}); //exibe o help com as opcoes
	}

	//executa arquivos .sql
	/** Abre e executa os comando de um arquivo SQL
	 * @param enderecoArquivoSQL <code>String</code> com o endereco do arquivo sql
	 * @return <code>boolean</code> com <code>true</code> se o arquivo foi aberto e executado e <code>false</code> senão.
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 * @throws IOException possivel erro ao abrir o arquivo
	 */
	protected boolean abreArquivoSQL(String enderecoArquivoSQL) throws SQLException, IOException{
		boolean abriu = true;
		File arquivo = new File(enderecoArquivoSQL);
		SqlFile arquivoSql = new SqlFile(arquivo);
		this.abreConexao();
		arquivoSql.setConnection(this.conexao);
		try{
			arquivoSql.execute();
			abriu = true;
		}
		catch(SqlToolError e){
			e.printStackTrace();
			LogSistema.mostraMensagemErroBD(e.getMessage());
			abriu = false;
		}
		this.fechaConexao();
		arquivoSql.closeReader();
		return abriu;
	}

	//mais info em http://hsqldb.org/doc/2.0/apidocs/org/hsqldb/lib/tar/DbBackup.html
	/** Cria um arquivo no formato tar.gz com endereço especifico contendo um backup do banco de dados.
	 * @param enderecoArquivo <code>String</code> com o endereco do arquivo que será salvo.
	 * @return <code>boolean</code> com <code>true</code> se o backup foi criado e <code>false</code> senão.
	 * @throws TarMalformatException possivel erro ao gerar o arquivo no formato tar
	 * @throws IOException possivel erro ao criar o arquivo
	 */
	protected boolean criaBackupBancoDados(String enderecoArquivo) throws TarMalformatException, IOException{
		File arquivoBackup              = new File(enderecoArquivo + ".tar.gz"); //salva o arquivo em "diretorio" com o nome de backup.tar.gz
		String enderecoBanco    = new String(BDPlayer.ENDERECO_BANCO_DE_DADOS);
		DbBackup backup                 = new DbBackup(arquivoBackup, enderecoBanco);
		backup.setAbortUponModify(false); //cria o backup, mesmo com o arquivo DBEleicao.properties com modified=yes. Nao sei como fazer ele ficar no

		if(arquivoBackup.exists()){
			boolean opcao;
			/*opcao = JanelaMensagem.mostraMensagemConfirma(null, "Criar Backup", "Existe um arquivo com o nome backup.tar.gz neste diretorio\n" +
                                        "Sobrescrever backup existente? ");*/
			opcao = true;
			if (opcao){
				backup.setOverWrite(true); //permite escrever por cima do arquivo ja existente
				backup.write(); //escreve no arquivo
				return true;
			}
		}
		else{
			backup.write(); //escreve no arquivo
			return true;
		}
		return false;
	}

	/** Restaura o banco de dados a partir de um backup
	 * @param arquivoBackup <code>String</code> com o endereco do arquivo de backup
	 * @throws TarMalformatException possivel erro ao ler o arquivo no formato tar
	 * @throws IOException possivel erro ao abrir o arquivo ou restaurar os arquivos do backup
	 */
	public void restauraBackupBancoDeDados(String arquivoBackup) throws TarMalformatException, IOException{
		String metodo = "--extract"; //extrai
		String sobrescrever = "--overwrite"; //extrai
		String enderecoDestino = "banco/"; //endereco do banco de dados
		/*if(JanelaMensagem.mostraMensagemConfirmaWarning(null, "Importar Backup", "Esta operacao ira apagar o banco de dados atual e nao sera possivel restura-lo, a nao ser que tenha um backup\n" +
                                "Importar backup assim mesmo?"));*/
		DbBackup.main(new String[]{metodo, sobrescrever, arquivoBackup, enderecoDestino});
	}

	/** Substitui as aspas de uma string por aspas validas no banco de dados. Cada aspas simples sao substituidas por 2 aspas simples.
	 * @param string <code>String</code> com a string que tera suas aspas substituidas
	 * @return <code>String<code> com a string com as aspas validas no banco de dados
	 */
	protected static String substituiAspasSimplesPorUmaValidaNoBD(String string){
		if(string != null)
			return string.replace("'", "''");
		return string;
	}

}
