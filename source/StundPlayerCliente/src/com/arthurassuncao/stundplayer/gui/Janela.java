package com.arthurassuncao.stundplayer.gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.arthurassuncao.stundplayer.classes.Configuracoes;
import com.arthurassuncao.stundplayer.recursos.Recursos;
import com.arthurassuncao.stundplayer.util.Constantes;

/** Janela abstrata para todas as janelas do sistema
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see JFrame
 */
public abstract class Janela extends JFrame{

	private static final long serialVersionUID = 7509794627595480563L;
	private int largura 	= 800;
	private int altura 		= 600;
	private StringBuilder erros = new StringBuilder(0); //variavel para os erros

	private static Color corPadraoPlayer = Configuracoes.getInstance().getCorPlayer();
	private static Color corFundoPadraoPlayer = Configuracoes.getInstance().getCorFundoPlayer();

	/** <code>String</code> com o endereco relativo do icone das janelas */
	protected static final String ICONE 	= "imagens/icon_16x16.png";
	private final Image IMAGEM_ICONE = new ImageIcon(Recursos.getResource(ICONE)).getImage();
	private static final String ENDERECO_LOGO_PLAYER = "imagens/logo_95_opaco.png";

	/** <code>Image</code> com o icone das janelas */
	protected Image icone = new ImageIcon(Recursos.getResource(Janela.ICONE)).getImage();
	/** <code>SystemTray</code> com o systemTray das janelas */
	protected SystemTray systemTray 	= SystemTray.getSystemTray();
	/** <code>JMenuItem</code> com o item restaurar do menuPopUp do minimizar das janelas */
	protected JMenuItem restaurar     		= new JMenuItem("Restaurar");
	/** <code>JMenuItem</code> com o item sair do menuPopUp do minimizar das janelas */
	protected JMenuItem sair      				= new JMenuItem("Sair");
	/** <code>JPopupMenu</code> com o menuPopUp do minimizar das janelas */
	protected JPopupMenu sysPopupMenu = new JPopupMenu();
	/** <code>TrayIcon</code> com o trayIcon do minimizar das janelas */
	protected TrayIcon trayIcon  				= new TrayIcon(icone, Constantes.PLAYER_NOME, null);
	/** {@code List<Frame>} com a lista de janelas abertas*/
	protected List<Frame> listaJanelasAbertas = new ArrayList<Frame>();

	/** Construtor com as principais caracteristicas das janelas do sistema, com titulo especifico, o tamanho(largura e altura) são os padrão da janela, 800 e 600, respectivamente
	 * @param titulo <code>String</code> com o titulo da janela
	 * @see Fonte
	 */
	public Janela(String titulo) {
		super();
		this.setTitle(titulo);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setSize(this.largura, this.altura);
		this.setFont(Fonte.FONTE_NORMAL);

		this.setBackground(Color.WHITE);
		this.setIconImage(IMAGEM_ICONE);

		this.setLocationRelativeTo(null);
		this.setResizable(false);
		//this.setVisible(true);
		this.adicionarListeners();
	}

	/** Construtor com as principais caracteristicas das janelas do sistema, usando o titulo, largura e altura especificos
	 * @param titulo <code>String</code> com o titulo da janela
	 * @param largura <code>int</code> com a largura da janela
	 * @param altura <code>int</code> com a altura da janela
	 * @see Fonte
	 */
	public Janela(String titulo, int largura, int altura) {
		super();
		this.setTitle(titulo);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.largura = largura;
		this.altura = altura;
		this.setSize(largura, altura);
		//this.setResizable(false);
		this.setFont(Fonte.FONTE_NORMAL);

		this.setBackground(Color.WHITE);
		this.setIconImage(IMAGEM_ICONE);

		this.setLocationRelativeTo(null);
		this.setResizable(false);
		//this.setVisible(true);
		this.adicionarListeners();

		JFrame.setDefaultLookAndFeelDecorated(true);
		this.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				Janela.this.atualizaInterface();
			}
		});

	}

	/** Construtor com as principais caracteristicas das janelas do sistema, usando o titulo, largura e altura especificos e um <code>JMenuBar</code>
	 * @param titulo <code>String</code> com o titulo da janela
	 * @param largura <code>int</code> com a largura da janela
	 * @param altura <code>int</code> com a altura da janela
	 * @param menuBar <code>JMenuBar</code> com os menus da janela
	 * @see Fonte
	 */
	public Janela(String titulo, int largura, int altura, JMenuBar menuBar) {
		super();
		this.setTitle(titulo);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.largura = largura;
		this.altura = altura;
		this.setSize(largura, altura);
		this.setResizable(false);
		this.setJMenuBar(menuBar);
		this.setFont(Fonte.FONTE_NORMAL);

		this.setBackground(Color.WHITE);
		this.setIconImage(IMAGEM_ICONE);

		this.setLocationRelativeTo(null);
		this.setResizable(false);
		//this.setVisible(true);
		this.adicionarListeners();
	}

	/** Retorna os erros, normalmente sao erros no preenchimento de formularios da janela
	 * @return <code>String</code> com os erros
	 */
	public String getErros() {
		return this.erros.toString();
	}

	/** Apaga todos os erros da janela
	 * 
	 */
	public void removeErros(){
		this.erros.delete(0, this.erros.length()); //apaga o conteudo da variavel erros
		this.erros.setLength(0);
	}

	/** Adiciona um erro aos erros da janela
	 * @param erro <code>String</code> com o erro
	 */
	public void addError(String erro){
		this.erros.append(erro + "\n");
	}

	/** Janela para abrir arquivos
	 * @param componentePai <code>Component</code> sobre o qual esta janela será aberta, a qual esta sera "filha"
	 * @param titulo <code>String</code> com o titulo da janela
	 * @param diretorioCorrente <code>String</code> com o diretorio onde a janela de abrir inicializara, pode ser <code>null</code>
	 * @param opcaoTodosArquivos <code>boolean</code> informando se a opcao(filtro) Todos Arquivos será mostrada ou não
	 * @param nomeFiltro <code>String</code> com o nome do filtro de extensões
	 * @param extensao <code>String...</code> com as extensões usadas no filtro
	 * @return <code>String</code> com o endereço do arquivo selecionado, caso nenhum arquivo seja selecionado, é retornado <code>null</code>
	 * @see  JFileChooser
	 */
	public static String janelaAbrirArquivo(Component componentePai, String titulo, String diretorioCorrente, boolean opcaoTodosArquivos, String nomeFiltro, String... extensao){
		JFileChooser janelaAbrir = new JFileChooser(titulo);

		janelaAbrir.setAcceptAllFileFilterUsed(false);
		janelaAbrir.setDialogType(JFileChooser.OPEN_DIALOG);
		janelaAbrir.setFileSelectionMode(JFileChooser.FILES_ONLY);
		janelaAbrir.setDialogTitle(titulo);
		janelaAbrir.setFont(new Fonte().getFont());
		janelaAbrir.setFileFilter(new FileNameExtensionFilter(nomeFiltro, extensao));
		janelaAbrir.setAcceptAllFileFilterUsed(opcaoTodosArquivos);

		if(diretorioCorrente != null){
			if (diretorioCorrente.isEmpty()){
				janelaAbrir.setCurrentDirectory(new File("."));
			}
			else{
				janelaAbrir.setCurrentDirectory(new File(diretorioCorrente));
			}
		}

		janelaAbrir.showOpenDialog(componentePai);

		return janelaAbrir.getSelectedFile() != null ? janelaAbrir.getSelectedFile().getPath() : null;
	}

	/** Janela para selecionar um diretorio
	 * @param componentePai <code>Component</code> sobre o qual esta janela será aberta, a qual esta sera "filha"
	 * @param titulo <code>String</code> com o titulo da janela
	 * @param diretorioCorrente <code>String</code> com o diretorio onde a janela de abrir inicializara
	 * @return <code>String</code> com o endereço do diretorio selecionado, caso nenhum diretorio seja selecionado, é retornado <code>null</code>
	 * @see  JFileChooser 
	 */
	public static String janelaAbrirDiretorio(Component componentePai, String titulo, String diretorioCorrente){
		JFileChooser janelaAbrir = new JFileChooser(titulo);

		janelaAbrir.setAcceptAllFileFilterUsed(false);
		janelaAbrir.setDialogType(JFileChooser.OPEN_DIALOG);
		janelaAbrir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		janelaAbrir.setDialogTitle(titulo);
		janelaAbrir.setFont(new Fonte().getFont());

		if(diretorioCorrente != null){
			if (diretorioCorrente.isEmpty()){
				janelaAbrir.setCurrentDirectory(new File("."));
			}
			else{
				janelaAbrir.setCurrentDirectory(new File(diretorioCorrente));
			}
		}

		janelaAbrir.showOpenDialog(componentePai);

		return janelaAbrir.getSelectedFile() != null ? janelaAbrir.getSelectedFile().getPath() : null;
	}

	/** Janela para salvar um arquivo
	 * @param componentePai <code>Component</code> sobre o qual esta janela será aberta, a qual esta sera "filha"
	 * @param titulo <code>String</code> com o titulo da janela
	 * @param diretorioCorrente <code>String</code> com o diretorio onde a janela de abrir inicializara
	 * @param opcaoTodosArquivos <code>boolean</code> informando se a opcao(filtro) Todos Arquivos será mostrada ou não
	 * @param nomeFiltro <code>String</code> com o nome do filtro de extensões
	 * @param extensao <code>String...</code> com as extensões usadas no filtro
	 * @return <code>String</code> com o endereço onde o arquivo será salvo, caso nenhum arquivo seja selecionado, é retornado <code>null</code>
	 * @see  JFileChooser 
	 */
	public static String janelaSalvarArquivo(Component componentePai, String titulo, String diretorioCorrente, boolean opcaoTodosArquivos, String nomeFiltro, String... extensao){
		JFileChooser janelaSalvar = new JFileChooser(titulo);

		janelaSalvar.setAcceptAllFileFilterUsed(false);
		janelaSalvar.setDialogType(JFileChooser.SAVE_DIALOG);
		janelaSalvar.setFileSelectionMode(JFileChooser.FILES_ONLY);
		janelaSalvar.setDialogTitle(titulo);
		janelaSalvar.setFont(new Fonte().getFont());
		janelaSalvar.setFileFilter(new FileNameExtensionFilter(nomeFiltro, extensao));
		janelaSalvar.setAcceptAllFileFilterUsed(opcaoTodosArquivos);

		if(diretorioCorrente != null){
			if (diretorioCorrente.isEmpty()){
				janelaSalvar.setCurrentDirectory(new File("."));
			}
			else{
				janelaSalvar.setCurrentDirectory(new File(diretorioCorrente));
			}
		}

		janelaSalvar.showSaveDialog(componentePai);

		return janelaSalvar.getSelectedFile() != null ? janelaSalvar.getSelectedFile().getPath() : null;
	}

	//SYSTRAY
	/** Adiciona o icone da bandeja (SystemTray) à janela. Ao minimizar uma janela todas as janelas são escondidas e é exibido um icone no SystemTray do
	 * Sistema de gerenciamento de janelas do sistema operacional. No icone há dois menus Restaurar e Sair, o primeiro restaura as janelas, já o segundo fecha o programa.
	 * @see SystemTray
	 * @see TrayIcon
	 */
	private final void addSystemTray() {
		//trayIcon.setToolTip(this.getTitle());
		if (SystemTray.isSupported()) {
			this.addEventoTrayIcon();
			this.addItensPrincipaisPopupMenu();
			this.addEventoItensPrincipais();

			this.addItensPopupMenu();
			this.addEventoItens();

			try {
				systemTray.add(trayIcon);
				//setVisible(false);
				this.dispose();
				//this.listaJanelasAbertas.removeAll(this.listaJanelasAbertas); //limpa a lista
				Frame[] janelas = JFrame.getFrames();
				for(Frame janela : janelas){
					if(janela != this){
						if(!janela.getTitle().equalsIgnoreCase("PopupMessageWindow") && !janela.getTitle().equalsIgnoreCase("")){
							if(janela.isVisible()){ //pega so as janelas visiveis
								this.listaJanelasAbertas.add(janela); //guarda as referencias
								janela.setVisible(false);
							}
						}
					}
				}
			}
			catch(AWTException e) {
				JanelaMensagem.mostraMensagemErro(this, "System Tray Não Suportado");
			}

		}
	}

	/** Adiciona o evento do menu popup ao trayicon 
	 * 
	 */
	private final void addEventoTrayIcon(){
		trayIcon.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent evento){
				if (evento.isPopupTrigger()){
					sysPopupMenu.setLocation(evento.getX(), evento.getY());
					sysPopupMenu.setInvoker(sysPopupMenu);
					sysPopupMenu.setVisible(true);
				}
				else{
					if (evento.getButton() == MouseEvent.BUTTON1){ //botao esquerdo
						if(evento.getClickCount() == 2){
							restauraJanelas();
						}
					}
				}
			}
		});
	}

	//adiciona os itens do menuPopup
	/** Adiciona os itens principais do menuPopup
	 * 
	 */
	private final void addItensPrincipaisPopupMenu(){
		sysPopupMenu.add(restaurar);
		sysPopupMenu.add(sair);
	}

	//adiciona eventos aos itens do menuPopup
	/** Adiciona os eventos dos itens principais
	 * 
	 */
	private final void addEventoItensPrincipais(){
		sair.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				System.exit(0);
			}
		});
		restaurar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				Janela.this.restauraJanelas();
			}
		});
	}

	/** Restaura as janelas que foram minimizadas
	 * 
	 */
	private void restauraJanelas(){
		SystemTray.getSystemTray().remove(trayIcon);
		this.setVisible(true);
		this.setExtendedState(Frame.NORMAL);
		/*Frame[] janelas = JFrame.getFrames();
		for(Frame janela : janelas){
			if(!janela.getTitle().equalsIgnoreCase("PopupMessageWindow") && !janela.getTitle().equalsIgnoreCase("")){ //nao deixa a janela PopupMessageWindow aparecer
				janela.setVisible(true);
			}
			else{
				janela.dispose();
			}
		}*/
		for(Frame janela : Janela.this.listaJanelasAbertas){
			janela.setVisible(true);
		}
		this.listaJanelasAbertas.removeAll(Janela.this.listaJanelasAbertas); //limpa a lista
	}

	//adiciona outros itens ao popupMenu, as janelas filho podem sobreescreve-lo
	/** Permite adicao de outros itens ao popupMenu
	 * 
	 */
	protected abstract void addItensPopupMenu();

	//adiciona eventos aos outros itens ao popupMenu, as janelas filho podem sobreescreve-lo
	/** Permite adicao de eventos dos itens do popupMenu
	 * 
	 */
	protected abstract  void addEventoItens();

	/** Adiciona tratador de eventos aos componentes da janela e à janela
	 * @see WindowStateListener
	 */
	/** Adiciona os listeners a janela
	 * 
	 */
	private final void adicionarListeners(){
		addWindowStateListener(new WindowStateListener() {

			@Override
			public void windowStateChanged(WindowEvent evento) {
				int estadoAntigo = evento.getOldState();
				int estado = evento.getNewState();

				if (estado == JFrame.ICONIFIED && estadoAntigo == JFrame.NORMAL){ //foi esta minimizada
					addSystemTray();
				}
			}
		});
	}

	/** Seta a cor padrao do player
	 * @param novaCorPadrao <code>Color</code> com a nova cor padrao
	 * @see Color
	 */
	public static void setCorPadraoPlayer(Color novaCorPadrao) {
		Janela.corPadraoPlayer = novaCorPadrao;
	}

	/** Retorna a cor padrao do player
	 * @return <code>Color</code> com a cor padrao do player
	 * @see Color
	 */
	public static Color getCorPadraoPlayer() {
		return Janela.corPadraoPlayer;
	}

	/** Seta a cor de fundo padrao do player
	 * @param novaCorFundoPadrao <code>Color</code> com a nova cor de fundo padrao
	 * @see Color
	 */
	public static void setCorFundoPadraoPlayer(Color novaCorFundoPadrao) {
		Janela.corFundoPadraoPlayer = novaCorFundoPadrao;
	}

	/** Retorna a cor de fundo padrao do player
	 * @return <code>Color</code> com a cor de fundo padrao do player
	 * @see Color
	 */
	public static Color getCorFundoPadraoPlayer() {
		return Janela.corFundoPadraoPlayer;
	}

	/** Retorna a imagem do logo do player
	 * @return <code>ImageIcon</code> com o logo do player
	 * @see ImageIcon
	 */
	public static ImageIcon getLogoPlayer(){
		ImageIcon logo = null;
		try {
			logo = Imagem.mudaCor(Janela.ENDERECO_LOGO_PLAYER, new Color(249, 127, 16), corPadraoPlayer);
		}
		catch (IOException e) {
			e.printStackTrace();
			if(logo == null){
				logo = new ImageIcon(Janela.ENDERECO_LOGO_PLAYER);
			}
		}
		return logo;
	}

	/** Repinta todos os frames associados ao sistema
	 * 
	 */
	public static void repintaTodasJanelas(){
		Frame[] frames = JFrame.getFrames();
		for(Frame frame : frames){
			//frame.revalidate();
			frame.repaint();
		}
	}

	/** Atualiza a interface da janela
	 * 
	 */
	public void atualizaInterface(){  
		try {  
			SwingUtilities.updateComponentTreeUI(this);
			this.repaint();
		}
		catch(Exception e) {  
			e.printStackTrace();  
		}  
	}

}
