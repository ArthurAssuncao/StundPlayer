package com.arthurassuncao.stundplayer.gui.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.arthurassuncao.stundplayer.classes.Configuracoes;
import com.arthurassuncao.stundplayer.classes.MusicaDados;
import com.arthurassuncao.stundplayer.classes.Usuario;
import com.arthurassuncao.stundplayer.cliente.ftp.ExecutorClienteFTP;
import com.arthurassuncao.stundplayer.cliente.rtsp.ClienteRTSP;
import com.arthurassuncao.stundplayer.eventos.EventoMenuPopup;
import com.arthurassuncao.stundplayer.eventos.EventoMoverJanelaPeloPainel;
import com.arthurassuncao.stundplayer.eventos.player.TratadorEventoTimerMusicaPlayer;
import com.arthurassuncao.stundplayer.eventos.player.TratadorEventosMousePlayer;
import com.arthurassuncao.stundplayer.eventos.player.TratadorEventosTecladoPlayer;
import com.arthurassuncao.stundplayer.gui.Fonte;
import com.arthurassuncao.stundplayer.gui.Imagem;
import com.arthurassuncao.stundplayer.gui.Janela;
import com.arthurassuncao.stundplayer.gui.OvalBorder;
import com.arthurassuncao.stundplayer.gui.Painel;
import com.arthurassuncao.stundplayer.gui.SoundMeter;
import com.arthurassuncao.stundplayer.gui.sistema.JanelaConfiguracoes;
import com.arthurassuncao.stundplayer.gui.sistema.JanelaLogin;
import com.arthurassuncao.stundplayer.gui.usuario.JanelaDeletarConta;
import com.arthurassuncao.stundplayer.recursos.Recursos;
import com.arthurassuncao.stundplayer.util.Constantes;
import com.sun.awt.AWTUtilities;

/** Janela do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see Janela
 */
public class JanelaPlayer extends Janela {

	private static final long serialVersionUID = -621396868537173203L;
	private static final int BORDA_JANELA = 0;
	private static final int LARGURA_JANELA_INTERNA = 370 + BORDA_JANELA;
	private static final int ALTURA_JANELA_INTERNA = 510 + BORDA_JANELA;
	private static final int LARGURA = LARGURA_JANELA_INTERNA + BORDA_JANELA;
	private static final int ALTURA = ALTURA_JANELA_INTERNA + BORDA_JANELA;

	private final int REPETIR_DESLIGADO = 0;
	private final int REPETIR_LIGADO = 1;
	private final int REPETIR_TUDO = 2;

	private Usuario usuario;
	private ClienteRTSP clienteRTSP;
	private ExecutorClienteFTP executorClienteFTP;
	private MusicaDados musicaEmExecucao;
	private int linhaMusicaEmExecucao;
	private Timer timerBarraProgressoMusica;
	private JLabel labelMusicaEmExecucao;
	private Queue<Character> filaLabelMusicaEmExecucao;
	private final int maximoCaracteresLabelMusicaEmExecucao = 67;
	private Timer timerLabelMusicaEmExecucao;
	private final int numeroVezesMaiorNormalBarraProgressoMusica = 2; //numero de vezes q o tempo na barra sera maior q o normal
	private int repetir = REPETIR_LIGADO;
	private int shuffle = 0;

	//tabela
	/** <code>String</code> representando a coluna do numero das musicas */
	public final String COLUNA_NUMERO_MUSICAS = " ";
	/** <code>String</code> representando a coluna do nome das musicas */
	public final String COLUNA_NOME_MUSICAS = "Musicas";
	/** <code>String</code> representando a coluna da duracao das musicas */
	public final String COLUNA_DURACAO_MUSICAS = "Duração";
	private final String[] COLUNAS_PLAYLIST = {COLUNA_NUMERO_MUSICAS, COLUNA_NOME_MUSICAS, COLUNA_DURACAO_MUSICAS};
	/** <code>int</code> representando o numero de colunas do tabela playlist */
	public final int NUMERO_COLUNAS_TABELA_PLAYLIST = COLUNAS_PLAYLIST.length;
	private String[][] linhasTabelaPlaylist = new String[0][NUMERO_COLUNAS_TABELA_PLAYLIST];

	//private int larguraTabelaPlaylist = 290;
	private int larguraTabelaPlaylist = LARGURA_JANELA_INTERNA - 40;
	private int alturaTabelaPlaylist = 210;

	/** <code>String</code> representando o texto padrao do campo pesquisar*/
	public String TEXTO_PADRAO_PESQUISAR = "Pesquisa rápida";

	private JLayeredPane painelFundo;
	private JLayeredPane painelFundo2;

	//tratadores de eventos
	TratadorEventosMousePlayer tratadorEventosMouse = new TratadorEventosMousePlayer(this);
	TratadorEventosTecladoPlayer tratadorEventosTeclado= new TratadorEventosTecladoPlayer(this);

	//paineis
	private Painel painelImagemFundo;
	private Painel painelSuperior;
	private Painel painelBotoesMenu;
	private Painel painelBotoesSuperior;
	private Painel painelPlayer;
	private Painel painelTempoMusica;
	private JTabbedPane painelPlaylists;
	private Painel painelPlaylist;
	private Painel painelInferior;
	private Painel painelBotoesInferior;

	private JLabel labelNomePrograma = new JLabel(Constantes.PLAYER_NOME);
	//botoes
	private BotaoPopupMenuPrincipal botaoMenuPrincipal;
	private BotaoMenuPlayer botaoPreferencias;
	private BotaoMenuPlayer botaoMinimizar;
	private BotaoMenuPlayer botaoSempreNoTopo;
	private BotaoMenuPlayer botaoFechar;

	private BotaoMenuPlayer botaoShuffle;
	private BotaoMenuPlayer botaoRepetir;

	//botoes do player
	private BotaoPlayer botaoPlay;
	private BotaoPlayer botaoPause;
	private BotaoPlayer botaoStop;
	private BotaoPlayer botaoProximaMusica;
	private BotaoPlayer botaoMusicaAnterior;
	private BotaoPlayer botaoAbrirMusica;

	private JButton botaoPesquisar;

	private BotaoPopupUsuario botaoUsuario;

	//Sliders
	private JSlider sliderVolume;
	private FloatControl controleVolume;

	//barra de progresso
	private JProgressBar barraProgressoMusica;
	private JProgressBar barraProgressoUploading;

	//campos de texto
	private JTextField campoPesquisa;

	//imagens
	private ImageIcon imagemLogo;

	//imagens do player
	private ImageIcon imagemPlay;
	private ImageIcon imagemPause;
	private ImageIcon imagemStop;
	private ImageIcon imagemProxima;
	private ImageIcon imagemAnterior;
	private ImageIcon imagemAbrir;

	private ImageIcon imagemProcurar;
	private ImageIcon imagemRepetir;
	private ImageIcon imagemShuffle;

	//labels
	private JLabel labelImagemLogo;
	private JLabel labelTempoMusica;

	//tabela com as musicas
	private TabelaPlaylist tabelaPlaylist;

	//barra de rolagem
	private JScrollPane barraRolagem;

	//cores
	private Color corFundo = Configuracoes.getInstance().getCorFundoPlayer();

	/** Cria a janela do player
	 * @param usuario <code>Usuario</code> com o usuario logado
	 */
	public JanelaPlayer(Usuario usuario){
		super(Constantes.PLAYER_NOME, LARGURA, ALTURA);

		this.usuario = usuario;

		this.iniciaElementos();

		this.setaPropriedades();

		this.addMusicasDadosTabelaPlaylist(usuario.getListaMusicas());

		this.addElementos();

		this.addEventos();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.addMouseListener(tratadorEventosMouse);
		this.addKeyListener(tratadorEventosTeclado);

		this.setLocationRelativeTo(null);

		this.setResizable(true);

		final int alturaAMais = 150; //pode ir de ALTURA a ALTURA + alturaAMais
		this.setPreferredSize(new Dimension(LARGURA, ALTURA));
		this.setMinimumSize(new Dimension(LARGURA, ALTURA));
		this.setMaximumSize(new Dimension(LARGURA, ALTURA + alturaAMais));

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension size = JanelaPlayer.this.getSize();
				int altura = size.height < ALTURA + alturaAMais ? size.height : ALTURA + alturaAMais;
				JanelaPlayer.this.setPreferredSize(new Dimension(LARGURA, altura));
				JanelaPlayer.this.pack();
			}
		});

		//this.pack();

		if(this.getToolkit().isFrameStateSupported(Frame.ICONIFIED)){
			this.setUndecorated(true); //magica
			this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

			if (AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT)) {
				AWTUtilities.setWindowOpacity(this, 0.97F);
				//AWTUtilities.setWindowOpaque(this, false);
			}

			Shape shape = null;
			shape = new RoundRectangle2D.Float(0, 0, this.getWidth(), this.getHeight(), 20, 20);
			AWTUtilities.setWindowShape(this, shape);

			EventoMoverJanelaPeloPainel eventoMoverJanelaComMouse = new EventoMoverJanelaPeloPainel(this.painelFundo2);
			this.addMouseListener(eventoMoverJanelaComMouse);
			this.addMouseMotionListener(eventoMoverJanelaComMouse);
		}

		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent evento) {
				super.windowClosing(evento);
				logout();
			}
		});

		this.setVisible(true);
	}

	/** Inicializa os elementos
	 * 
	 */
	public void iniciaElementos(){

		this.filaLabelMusicaEmExecucao = new LinkedList<Character>();

		this.timerBarraProgressoMusica = new Timer(1000 / numeroVezesMaiorNormalBarraProgressoMusica, new TratadorEventoTimerMusicaPlayer(this));
		this.timerLabelMusicaEmExecucao = new Timer(151, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				JanelaPlayer.this.atualizaLabelMusicaEmExecucao();			
			}
		});
		this.timerLabelMusicaEmExecucao.setCoalesce(false);

		//      Elementos graficos
		//inicia os paineis
		this.painelFundo2 = this.getLayeredPane();
		this.painelFundo = new JLayeredPane();
		this.painelFundo.setLayout(this.painelFundo2.getLayout());
		this.painelImagemFundo = new Painel(new GridBagLayout()){
			private static final long serialVersionUID = 6985272057114473313L;
			/* (non-Javadoc)
			 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
			 */
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int largura = getWidth();
				int altura = getHeight();
				Graphics2D g2d = (Graphics2D)g;
				// verifica se antialisiang esta ativado
				RenderingHints rhints = g2d.getRenderingHints();
				boolean antialiasOn = rhints.containsValue(RenderingHints.VALUE_ANTIALIAS_ON);
				if(!antialiasOn){ // ativa o antialiasing
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				}
				g2d.setColor(JanelaPlayer.this.corFundo); //seta a cor de fundo
				g2d.fillRoundRect(0, 0, largura, altura, 20, 20); //desenha
			}
		};
		this.painelSuperior = new Painel(new GridBagLayout());
		this.painelBotoesMenu = new Painel(new GridBagLayout());
		this.painelBotoesSuperior = new Painel(new GridBagLayout());
		this.painelPlayer = new Painel(new GridBagLayout());
		this.painelTempoMusica = new Painel(new GridBagLayout());
		this.painelPlaylists = new JTabbedPane();
		this.painelPlaylist = new Painel(new GridBagLayout());
		this.painelInferior = new Painel(new GridBagLayout());
		this.painelBotoesInferior = new Painel(new GridBagLayout());

		this.tabelaPlaylist = new TabelaPlaylist(new DefaultTableModel(this.linhasTabelaPlaylist, this.COLUNAS_PLAYLIST));
		DefaultTableCellRenderer cellRendererCentralizado = new DefaultTableCellRenderer(){
			private static final long serialVersionUID = 5382663962043209313L;
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
				if(column == 0){
					this.setHorizontalAlignment(CENTER);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};
		this.tabelaPlaylist.getColumn(COLUNA_NUMERO_MUSICAS).setCellRenderer(cellRendererCentralizado);
		this.tabelaPlaylist.getColumn(COLUNA_DURACAO_MUSICAS).setCellRenderer(cellRendererCentralizado);

		barraRolagem = new JScrollPane(this.tabelaPlaylist);

		//inicia a imagem e o labelImagem
		this.imagemLogo = Janela.getLogoPlayer();
		double escala = 0.30;
		this.imagemLogo.setImage(Imagem.redimensionaImagem(imagemLogo, escala, escala));
		this.labelImagemLogo = new JLabel(this.imagemLogo);
		this.labelImagemLogo.setOpaque(false);

		//inicia as imagens
		double escalaIcones = 0.5;
		double escalaIconePlay = 0.75;
		imagemPlay = new ImageIcon(Recursos.getResource("icones/media_play.png"));
		imagemPause = new ImageIcon(Recursos.getResource("icones/media_pause.png"));
		imagemStop = new ImageIcon(Recursos.getResource("icones/media_stop.png"));
		imagemProxima = new ImageIcon(Recursos.getResource("icones/media_fast-forward.png"));
		imagemAnterior = new ImageIcon(Recursos.getResource("icones/media_fast-backward.png"));
		imagemAbrir = new ImageIcon(Recursos.getResource("icones/media_ejetar.png"));

		imagemProcurar = new ImageIcon(Recursos.getResource("icones/media_procurar.png"));
		
		imagemRepetir = new ImageIcon(Recursos.getResource("icones/media_repetir.png"));
		imagemShuffle  = new ImageIcon(Recursos.getResource("icones/media_shuffle.png"));

		//redimensiona as imagens
		imagemPlay.setImage(Imagem.redimensionaImagem(imagemPlay, escalaIconePlay, escalaIconePlay));
		imagemPause.setImage(Imagem.redimensionaImagem(imagemPause, escalaIcones, escalaIcones));
		imagemStop.setImage(Imagem.redimensionaImagem(imagemStop, escalaIcones, escalaIcones));
		imagemProxima.setImage(Imagem.redimensionaImagem(imagemProxima, escalaIcones, escalaIcones));
		imagemAnterior.setImage(Imagem.redimensionaImagem(imagemAnterior, escalaIcones, escalaIcones));
		imagemAbrir.setImage(Imagem.redimensionaImagem(imagemAbrir, escalaIcones, escalaIcones));

		imagemProcurar.setImage(Imagem.redimensionaImagem(imagemProcurar, escalaIcones, escalaIcones));
		imagemRepetir.setImage(Imagem.redimensionaImagem(imagemRepetir, escalaIcones, escalaIcones));
		imagemShuffle.setImage(Imagem.redimensionaImagem(imagemShuffle, 0.4, escalaIcones));

		ImageIcon imagemConfiguracoes = new ImageIcon(Recursos.getResource("icones/engrenagem_preta.png"));
		imagemConfiguracoes.setImage(Imagem.redimensionaImagem(imagemConfiguracoes, 0.4, 0.4));

		this.labelTempoMusica = new JLabel("00:00");
		this.labelMusicaEmExecucao = new JLabel(" ");

		//inicia os botoes
		this.botaoMenuPrincipal = new BotaoPopupMenuPrincipal(this);
		this.botaoPreferencias = new BotaoMenuPlayer(imagemConfiguracoes);
		this.botaoPreferencias.setVerticalAlignment(SwingConstants.TOP);
		this.botaoMinimizar = new BotaoMenuPlayer("_");
		this.botaoSempreNoTopo = new BotaoMenuPlayer(" ");
		this.botaoFechar = new BotaoMenuPlayer("X");

		this.botaoShuffle = new BotaoMenuPlayer(imagemShuffle);
		this.botaoRepetir = new BotaoMenuPlayer(imagemRepetir);

		//inicia botoes do player
		this.botaoPlay = new BotaoPlayer(30, 30, imagemPlay);
		this.botaoPause = new BotaoPlayer(imagemPause);
		this.botaoStop = new BotaoPlayer(imagemStop);
		this.botaoProximaMusica = new BotaoPlayer(imagemProxima);
		this.botaoMusicaAnterior = new BotaoPlayer(imagemAnterior);
		this.botaoAbrirMusica = new BotaoPlayer(20, 20, imagemAbrir);

		this.botaoPesquisar = new JButton(imagemProcurar);

		this.botaoUsuario = new BotaoPopupUsuario(this, 25, 100);
		this.botaoUsuario.setText(this.usuario.getUsername());
		//inicia o slider
		this.sliderVolume = new JSlider(0, 100, 50);
		this.controleVolume = SoundMeter.getControleVolumeSpeaker();
		if(this.controleVolume != null){
			this.sliderVolume.setValue((int)(this.controleVolume.getValue() * 100));
		}

		//inicia a barra de progresso
		this.barraProgressoMusica = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);

		this.barraProgressoUploading = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);

		//inicia o campo de texto
		this.campoPesquisa = new JTextField(10);
	}

	/** Seta as propriedades dos elementos da janela
	 * 
	 */
	public void setaPropriedades(){
		//paineis
		//this.painelFundo.setLayout(this.getLayeredPane().getLayout());
		this.painelFundo.setOpaque(false);
		this.painelFundo2.setOpaque(true);
		this.painelImagemFundo.setPreferredSize(new Dimension(LARGURA_JANELA_INTERNA, ALTURA_JANELA_INTERNA));
		this.painelPlaylists.setFocusable(false);

		this.painelImagemFundo.setOpaque(false);
		this.painelSuperior.setOpaque(false);
		this.painelBotoesMenu.setOpaque(false);
		this.painelBotoesSuperior.setOpaque(false);
		this.painelPlayer.setOpaque(false);
		this.painelTempoMusica.setOpaque(false);
		this.painelPlaylist.setOpaque(false);
		this.painelPlaylists.setOpaque(false);
		this.painelInferior.setOpaque(false);
		this.painelBotoesInferior.setOpaque(false);

		this.painelSuperior.setBackground(corFundo);
		this.painelBotoesMenu.setBackground(corFundo);
		this.painelBotoesSuperior.setBackground(corFundo);
		this.painelPlayer.setBackground(corFundo);
		this.painelTempoMusica.setBackground(corFundo);
		this.painelPlaylist.setBackground(corFundo);
		this.painelPlaylists.setBackground(corFundo);
		this.painelInferior.setBackground(corFundo);
		this.painelBotoesInferior.setBackground(corFundo);

		//this.painelSuperior.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.painelSuperior.setBorder(new OvalBorder(8, 8, Color.GRAY.darker(), Color.DARK_GRAY));
		this.painelInferior.setBorder(new OvalBorder(8, 8, Color.GRAY.darker(), Color.DARK_GRAY));
		this.painelBotoesInferior.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));

		this.painelBotoesInferior.setPreferredSize(new Dimension(larguraTabelaPlaylist, 30));

		this.labelTempoMusica.setFont(new Fonte("digifaw.ttf", 15, Fonte.ESTILO_NORMAL).getFont());
		this.labelTempoMusica.setForeground(Color.LIGHT_GRAY);
		this.labelMusicaEmExecucao.setForeground(Color.LIGHT_GRAY);

		//botoes
		this.botaoMenuPrincipal.setToolTipText("Menu Principal");
		this.botaoPreferencias.setToolTipText("Preferencias");
		this.botaoMinimizar.setToolTipText("Minimizar");
		this.botaoSempreNoTopo.setToolTipText("Sempre No Topo");
		this.botaoFechar.setToolTipText("Fechar " + Constantes.PLAYER_NOME);

		this.botaoShuffle.setToolTipText("Misturar [off]");
		this.botaoRepetir.setToolTipText("Repetir [on]");

		//botoes do player
		this.botaoPlay.setToolTipText("Play");
		this.botaoPause.setToolTipText("Pause");
		this.botaoStop.setToolTipText("Stop");
		this.botaoProximaMusica.setToolTipText("Proxima");
		this.botaoMusicaAnterior.setToolTipText("Anterior");
		this.botaoAbrirMusica.setToolTipText("Abrir musica");

		this.botaoPesquisar.setToolTipText("Pesquisar");

		//tira o foco botoes
		this.botaoMenuPrincipal.setFocusable(false);
		this.botaoPreferencias.setFocusable(false);
		this.botaoMinimizar.setFocusable(false);
		this.botaoSempreNoTopo.setFocusable(false);
		this.botaoFechar.setFocusable(false);
		//botoes do player
		this.botaoPlay.setFocusable(false);
		this.botaoPause.setFocusable(false);
		this.botaoStop.setFocusable(false);
		this.botaoProximaMusica.setFocusable(false);
		this.botaoMusicaAnterior.setFocusable(false);
		this.botaoAbrirMusica.setFocusable(false);
		this.botaoPesquisar.setFocusable(false);

		//this.botaoUsuario.setFont(new Fonte(10F).getFont());
		this.botaoUsuario.setFocusable(false);
		this.botaoUsuario.setBackground(Janela.getCorPadraoPlayer().brighter());
		this.botaoUsuario.setForeground(Color.BLACK);

		Font fonteBotoesMenu = new Fonte(10F).getFont();
		this.botaoMenuPrincipal.setFont(fonteBotoesMenu);
		this.botaoPreferencias.setFont(fonteBotoesMenu);
		this.botaoMinimizar.setFont(fonteBotoesMenu);
		this.botaoSempreNoTopo.setFont(fonteBotoesMenu);
		this.botaoFechar.setFont(fonteBotoesMenu);

		this.labelNomePrograma.setFont(new Fonte(13F, Fonte.ESTILO_NEGRITO).getFont());
		this.labelNomePrograma.setForeground(Janela.getCorPadraoPlayer());
		this.labelNomePrograma.setPreferredSize(new Dimension(170, 19));
		this.labelNomePrograma.setMinimumSize(new Dimension(170, 19));
		this.labelNomePrograma.setHorizontalAlignment(SwingConstants.CENTER);

		//campos de texto
		this.botaoPesquisar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.campoPesquisa.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		this.campoPesquisa.setText(TEXTO_PADRAO_PESQUISAR);
		this.campoPesquisa.setFont(new Fonte(10F).getFont());
		this.campoPesquisa.setBackground(corFundo.brighter().brighter().brighter().brighter());
		this.campoPesquisa.setForeground(Color.DARK_GRAY);

		//tabela
		this.tabelaPlaylist.getColumn(COLUNA_NUMERO_MUSICAS).setPreferredWidth(30);
		this.tabelaPlaylist.getColumn(COLUNA_NOME_MUSICAS).setPreferredWidth(larguraTabelaPlaylist);
		this.tabelaPlaylist.getColumn(COLUNA_DURACAO_MUSICAS).setPreferredWidth(62);

		this.tabelaPlaylist.getColumn(COLUNA_NUMERO_MUSICAS).setResizable(false);
		this.tabelaPlaylist.getColumn(COLUNA_DURACAO_MUSICAS).setResizable(false);

		this.tabelaPlaylist.requestFocus();

		//this.tabelaPlaylist.setHighlighters(Color.GREEN);
		JTableHeader cabecalhoTabela = this.tabelaPlaylist.getTableHeader();
		cabecalhoTabela.setDefaultRenderer(new DefaultTableCellRenderer(){
			private static final long serialVersionUID = 5212537814414616064L;
			@Override
			public Component getTableCellRendererComponent(JTable arg0,
					Object objeto, boolean arg2, boolean arg3, int arg4, int arg5) {
				//return super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
				JLabel label = new JLabel(objeto.toString());
				label.setOpaque(true);
				label.setForeground(Janela.getCorPadraoPlayer());
				label.setBackground(corFundo.brighter());
				return label;
			}
		});

		//barra rolagem
		this.barraRolagem.setOpaque(false);
		this.barraRolagem.setPreferredSize(new Dimension(larguraTabelaPlaylist, alturaTabelaPlaylist));
		this.barraRolagem.setBorder(null);

		//slider volume
		this.sliderVolume.setPreferredSize(new Dimension(100, 20));
		this.sliderVolume.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.sliderVolume.setFocusable(false);

		//barra de progresso
		this.barraProgressoUploading.setOpaque(false);
		this.barraProgressoUploading.setPreferredSize(new Dimension(50, 20));
		this.barraProgressoUploading.setIndeterminate(true);
		this.barraProgressoUploading.setToolTipText("Uploading...");
		this.barraProgressoUploading.setVisible(false);		
	}

	/** Adiciona os elementos a janela
	 * 
	 */
	public void addElementos(){
		//adiciona ao painelPlayer
		GridBagConstraints grid = new GridBagConstraints();
		grid.insets = new Insets(5, 5, 0, 5);
		grid.gridy = 0;
		int coluna = 0;
		grid.gridx = coluna++;
		this.painelPlayer.add(this.botaoPlay, grid);
		grid.gridx = coluna++;
		this.painelPlayer.add(this.botaoPause, grid);
		grid.gridx = coluna++;
		this.painelPlayer.add(this.botaoStop, grid);
		grid.gridx = coluna++;
		this.painelPlayer.add(this.botaoMusicaAnterior, grid);
		grid.gridx = coluna++;
		this.painelPlayer.add(this.botaoProximaMusica, grid);
		grid.gridx = coluna++;
		this.painelPlayer.add(this.botaoAbrirMusica, grid);

		//Adiciona ao painelBotoesMenu
		coluna = 0;
		grid.gridx = coluna++;
		grid.gridy = 0;
		grid.insets = new Insets(0, 0, 0, 0);
		this.painelBotoesMenu.add(this.botaoMenuPrincipal, grid);
		grid.gridx = coluna++;
		this.painelBotoesMenu.add(this.botaoPreferencias, grid);
		grid.gridx = coluna++;
		this.painelBotoesMenu.add(this.labelNomePrograma, grid);
		grid.gridx = coluna++;
		this.painelBotoesMenu.add(this.botaoMinimizar, grid);
		grid.gridx = coluna++;
		this.painelBotoesMenu.add(this.botaoSempreNoTopo, grid);
		grid.gridx = coluna++;
		this.painelBotoesMenu.add(this.botaoFechar, grid);

		//Adiciona ao painelSuperior
		grid.gridx = 0;
		grid.gridy = 0;
		this.labelMusicaEmExecucao.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, corFundo.brighter()));
		this.labelMusicaEmExecucao.setPreferredSize(new Dimension(LARGURA_JANELA_INTERNA - 50, 15));
		grid.gridwidth = 2;
		grid.insets = new Insets(0, 5, 11, 5);
		this.painelSuperior.add(this.labelMusicaEmExecucao, grid);
		grid.gridwidth = 1;
		grid.gridx = 0;
		grid.gridy = 1;
		grid.insets = new Insets(5, 5, 5, 5);
		grid.insets = new Insets(0, 5, 0, 5);
		this.painelSuperior.add(this.labelImagemLogo, grid);
		grid.gridx = 0;
		grid.gridy = 2;
		this.painelSuperior.add(this.painelPlayer, grid);
		grid.gridx = 1;
		this.painelSuperior.add(this.sliderVolume, grid);

		//adiciona ao painelPlaylist
		//this.painelPlaylist.add(this.barraRolagem);
		/*for(int i = 0; i < 20; i ++){
			addLinhaTabelaPlaylist("Musica " + i);
		}*/
		this.painelPlaylists.addTab("Playlist", this.barraRolagem);

		this.mudaCorAbasPlaylist();

		//Adiciona ao painelBotoesInferior
		this.painelBotoesInferior.add(this.botaoPesquisar);
		this.painelBotoesInferior.add(this.campoPesquisa);

		//Adiciona ao painelInferior
		grid.gridx = 0;
		grid.gridy = 0;
		grid.insets = new Insets(0, 0, 0, 0);
		this.painelInferior.add(this.painelPlaylists, grid);
		grid.gridy = 1;
		grid.insets = new Insets(5, 0, 0, 0);
		this.painelInferior.add(this.painelBotoesInferior, grid);

		//this.painelFundo.setPreferredSize(new Dimension(LARGURA_JANELA_INTERNA, ALTURA_JANELA_INTERNA));

		//adiciona ao painelFundo
		int camada = 0;
		int margem = 10;
		int posicaoAlturaAnterior = 0;
		//JLabel labelImagemFundo = new JLabel(new ImageIcon("imagens/fundoLogin.png"));
		JLabel labelImagemFundo = new JLabel();
		labelImagemFundo.setBackground(corFundo);
		labelImagemFundo.setOpaque(true);
		labelImagemFundo.setBounds(0, 0, LARGURA_JANELA_INTERNA, ALTURA_JANELA_INTERNA);
		this.painelImagemFundo.setBounds(0, 0, LARGURA_JANELA_INTERNA, ALTURA_JANELA_INTERNA);
		this.painelFundo.add(this.painelImagemFundo, new Integer(camada++));

		this.painelBotoesMenu.setBounds(margem, posicaoAlturaAnterior, LARGURA_JANELA_INTERNA - margem * 2 , 18); //era para ser 20 / 2, mas nao fica no meio
		this.painelFundo.add(this.painelBotoesMenu, new Integer(camada++));

		posicaoAlturaAnterior += 18;
		this.painelSuperior.setBounds(margem, posicaoAlturaAnterior, LARGURA_JANELA_INTERNA - margem * 2 , 150);
		this.painelFundo.add(this.painelSuperior, new Integer(camada++));

		posicaoAlturaAnterior += 150;
		this.barraProgressoMusica.setBounds(margem + 5, posicaoAlturaAnterior, LARGURA_JANELA_INTERNA - (margem + 5) * 2 , 20);
		this.painelFundo.add(this.barraProgressoMusica, new Integer(camada++));

		posicaoAlturaAnterior += 20;
		System.out.println("Descomentar linha 368 -  this.painelInferior.setBounds(20 / 2, posicaoAlturaAnterior, LARGURA - 20, alturaTabelaPlaylist + 80);");
		this.painelInferior.setBounds(margem, posicaoAlturaAnterior, LARGURA_JANELA_INTERNA - margem * 2, alturaTabelaPlaylist + 90);
		this.painelFundo.add(this.painelInferior, new Integer(camada++));

		this.barraProgressoUploading.setBounds(LARGURA_JANELA_INTERNA - 67, ALTURA_JANELA_INTERNA - 60, 50, 20);
		this.painelFundo.add(this.barraProgressoUploading, new Integer(camada++));

		this.botaoUsuario.setBounds(LARGURA_JANELA_INTERNA - 105, 40, 80, 25);
		this.painelFundo.add(this.botaoUsuario, new Integer(camada++));

		/*this.botaoShuffle.setBounds(LARGURA_JANELA_INTERNA - 95, 90, 25, 25);
		this.painelFundo.add(this.botaoShuffle, new Integer(camada++));

		this.botaoRepetir.setBounds(LARGURA_JANELA_INTERNA - 70, 90, 25, 25);
		this.painelFundo.add(this.botaoRepetir, new Integer(camada++));*/
		
		this.botaoShuffle.setBounds(LARGURA_JANELA_INTERNA - 50, 85, 25, 25);
		this.painelFundo.add(this.botaoShuffle, new Integer(camada++));

		this.botaoRepetir.setBounds(LARGURA_JANELA_INTERNA - 50, 110, 25, 25);
		this.painelFundo.add(this.botaoRepetir, new Integer(camada++));

		this.labelTempoMusica.setBounds(LARGURA_JANELA_INTERNA - 75, 65, 50, 25);
		this.painelFundo.add(this.labelTempoMusica, new Integer(camada++));

		//Adiciona ao painelFundo2
		int camada2 = 0;

		JLabel labelImagemFundo2 = new JLabel();
		labelImagemFundo2.setBackground(corFundo);
		labelImagemFundo2.setOpaque(true);
		labelImagemFundo2.setBounds(0, 0, LARGURA, ALTURA);
		this.painelFundo2.add(labelImagemFundo2, new Integer(camada2++));

		this.painelFundo.setBounds(BORDA_JANELA / 2, BORDA_JANELA / 2, LARGURA_JANELA_INTERNA, ALTURA_JANELA_INTERNA);
		this.painelFundo2.add(this.painelFundo, new Integer(camada2++));

		/*this.painelBotoesInferior.setBounds(0, ALTURA - 100, LARGURA, 100);
		this.painelFundo.add(this.painelBotoesInferior, new Integer(camada++));*/

		posicaoAlturaAnterior += 1;
	}

	/** Adiciona eventos aos elementos da janela
	 * 
	 */
	public void addEventos(){
		this.painelPlaylists.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				mudaCorAbasPlaylist();
			}
		});

		this.campoPesquisa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				List<MusicaDados> listaMusicas = null;
				if(campoPesquisa.getText().isEmpty()){
					addTodasMusicas();
				}
				else{
					listaMusicas = pesquisaMusicas(campoPesquisa.getText());
					JanelaPlayer.this.addMusicasDadosTabelaPlaylist(listaMusicas);
				}
			}
		});

		this.campoPesquisa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent evento) {
				JTextField campo = (JTextField)evento.getSource();
				if(campo.getText().equalsIgnoreCase("")){
					campo.setText(TEXTO_PADRAO_PESQUISAR);
				}
			}
			@Override
			public void focusGained(FocusEvent evento) {
				super.focusGained(evento);
				JTextField campo = (JTextField)evento.getSource();
				if(campo.getText().equalsIgnoreCase(TEXTO_PADRAO_PESQUISAR)){
					campo.setText("");
				}
			}
		});
		this.campoPesquisa.addMouseListener(tratadorEventosMouse);

		//botoes
		this.botaoAbrirMusica.addMouseListener(tratadorEventosMouse);
		//this.botaoFechar.addMouseListener(tratadorEventosMouse);
		//this.botaoMaximizar.addMouseListener(tratadorEventosMouse);
		this.botaoMenuPrincipal.addMouseListener(tratadorEventosMouse);
		//this.botaoMinimizar.addMouseListener(tratadorEventosMouse);
		this.botaoMusicaAnterior.addMouseListener(tratadorEventosMouse);
		this.botaoPause.addMouseListener(tratadorEventosMouse);
		this.botaoPesquisar.addMouseListener(tratadorEventosMouse);
		this.botaoPlay.addMouseListener(tratadorEventosMouse);
		//this.botaoPreferencias.addMouseListener(tratadorEventosMouse);
		this.botaoProximaMusica.addMouseListener(tratadorEventosMouse);
		this.botaoStop.addMouseListener(tratadorEventosMouse);
		
		this.botaoRepetir.addMouseListener(tratadorEventosMouse);
		this.botaoShuffle.addMouseListener(tratadorEventosMouse);

		//setAlwaysOnTop(true)
		this.botaoSempreNoTopo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(JanelaPlayer.this.isAlwaysOnTopSupported()){
					if(JanelaPlayer.this.isAlwaysOnTop()){
						JanelaPlayer.this.setAlwaysOnTop(false);
						JanelaPlayer.this.botaoSempreNoTopo.setBackground(JanelaPlayer.this.botaoSempreNoTopo.getBackground());
					}
					else{
						JanelaPlayer.this.setAlwaysOnTop(true);
						JanelaPlayer.this.botaoSempreNoTopo.setBackground(corFundo.darker().darker());
					}

				}
			}
		});
		this.botaoMinimizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JanelaPlayer.this.setExtendedState(JFrame.ICONIFIED);
			}
		});
		//this.botaoMaximizar = new JButton();
		this.botaoFechar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.botaoPreferencias.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JanelaConfiguracoes();
			}
		});

		this.tabelaPlaylist.addMouseListener(tratadorEventosMouse);
		this.tabelaPlaylist.addKeyListener(tratadorEventosTeclado);
		this.tabelaPlaylist.addMouseListener(new EventoMenuPopup(this.tabelaPlaylist, new MenuPopup(this.tabelaPlaylist, this)));

		this.sliderVolume.addMouseListener(tratadorEventosMouse);
		this.sliderVolume.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JanelaPlayer.this.setValorVolume();
			}
		});
		this.sliderVolume.setValue(this.sliderVolume.getValue());

		this.barraProgressoMusica.addMouseListener(tratadorEventosMouse);
		this.barraProgressoMusica.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JanelaPlayer.this.setBarraProgressoMusicaTooltipText();
			}
		});
	}

	/** Encerra as dependencias do player, como a musica que esta executando, a conexao etc.
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void encerraDependencias(){
		if(this.clienteRTSP != null && this.clienteRTSP.isMusicaRodando()){
			this.clienteRTSP.encerraSemResposta();
		}
	}

	/** Faz logout
	 * 
	 */
	public void logout(){
		this.paraMusica();
		this.encerraDependencias();
		this.dispose();
		new JanelaLogin();
	}

	/** Seta o valor do volume e a mensagem de dica(tooltip) do componente de volume
	 * 
	 */
	public void setValorVolume(){
		this.sliderVolume.setToolTipText("Volume " + sliderVolume.getValue() + "%");
		//this.sliderVolume.setValue((int)this.controleVolume.getValue());
		if(this.controleVolume != null){
			JanelaPlayer.this.controleVolume.setValue(sliderVolume.getValue() / 100.0F); //controle do pc vai de 0.0 a 1.0

			/*float novoVolume = (float) (Math.log(this.sliderVolume.getValue() / 100.0f) / Math.log(2.0f) * this.controleVolume.getMaximum()) ;
			this.controleVolume.setValue(novoVolume);*/
		}
	}

	/** Atualiza o tempo da musica
	 * @param minutos <code>int</code> com os minutos atuais da musica
	 * @param segundos <code>int</code> com os segundos atuais da musica
	 */
	private void atualizaTempoMusica(int minutos, int segundos){
		this.labelTempoMusica.setText((minutos < 10 ? "0" + minutos  : minutos) + ":" + (segundos < 10 ? "0" + segundos : segundos));
	}

	/** Seta o texto de tooltip na barra de progresso da musica
	 * 
	 */
	public void setBarraProgressoMusicaTooltipText(){
		MusicaDados musicaTocando = JanelaPlayer.this.getMusicaEmExecucao();
		if(musicaTocando != null){
			//int duracaoMusica = musicaTocando.getDuracao();
			int tempoAtualSegundos = (int)((this.barraProgressoMusica.getValue() / (numeroVezesMaiorNormalBarraProgressoMusica * 1.0)));

			int minutos = (tempoAtualSegundos / 60);
			int segundos = (tempoAtualSegundos % 60);
			String minutoSegundosAtual = (minutos < 10 ? "0" + minutos  : minutos) + ":" + (segundos < 10 ? "0" + segundos : segundos); 
			this.barraProgressoMusica.setToolTipText("" + minutoSegundosAtual);
			this.atualizaTempoMusica(minutos, segundos);
		}
		else{
			this.barraProgressoMusica.setToolTipText("" + "00:00");
			this.atualizaTempoMusica(0, 0);
		}
	}

	/** Atualiza o valor da barra de progresso da musica
	 * 
	 */
	public void atualizaValorBarraProgressoMusica() {
		int tempoAtual = this.barraProgressoMusica.getValue();
		/*if (tempoAtual >= (this.getMusicaEmExecucao().getDuracao() * numeroVezesMaiorNormalBarraProgressoMusica) - 1) {
			this.timerBarraProgressoMusica.stop();
		}*/
		this.barraProgressoMusica.setValue(tempoAtual + 1);
	}

	/** Adiciona uma linha a tabela
	 * @param musicaDados <code>MusicaDados</code> com os dados da musica que sera adicionada a tabela
	 */
	public void addLinhaTabelaPlaylist(MusicaDados musicaDados){
		//DefaultTableModel model = this.limpaTabelaPlaylist();
		DefaultTableModel model = (DefaultTableModel)(this.tabelaPlaylist.getModel());

		this.usuario.addMusica(musicaDados);
		Object[] linha = new Object[NUMERO_COLUNAS_TABELA_PLAYLIST];
		linha[0] = model.getRowCount() + 1;
		linha[1] = musicaDados;
		linha[2] = musicaDados.getDuracaoMMSS();
		model.addRow(linha);
	}

	/** Adiciona uma lista de musicas a tabela
	 * @param listaDeMusicas {@code List<MusicaDados>} com a lista de musicas
	 */
	public void addMusicasDadosTabelaPlaylist(List<MusicaDados> listaDeMusicas){
		DefaultTableModel model = limpaTabelaPlaylist();

		if(listaDeMusicas != null){
			if (listaDeMusicas.size() > 0){
				Object[] linha = new Object[NUMERO_COLUNAS_TABELA_PLAYLIST];
				for(int i = 0; i < listaDeMusicas.size(); i++){
					linha[0] = new Integer(i+1); //Integer
					linha[1] = listaDeMusicas.get(i); //MusicaDados
					linha[2] = listaDeMusicas.get(i).getDuracaoMMSS(); //String
					model.addRow(linha);
				}
			}
			else{
				//nao faz nada
			}
		}
	}

	/** Limpa a tabela da playlist apando os dados contidos nela
	 * @return <code>DefaultTableModel</code> com o modelo da tabela
	 */
	private DefaultTableModel limpaTabelaPlaylist(){
		DefaultTableModel model = (DefaultTableModel)(this.tabelaPlaylist.getModel());
		model.setNumRows(0);
		return model;
	}

	/** Adiciona todas as musicas do usuario a tabela, esse metodo limpa a tabela antes de adicionar
	 * 
	 */
	public void addTodasMusicas(){
		this.limpaTabelaPlaylist();
		//this.addLinhasTabelaPlaylist(this.listaTodasMusicas);
		this.addMusicasDadosTabelaPlaylist(usuario.getListaMusicas());
	}

	/** Muda a cor da aba tabela playlist
	 * 
	 */
	private void mudaCorAbasPlaylist(){
		int numeroAbas = this.painelPlaylists.getTabCount();
		JLabel tituloAba;

		for(int i = 0; i < numeroAbas; i++){
			tituloAba = new JLabel(this.painelPlaylists.getTitleAt(i));
			if(this.painelPlaylists.getSelectedIndex() == i){
				tituloAba.setForeground(getCorPadraoPlayer());
			}
			else{
				tituloAba.setForeground(getCorPadraoPlayer().darker());
			}
			tituloAba.setFocusable(false);
			this.painelPlaylists.setTabComponentAt(i, tituloAba);
		}
	}

	//so remove qndo o cliente apagar a musica do servidor
	/** Remove uma linha da tabela
	 * @param linha <code>int</code> com a linha que sera excluida
	 */
	private void removeLinhaTabela(int linha){
		//int coluna = this.tabelaPlaylist.getColumn(COLUNA_NOME_MUSICAS).getModelIndex();
		DefaultTableModel model = (DefaultTableModel)(this.tabelaPlaylist.getModel());

		//MusicaDados musicaRemovida = (MusicaDados)model.getValueAt(linha, coluna);
		model.removeRow(linha);
	}

	/** Pesquisa por musicas na playlist, este metodo leva em consideracao se o argumento passado contem nos nomes das musicas da playlist
	 * @param musica <code>String</code> nome ou pedaco do nome da musica que sera procurada
	 * @return <code>boolean</code> com <code>true</code> se a musica foi encontrada e <code>false</code> senao
	 */
	public List<MusicaDados> pesquisaMusicas(String musica){ //pesquisa musica na tabela playlist
		List<MusicaDados> listaMusicas = new ArrayList<MusicaDados>();
		for(MusicaDados algumaMusica : this.usuario.getListaMusicas()){
			if( algumaMusica.toString().toLowerCase().contains(musica.toLowerCase()) ){
				listaMusicas.add(algumaMusica);
			}
		}
		return listaMusicas;
	}

	/** Permite que o usuario faca upload de uma musica
	 * 
	 */
	public void abrirMusica(){
		String arquivoParaUpload = null;
		arquivoParaUpload = Janela.janelaAbrirArquivo(this, "Enviar Arquivos", null, false, "Musicas mp3", "mp3");
		if(arquivoParaUpload != null){
			//new ClienteFTP(this.usuario.getUsername(), arquivoParaUpload);
			//boolean enviou = false;
			//enviou = ClienteFTP.enviar(this.usuario.getUsername(), arquivoParaUpload);
			//ClienteFTP clienteFTP = new ClienteFTP(this, this.usuario.getUsername(), arquivoParaUpload); //ja inicia a thread
			if(this.executorClienteFTP == null){
				this.executorClienteFTP = new ExecutorClienteFTP(this, usuario.getUsername());
			}
			this.executorClienteFTP.addTransferenciaClienteFTP(arquivoParaUpload);
		}
	}

	/** Retorna a barra de progresso da musica
	 * @return <code>JProgressBar</code> com a barra de progresso
	 */
	public JProgressBar getBarraProgressoUploading() {
		return barraProgressoUploading;
	}

	/** Abre uma janela para excluir a conta do usuario
	 * 
	 */
	public void deletarConta(){
		new JanelaDeletarConta(this, usuario.getUsername());
	}

	/** Executa a proxima musica, caso shuffle estiver desativado a proxima musica sera a da proxima linha, caso nao houver proxima linha retorna a primeira musica.<br>
	 * Caso shuffle esteja ativado, a proxima musica é gerada aleatoria, sendo, sempre que possivel, diferente da musica atual
	 * 
	 */
	public void executaProximaMusica(){
		MusicaDados proximaMusica = null;
		switch(this.repetir){
		case REPETIR_DESLIGADO:
			this.clienteRTSP.stop();
			break;
		case REPETIR_LIGADO:
			//proximaMusica = this.getMusicaEmExecucao();
			this.executaMusica();
			break;
		case REPETIR_TUDO:
			int linhaMusicaEmExecucao = this.getLinhaMusicaEmExecucao();
			int coluna = this.tabelaPlaylist.getColumn(this.COLUNA_NOME_MUSICAS).getModelIndex();
			int novaLinhaPraExecucao = linhaMusicaEmExecucao;
			novaLinhaPraExecucao = proximaMusica();
			proximaMusica = (MusicaDados)this.tabelaPlaylist.getValueAt(novaLinhaPraExecucao, coluna);
			if(proximaMusica != null){
				this.executaMusica(proximaMusica, novaLinhaPraExecucao);
			}
			break;
		}
	}
	
	/** Retorna a linha da proxima musica, caso shuffle estiver desativado a proxima musica sera a da proxima linha, caso nao houver proxima linha retorna a primeira musica.<br>
	 * Caso shuffle esteja ativado, a proxima musica é gerada aleatoria, sendo, sempre que possivel, diferente da musica atual
	 * @return <code>int</code> com a linha da musica
	 */
	public int proximaMusica(){
		int novaLinhaPraExecucao = 0;
		if(this.shuffle == 0){
			if(this.linhaMusicaEmExecucao + 1 < this.tabelaPlaylist.getRowCount()){
				novaLinhaPraExecucao = this.linhaMusicaEmExecucao + 1;
			}
			else{
				novaLinhaPraExecucao = 0;
			}
		}
		else{
			novaLinhaPraExecucao = musicaAleatoria();
		}
		return novaLinhaPraExecucao;
	}
	
	/** Retorna a linha da proxima musica, caso shuffle estiver desativado a musica anterior sera a da linha anterior, caso a musica em execucao esteja na primeira linha, sera a ultima musica.<br>
	 * Caso shuffle esteja ativado, a proxima musica é gerada aleatoria, sendo, sempre que possivel, diferente da musica atual
	 * @return <code>int</code> com a linha da musica
	 */
	public int musicaAnterior(){
		int novaLinhaPraExecucao = 0;
		if(this.shuffle == 0){
			if(this.linhaMusicaEmExecucao - 1 >= 0 ){
				novaLinhaPraExecucao = this.linhaMusicaEmExecucao - 1;
			}
			else{
				novaLinhaPraExecucao = this.tabelaPlaylist.getRowCount() - 1;
			}
		}
		else{
			novaLinhaPraExecucao = musicaAleatoria();
		}
		return novaLinhaPraExecucao;
	}
	
	/** Retorna a linha de uma musica de forma aleatoria, sendo, sempre que possivel, diferente da musica que esteja em execucao
	 * @return <code>int</code> com a linha da musica
	 */
	public int musicaAleatoria(){
		int novaLinhaPraExecucao = 0;
		int numeroLinhas = this.tabelaPlaylist.getRowCount();
		Random random = new Random();
		if(numeroLinhas > 1){
			while(novaLinhaPraExecucao == this.linhaMusicaEmExecucao){
				novaLinhaPraExecucao = random.nextInt(numeroLinhas);
			}
		}
		else{
			novaLinhaPraExecucao = 0; //repetir a mesma musica
		}
		return novaLinhaPraExecucao;
	}

	/** Muda o estado de shuffle, caso shuffle esteja ativado, o desativa, em caso contrario, o ativa.
	 * 
	 */
	public void mudaShuffle(){
		this.shuffle = (this.shuffle + 1) % 2;
		switch(this.shuffle){
		case 0:
			this.botaoShuffle.setToolTipText("Shuffle [off]");
			break;
		case 1:
			this.botaoShuffle.setToolTipText("Shuffle [on]");
			break;
		}
	}

	/** Muda o estado do repetir, caso esteja desligado, é ligado, caso esta ligado, fica em modo repetir tudo, se estiver em repetir tudo, é desligado.
	 * 
	 */
	public void mudaRepetir(){
		this.repetir = (this.repetir + 1) % 3;
		switch(this.repetir){
		case REPETIR_DESLIGADO:
			this.botaoRepetir.setToolTipText("Repetir [off]");
			break;
		case REPETIR_LIGADO:
			this.botaoRepetir.setToolTipText("Repetir [on]");
			break;
		case REPETIR_TUDO:
			this.botaoRepetir.setToolTipText("Repetir Tudo");
			break;
		}
	}

	/** Retorna um objeto com os dados da musica em execucao
	 * @return <code>MusicaDados</code> com os dados da musica em execucao
	 */
	public MusicaDados getMusicaEmExecucao(){
		return this.musicaEmExecucao;
	}

	/** Retorna a linha da musica em execucao
	 * @return <code>int</code> com a linha da musica em execucao
	 */
	public int getLinhaMusicaEmExecucao(){
		return this.linhaMusicaEmExecucao;
	}

	/** Retorna a musica selecionada
	 * @return <code>MusicaDados</code> com a musica selecionada, caso nenhuma musica esteja selecionada, retorna <code>null</code>
	 */
	public MusicaDados getMusicaSelecionada(){
		int linhaSelecionada = -1;
		MusicaDados musicaSelecionada = null;
		linhaSelecionada = this.tabelaPlaylist.getSelectedRow();
		if(linhaSelecionada != -1){
			int coluna = tabelaPlaylist.getColumn(COLUNA_NOME_MUSICAS).getModelIndex();
			musicaSelecionada = ((MusicaDados)this.tabelaPlaylist.getModel().getValueAt(linhaSelecionada, coluna));
		}
		return musicaSelecionada;
	}

	/** Exclui a musica selecionada
	 * 
	 */
	public void excluirMusicaSelecionada(){
		MusicaDados musicaSelecionada = null;
		musicaSelecionada = this.getMusicaSelecionada();
		if(musicaSelecionada != null){
			int linha = this.tabelaPlaylist.getSelectedRow();
			if(this.usuario.removeMusica(musicaSelecionada) != 2){//se excluiu da lista ou da lista e do servidor, exclui da tabela
				this.removeLinhaTabela(linha);
			}
		}
	}

	/** Executa a musica selecionada
	 * 
	 */
	public void executaMusicaSelecionada(){
		MusicaDados musicaSelecionada = this.getMusicaSelecionada();
		if(musicaSelecionada != null){
			executaMusica(musicaSelecionada, this.tabelaPlaylist.getSelectedRow());
		}
	}
	
	/** Reinicia a execucao da musica que estava em execucao
	 * 
	 */
	public void reiniciaMusica(){
		if(this.musicaEmExecucao != null){
			executaMusica(this.musicaEmExecucao, this.linhaMusicaEmExecucao);
		}
	}

	/** Executa uma musica
	 * @param musica <code>MusicaDados</code> com a musica que sera executada
	 * @param linha <code>int</code> com a linha da musica que sera executada
	 * @throws IllegalArgumentException caso o numero da linha seja superior ou igual ao numero de linhas da tabela ou caso a musica seja <code>null</code>
	 */
	public void executaMusica(MusicaDados musica, int linha) throws IllegalArgumentException{
		if(linha >= this.tabelaPlaylist.getRowCount()){
			throw new IllegalArgumentException("parametro linha invalido");
		}
		if(musica == null){
			throw new NullPointerException("parametro musica é nulo");
		}

		if(this.musicaEmExecucao == null || !(this.musicaEmExecucao.getNomeMusica().equals(musica.getNomeMusica()) && this.clienteRTSP.isMusicaRodando() ) ){
			if(this.clienteRTSP != null){
				this.clienteRTSP.stop();
			}
			this.clienteRTSP = new ClienteRTSP(usuario.getUsername(), musica.getNomeMusica(), musica.getTamanhoBytes());
			this.musicaEmExecucao = musica;
			this.linhaMusicaEmExecucao = linha;
			this.tabelaPlaylist.setLinhaEmExecucao(this.getLinhaMusicaEmExecucao()); //muda a cor da linha da musica em execucao
			this.limpaLabelMusicaEmExecucao();
			this.setTextoMusicaEmExecucao(musica.toString() + " - " + musica.getDuracaoMMSS());

			Thread threadClienteRTSP = new Thread(this.clienteRTSP); 
			threadClienteRTSP.start();

			this.barraProgressoMusica.setMaximum(musica.getDuracao() * numeroVezesMaiorNormalBarraProgressoMusica);
			this.barraProgressoMusica.setValue(0);
			if(!this.timerBarraProgressoMusica.isRunning()){
				this.timerBarraProgressoMusica.start();
			}
			this.timerBarraProgressoMusica.restart();
			this.timerLabelMusicaEmExecucao.restart();
		}
		else{
			this.executaMusica(); //da play na musica atual
		}
	}

	/** Seta o texto do letreiro da musica em execucao
	 * @param texto <code>String</code> com o texto da musica em execucao
	 */
	private void setTextoMusicaEmExecucao(String texto){
		this.labelMusicaEmExecucao.setHorizontalAlignment(JLabel.RIGHT);
		final int numeroEspacos = 20;
		for(int i = 0; i < texto.length(); i++){
			this.filaLabelMusicaEmExecucao.offer(texto.charAt(i));
		}
		for(int i = 0; i < numeroEspacos; i++){
			this.filaLabelMusicaEmExecucao.offer(' ');
		}
	}

	/** Atualia o texto da musica em execucao
	 * 
	 */
	private void atualizaLabelMusicaEmExecucao(){
		String textoAtual = this.labelMusicaEmExecucao.getText();
		if(!this.filaLabelMusicaEmExecucao.isEmpty()){
			Character caracterRetirado = this.filaLabelMusicaEmExecucao.poll();
			//this.labelMusicaEmExecucao.setText(caracterRetirado + textoAtual);
			if(textoAtual.length() >= maximoCaracteresLabelMusicaEmExecucao){
				textoAtual = textoAtual.substring(1);
			}
			this.labelMusicaEmExecucao.setText(textoAtual + caracterRetirado);
			this.filaLabelMusicaEmExecucao.offer(caracterRetirado);
		}
	}

	/** Limpa o texto da musica em execucao
	 * 
	 */
	private void limpaLabelMusicaEmExecucao(){
		this.labelMusicaEmExecucao.setText("");
		this.filaLabelMusicaEmExecucao.removeAll(this.filaLabelMusicaEmExecucao);
	}


	/** Executa a musica, caso haja uma musica em execucao a reinicia
	 * 
	 */
	public void executaMusica(){
		if(this.clienteRTSP != null){
			this.barraProgressoMusica.setValue(0);
			this.timerBarraProgressoMusica.restart();
			this.clienteRTSP.play();
		}
	}

	/** Pausa a musica em execucao
	 * 
	 */
	public void pausaMusica(){
		if(this.clienteRTSP != null){
			if(this.timerBarraProgressoMusica.isRunning()){
				this.timerBarraProgressoMusica.stop();
			}
			else{
				this.timerBarraProgressoMusica.start();
			}
			this.clienteRTSP.pause();
		}
	}

	/** Para a musica em execucao
	 * 
	 */
	public void paraMusica(){
		if(this.clienteRTSP != null){
			this.barraProgressoMusica.setValue(0);
			this.timerBarraProgressoMusica.stop();
			this.timerLabelMusicaEmExecucao.stop();
			this.clienteRTSP.stop();
		}
	}

	/** Seta o controle de volume do player
	 * @param controleVolume <code>FloatControl</code> com o controle de volume do sistema
	 */
	public void setControleVolume(FloatControl controleVolume){
		this.controleVolume = controleVolume;
	}

	/* (non-Javadoc)
	 * @see br.java.redes.gui.Janela#addItensPopupMenu()
	 */
	@Override
	protected void addItensPopupMenu() {
		this.sysPopupMenu.setBackground(corFundo);
	}

	/** Retorna a tabela da playlist do player
	 * @return <code>TabelaPlaylist</code> com a tabela da playlist
	 */
	public TabelaPlaylist getTabelaPlaylist() {
		return tabelaPlaylist;
	}

	/** Retorna o botao de play do player
	 * @return <code>BotaoPlayer</code> com botao
	 */
	public BotaoPlayer getBotaoPlay() {
		return botaoPlay;
	}

	/** Retorna o botao de pause do player
	 * @return <code>BotaoPlayer</code> com botao
	 */
	public BotaoPlayer getBotaoPause() {
		return botaoPause;
	}

	/** Retorna o botao de stop do player
	 * @return <code>BotaoPlayer</code> com botao
	 */
	public BotaoPlayer getBotaoStop() {
		return botaoStop;
	}

	/** Retorna o botao de proxima musica do player
	 * @return <code>BotaoPlayer</code> com botao
	 */
	public BotaoPlayer getBotaoProximaMusica() {
		return botaoProximaMusica;
	}

	/** Retorna o botao de musica anterior do player
	 * @return <code>BotaoPlayer</code> com botao
	 */
	public BotaoPlayer getBotaoMusicaAnterior() {
		return botaoMusicaAnterior;
	}

	/** Retorna o botao de abrir musica do player
	 * @return <code>BotaoPlayer</code> com botao
	 */
	public BotaoPlayer getBotaoAbrirMusica() {
		return botaoAbrirMusica;
	}

	/** Retorna o botao de shuffle do player
	 * @return <code>BotaoMenuPlayer</code> com botao
	 */
	public BotaoMenuPlayer getBotaoShuffle() {
		return botaoShuffle;
	}

	/** Retorna o botao de repetir do player
	 * @return <code>BotaoMenuPlayer</code> com botao
	 */
	public BotaoMenuPlayer getBotaoRepetir() {
		return botaoRepetir;
	}

	/** Retorna o botao de pesquisar do player
	 * @return <code>JButton</code> com botao
	 */
	public JButton getBotaoPesquisar() {
		return botaoPesquisar;
	}

	/** Retorna o campo de pesquisa
	 * @return <code>JTextField</code> com o campo de pesquisa
	 */
	public JTextField getCampoPesquisa() {
		return campoPesquisa;
	}

	/** Retorna o cliente RTSP
	 * @return <code>ClienteRTSP</code> com o cliente RTSP
	 */
	public ClienteRTSP getClienteRTSP() {
		return clienteRTSP;
	}

	/* (non-Javadoc)
	 * @see br.java.redes.gui.Janela#addEventoItens()
	 */
	@Override
	protected void addEventoItens() {
		//nenhum item a ser adicionado
	}

}
