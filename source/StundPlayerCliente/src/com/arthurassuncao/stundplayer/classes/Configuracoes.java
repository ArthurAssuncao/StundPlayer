package com.arthurassuncao.stundplayer.classes;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.arthurassuncao.stundplayer.gui.Janela;
import com.arthurassuncao.stundplayer.gui.temas.Temas;

/** Classe para manipular as configuracoes do sistema
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 */
public class Configuracoes {
	/*private ClienteFTP clienteFTP;
	private ClienteRTSP cliente;*/
	
	private int portaFTP;
	private int portaServidor;
	private int portaRTP;
	private int portaRTSP;
	
	private String ipServidor;
	
	private Color corPlayer;
	private Color corFundoPlayer;
	
	/** <code>String</code> com o ip padrao do servidor*/
	public static final String IP_SERVIDOR_PADRAO = "127.0.0.1";
	/** <code>int</code> com a porta padrao do servidor*/
	public static final int PORTA_SERVIDOR_PADRAO = 5679;
	/** <code>int</code> com a porta FTP padrao do servidor*/
	public static final int PORTA_FTP_PADRAO = 5678;
	/** <code>int</code> com a porta RTP padrao do servidor*/
	public static final int PORTA_RTP_PADRAO = 5556;
	/** <code>int</code> com a porta RTSP padrao do servidor*/
	public static final int PORTA_RTSP_PADRAO = 9000;
	/** <code>Color</code> com a cor padrao do sistema*/
	public static final Color COR_PLAYER_PADRAO = new Color(249, 127, 16);
	/** <code>Color</code> com a cor de fundo padrao do sistema*/
	public static final Color COR_FUNDO_PLAYER_PADRAO = new Color(40, 40, 40);
	
	private static Configuracoes CONFIGURACOES = null;
	
	private Configuracoes(){
		this.setDefaultValores();
	}
	
	/** Retorna um objeto <code>Configuracoes</code>
	 * @return <code>Configuracoes</code> com as configuracoes do sistema
	 */
	public static Configuracoes getInstance(){
		if(CONFIGURACOES == null){
			CONFIGURACOES = new Configuracoes();
			CONFIGURACOES.leConfiguracoesArquivo();
		}
		return CONFIGURACOES;
	}
	
	/** Grava as configuracoes do sistema num arquivo em disco
	 * @return <code>boolean</code> com <code>true</code> se não houve erros ao gravar e <code>false</code> se houve erros.
	 */
	public boolean escreveConfiguracoesArquivo(){
		File config = new File("config.conf");
		if(!config.exists()){
			try {
				config.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileOutputStream file = new FileOutputStream(config);
			file.write(ipServidor.getBytes());
			file.write("\n".getBytes());
			
			String ftp = Integer.toString(portaFTP);
			file.write(ftp.getBytes());
			file.write("\n".getBytes());
			
			String ftp2 = Integer.toString(portaServidor);
			file.write(ftp2.getBytes());
			file.write("\n".getBytes());
			
			String rtp = Integer.toString(portaRTP);
			file.write(rtp.getBytes());
			file.write("\n".getBytes());
			
			String rtsp = Integer.toString(portaRTSP);
			file.write(rtsp.getBytes());
			file.write("\n".getBytes());
			
			String corPlayer = this.corPlayer.getRed() + " " + this.corPlayer.getGreen() + " " + this.corPlayer.getBlue();
			file.write(corPlayer.getBytes());
			file.write("\n".getBytes());
			
			String corFundoPlayer = this.corFundoPlayer.getRed() + " " + this.corFundoPlayer.getGreen() + " " + this.corFundoPlayer.getBlue();
			file.write(corFundoPlayer.getBytes());
			
			file.close();
			
		}
		catch (IOException e){
			System.out.println("Erro ao gravar o arquivo de configuração");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/** Le as configuracoes do arquivo de configuracoes
	 * @return <code>boolean</code> com <code>true</code> se não houve erros ao ler o arquivo e <code>false</code> se houve erros.
	 */
	public boolean leConfiguracoesArquivo(){
		File config = new File("config.conf");
		if(!config.exists()){
			try {
				config.createNewFile();
				criaArquivoConfiguracao();
				return true;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileReader fileReader = new FileReader(config);
			BufferedReader file = new BufferedReader(fileReader);
			
			String linha = null;
			int op = 1;
	        while( (linha = file.readLine()) != null ) {
	        	System.out.print( linha + "\n");
	        	switch(op){
	        	case 1:
	        		this.setIpServidor(linha);
	        		break;
	        	
	        	case 2:
	        		this.setPortaFTP(Integer.parseInt(linha));
	        		break;
	        	
	        	case 3:
	        		this.setPortaServidor(Integer.parseInt(linha));
	        		break;
	        	
	        	case 4:
	        		this.setPortaRTP(Integer.parseInt(linha));
	        		break;
	        		
	        	case 5:
	        		this.setPortaRTSP(Integer.parseInt(linha));
	        		break;
	        		
	        	case 6: //pega os valores da cor do programa
	        		StringTokenizer cores = new StringTokenizer(linha);
	        		int red = -1;
	        		int green = -1;
	        		int blue = -1;
	        		
	        		try{
	        			red = Integer.parseInt(cores.nextToken(" "));
	        		}
	        		catch(NumberFormatException e){
	        			e.printStackTrace();
	        		}
	        		
	        		try{
	        			green = Integer.parseInt(cores.nextToken(" "));
	        		}
	        		catch(NumberFormatException e){
	        			e.printStackTrace();
	        		}
	        		
	        		try{
	        			blue = Integer.parseInt(cores.nextToken(" "));
	        		}
	        		catch(NumberFormatException e){
	        			e.printStackTrace();
	        		}
	        		if(red == -1){
	        			red = 249;
	        		}
	        		if(green == -1){
	        			green = 127;
	        		}
	        		if(blue == -1){
	        			blue = 16;
	        		}
	        		
	        		this.setCorPlayer(new Color(red, green, blue));
	        		break;
	        		
	        	case 7: //pega os valores da cor do programa
	        		StringTokenizer coresFundo = new StringTokenizer(linha);
	        		int redFundo = -1;
	        		int greenFundo = -1;
	        		int blueFundo = -1;
	        		
	        		try{
	        			redFundo = Integer.parseInt(coresFundo.nextToken(" "));
	        		}
	        		catch(NumberFormatException e){
	        			e.printStackTrace();
	        		}
	        		
	        		try{
	        			greenFundo = Integer.parseInt(coresFundo.nextToken(" "));
	        		}
	        		catch(NumberFormatException e){
	        			e.printStackTrace();
	        		}
	        		
	        		try{
	        			blueFundo = Integer.parseInt(coresFundo.nextToken(" "));
	        		}
	        		catch(NumberFormatException e){
	        			e.printStackTrace();
	        		}
	        		if(redFundo == -1){
	        			redFundo = 249;
	        		}
	        		if(greenFundo == -1){
	        			greenFundo = 127;
	        		}
	        		if(blueFundo == -1){
	        			blueFundo = 16;
	        		}
	        		
	        		this.setCorFundoPlayer(new Color(redFundo, greenFundo, blueFundo));
	        		break;	
	        		
	        	}//switch
	        	op++;
	        }//while
			
			file.close();
			fileReader.close();
			
		}
		catch (IOException e) {
			System.out.println("Erro na leitura do arquivo");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 */
	public void criaArquivoConfiguracao(){
		portaFTP = PORTA_FTP_PADRAO;
		portaServidor = PORTA_SERVIDOR_PADRAO;
		portaRTP = PORTA_RTP_PADRAO;
		portaRTSP = PORTA_RTSP_PADRAO;
		ipServidor = IP_SERVIDOR_PADRAO;
		corPlayer = COR_PLAYER_PADRAO;
		corFundoPlayer = COR_FUNDO_PLAYER_PADRAO;
		escreveConfiguracoesArquivo();
	}
	
	/** Seta os valores padroes
	 * 
	 */
	public void setDefaultValores(){
		this.ipServidor = IP_SERVIDOR_PADRAO;
		this.portaFTP = PORTA_FTP_PADRAO;
		this.portaServidor = PORTA_SERVIDOR_PADRAO;
		this.portaRTP = PORTA_RTP_PADRAO;
		this.portaRTSP = PORTA_RTSP_PADRAO;
		
		this.corPlayer = COR_PLAYER_PADRAO;
		this.corFundoPlayer = COR_FUNDO_PLAYER_PADRAO;
	}

	/** Retorna a porta FTP do servidor
	 * @return <code>int</code> com a porta FTP
	 */
	public int getPortaFTP() {
		return portaFTP;
	}

	/** Seta o valor da porta FTP do servidor
	 * @param portaFTP <code>int</code> com a porta FTP do servidor
	 */
	public void setPortaFTP(int portaFTP) {
		this.portaFTP = portaFTP;
	}

	/** Retorna a porta do servidor
	 * @return <code>int</code> com a porta
	 */
	public int getPortaServidor() {
		return portaServidor;
	}

	/** Seta o valor da porta do servidor
	 * @param portaServidor <code>int</code> com a porta do servidor
	 */
	public void setPortaServidor(int portaServidor) {
		this.portaServidor = portaServidor;
	}

	/** Retorna a porta RTSP do servidor
	 * @return <code>int</code> com a porta RTSP
	 */
	public int getPortaRTSP() {
		return portaRTSP;
	}

	/** Seta o valor da porta RTSP do servidor
	 * @param portaRTSP <code>int</code> com a porta RTSP do servidor
	 */
	public void setPortaRTSP(int portaRTSP) {
		this.portaRTSP = portaRTSP;
	}

	/** Retorna a porta RTP do servidor
	 * @return <code>int</code> com a porta RTP
	 */
	public int getPortaRTP() {
		return portaRTP;
	}

	/** Seta o valor da porta RTP do servidor
	 * @param portaRTP <code>int</code> com a porta RTP do servidor
	 */
	public void setPortaRTP(int portaRTP) {
		this.portaRTP = portaRTP;
	}

	/** Retorna a o endereco IP do servidor
	 * @return <code>String</code> com o IP
	 */
	public String getIpServidor() {
		return ipServidor;
	}

	/** Seta o endereco IP do servidor
	 * @param ipServidor <code>int</code> com o IP do servidor
	 */
	public void setIpServidor(String ipServidor) {
		this.ipServidor = ipServidor;
	}

	/** Retorna a cor do sistema
	 * @return <code>Color</code> com a cor do sistema
	 */
	public Color getCorPlayer() {
		return corPlayer;
	}

	/** Seta a cor do sistema
	 * @param corPlayer <code>Color</code> com a cor do sistema
	 */
	public void setCorPlayer(Color corPlayer) {
		this.corPlayer = corPlayer;
		Janela.setCorPadraoPlayer(corPlayer);
	}
	
	/** Retorna a cor de fundo do sistema
	 * @return <code>Color</code> com a cor de fundo do sistema
	 */
	public Color getCorFundoPlayer() {
		return corFundoPlayer;
	}

	/** Seta a cor de fundo do sistema
	 * @param corFundoPlayer <code>Color</code> com a cor de fundo do sistema
	 */
	public void setCorFundoPlayer(Color corFundoPlayer) {
		this.corFundoPlayer = corFundoPlayer;
		Janela.setCorFundoPadraoPlayer(corFundoPlayer);
		Temas.setCoresBase(); //colocar isso aki na ultima configuracao lida do arquivo e usada no metodo setCoresBase
	}
}
