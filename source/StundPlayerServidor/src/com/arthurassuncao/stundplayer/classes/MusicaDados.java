package com.arthurassuncao.stundplayer.classes;

import java.io.Serializable;

/** Classe para manipular musicas
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see Serializable
 */
public class MusicaDados implements Serializable{

	private static final long serialVersionUID = 1471510478478835688L;
	private String nomeMusica;
	private int duracao;
	private String titulo;
	private String autor; 
	private String album;
	private String data;
	private long tamanhoBytes;

	/** Cria uma instancia com nome, duracao, titulo, autor, album, data e tamanho especificos
	 * @param nomeMusica <code>String</code> com o nome do arquivo
	 * @param duracao <code>int</code> com a duracao em segundos
	 * @param titulo <code>String</code> com o titulo da musica
	 * @param autor <code>String</code> com o autor da musica
	 * @param album <code>String</code> com o album da musica
	 * @param data <code>String</code> com a data da musica
	 * @param tamanhoBytes <code>long</code> com o tamanho em bytes da musica
	 *
	 */
	public MusicaDados(String nomeMusica, int duracao, String titulo, String autor, String album, String data, long tamanhoBytes){
		this.nomeMusica = nomeMusica;
		this.album = album;
		this.autor = autor;
		this.data = data;
		this.duracao = duracao;
		this.titulo = titulo;
		this.tamanhoBytes = tamanhoBytes;
	}

	/** Retorna o nome do arquivo da musica
	 * @return <code>String</code> com o nome do arquivo
	 */
	public String getNomeMusica() {
		return nomeMusica;
	}

	/** Retorna a duracao da musica em segundos
	 * @return <code>int</code> com a duracao em segundos
	 */
	public int getDuracao() {
		return duracao;
	}

	/** Retorna o titulo da musica
	 * @return <code>String</code> com o titulo da musica
	 */
	public String getTitulo() {
		return titulo;
	}

	/** Retorna o autor da musica
	 * @return <code>String</code> com o autor da musica
	 */
	public String getAutor() {
		return autor;
	}

	/** Retorna o album da musica
	 * @return <code>String</code> com o album da musica
	 */
	public String getAlbum() {
		return album;
	}

	/** Retorna a data da musica
	 * @return <code>String</code> com a data da musica
	 */
	public String getData() {
		return data;
	}
	
	/** Retorna o tamanho em bytes da musica
	 * @return <code>long</code> com o tamanho em bytes da musica
	 */
	public long getTamanhoBytes() {
		return tamanhoBytes;
	}
	
	/** Retorna o tamanho em Kilobytes da musica
	 * @return <code>double</code> com o tamanho em Kilobytes da musica
	 */
	public double getTamanhoKiloBytes() {
		return tamanhoBytes / 1024.0;
	}
	
	/** Retorna o tamanho em Megabytes da musica
	 * @return <code>double</code> com o tamanho em Megabytes da musica
	 */
	public double getTamanhoMegaBytes() {
		return tamanhoBytes / 1024.0 / 1024.0;
	}

	/** Retorna a duracao da musica no formato MM:SS
	 * @return <code>String</code> com a duracao da musica
	 */
	public String getDuracaoMMSS(){
		int minutos = this.duracao / 60;
		int segundos = this.duracao % 60;
		return (minutos < 10 ? "0" + minutos : minutos) + ":" + (segundos < 10 ? "0" + segundos : segundos);
	}

	/* Retorna um <code>String</code> com o nome do autor - titulo da musica
	 * @return <code>String</code> com o nome do autor - titulo da musica
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String texto = null;
		if(!this.titulo.isEmpty()){
			if(this.autor.isEmpty()){
				texto = this.titulo;
			}
			else{
				texto = this.autor + " - " + this.titulo;
			}
		}
		else{
			texto = this.nomeMusica.substring(0, this.nomeMusica.lastIndexOf("."));
		}
		return texto;
	}
	
	/** Verifica se o texto passado contem no nome do autor, no titulo, no album ou no nome do arquivo
	 * @param texto <code>String</code> com o texto que sera verificado
	 * @return <code>boolean</code> com <code>true</code> se o texto aparece em um desses campos e <code>false</code> senao
	 */
	public boolean contains(String texto){
		boolean contem = false;
		
		if(this.autor.contains(texto) || 
				this.titulo.contains(texto) || 
				this.album.contains(texto) || 
				this.nomeMusica.substring(0, this.nomeMusica.lastIndexOf(".")).contains(texto) 
			){
			contem = true;
		}
		
		return contem;
	}

}
