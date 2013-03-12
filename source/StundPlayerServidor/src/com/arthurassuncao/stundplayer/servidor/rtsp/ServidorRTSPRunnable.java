package com.arthurassuncao.stundplayer.servidor.rtsp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.Timer;

import com.arthurassuncao.stundplayer.RTP.RTPStream;
import com.arthurassuncao.stundplayer.classes.LogSistema;

/** Classe para manipular os servicos servidor RTSP
 * @see Runnable
 * @see ServidorRTSP
 */
public class ServidorRTSPRunnable implements Runnable{
	//protected Socket clienteSocket;
	@SuppressWarnings("unused")
	private String nomeServer;

	private static final String ENDERECO_MUSICA_USUARIOS = "usuarios/"; 

	//Variaveis RTP:
	private DatagramSocket RTPsocket; //socket usado para envio e recebimento de pacotes

	private InetAddress clienteEnderecoIP; //endereço IP do cliente
	private int PortaDestinoRTP = 5556; //porta de destino para os pacotes RTP oriundos do cliente

	//Variaveis referentes a musica
	private int numeroFrame = 0; //numero do frame transmitido atualmente
	private static final int FRAME_PERIOD = 20 - (int)(RTPStream.FREQUENCE_SLEEP / 1000.0 / 1000.0); //Frame periodico de um stream de audio, em milissegundos
	//private static final int TAMANHO_BUFFER_BYTES = 15000; //15KB

	private Timer timer; //temporizador usado para enviar os pacotes da musica
	//private byte[] buffer; //buffer usado para armazenar os pedacos da musica

	//variaveis RTSP
	//rtsp status
	private final static int INIT 		= 0; //nao iniciado
	private final static int READY 		= 1; //pronto para enviar
	private final static int PLAYING 	= 2; 
	private final static int SETUP 		= 3; //aloca recursos
	private final static int PLAY 		= 4; //envia a musica
	private final static int PAUSE 		= 5;
	private final static int TEARDOWN 	= 6; //libera recursos
	private final static int FINALIZE 	= 7;

	private int estado; //status do servidor RTSP
	private Socket RTSPsocket; //socket usado para envio e recebimento de mensagens RTSP

	private RTPStream rtpStream;

	//entrada e saida de filtros stream 
	private BufferedReader RTSPBufferedReader;
	private BufferedWriter RTSPBufferedWriter;
	private String nomeAudio; //arquivo de audio requisitado pelo cliente
	private int RTSP_ID = 123456; //ID de uma sessao RTSP, esse numero é gerado aleatoriamente ao iniciar a conexao
	private int RTSPNumeroSequencia = 0; //Sequencia de numeros da mensagem RTSP utilizadas em uma sessao

	private final static String CRLF = "\r\n"; //fim de linha com retorno

	/** Cria uma instancia para tratar da conexao com um cliente
	 * @param clienteSocket <code>Socket</code> com o soquete com a conexao com o cliente
	 * @param nomeServer <code>String</code> com o nome do servidor
	 */
	public ServidorRTSPRunnable(Socket clienteSocket, String nomeServer) {

		//this.clienteSocket = clienteSocket;
		this.RTSPsocket = clienteSocket;
		this.nomeServer   = nomeServer;
		this.RTSP_ID = this.geraIDSessaoRTSP();

		this.timer = new Timer(ServidorRTSPRunnable.FRAME_PERIOD, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				//Se o frame da musica atual é menor que o tamanho da musica
				if (numeroFrame < ServidorRTSPRunnable.this.rtpStream.getTostream().getSize()){
					//atualiza o numeroFrame corrente
					numeroFrame++;
					try {
						ServidorRTSPRunnable.this.rtpStream.run();
						//new Thread(ServidorRTSPRunnable.this.rtpStream).start();
					}
					catch(Exception e){
						LogSistema.mostraMensagem("Exception caught: " + e.getMessage());
						try {
							ServidorRTSPRunnable.this.RTSPsocket.close();
						}
						catch (IOException e1) {
							e1.printStackTrace();
						}
						ServidorRTSPRunnable.this.RTPsocket.close();
						ServidorRTSPRunnable.this.estado = ServidorRTSPRunnable.TEARDOWN;
						System.out.flush(); //libera os recursos da Thread
						//System.exit(0);
					}
				}
				else{
					// se chegamos ao final do arquivo de audio, parar o temporizador
					LogSistema.mostraMensagem("Terminou de Enviar o arquivo para o cliente " + clienteEnderecoIP);
					//envia mensagem para o cliente informando q o arquivo ja foi enviado
					//envia pacote vazio
					byte[] bytesPacoteVazio = new byte[0];
					DatagramSocket socketVazio;
					try {
						socketVazio = new DatagramSocket();
						DatagramPacket pacoteVazio = new DatagramPacket(bytesPacoteVazio, bytesPacoteVazio.length);
						pacoteVazio.setAddress(clienteEnderecoIP);
						pacoteVazio.setPort(PortaDestinoRTP);
						try {
							socketVazio.send(pacoteVazio);
						}
						catch (IOException e) {
							LogSistema.mostraMensagemErroServidor("Erro ao enviar pacote vazio");
							e.printStackTrace();
						}
					}
					catch (SocketException e1) {
						e1.printStackTrace();
					}
					timer.stop();
				}
			}
		});
		this.timer.setInitialDelay(0);
		this.timer.setCoalesce(true);

		//alocar memoria para o buffer de envio
		//this.buffer = new byte[TAMANHO_BUFFER_BYTES];
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		try {
			this.inicia();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/** Inicia o servidor
	 * @throws SocketException erro de soquete
	 * @throws IOException erro ao ler ou gravar dados para o cliente
	 */
	public void inicia() throws SocketException, IOException{

		//theServer.RTSPsocket = listenSocket.accept();
		//listenSocket.close();

		//obtem o IP do cliente
		this.clienteEnderecoIP = this.RTSPsocket.getInetAddress();

		//iniciando RTSPstate
		this.estado = ServidorRTSPRunnable.INIT;

		//Define filtros de fluxo de entrada e saida:
		this.RTSPBufferedReader = new BufferedReader(new InputStreamReader(this.RTSPsocket.getInputStream()) );
		this.RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.RTSPsocket.getOutputStream()) );

		//Aguarda a mensagem de configuração do cliente
		int tipoRequisicao;
		boolean conexaoFeita = false;

		while(!conexaoFeita){
			tipoRequisicao = this.analisaRequisicaoRTSP(); //bloqueio

			if (tipoRequisicao == ServidorRTSPRunnable.SETUP){
				conexaoFeita = true;

				//atualiza o estado RTSP
				this.estado = ServidorRTSPRunnable.READY;
				LogSistema.mostraMensagem("Novo status RTSP: READY");

				//envias uma resposta
				this.enviaRespostaRTSP();

				//inicialização do socket RTP
				//this.RTPsocket = new DatagramSocket(5555);
				this.RTPsocket = new DatagramSocket(this.RTSPsocket.getPort());
			}
		}

		//loop para lidar com as solicitações RTSP
		while(true){
			//analisa a requisição
			tipoRequisicao = this.analisaRequisicaoRTSP(); //bloqueio
			LogSistema.mostraMensagem("Conectado ao Servidor");

			if ((tipoRequisicao == PLAY) && (this.estado == ServidorRTSPRunnable.READY)){
				//envia uma resposta
				this.enviaRespostaRTSP();

				//inicia o timer
				this.timer.start();

				//atualiza o status
				this.estado = PLAYING;

				LogSistema.mostraMensagem("Novo status RTSP: PLAYING");
			}
			else if ((tipoRequisicao == ServidorRTSPRunnable.PAUSE) && (this.estado == ServidorRTSPRunnable.PLAYING)){
				//envia uma resposta
				this.enviaRespostaRTSP();
				//para o timer
				this.timer.stop();
				//atualiza o status
				this.estado = ServidorRTSPRunnable.READY;
				LogSistema.mostraMensagem("Novo status RTSP: READY");
			}
			else if (tipoRequisicao == ServidorRTSPRunnable.TEARDOWN){
				//envia uma resposta
				this.enviaRespostaRTSP();
				this.finalizaConexao();
				break;
				//System.exit(0);
			}
			else if(tipoRequisicao == ServidorRTSPRunnable.FINALIZE){
				this.finalizaConexao();
				break;
			}
			else{ //requisicao desconhecida
				this.finalizaConexao();
				break;
			}
		}//while
	}//main

	/** Analisa a requisição RTSP
	 * @return <code>int</code> com o tipo da requisicao do cliente 
	 */
	private int analisaRequisicaoRTSP(){ //requisicao como SETUP usuario RTSP/1.0 
		int tipoRequisicao = -1;
		try{
			//analisa a linha de requisição e extrai o tipo de requisicao
			String linhaRequisicao = this.RTSPBufferedReader.readLine();

			if(linhaRequisicao == null){
				return FINALIZE;
			}
			//LogSistema.mostraMensagem("RTSP Server - Received from Client:");
			LogSistema.mostraMensagem(linhaRequisicao);
			StringTokenizer tokens = new StringTokenizer(linhaRequisicao);
			String stringTipoRequisicao = tokens.nextToken();

			//atribui ao tipoRequisicao o int referente a requisicao
			if ((stringTipoRequisicao).compareTo("SETUP") == 0){
				tipoRequisicao = ServidorRTSPRunnable.SETUP;
			}
			else if ((stringTipoRequisicao).compareTo("PLAY") == 0){
				tipoRequisicao = ServidorRTSPRunnable.PLAY;
			}
			else if ((stringTipoRequisicao).compareTo("PAUSE") == 0){
				tipoRequisicao = ServidorRTSPRunnable.PAUSE;
			}
			else if ((stringTipoRequisicao).compareTo("TEARDOWN") == 0){
				tipoRequisicao = ServidorRTSPRunnable.TEARDOWN;
			}
			else{ //requisicao desconhecida
				return FINALIZE;
			}

			//pega o usuario
			//String usuario = tokens.nextToken("RTSP/");
			String usuario = tokens.nextToken("\n");
			usuario = usuario.substring(0, usuario.length() - 8);
			String musica = usuario.substring(usuario.indexOf("|") + 1, usuario.length() - 1);
			usuario = (usuario.substring(0, usuario.indexOf("|"))).trim();

			this.nomeAudio = ENDERECO_MUSICA_USUARIOS + usuario + "/" + musica;
			
			LogSistema.mostraMensagem("Usuario: " + usuario);
			LogSistema.mostraMensagem("Musica: " + this.nomeAudio);

			/*if (tipoRequisicao == ServidorRTSPRunnable.SETUP){
				//estrai o nomeAudio para uma uma linha de requisição
				//this.nomeAudio = tokens.nextToken();
				//this.rtpStream = new RTPStream(this.nomeAudio, this.clienteEnderecoIP, this.PortaDestinoRTP);
				String linhaNomeMusica = this.RTSPBufferedReader.readLine();
				LogSistema.mostraMensagem(linhaNomeMusica);
				this.nomeAudio = ENDERECO_MUSICA_USUARIOS + usuario + "/" + linhaNomeMusica;
				this.rtpStream = new RTPStream(this.nomeAudio, this.clienteEnderecoIP, this.PortaDestinoRTP);
			}*/
			
			if (tipoRequisicao == ServidorRTSPRunnable.SETUP){
				System.out.println(this.nomeAudio);
				this.rtpStream = new RTPStream(this.nomeAudio, this.clienteEnderecoIP, this.PortaDestinoRTP);
			}

			//analisa a SeqNumLine e extrai o CSeq field
			String linhaNumeroSequencia = this.RTSPBufferedReader.readLine();
			LogSistema.mostraMensagem(linhaNumeroSequencia);
			tokens = new StringTokenizer(linhaNumeroSequencia);
			tokens.nextToken();
			String numeroSequencia = tokens.nextToken();
			this.RTSPNumeroSequencia = Integer.parseInt(numeroSequencia);

			//pega a linha com o protocolo e porta do cliente ou a linha com a sessao
			String linhaInfoTransferencia = this.RTSPBufferedReader.readLine();
			LogSistema.mostraMensagem(linhaInfoTransferencia);

			if (tipoRequisicao == ServidorRTSPRunnable.SETUP){
				//extrai o RTP_dest_port de LastLine
				tokens = new StringTokenizer(linhaInfoTransferencia);
				for (int i = 0; i < 3; i++){
					tokens.nextToken(); //ignora coisas não utilizadas
				}
				this.PortaDestinoRTP = Integer.parseInt(tokens.nextToken());
			}
			else if(tipoRequisicao == ServidorRTSPRunnable.TEARDOWN){ //stop
				LogSistema.mostraMensagem("Cliente " + clienteEnderecoIP + " solicitou STOP, parar de enviar musica");
			}
			//else LastLine will be the SessionId line ... do not check for now.
		}
		catch(SocketException e){
			if(e.getMessage().equalsIgnoreCase("Connection reset") ){
				LogSistema.mostraMensagem("Erro: " + e.getMessage());
				LogSistema.mostraMensagem("Conexao com o cliente será fechada");
				tipoRequisicao = FINALIZE;
			}
			else{
				e.printStackTrace();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			//System.exit(0);
		}
		LogSistema.mostraMensagem(""); //linha em branco
		return(tipoRequisicao);
	}

	/**
	 * Envia uma resposta RTSP ao cliente
	 */
	private void enviaRespostaRTSP(){
		if(this.RTSPsocket.isConnected()){
			try{
				this.RTSPBufferedWriter.write("RTSP/1.0 200 OK" + CRLF);
				this.RTSPBufferedWriter.write("CSeq: " + RTSPNumeroSequencia + CRLF);
				this.RTSPBufferedWriter.write("Sessao: " + RTSP_ID + CRLF);
				this.RTSPBufferedWriter.flush();
				LogSistema.mostraMensagem("Servidor RTSP - Enviou resposta ao Cliente " + clienteEnderecoIP);
			}
			catch(Exception e){
				if(e.getMessage().equalsIgnoreCase("Connection reset by peer: socket write error")){
					LogSistema.mostraMensagem("Cliente " + clienteEnderecoIP + " desconectou: " + e.getMessage());
					this.finalizaConexao();
				}
				else{
					e.printStackTrace();
				}
				//System.exit(0);
			}
		}
	}

	/** Finaliza a conexao com o cliente
	 * 
	 */
	private void finalizaConexao(){
		LogSistema.mostraMensagem(this.clienteEnderecoIP + " desconectado do servidor");
		//"para" o timer
		this.timer.stop();
		//fecha o socket
		//this.RTSPsocket.close();
		this.RTPsocket.close();
		this.estado = ServidorRTSPRunnable.FINALIZE;
		System.out.flush(); //libera os recursos da Thread
	}

	/** Gera um id randomico para a sessao com o cliente. É gerado um numero de 1000000 a 1999999
	 * @return <code>int</code> com o id da sessao
	 */
	private int geraIDSessaoRTSP(){
		int numeroAleatorio = 1000000;
		Random randomGenerator = new Random();
		numeroAleatorio += randomGenerator.nextInt(1000000);
		return numeroAleatorio;
	}

}
