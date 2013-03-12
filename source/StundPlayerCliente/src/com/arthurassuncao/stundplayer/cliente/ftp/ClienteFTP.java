package com.arthurassuncao.stundplayer.cliente.ftp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.arthurassuncao.stundplayer.classes.Configuracoes;
import com.arthurassuncao.stundplayer.classes.MP3Dados;
import com.arthurassuncao.stundplayer.classes.MusicaDados;
import com.arthurassuncao.stundplayer.gui.JanelaMensagem;
import com.arthurassuncao.stundplayer.gui.player.JanelaPlayer;

/** Classe para manipular o cliente FTP
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see Runnable
 * @see JanelaPlayer
 */
public class ClienteFTP implements Runnable{
	//private static String ipServidor;
	//private static int portaServidor;
	private JanelaPlayer janela;
	private String usuario;
	private String arquivo;
	@SuppressWarnings("unused")
	private boolean enviou;

	/** Cria uma instancia do cliente FTP com a janela do player, usuario e arquivo especificos
	 * @param janela <code>JanelaPlayer</code> com a janela do player
	 * @param usuario <code>String</code> com o usuario
	 * @param arquivo <code>String</code> com o arquivo que será enviado para o servidor
	 * @see JanelaPlayer
	 */
	protected ClienteFTP(JanelaPlayer janela, String usuario, String arquivo){
		//setIpServidor(Configuracoes.getInstance().getIpServidor());
		//setPortaServidor(Configuracoes.getInstance().getPortaFTP());
		this.janela = janela;
		this.usuario = usuario;
		this.arquivo = arquivo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.enviou = enviar();
	}

	/** Envia o arquivo via FTP para o servidor
	 * @return <code>boolean</code> com <code>true</code> se o arquivo foi enviado e <code>false</code> senao
	 */
	public boolean enviar() {
		janela.getBarraProgressoUploading().setVisible(true);
		String enderecoArquivo = this.arquivo;
		boolean enviou = false;
		String ipServidor = Configuracoes.getInstance().getIpServidor();
		int portaServidor = Configuracoes.getInstance().getPortaFTP();
		try{
			File arquivo = new File(enderecoArquivo);

			FileInputStream in = new FileInputStream(arquivo);

			//ponto de conexão com o servidor
			Socket socket = new Socket(ipServidor, portaServidor);
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out);
			BufferedWriter writer = new BufferedWriter(osw);
			if(this.usuario != null){
				writer.write(usuario + "\n");
			}
			else{
				writer.write("\n");
			}
			writer.write(arquivo.getName() + "\n"); 
			writer.flush();  
			int tamanho = 4096; // buffer de 4KB    
			byte[] buffer = new byte[tamanho];    
			int lidos = -1;    
			while ((lidos = in.read(buffer, 0, tamanho)) != -1) {
				out.write(buffer, 0, lidos);
			}
			socket.close();
			in.close();
			enviou = true;
		}
		catch(IOException e){
			enviou = false;
		}

		String nomeArquivoParaUploadString = arquivo.substring(arquivo.lastIndexOf(File.separatorChar) + 1, arquivo.length() );
		janela.getBarraProgressoUploading().setVisible(false); //acabou o upload
		if(enviou){
			//JanelaMensagem.mostraMensagem(null, "Enviar Arquivo", "Arquivo " + nomeArquivoParaUploadString + " foi enviado com sucesso");
			//System.out.println("Classe ClienteFTP: Falta atualizar a tabela"); //nao falta mais nao
			//vai ter q pedir os dados da musica pro servidor
			MusicaDados musicaDados = null;
			String album = null;
			String autor = null;
			String data = null;
			int duracao = 0;
			String titulo = null;
			long tamanhoBytes = 0;
			try {
				album = MP3Dados.getInformacaoMusicaComMp3Spi(enderecoArquivo, MP3Dados.ALBUM);
				autor = MP3Dados.getInformacaoMusicaComMp3Spi(enderecoArquivo, MP3Dados.AUTOR);
				data = MP3Dados.getInformacaoMusicaComMp3Spi(enderecoArquivo, MP3Dados.DATA);
				duracao = MP3Dados.getDuracaoComMp3Spi(enderecoArquivo);
				titulo = MP3Dados.getInformacaoMusicaComMp3Spi(enderecoArquivo, MP3Dados.TITULO);
				tamanhoBytes = MP3Dados.getLengthMusica(enderecoArquivo);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
			musicaDados = new MusicaDados(nomeArquivoParaUploadString, duracao, titulo, autor, album, data, tamanhoBytes);
			this.janela.addLinhaTabelaPlaylist(musicaDados);
		}
		else{
			JanelaMensagem.mostraMensagemErro(null, "Erro ao enviar arquivo " + nomeArquivoParaUploadString);
		}

		return enviou;
	}//enviar
}//Cliente

