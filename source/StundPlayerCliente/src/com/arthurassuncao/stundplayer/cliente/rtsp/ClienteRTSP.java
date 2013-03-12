package com.arthurassuncao.stundplayer.cliente.rtsp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.Timer;

import com.arthurassuncao.stundplayer.classes.Configuracoes;
import com.arthurassuncao.stundplayer.cliente.rtp.RTPpacket;
import com.arthurassuncao.stundplayer.player.SoundJLayer;

/** Classe para manipular o cliente RTSP
 * @see Runnable
 */
public class ClienteRTSP implements Runnable{
	private static final String DIRETORIO_ARQUIVO_TEMPORARIO = "temp/";
	private static final String ARQUIVO_MP3_TEMPORARIO = "musica.mp3";
	//Variaveis RTP
	private String usuario;
	private DatagramPacket pacoteUDPRecebido; //pacote UDP para recebimento do servidor
	private DatagramSocket RTPsocket; //Soquete para enviar e receber pacotes UDP
	private int RTPPortaRecebimento = Configuracoes.getInstance().getPortaRTP(); //Porta onde o cliente vai receber pacotes RTP
	//private String IPServidor = Configuracoes.getInstance().getIpServidor();
	//private int RTSPPortaServidor = Configuracoes.getInstance().getPortaRTSP();

	private Timer timer; //Tempo usado para receber dados do soquete UDP
	private byte[] buffer; //Buffer para armazenas os dados recebidos do servidor 
	private static final int TAMANHO_BUFFER_BYTES = 15000; //15KB

	//Variaveis RTSP
	//estados do RTSP
	private final static int INIT = 0; //nao iniciado
	private final static int READY = 1; //recursos alocados
	private final static int PLAYING = 2; //Comeca a receber os pacotes
	//private final static int PAUSE = 5;
	private final static int TEARDOWN = 6; //recursos liberados

	private final static int CODIGO_STATUS_OK = 200;
	private int estado; //RTSP state == INIT or READY or PLAYING
	private Socket RTSPsocket; //soquete usado para enviar e receber mensagens RTSP
	//filtros de stream de entrada e saida
	private BufferedReader RTSPBufferedReader;
	private BufferedWriter RTSPBufferedWriter;
	private String NomeArquivoMusica; //arquivo de audio que sera pedido do servidor
	private String musicaASerExecutada = "musica.mp3"; //musica qualquer
	private int RTSPNumeroSequencia = 0; //Numero de sequencia das mensagens RTSP numa sessao
	private int RTSPid = 0; //ID da sessao RTSP (pega do servidor RTSP)

	//private InputStream input;
	//private MP3Thread musica;
	private SoundJLayer player;
	private long tamanhoBufferAtual;
	@SuppressWarnings("unused")
	private long tamanhoMusicaEsperado;
	//private final int tempoEspera = 5; //5 segundos de espera
	private FileOutputStream fileOutput;
	//private BufferedOutputStream bufferOut;
	//private BufferedInputStream bufferIn;
	private byte[] payload; //pacote

	//private Thread threadArquivo;

	private static final int FRAME_PERIOD = 20; //Frame periódico de um stream de áudio, em milissegundos.
	private final static String CRLF = "\r\n"; //nova linha

	/** Cria uma instancia do cliente RTSP
	 * @param usuario <code>String</code> com o usuario
	 */
	@SuppressWarnings("unused")
	private ClienteRTSP(String usuario){
		this.usuario = usuario;

		timer = new Timer(FRAME_PERIOD, new timerListener());
		timer.setInitialDelay(0);
		timer.setCoalesce(true);

		buffer = new byte[TAMANHO_BUFFER_BYTES];

		try {
			File diretorioTemporario = new File(DIRETORIO_ARQUIVO_TEMPORARIO);
			if(!diretorioTemporario.exists()){
				diretorioTemporario.mkdir();
				diretorioTemporario = null;
			}
			fileOutput = new FileOutputStream(ClienteRTSP.DIRETORIO_ARQUIVO_TEMPORARIO + ClienteRTSP.ARQUIVO_MP3_TEMPORARIO);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** Cria uma instancia do cliente RTSP
	 * @param usuario <code>String</code> com o usuario
	 * @param musicaASerExecutada <code>String</code> com a musica que sera executada
	 */
	public ClienteRTSP(String usuario, String musicaASerExecutada, long tamanhoMusicaEsperado){
		this.usuario = usuario;
		this.musicaASerExecutada = musicaASerExecutada;
		this.tamanhoMusicaEsperado = tamanhoMusicaEsperado;

		timer = new Timer(FRAME_PERIOD, new timerListener());
		timer.setInitialDelay(0);
		timer.setCoalesce(true);

		buffer = new byte[TAMANHO_BUFFER_BYTES];

		try {
			File diretorioTemporario = new File(DIRETORIO_ARQUIVO_TEMPORARIO);
			if(!diretorioTemporario.exists()){
				diretorioTemporario.mkdir();
				diretorioTemporario = null;
			}
			fileOutput = new FileOutputStream(ClienteRTSP.DIRETORIO_ARQUIVO_TEMPORARIO + ClienteRTSP.ARQUIVO_MP3_TEMPORARIO);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** Classe para manipular os eventos do timer
	 * @see ActionListener
	 */
	private class timerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(estado != TEARDOWN){
				//Controi o DatagramPacket para receber dados do soquete UDP
				pacoteUDPRecebido = new DatagramPacket(buffer, buffer.length);

				try{
					//recebe o DatagramPacket do soquete
					RTPsocket.receive(pacoteUDPRecebido);

					//Cria um objeto RTPpacket do pacote UDP
					RTPpacket pacoteRTP = new RTPpacket(pacoteUDPRecebido.getData(), pacoteUDPRecebido.getLength());

					//Imprime campos importantes do cabecalho do pacote RTP recebido
					//System.out.println("Pacote RTP com numero de sequencia: " + pacoteRTP.getSequenceNumber() + "\tTimeStamp: " + pacoteRTP.getTimestamp() + " ms\t Tipo: " + pacoteRTP.getPayloadType());

					//imprime o cabecalho do pacote RTP
					//pacoteRTP.printheader();

					//pega o bitstream do pacote do objeto RTPpacket
					int payloadLength = pacoteRTP.getPayloadLength();
					payload = new byte[payloadLength];
					pacoteRTP.getPayload(payload);
					if(payload.length == 0){//recebeu o ultimo pacote, pacote vazio
						System.out.println("Terminou de receber os pacotes do arquivo");
						timer.stop();
						return;
					}

					fileOutput.write(payload);
					fileOutput.flush();
					//musica.aumentaBuffer(payload);
					tamanhoBufferAtual += payload.length;
					if(player == null && tamanhoBufferAtual >= TAMANHO_BUFFER_BYTES){
						player = new SoundJLayer(DIRETORIO_ARQUIVO_TEMPORARIO + ARQUIVO_MP3_TEMPORARIO);
						player.play();
					}
				}
				catch (IOException ioe) {
					//ioe.printStackTrace();
					System.err.println("Excecao IOException no timelistener da classe ClienteRTSP: " + ioe.getMessage());
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			} //se nao estiver parado
			else{
				System.out.println("Parou de tocar");
				timer.stop();
			}
		}
	}//class timerListener

	/** Analisa a mensagem recebido do servidor
	 * @return <code>int</code> com o codigo de status da mensagem recebida
	 */
	private int analisaMensagemServidor() {
		int codigoStatusRecebido = -1;

		try{
			//analiza a linha de status e extrai o codigo de retorno
			String linhaEstado = this.RTSPBufferedReader.readLine();
			//System.out.println("RTSP Client - Received from Server:");
			if(linhaEstado == null){
				return codigoStatusRecebido; //-1
			}
			System.out.println(linhaEstado);

			StringTokenizer tokens = new StringTokenizer(linhaEstado);
			tokens.nextToken(); //pula a versao do RTSP
			codigoStatusRecebido = Integer.parseInt(tokens.nextToken());

			//if codigo de retorno for OK pega e imprime as outras duas linhas
			if (codigoStatusRecebido == CODIGO_STATUS_OK){
				String linhaNumeroSequencia = this.RTSPBufferedReader.readLine();
				System.out.print(linhaNumeroSequencia + "\t");

				String linhaSessao = this.RTSPBufferedReader.readLine();
				System.out.println(linhaSessao);

				//if state == INIT gets the Session Id from the SessionLine
				tokens = new StringTokenizer(linhaSessao);
				tokens.nextToken(); //pula o Sessao:
				RTSPid = Integer.parseInt(tokens.nextToken());
			}
		}
		catch(Exception ex){
			//System.out.println("Exception caught : "+ex.getMessage());
			ex.printStackTrace();
			System.exit(0);
		}

		return(codigoStatusRecebido);
	}

	/** Envia uma requisicao RTSP ao servidor
	 * @param tipoRequisicao <code>String</code> com o tipo da requisicao
	 */
	private void enviaRequisicaoRTSP(String tipoRequisicao){ //enviaRequisicaoRTSP
		System.out.println();
		try{

			/*RTSPBufferedWriter.write(tipoRequisicao + " " + this.usuario + " RTSP/1.0" + CRLF); //envia so a requisicao, depois a musica, linhas separadas
			System.out.println("Enviado-> " + tipoRequisicao + " " + this.usuario + " RTSP/1.0");
			if(tipoRequisicao.equalsIgnoreCase("SETUP")){
				RTSPBufferedWriter.write(this.NomeArquivoMusica + CRLF);
				System.out.println("this.NomeArquivoMusica");
			}*/

			RTSPBufferedWriter.write(tipoRequisicao + " " + this.usuario + "|" + this.NomeArquivoMusica + " RTSP/1.0" + CRLF); //envia usuario|musica
			System.out.println("Enviado-> " + tipoRequisicao + " " + this.usuario + "|" + this.NomeArquivoMusica + " RTSP/1.0");
			
			//envia a linha com o numero de sequencia
			RTSPBufferedWriter.write("CSeq: " + RTSPNumeroSequencia + CRLF);
			System.out.print("CSeq: " + RTSPNumeroSequencia + "\t");

			//verifica se o tipo da requiscao e SETUP e se for envia Transporte RTP/UDP e PortaCliente + a porta rtp
			//se nao for, envia Sessao e o id da sessao
			if ((tipoRequisicao).compareTo("SETUP") == 0){
				RTSPBufferedWriter.write("Transporte: RTP/UDP; PortaCliente= " + RTPPortaRecebimento + CRLF);
				System.out.println("Transporte: RTP/UDP; PortaCliente= " + RTPPortaRecebimento);
			}
			else{
				RTSPBufferedWriter.write("Sessao: " + RTSPid + CRLF);
				System.out.println("Sessao: " + RTSPid);
			}
			RTSPBufferedWriter.flush();
		}
		catch(Exception ex){
			//System.out.println("Exception caught : " + ex.getMessage());
			ex.printStackTrace();
			//System.exit(0);
		}
		finally{
			System.out.println();
		}
	}

	/*private byte[] getPayload() {
		return this.payload;
	}*/

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		String ipServidorRTSP = Configuracoes.getInstance().getIpServidor();
		int portaServidorRTSP = Configuracoes.getInstance().getPortaRTSP();

		//this.NomeArquivoMusica = "musica/musica.mp3";
		this.NomeArquivoMusica = this.musicaASerExecutada;

		try {
			this.RTSPsocket = new Socket(InetAddress.getByName(ipServidorRTSP), portaServidorRTSP);
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//cria os stream de entrada e saida
		try{
			this.RTSPBufferedReader = new BufferedReader(new InputStreamReader(this.RTSPsocket.getInputStream()) );
			this.RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.RTSPsocket.getOutputStream()) );
		}
		catch(IOException e){
			e.printStackTrace();
		}

		if (this.estado == ClienteRTSP.INIT) {
			
			try{
				//cria um novo DatagramSocket para receber os pacotes RTP do servidor na porta RTPPortaRecebimento
				this.RTPsocket = new DatagramSocket(RTPPortaRecebimento);

				//set TimeOut para 5msec
				//this.RTPsocket.setSoTimeout(5);
			}
			catch (SocketException se){
				System.out.println("Socket exception: " + se.getMessage());
				this.encerraSemResposta();
				//System.exit(0);
			}

			//inicia o numero de sequencia do RTSP
			this.RTSPNumeroSequencia = 1;

			//envia mensagem de SETUP para o servidor
			this.enviaRequisicaoRTSP("SETUP");

			//esperando por resposta do servidor
			if (analisaMensagemServidor() != CODIGO_STATUS_OK){
				System.out.println("\nResposta do servidor invalida");
			}
			else {
				//muda o estado do RTSP e imprime o novo estado
				this.estado = ClienteRTSP.READY;
				System.out.println("\nNovo estado RTSP: READY");
			}
		}

		//ja inicia
		this.play();
	}

	/** Reinicia o cliente
	 * 
	 */
	private void reinicia(){
		player = null;
		
		timer = new Timer(FRAME_PERIOD, new timerListener());
		timer.setInitialDelay(0);
		timer.setCoalesce(true);

		buffer = new byte[TAMANHO_BUFFER_BYTES];

		try {
			File diretorioTemporario = new File(DIRETORIO_ARQUIVO_TEMPORARIO);
			if(!diretorioTemporario.exists()){
				diretorioTemporario.mkdir();
				diretorioTemporario = null;
			}
			fileOutput = new FileOutputStream(ClienteRTSP.DIRETORIO_ARQUIVO_TEMPORARIO + ClienteRTSP.ARQUIVO_MP3_TEMPORARIO);
		}
		catch (IOException e){
			e.printStackTrace();
		}

		String ipServidorRTSP = Configuracoes.getInstance().getIpServidor();
		int portaServidorRTSP = Configuracoes.getInstance().getPortaRTSP();

		this.NomeArquivoMusica = this.musicaASerExecutada;

		try {
			this.RTSPsocket = new Socket(InetAddress.getByName(ipServidorRTSP), portaServidorRTSP);
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//cria os stream de entrada e saida
		try{
			this.RTSPBufferedReader = new BufferedReader(new InputStreamReader(this.RTSPsocket.getInputStream()) );
			this.RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.RTSPsocket.getOutputStream()) );
		}
		catch(IOException e){
			e.printStackTrace();
		}

		if (this.estado == ClienteRTSP.INIT) {
			//Inicia o bloco do RTPsocket para receber os dados
			try{
				//cria um novo DatagramSocket para receber os pacotes RTP do servidor na porta RTPPortaRecebimento
				this.RTPsocket = new DatagramSocket(RTPPortaRecebimento);

				//seta o timeout como 5 msec
				this.RTPsocket.setSoTimeout(5);
			}
			catch (SocketException se){
				System.out.println("Socket exception: " + se.getMessage());
				System.exit(0);
			}

			//numero de sequencia do RTSP
			this.RTSPNumeroSequencia = 1;

			//envia mensagem de SETUP ao servidor
			this.enviaRequisicaoRTSP("SETUP");

			//espera por resposta do servidor
			if (analisaMensagemServidor() != CODIGO_STATUS_OK){
				System.out.println("\nResposta do servidor invalida");
			}
			else {
				//muda o estado do RTSP e imprime o novo estado
				this.estado = ClienteRTSP.READY;
				System.out.println("\nNovo estado RTSP: READY");
			}
		}

		//ja inicia
		this.play();
	}

	/** Inicia a conexao com o servidor caso ainda nao tenha feito ou executa a musica caso ja tenha feito a conexao
	 * 
	 */
	public void play(){
		if (this.estado == ClienteRTSP.READY) {
			//aumenta o numero de sequencia do RTSP
			this.RTSPNumeroSequencia++;

			//envia a mensagem de PLAY para o servidor
			enviaRequisicaoRTSP("PLAY");
 
			//espera por resposta
			if (analisaMensagemServidor() != CODIGO_STATUS_OK){
				System.out.println("\nResposta do servidor invalida");
			}
			else {
				//muda o estado RTSP e imprime o novo estado
				this.estado = ClienteRTSP.PLAYING;
				System.out.println("\nNovo estado RTSP: PLAYING");

				//inicia o timer
				this.timer.start();

				//threadTimer.start();
			}
		}
		else if(this.estado == ClienteRTSP.PLAYING){
			this.player.play();
		}
		else if(this.estado == ClienteRTSP.INIT){ //Reinicia o clienteRTSP
			this.reinicia();
		}
	}

	/** Pausa a musica caso a musica esteja em execucao e continua caso esteja pausada
	 * 
	 */
	public void pause(){
		if(player != null){
			player.pauseToggle();
		}
	}

	/** Para a execucao da musica
	 * 
	 */
	public void stop(){
		//System.out.println("botao Teardown pressionado !");  

		if(this.RTSPsocket != null && !this.RTSPsocket.isClosed()){
			//aumenta o numero de sequencia do RTSP
			this.RTSPNumeroSequencia++;

			//envia a mensagem de TEARDOWN para o servidor
			this.enviaRequisicaoRTSP("TEARDOWN");
 
			//espera por resposta
			if (analisaMensagemServidor() != CODIGO_STATUS_OK){
				System.out.println("Resposta do servidor invalida");
			}
			else {
				//muda o estado RTSP e imprimir o novo estado
				this.estado = ClienteRTSP.INIT;
				System.out.println("\nNovo estado RTSP: INIT");

				//fecha conexao
				try {
					this.RTSPsocket.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				this.RTPsocket.close();
				this.tamanhoBufferAtual = 0;
				try {
					fileOutput.flush();
					fileOutput.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				//System.exit(0);
			}
		}
		if(player != null){
			player.stop();
		}
		player = null;
		//stop the timer
		this.timer.stop();
	}

	/** Encerra a execucao da musica, mas nao espera por resposta
	 * 
	 */
	@Deprecated
	public void encerraSemResposta(){
		//System.out.println("botao Teardown pressionado !");

		if(this.RTSPsocket != null && !this.RTSPsocket.isClosed()){
			//aumenta o numero de sequencia do RTSP
			this.RTSPNumeroSequencia++;

			//envia a mensagem de FINALIZE para o servidor
			this.enviaRequisicaoRTSP("FINALIZE");

			//muda o estado RTSP e imprime o novo esatdo
			this.estado = ClienteRTSP.INIT;
			System.out.println("\nNovo estado RTSP: INIT");

			//fecha conexao
			try {
				this.RTSPsocket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			this.RTPsocket.close();
			this.tamanhoBufferAtual = 0;
			try {
				fileOutput.flush();
				fileOutput.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			//System.exit(0);
		}
		player.stop();
		player = null;
		//stop the timer
		this.timer.stop();
		System.out.flush(); //libera os recursos da Thread
	}
	
	/** Encerra a execucao da musica
	 * 
	 */
	public void encerra(){

		if(this.RTSPsocket != null && !this.RTSPsocket.isClosed()){
			//aumenta o numero de sequencia do RTSP
			this.RTSPNumeroSequencia++;
 
			//espera por resposta
			if (analisaMensagemServidor() != CODIGO_STATUS_OK){
				System.out.println("Resposta do servidor invalida");
			}
			//muda estado do RTSP e imprime o novo estado
			this.estado = ClienteRTSP.INIT;
			System.out.println("\nNovo estado RTSP: INIT");

			//fecha conexao
			try {
				this.RTSPsocket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			this.RTPsocket.close();
			this.tamanhoBufferAtual = 0;
			try {
				fileOutput.flush();
				fileOutput.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			//System.exit(0);
		}
		player.stop();
		player = null;
		//stop the timer
		this.timer.stop();
		System.out.flush(); //libera os recursos da Thread
	}

	/** Seta a musica que sera executada
	 * @param musica <code>String</code> com a musica
	 */
	public void setMusicaASerExecutada(String musica){
		this.musicaASerExecutada = musica;
	}

	/** Verifica se a musica esta em execucao ou esta parada
	 * @return <code>boolean</code> com <code>true</code> se a musica esta rodando e <code>false</code> senao
	 */
	public boolean isMusicaRodando(){
		if(player == null){
			return false;
		}
		else{
			return !player.isComplete();
			//return !player.isComplete() || !((SoundJLayerForOnline)player).isWaiting();
		}
	}

	/** Verifica se o download da musica esta em execucao
	 * @return <code>boolean</code> com <code>true</code> se o download da musica esta em execucao e <code>false</code> senao
	 */
	public boolean isDownloading(){
		return this.timer.isRunning();
	}

	/** Retorna o player
	 * @return <code>SoundJLayer</code> com o player
	 * @see SoundJLayer
	 */
	public SoundJLayer getPlayer(){
		return this.player;
	}
}