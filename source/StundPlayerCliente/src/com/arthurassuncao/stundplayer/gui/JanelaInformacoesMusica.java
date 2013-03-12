package com.arthurassuncao.stundplayer.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

import com.arthurassuncao.stundplayer.classes.MusicaDados;

/** Janela para exibir as informacoes de uma musica
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see Janela
 */
public class JanelaInformacoesMusica extends Janela {

	private static final long serialVersionUID = -1511776193289994262L;
	/** <code>int</code> com a largura padrao da janela */
	private static final int LARGURA = 450;
	/** <code>int</code> com a altura padrao da janela */
	private static final int ALTURA 	= 300;

	private MusicaDados musica;

	private Painel painelTotal;

	private LabelRotulo labelTitulo;
	private LabelRotulo labelArtista;
	private LabelRotulo labelAlbum;
	private LabelRotulo labelAno;
	private LabelRotulo labelDuracao;
	private LabelRotulo labelTamanho;

	private LabelRotulo labelTituloMusica;
	private LabelRotulo labelArtistaMusica;
	private LabelRotulo labelAlbumMusica;
	private LabelRotulo labelAnoMusica;
	private LabelRotulo labelDuracaoMusica;
	private LabelRotulo labelTamanhoMusica;

	/** Cria uma instancia da janela com os dados da musica
	 * @param musica <code>MusicaDados</code> com os dados da musica
	 * @see MusicaDados
	 */
	public JanelaInformacoesMusica(MusicaDados musica){
		super(musica.getTitulo(), LARGURA, ALTURA);

		this.musica = musica;

		this.iniciaElementos();

		this.addElementos();
		
		this.pack();
		
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	/** Inicializa os elementos da janela
	 * 
	 */
	public void iniciaElementos(){
		this.painelTotal = new Painel(new GridBagLayout());

		this.labelTitulo = new LabelRotulo("Titulo: ");
		this.labelArtista = new LabelRotulo("Artista: ");
		this.labelAlbum = new LabelRotulo("Album: ");
		this.labelAno = new LabelRotulo("Ano: ");
		this.labelDuracao = new LabelRotulo("Duracao: ");
		this.labelTamanho = new LabelRotulo("Tamanho: ");

		this.labelTituloMusica = new LabelRotulo(musica.getTitulo());
		this.labelArtistaMusica = new LabelRotulo(musica.getAutor());
		this.labelAlbumMusica = new LabelRotulo(musica.getAlbum());
		this.labelAnoMusica = new LabelRotulo(musica.getData());
		this.labelDuracaoMusica = new LabelRotulo(musica.getDuracaoMMSS());
		DecimalFormat formatoDecimal = new DecimalFormat("0.##");
		this.labelTamanhoMusica = new LabelRotulo(formatoDecimal.format(musica.getTamanhoMegaBytes()).replace(',', '.') + " MB" );
	}

	/** Adiciona os elementos a janela
	 * 
	 */
	public void addElementos(){
		GridBagConstraints grid = new GridBagConstraints();
		int linha = 0; 
		
		grid.gridx = 0;
		grid.gridy = 0;
		grid.anchor= GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelTitulo, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelArtista, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelAlbum, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelAno, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelTitulo, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelDuracao, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelTamanho, grid);

		linha = 0;
		grid.gridx = 1;
		grid.gridy = linha++;
		this.painelTotal.add(this.labelTituloMusica, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelArtistaMusica, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelAlbumMusica, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelAnoMusica, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelTituloMusica, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelDuracaoMusica, grid);
		grid.gridy = linha++;
		this.painelTotal.add(this.labelTamanhoMusica, grid);
		
		this.add(painelTotal);
	}

	/* (non-Javadoc)
	 * @see br.java.redes.gui.Janela#addItensPopupMenu()
	 */
	@Override
	protected void addItensPopupMenu() {
		//nenhum item a ser adicionado
	}

	/* (non-Javadoc)
	 * @see br.java.redes.gui.Janela#addEventoItens()
	 */
	@Override
	protected void addEventoItens() {
		//nenhum evento a ser adicionado
	}

}
