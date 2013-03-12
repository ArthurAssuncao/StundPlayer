package com.arthurassuncao.stundplayer.classes;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

//http://www.javazoom.net/mp3spi/documents.html
/** Classe para manipular os dados de arquivos MP3
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * 
 * @see AudioFileFormat
 * @see TAudioFileFormat
 * @see AudioSystem
 */
public class MP3Dados {

	/** <code>String</code> representando a duracao*/
	public static String DURACAO = "duration"; //retorna [Long], duration in microseconds.
	/** <code>String</code> representando o titulo*/
	public static String TITULO = "title"; //retorna [String], Title of the stream.
	/** <code>String</code> representando o autor*/
	public static String AUTOR = "author"; //retorna [String], Name of the artist of the stream.
	/** <code>String</code> representando o album*/
	public static String ALBUM = "album"; //retorna [String], Name of the album of the stream.
	/** <code>String</code> representando a data*/
	public static String DATA = "date"; //retorna [String], The date (year) of the recording or release of the stream.
	/** <code>String</code> representando o tamanho*/
	public static String TAMANHO = "length"; //retorna [String], The length (bytes) of the stream.
	//public static String COPYRIGHT = "copyright"; //retorna [String], Copyright message of the stream.
	//public static String COMENTARIOS = "comment"; //retorna [String], Comment of the stream. 
	
	/** Retorna a duracao em segundos de um arquivo mp3
	 * @param enderecoMusica <code>String</code> com o endereco do arquivo mp3
	 * @return <code>int</code> com a duracao em segundos do audio do arquivo
	 * @throws IOException caso ocorra um erro ao ler o arquivo
	 * @throws UnsupportedAudioFileException caso a extensao do arquivo nao seja suportada
	 */
	public static int getDuracaoComMp3Spi(String enderecoMusica) throws IOException, UnsupportedAudioFileException{
		File arquivo = new File(enderecoMusica);
		int tempoMilissegundos = 0;
		int segundos = 0;
		if(arquivo.exists()){
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(arquivo);
			Map<?, ?> propriedades = ((TAudioFileFormat) fileFormat).properties();
			String key = MP3Dados.DURACAO;
			Long microsegundos = (Long) propriedades.get(key);
			tempoMilissegundos = (int) (microsegundos / 1000);
		}
		//segundos = (tempoMilissegundos / 1000) % 60;
		segundos = tempoMilissegundos / 1000;
		return segundos;
	}

	/** Retorna uma informacao de um arquivo mp3
	 * @param enderecoMusica <code>String</code> com o endereco do arquivo mp3
	 * @param tipoInformacao <code>String</code> com o tipo da informacao
	 * @return <code>String</code> com a duracao, caso nao encontre retorna vazio
	 * @throws IOException caso ocorra um erro ao ler o arquivo
	 * @throws UnsupportedAudioFileException caso a extensao do arquivo nao seja suportada
	 */
	public static String getInformacaoMusicaComMp3Spi(String enderecoMusica, String tipoInformacao) throws IOException, UnsupportedAudioFileException{
		String informacao = null;
		File arquivo = new File(enderecoMusica);
		if(arquivo.exists()){
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(arquivo);
			Map<?, ?> propriedades = ((TAudioFileFormat) fileFormat).properties();
			if(tipoInformacao.equalsIgnoreCase(MP3Dados.TITULO)){
				informacao = (String)propriedades.get(MP3Dados.TITULO);
			}
			else if(tipoInformacao.equalsIgnoreCase(MP3Dados.AUTOR)){
				informacao = (String)propriedades.get(MP3Dados.AUTOR);
			}
			else if(tipoInformacao.equalsIgnoreCase(MP3Dados.ALBUM)){
				informacao = (String)propriedades.get(MP3Dados.ALBUM);
			}
			else if(tipoInformacao.equalsIgnoreCase(MP3Dados.DATA)){
				informacao = (String)propriedades.get(MP3Dados.DATA);
			}
			else if(tipoInformacao.equalsIgnoreCase(MP3Dados.TAMANHO)){
				informacao = String.valueOf(MP3Dados.getLengthMusica(enderecoMusica));
			}
			/*else if(tipoInformacao.equalsIgnoreCase(COPYRIGHT)){
				informacao = (String)propriedades.get(MP3Dados.COPYRIGHT);
			}
			else if(tipoInformacao.equalsIgnoreCase(COMENTARIOS)){
				informacao = (String)propriedades.get(MP3Dados.COMENTARIOS);
			}*/
		}
		if(informacao == null){
			informacao = "";
		}
		return informacao;
	}
	
	/** Retorna o tamanho em bytes do arquivo de audio mp3
	 * @param enderecoMusica <code>String</code> com o endereco do arquivo mp3
	 * @return <code>long</code> com o tamanho em bytes do arquivo mp3
	 * @throws IOException caso nao consiga acessar o arquivo
	 */
	public static long getLengthMusica(String enderecoMusica) throws IOException{
		File arquivoMusica = new File(enderecoMusica) ;
		long tamanhoMusicaBytes = 0;
		if(arquivoMusica.exists()){
			tamanhoMusicaBytes = arquivoMusica.length();
		}
		arquivoMusica = null;
		return tamanhoMusicaBytes;
	}

	/** Metodo main para testar a classe
	 * @param args <code>String[]</code> com argumentos de linha de comando
	 */
	public static void main(String[] args){
		try {
			String musica = "usuarios/arthur/The Who - The Seeker.mp3";
			int duracaoMusica = MP3Dados.getDuracaoComMp3Spi(musica);
			System.out.println("Duracao musica: " + (duracaoMusica / 60) + ":" +( duracaoMusica % 60));
			System.out.println("Titulo musica: " + MP3Dados.getInformacaoMusicaComMp3Spi(musica, MP3Dados.TITULO));
			System.out.println("Autor musica: " + MP3Dados.getInformacaoMusicaComMp3Spi(musica, MP3Dados.AUTOR));
			System.out.println("Album musica: " + MP3Dados.getInformacaoMusicaComMp3Spi(musica, MP3Dados.ALBUM));
			System.out.println("Data musica: " + MP3Dados.getInformacaoMusicaComMp3Spi(musica, MP3Dados.DATA));
			System.out.println("Tamanho em bytes musica: " + MP3Dados.getLengthMusica(musica));
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

}
