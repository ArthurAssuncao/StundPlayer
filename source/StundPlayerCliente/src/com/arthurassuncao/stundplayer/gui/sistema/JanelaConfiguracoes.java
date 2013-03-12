package com.arthurassuncao.stundplayer.gui.sistema;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.arthurassuncao.stundplayer.classes.Configuracoes;
import com.arthurassuncao.stundplayer.eventos.TratadorEventosMouseConfiguracoes;
import com.arthurassuncao.stundplayer.gui.Cor;
import com.arthurassuncao.stundplayer.gui.Janela;
import com.arthurassuncao.stundplayer.gui.LabelRotulo;
import com.arthurassuncao.stundplayer.gui.Painel;

/** Janela com as configuracoes do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 */
public class JanelaConfiguracoes extends Janela {
	private static final long serialVersionUID = 317331445150849351L;
	/** <code>int</code> com a largura padrao da janela */
	public static final int LARGURA = 450;
	/** <code>int</code> com a altura padrao da janela */
	public static final int ALTURA 	= 300;
	
	private int numeroColunasCamposTexto 	= 10;

	//paineis
	private Painel painelTotal;
	//private Painel painelNorte;
	private Painel painelSul;
	private Painel painelDados;
	private Painel painelCentro;

	//Labels
	private LabelRotulo labelIpServidor;
	private LabelRotulo labelPortaServidor;
	private LabelRotulo labelPortaFTP;
	private LabelRotulo labelPortaRTP;
	private LabelRotulo labelPortaRTSP;
	private LabelRotulo labelCorPlayer;
	private LabelRotulo labelCorFundo;
	private JLabel campoCorPlayer;
	private JLabel campoCorFundoPlayer;

	//Campos
	private JTextField campoIpServidor;
	private JTextField campoPortaServidor;
	private JTextField campoPortaFTP;
	private JTextField campoPortaRTP;
	private JTextField campoPortaRTSP;

	//Botoes
	private JButton botaoLimparCampos;
	private JButton botaoSalvar;

	private JButton botaoPadraoIpServidor;
	private JButton botaoPadraoPortaServidor;
	private JButton botaoPadraoPortaFTP;
	private JButton botaoPadraoPortaRTP;
	private JButton botaoPadraoPortaRTSP;
	private JButton botaoPadraoCorPlayer;
	private JButton botaoPadraoCorFundoPlayer;

	/** Cria uma instancia da janela de configuracao
	 * 
	 */
	public JanelaConfiguracoes(){
		super("Configuracoes", LARGURA, ALTURA);

		this.iniciaElementos();

		this.addElementos();

		this.setConfiguracoes();

		this.pack();

		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	/** Inicializa os elementos
	 * 
	 */
	private void iniciaElementos(){
		//cria os paineis
		painelTotal = new Painel();
		painelDados = new Painel(new GridBagLayout());

		painelTotal 	= new Painel(new GridBagLayout());
		//painelNorte 	= new Painel();
		painelSul 		= new Painel(new GridBagLayout());
		painelDados 	= new Painel(new GridBagLayout());
		painelCentro 	= new Painel(new GridBagLayout());

		//inicializa os labels
		labelIpServidor	= new LabelRotulo(String.format("%-20s%-15s", "","IP do Servidor: "));
		labelPortaServidor 	= new LabelRotulo(String.format("%-20s%-15s", "","Porta Servidor: "));
		labelPortaFTP 	= new LabelRotulo(String.format("%-20s%-15s", "","Porta FTP: "));
		labelPortaRTP 	= new LabelRotulo(String.format("%-20s%-15s", "","Porta RTP: "));
		labelPortaRTSP 			= new LabelRotulo(String.format("%-20s%-15s", "", "Porta RTSP: "));
		labelCorPlayer 			= new LabelRotulo(String.format("%-20s%-15s", "", "Cor do Sistema: "));
		labelCorFundo	= new LabelRotulo(String.format("%-20s%-15s", "", "Cor de fundo do Sistema: "));


		//inicializa os campos
		campoIpServidor = new JTextField(numeroColunasCamposTexto);
		campoIpServidor.setToolTipText("Digite o IP do Servidor");

		campoPortaServidor 	= new JTextField(numeroColunasCamposTexto);
		campoPortaServidor.setToolTipText("Digite o Número da porta do Servidor");
		campoPortaServidor.setEditable(false);

		campoPortaFTP 	= new JTextField(numeroColunasCamposTexto);
		campoPortaFTP.setToolTipText("Digite o Número da porta FTP");
		campoPortaFTP.setEditable(false);

		campoPortaRTP 	= new JTextField(numeroColunasCamposTexto);
		campoPortaRTP.setToolTipText("Digite o Número da porta RTP");
		campoPortaRTP.setEditable(false);

		campoPortaRTSP 	= new JTextField(numeroColunasCamposTexto);
		campoPortaRTSP.setToolTipText("Digite o Número da porta RTSP");
		campoPortaRTSP.setEditable(false);

		campoCorPlayer = new JLabel();
		campoCorPlayer.setToolTipText("Cor padrao do player");
		campoCorPlayer.setPreferredSize(new Dimension(50, 20));
		campoCorPlayer.setMinimumSize(new Dimension(50, 20));
		campoCorPlayer.setOpaque(true);
		campoCorPlayer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		campoCorFundoPlayer = new JLabel();
		campoCorFundoPlayer.setToolTipText("Cor de fundo padrao do player");
		campoCorFundoPlayer.setPreferredSize(new Dimension(50, 20));
		campoCorFundoPlayer.setMinimumSize(new Dimension(50, 20));
		campoCorFundoPlayer.setOpaque(true);
		campoCorFundoPlayer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		//Inicializa os Botoes
		botaoLimparCampos = new JButton("Limpar");
		botaoSalvar = new JButton("Salvar");

		botaoPadraoIpServidor = new JButton();
		botaoPadraoPortaServidor = new JButton();
		botaoPadraoPortaFTP = new JButton();
		botaoPadraoPortaRTP = new JButton();
		botaoPadraoPortaRTSP = new JButton();
		botaoPadraoCorPlayer = new JButton();
		botaoPadraoCorFundoPlayer = new JButton();

		botaoLimparCampos.setFocusable(false);
		botaoSalvar.setFocusable(false);

		botaoPadraoIpServidor.setFocusable(false);
		botaoPadraoPortaServidor.setFocusable(false);
		botaoPadraoPortaFTP.setFocusable(false);
		botaoPadraoPortaRTP.setFocusable(false);
		botaoPadraoPortaRTSP.setFocusable(false);
		botaoPadraoCorPlayer.setFocusable(false);
		botaoPadraoCorFundoPlayer.setFocusable(false);

		botaoPadraoIpServidor.setToolTipText("Volta o IP do Servidor Padrao");
		botaoPadraoPortaServidor.setToolTipText("Volta a porta do Servidor Padrao");
		botaoPadraoPortaFTP.setToolTipText("Volta a porta FTP Padrao");
		botaoPadraoPortaRTP.setToolTipText("Volta a porta RTP Padrao");
		botaoPadraoPortaRTSP.setToolTipText("Volta a porta RTSP Padrao");
		botaoPadraoCorPlayer.setToolTipText("Volta a cor padrao do Player");
		botaoPadraoCorFundoPlayer.setToolTipText("Volta a cor de fundo padrao do Player");

		//Adiciona eventos aos botoes
		TratadorEventosMouseConfiguracoes tratadorEventosMouse = new TratadorEventosMouseConfiguracoes(this);
		botaoLimparCampos.addMouseListener(tratadorEventosMouse);
		botaoSalvar.addMouseListener(tratadorEventosMouse);

		botaoPadraoIpServidor.addMouseListener(tratadorEventosMouse);
		botaoPadraoPortaServidor.addMouseListener(tratadorEventosMouse);
		botaoPadraoPortaFTP.addMouseListener(tratadorEventosMouse);
		botaoPadraoPortaRTP.addMouseListener(tratadorEventosMouse);
		botaoPadraoPortaRTSP.addMouseListener(tratadorEventosMouse);
		botaoPadraoCorPlayer.addMouseListener(tratadorEventosMouse);
		botaoPadraoCorFundoPlayer.addMouseListener(tratadorEventosMouse);

		this.campoCorPlayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evento) {
				super.mouseClicked(evento);
				Color novaCor = JColorChooser.showDialog(null, "Cor do sistema", campoCorPlayer.getBackground());
				campoCorPlayer.setBackground(novaCor);
			}
		});

		this.campoCorFundoPlayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evento) {
				super.mouseClicked(evento);
				Color novaCorFundo = JColorChooser.showDialog(null, "Cor de fundo do sistema", campoCorFundoPlayer.getBackground());
				campoCorFundoPlayer.setBackground(novaCorFundo);
			}
		});

	}

	/** Adiciona os elementos a janela
	 * 
	 */
	private void addElementos(){
		//Grid
		GridBagConstraints gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.insets = new Insets(5, 5, 5, 5); //espacos pro GridBadLayout 
		gridBagConstraint.fill = GridBagConstraints.BOTH;  //preenche toda coluna

		//adiciona componentes ao painelTotal
		//painelTotal.add(labelTitulo, BorderLayout.NORTH);
		painelTotal.add(painelDados);

		//adiciona o painelTotal a janela
		this.add(painelTotal);

		//Adiciona ao painelDados
		int linha = 0;
		int coluna = 0;
		//IP Servidor
		gridBagConstraint.gridx = coluna++;
		gridBagConstraint.gridy = linha++;
		painelDados.add(labelIpServidor, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(campoIpServidor, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(botaoPadraoIpServidor, gridBagConstraint);

		//Porta Servidor Principal
		coluna = 0;
		gridBagConstraint.gridx = coluna++;
		gridBagConstraint.gridy = linha++;
		painelDados.add(labelPortaServidor, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(campoPortaServidor, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(botaoPadraoPortaServidor, gridBagConstraint);

		//Porta FTP
		coluna = 0;
		gridBagConstraint.gridx = coluna++;
		gridBagConstraint.gridy = linha++;
		painelDados.add(labelPortaFTP, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(campoPortaFTP, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(botaoPadraoPortaFTP, gridBagConstraint);

		//Porta RTP
		coluna = 0;
		gridBagConstraint.gridx = coluna++;
		gridBagConstraint.gridy = linha++;
		painelDados.add(labelPortaRTP, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(campoPortaRTP, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(botaoPadraoPortaRTP, gridBagConstraint);

		//Porta RTSP
		coluna = 0;
		gridBagConstraint.gridx = coluna++;
		gridBagConstraint.gridy = linha++;
		painelDados.add(labelPortaRTSP, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(campoPortaRTSP, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(botaoPadraoPortaRTSP, gridBagConstraint);

		//cor
		coluna = 0;
		gridBagConstraint.gridx = coluna++;
		gridBagConstraint.gridy = linha++;
		painelDados.add(labelCorPlayer, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(campoCorPlayer, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(botaoPadraoCorPlayer, gridBagConstraint);

		//cor fundo
		coluna = 0;
		gridBagConstraint.gridx = coluna++;
		gridBagConstraint.gridy = linha++;
		painelDados.add(labelCorFundo, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(campoCorFundoPlayer, gridBagConstraint);
		gridBagConstraint.gridx = coluna++;
		painelDados.add(botaoPadraoCorFundoPlayer, gridBagConstraint);

		//Adiciona ao painelCentro
		gridBagConstraint.gridy = 0;
		gridBagConstraint.gridx = 0;
		painelCentro.add(painelDados, gridBagConstraint);

		//Adiciona ao painel Sul
		gridBagConstraint.gridy = 0;
		gridBagConstraint.gridx = 0;
		gridBagConstraint.insets = new Insets(10, 10, 5, 15); //espacos pro GridBadLayout 
		painelSul.add(botaoLimparCampos, gridBagConstraint);
		gridBagConstraint.gridx = 1;
		painelSul.add(botaoSalvar, gridBagConstraint);

		//Adiciona ao painelTotal
		linha = 0;
		gridBagConstraint.insets = new Insets(0, 5, 0, 5); //espacos pro GridBadLayout 
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = linha++;
		painelTotal.add(painelCentro, gridBagConstraint);
		gridBagConstraint.gridy = linha++;
		painelTotal.add(painelSul, gridBagConstraint);

		this.add(painelTotal);
	}

	/** Seta os valores das configuracoes nos campos da janela
	 * 
	 */
	private void setConfiguracoes(){
		Configuracoes configuracoes = Configuracoes.getInstance();
		this.campoIpServidor.setText(configuracoes.getIpServidor());
		this.campoPortaFTP.setText(String.valueOf(configuracoes.getPortaFTP()));
		this.campoPortaServidor.setText(String.valueOf(configuracoes.getPortaServidor()));
		this.campoPortaRTP.setText(String.valueOf(configuracoes.getPortaRTP()));
		this.campoPortaRTSP.setText(String.valueOf(configuracoes.getPortaRTSP()));
		this.campoCorPlayer.setBackground(configuracoes.getCorPlayer());
		this.campoCorFundoPlayer.setBackground(configuracoes.getCorFundoPlayer());

		if(Cor.getBrilho(this.campoCorPlayer.getBackground()) < 130){ //cor clara
			this.campoCorPlayer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		}
		else{
			this.campoCorPlayer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		}

		if(Cor.getBrilho(this.campoCorFundoPlayer.getBackground()) < 130){ //cor clara
			this.campoCorFundoPlayer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		}
		else{
			this.campoCorFundoPlayer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		}
	}

	/** Limpa os campos da janela restaurando os valores atuais
	 * 
	 */
	public void limpaCamposJanela(){
		Configuracoes configuracoes = Configuracoes.getInstance();
		this.campoIpServidor.setText(configuracoes.getIpServidor());
		this.campoPortaFTP.setText(String.valueOf(configuracoes.getPortaFTP()));
		this.campoPortaServidor.setText(String.valueOf(configuracoes.getPortaServidor()));
		this.campoPortaRTP.setText(String.valueOf(configuracoes.getPortaRTP()));
		this.campoPortaRTSP.setText(String.valueOf(configuracoes.getPortaRTSP()));
		this.campoCorPlayer.setBackground(configuracoes.getCorPlayer());
		this.campoCorFundoPlayer.setBackground(configuracoes.getCorFundoPlayer());
		/*this.campoIpServodor.setText("");
		this.campoPortaFTP.setText("");
		this.campoPortaFTP2.setText("");
		this.campoPortaRTP.setText("");
		this.campoPortaRTSP.setText("");
		this.campoCorPlayer.setBackground(Janela.getCorPadraoPlayer());*/
	}

	/** Verifica se os campos estao corretamente preenchidos
	 * @return <code>boolean</code> com <code>true</code> se os campos estao validos e <code>false</code> senao
	 */
	public boolean verificaCampos() {
		String ip = campoIpServidor.getText().trim();
		String ftp = campoPortaFTP.getText().trim();
		String ftp2 = campoPortaFTP.getText().trim();
		String rtp = campoPortaRTP.getText().trim();
		String rtsp = campoPortaRTSP.getText().trim();

		if(ip.isEmpty()){
			this.addError("Campo IP Servidor está vazio");
		}
		if(ftp.isEmpty()){
			this.addError("Campo Porta FTP está vazio");
		}
		if(ftp2.isEmpty()){
			this.addError("Campo Porta FTP2 está vazio");
		}
		if(rtp.isEmpty()){
			this.addError("Campo Porta RTP está vazio");
		}
		if(rtsp.isEmpty()){
			this.addError("Campo Porta RTSP está vazio");
		}
		if(!this.getErros().isEmpty()){
			return false;
		}
		return true;
	}

	/** Retorna o botao limpar campos
	 * @return <code>JButton</code> com o botao limpar campos
	 */
	public JButton getBotaoLimparCampos() {
		return botaoLimparCampos;
	}

	/** Retorna o botao salvar
	 * @return <code>JButton</code> com o botao salvar
	 */
	public JButton getBotaoSalvar() {
		return botaoSalvar;
	}

	/** Retorna o campo com o ip do servidor
	 * @return <code>JTextField</code> com o campo
	 */
	public JTextField getCampoIpServidor() {
		return campoIpServidor;
	}

	/** Retorna o campo com a porta FTP do servidor
	 * @return <code>JTextField</code> com o campo
	 */
	public JTextField getCampoPortaFTP() {
		return campoPortaFTP;
	}

	/** Retorna o campo com a porta do servidor
	 * @return <code>JTextField</code> com o campo
	 */
	public JTextField getCampoPortaServidor() {
		return campoPortaServidor;
	}

	/** Retorna o campo com a porta RTP do servidor
	 * @return <code>JTextField</code> com o campo
	 */
	public JTextField getCampoPortaRTP() {
		return campoPortaRTP;
	}

	/** Retorna o campo com a porta RTSP do servidor
	 * @return <code>JTextField</code> com o campo
	 */
	public JTextField getCampoPortaRTSP() {
		return campoPortaRTSP;
	}

	/** Retorna o campo com a cor do player
	 * @return <code>JLabel</code> com o campo
	 */
	public JLabel getCampoCorPlayer() {
		return campoCorPlayer;
	}

	/** Retorna o campo com a cor de fundo do player
	 * @return <code>JLabel</code> com o campo
	 */
	public JLabel getCampoCorFundoPlayer() {
		return campoCorFundoPlayer;
	}

	/** Retorna o botao padrao do ip do servidor
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoPadraoIpServidor() {
		return botaoPadraoIpServidor;
	}

	/** Retorna o botao padrao da porta do servidor
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoPadraoPortaServidor() {
		return botaoPadraoPortaServidor;
	}

	/** Retorna o botao padrao da porta FTP do servidor
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoPadraoPortaFTP() {
		return botaoPadraoPortaFTP;
	}

	/** Retorna o botao padrao da porta RTP do servidor
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoPadraoPortaRTP() {
		return botaoPadraoPortaRTP;
	}

	/** Retorna o botao padrao da porta RTSP do servidor
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoPadraoPortaRTSP() {
		return botaoPadraoPortaRTSP;
	}

	/** Retorna o botao padrao da cor do player
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoPadraoCorPlayer() {
		return botaoPadraoCorPlayer;
	}

	/** Retorna o botao padrao da cor de fundo do player
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoPadraoCorFundoPlayer() {
		return botaoPadraoCorFundoPlayer;
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
