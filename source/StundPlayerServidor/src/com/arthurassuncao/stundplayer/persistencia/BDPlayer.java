package com.arthurassuncao.stundplayer.persistencia;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.hsqldb.lib.tar.TarMalformatException;
import org.hsqldb.util.DatabaseManagerSwing;

import com.arthurassuncao.stundplayer.classes.LogSistema;

/** Classe para manipular o banco de dados do sistema
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see BancoDeDados
 */
public class BDPlayer extends BancoDeDados implements Runnable{

	/** Caminho do banco de dados*/
	private static final String DIRETORIO_BANCO_DE_DADOS = "banco/";
	protected static final String ENDERECO_BANCO_DE_DADOS = DIRETORIO_BANCO_DE_DADOS + "DBPlayer"; //caminho do banco de dados
	//private static final String ENDERECO_ARQUIVO_REMOVE_ESTRUTURA_SQL 	= DIRETORIO_BANCO_DE_DADOS + "DBPlayerDeletaTabelas.sql"; //arquivo para apagar as tabelas
	//private static final String ENDERECO_ARQUIVO_ESTRUTURA_SQL 	= DIRETORIO_BANCO_DE_DADOS + "DBPlayerCriaTabelas.sql"; //arquivo para criar as tabelas e sequencias
	//private static final String ENDERECO_ARQUIVO_EXCLUI_DADOS_SQL = DIRETORIO_BANCO_DE_DADOS + "DBPlayerDeletaRegistrosTabelas.sql";
	//private static final String ENDERECO_ARQUIVO_DADOS_SQL 	= DIRETORIO_BANCO_DE_DADOS + "DBPlayerInsereDados.sql"; //arquivo para inserir os dados
	private static final String[] CONFIGS_BANCO_DADOS = {"--url", "jdbc:hsqldb:file:" + ENDERECO_BANCO_DE_DADOS, "--user", "sa", "--password", "", "--noexit"};

	/** <code>BDPlayer</code> com a instancia do banco de dados */
	public static BDPlayer BANCO_DE_DADOS_PLAYER = null;
	private boolean iniciado = false;

	//Tamanho dos dados(varchar e numeric) no banco de dados
	/** <code>int</code> com o tamanho do nome da musica */
	public static final int TAMANHO_NOME_MUSICA = 100;

	/** Cria uma instancia do banco de dados do sistema
	 * 
	 */
	protected BDPlayer(){
		super(ENDERECO_BANCO_DE_DADOS);
	}

	/** Retorna a instancia do banco de dados do sistema
	 * @return <code>BDPlayer</code> com a instancia da classe
	 */
	public static BDPlayer getInstance(){
		if(BANCO_DE_DADOS_PLAYER == null){
			BANCO_DE_DADOS_PLAYER = new BDPlayer();
		}
		return BANCO_DE_DADOS_PLAYER;
	}

	@Override
	public void run(){
		try {
			this.iniciaBanco();
		}
		catch (SQLException e) {
			LogSistema.mostraMensagemErroBD("Falha ao iniciar o banco de dados: " + e.getMessage());
			LogSistema.mostraMensagemErroBD("Servidor sera finalizado");
			System.exit(0);
		}
	}

	/** Inicia o banco de dados
	 * @throws SQLException possivel erro gerado por má configuração do banco de dados
	 */
	private void iniciaBanco() throws SQLException{
		if(!iniciado){
			this.setArquivoBD(ENDERECO_BANCO_DE_DADOS);
			boolean diretorioExistia = true;
			File diretorio = new File(DIRETORIO_BANCO_DE_DADOS);
			if(!diretorio.exists()){
				diretorio.mkdirs();
				diretorioExistia = false;
			}
			this.abreConexao();
			this.fechaConexao();
			if(!diretorioExistia){
				try{
					//this.removeTabelas();
					LogSistema.mostraMensagem("Tabelas do banco de dados criadas");
					this.criaTabelas();
					//this.insereDados();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			BANCO_DE_DADOS_PLAYER.iniciado = true;
			LogSistema.mostraMensagem("Banco de Dados Iniciado");
		}
	}

	/** Abre e executa os comando de um arquivo SQL
	 * @param arquivo <code>String</code> com o endereco do arquivo sql
	 * @return <code>boolean</code> com <code>true</code> se o arquivo foi aberto e executado e <code>false</code> senão.
	 * @see BancoDeDados#abreArquivoSQL(java.lang.String)
	 */
	@Override
	public boolean abreArquivoSQL(String arquivo){
		boolean abriu = true;
		try{
			abriu = super.abreArquivoSQL(arquivo);
		}
		catch(IOException e){
			e.printStackTrace();
			LogSistema.mostraMensagemErroBD(e.getMessage());
			abriu = false;
		}
		catch(SQLException e){
			e.printStackTrace();
			LogSistema.mostraMensagemErroBD(e.getMessage());
			abriu = false;
		}
		return abriu;
	}

	/** Cria um arquivo no formato tar.gz com endereço especifico contendo um backup do banco de dados.
	 * @param enderecoArquivoBackup <code>String</code> com o endereco do arquivo que será salvo.
	 * @return <code>boolean</code> com <code>true</code> se o backup foi criado e <code>false</code> senão.
	 * @throws TarMalformatException possivel erro ao gerar o arquivo no formato tar
	 * @throws IOException possivel erro ao criar o arquivo
	 */
	public boolean criaBackupBancoDeDados(String enderecoArquivoBackup) throws TarMalformatException, IOException{
		boolean criouBackup = true;
		if (enderecoArquivoBackup != null){
			super.criaBackupBancoDados(enderecoArquivoBackup);
		}
		else{
			criouBackup = false;
		}
		return criouBackup;
	}

	/** Restaura o banco de dados a partir de um backup
	 * @param arquivoBackup <code>String</code> com o endereco do arquivo de backup
	 * @throws TarMalformatException possivel erro ao ler o arquivo no formato tar
	 * @throws IOException possivel erro ao abrir o arquivo ou restaurar os arquivos do backup
	 */
	@Override
	public void restauraBackupBancoDeDados(String arquivoBackup) throws TarMalformatException, IOException{
		if (arquivoBackup != null){
			super.restauraBackupBancoDeDados(arquivoBackup); //arquivo com extensao tar, tar.gz
			/*boolean reiniciar = false;
			reiniciar = JanelaMensagem.mostraMensagemConfirma(null, "Restaurar Backup", "Backup Restaurado\n" +
					"Para completar a restauracao do banco de dados, o banco de dados deve ser reiniciado.\n" +
					"Deseja reiniciar agora?");
			if (reiniciar){
				BANCO_DE_DADOS_ELEICOES = null;
				BANCO_DE_DADOS_ELEICOES = new BDEleicoes();
			}*/
			//else{
			LogSistema.mostraMensagemBD("O novo banco de dados estará disponivel na proxima iniciação");
			//}
		}
		else{
			//nao restaurou
		}
	}

	/** Remove as tabelas do banco de dados do sistema
	 * @throws IOException possivel erro ao abrir o arquivo sql
	 * @throws SQLException possivel erro caso o banco de dados esteja mal configurado
	 */
	@SuppressWarnings("unused")
	private void removeTabelas() throws IOException, SQLException{
		//remove todas as tabelas e sequencias do banco de dados
		//abreArquivoSQL(ENDERECO_ARQUIVO_REMOVE_ESTRUTURA_SQL);
		/* Deleta Tabelas */
		String comandoDeletaTabelas = "DROP TABLE usuario CASCADE";
		/* Deleta Sequencias*/
		String comandoDeletaSequencias = "DROP SEQUENCE seq_usuario IF EXISTS";
		BANCO_DE_DADOS_PLAYER.executaUpdate(comandoDeletaTabelas);
		BANCO_DE_DADOS_PLAYER.executaUpdate(comandoDeletaSequencias);
	}
	/** Exclui os registros das tabelas do banco de dados do sistema
	 * @throws IOException possivel erro ao abrir o arquivo sql
	 * @throws SQLException possivel erro caso o banco de dados esteja mal configurado
	 */
	public void excluiRegistros() throws IOException, SQLException{
		//remove todas as tabelas e sequencias do banco de dados
		//abreArquivoSQL(ENDERECO_ARQUIVO_EXCLUI_DADOS_SQL);
		/* Deleta Registos das Tabelas */
		String comandoRemoveRegistros = "TRUNCATE TABLE usuario";
		/* Reinicia Sequencias*/
		String comandoReiniciaSequecias = "ALTER SEQUENCE seq_usuario RESTART WITH 1";
		BANCO_DE_DADOS_PLAYER.executaUpdate(comandoRemoveRegistros);
		BANCO_DE_DADOS_PLAYER.executaUpdate(comandoReiniciaSequecias);
	}
	/** Cria as tabelas do banco de dados do sistema
	 * @throws IOException possivel erro ao abrir o arquivo sql
	 * @throws SQLException possivel erro caso o banco de dados esteja mal configurado
	 */
	private void criaTabelas() throws IOException, SQLException{
		//Cria a estrutura do banco de dados: tabelas, sequencias
		//abreArquivoSQL(ENDERECO_ARQUIVO_ESTRUTURA_SQL);
		/* Cria Tabelas */
		String comandoCriaTabelas = "" + 
				"CREATE TABLE IF NOT EXISTS usuario(" + 
				" idUsuario INTEGER NOT NULL UNIQUE," + 
				" username VARCHAR(40) NOT NULL," + 
				" senha VARCHAR(20) NOT NULL," + 
				" status VARCHAR(1) DEFAULT '0'," + 
				" CONSTRAINT pk_usuario PRIMARY KEY (username)" + 
				")";
		/* Sequencia */
		String comandoCriaSequencias = "CREATE SEQUENCE seq_usuario START WITH 1";
		BANCO_DE_DADOS_PLAYER.executaUpdate(comandoCriaTabelas);
		BANCO_DE_DADOS_PLAYER.executaUpdate(comandoCriaSequencias);
	}

	/** Insere dados predefinidos nas tabelas do banco de dados do sistema
	 * @throws IOException possivel erro ao abrir o arquivo sql
	 * @throws SQLException possivel erro caso o banco de dados esteja mal configurado
	 */
	public void insereDados() throws IOException, SQLException{
		//Insere os dados no banco de dados
		//abreArquivoSQL(ENDERECO_ARQUIVO_DADOS_SQL);
		String comandoInsereDados = "INSERT INTO usuario VALUES (NEXT VALUE FOR seq_usuario, 'paulo', 'paulo', '0'); " +
				"INSERT INTO usuario VALUES (NEXT VALUE FOR seq_usuario, 'arthur', 'arthur', '0')";
		BANCO_DE_DADOS_PLAYER.executaUpdate(comandoInsereDados);
	}

	/** Abre a interface do banco de dados com as configuracoes do sistema
	 * 
	 */
	public static void mostraJanelaBancoDeDados(){
		//DatabaseManagerSwing.main(new String[] {"--help", "--noexit"}); //exibe o help
		DatabaseManagerSwing.main(CONFIGS_BANCO_DADOS);
	}

}
