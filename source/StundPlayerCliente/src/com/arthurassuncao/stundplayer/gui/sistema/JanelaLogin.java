package com.arthurassuncao.stundplayer.gui.sistema;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.arthurassuncao.stundplayer.classes.Usuario;
import com.arthurassuncao.stundplayer.eventos.login.TratadorEventosMouseLogin;
import com.arthurassuncao.stundplayer.gui.Fonte;
import com.arthurassuncao.stundplayer.gui.Imagem;
import com.arthurassuncao.stundplayer.gui.Janela;
import com.arthurassuncao.stundplayer.gui.LabelRotulo;
import com.arthurassuncao.stundplayer.gui.Painel;
import com.arthurassuncao.stundplayer.recursos.Recursos;
import com.arthurassuncao.stundplayer.util.Constantes;

/** Janela de login
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see Janela
 */
public class JanelaLogin extends Janela {
	
	private static final long serialVersionUID = 2222120308978565258L;
	private static final int LARGURA = 300;
	private static final int ALTURA = 410;
	
	private LabelRotulo labelUsuario;
	private LabelRotulo labelSenha;
	
	private JTextField campoUsuario;
	private JPasswordField campoSenha;
	
	private JButton botaoLogin;
	private JButton botaoNovaConta;
	
	private ImageIcon imagemLogo;
	private JLabel labelImagemLogo;
	
	private ImageIcon imagemConfiguracoes;
	private ImageIcon imagemConfiguracoesCinza;
	private JLabel labelImagemConfiguracoes;
	
	private JLayeredPane painelFundo;
	private Painel painelDados;
	private Painel painelBotoes;
	
	private TratadorEventosMouseLogin tratadorEventosMouse = new TratadorEventosMouseLogin(this);
	
	/** Cria uma instancia da janela de login
	 * 
	 */
	public JanelaLogin(){
		super(Constantes.PLAYER_NOME, LARGURA, ALTURA);
		
		this.iniciaElementos();
		
		this.addElementos();
		
		this.addEventos();
		
		this.setaUltimoLogin(); //seta o username do ultimo usuario logado
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//this.pack();
		
		this.setLocationRelativeTo(null);
		
		//this.setUndecorated(true); //magica
		//this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		
		this.setVisible(true);
	}
	
	/** Inicializa os elementos da janela
	 * 
	 */
	public void iniciaElementos(){
		this.labelUsuario = new LabelRotulo(" Usuario: ");
		this.labelSenha = new LabelRotulo(" Senha: ");
		
		//this.labelUsuario.setForeground(Janela.getCorPadraoPlayer());
		//this.labelSenha.setForeground(Janela.getCorPadraoPlayer());
		
		this.labelUsuario.setFont(new Fonte(15F).getFont());
		this.labelSenha.setFont(new Fonte(15F).getFont());
		
		this.campoUsuario = new JTextField(12);
		this.campoSenha = new JPasswordField(12);
		
		this.botaoLogin = new JButton("Login");
		this.botaoNovaConta = new JButton("Cria Conta");
		
		this.botaoLogin.setBackground(Janela.getCorPadraoPlayer());
		
		this.botaoLogin.setFocusable(false);
		this.botaoNovaConta.setFocusable(false);
		
		this.botaoLogin.setPreferredSize(new Dimension(80, 32));
		this.botaoNovaConta.setPreferredSize(new Dimension(100, 32));

		
		this.imagemLogo = Janela.getLogoPlayer();
		double escala = 0.50;
		this.imagemLogo.setImage(Imagem.redimensionaImagem(imagemLogo, escala, escala));
		this.labelImagemLogo = new JLabel(this.imagemLogo);
		this.labelImagemLogo.setOpaque(false);
		
		//this.imagemConfiguracoes = new ImageIcon("icones/engrenagem.png");
		this.imagemConfiguracoes = new ImageIcon(Recursos.getResource("icones/engrenagem.png"));
		this.imagemConfiguracoes.setImage(Imagem.redimensionaImagem(imagemConfiguracoes, escala, escala));
		this.imagemConfiguracoesCinza = new ImageIcon(Recursos.getResource("icones/engrenagem_cinza.png"));
		this.imagemConfiguracoesCinza.setImage(Imagem.redimensionaImagem(imagemConfiguracoesCinza, escala, escala));
		this.labelImagemConfiguracoes = new JLabel(this.imagemConfiguracoesCinza);
		this.labelImagemConfiguracoes.setOpaque(false);
		this.labelImagemConfiguracoes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.labelImagemConfiguracoes.setToolTipText("Configuracoes");
		
		//this.painelFundo = new JLayeredPane();
		this.painelFundo = this.getLayeredPane();
		//this.painelFundo.setLayout(this.getLayeredPane().getLayout());
		this.painelFundo.setBackground(Color.BLACK);
		
		this.painelDados = new Painel(new GridBagLayout());
		this.painelBotoes = new Painel(new GridBagLayout());
		
		this.painelDados.setOpaque(false);
		this.painelBotoes.setOpaque(false);
	}
	
	/** Adiciona os elementos a janela
	 * 
	 */
	public void addElementos(){
		//adiciona ao painelDados
		int linha = 0;
		GridBagConstraints grid = new GridBagConstraints();
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(0, 5, 0, 5);
		grid.gridy = linha++;
		painelDados.add(labelUsuario, grid);
		grid.gridy = linha++;
		grid.anchor = GridBagConstraints.CENTER;
		painelDados.add(campoUsuario, grid);
		grid.gridy = linha++;
		grid.anchor = GridBagConstraints.LINE_START;
		painelDados.add(labelSenha, grid);
		grid.gridy = linha++;
		grid.anchor = GridBagConstraints.CENTER;
		painelDados.add(campoSenha, grid);
		
		//adiciona ao painelBotoes
		linha = 0;
		grid.gridx = 0;
		grid.gridy = 0;
		grid.insets = new Insets(0, 5, 10, 5);
		this.painelBotoes.add(botaoLogin, grid);
		grid.gridx = 1;
		this.painelBotoes.add(botaoNovaConta, grid);
		
		
		//adiciona ao painelFundo
		int camada = 0;
		int posicaoAnterior = 0;
		JLabel labelImagemFundo = new JLabel(new ImageIcon(Recursos.getResource("imagens/fundoLogin.png")));
		labelImagemFundo.setBounds(0, 0, LARGURA, ALTURA);
		this.painelFundo.add(labelImagemFundo, new Integer(camada++));
		
		posicaoAnterior += 10;
		this.labelImagemLogo.setBounds( LARGURA / 2 -  this.imagemLogo.getIconWidth() / 2, posicaoAnterior, this.imagemLogo.getIconWidth(), this.imagemLogo.getIconHeight());
		this.painelFundo.add(this.labelImagemLogo, new Integer(camada++));
		
		posicaoAnterior += this.imagemLogo.getIconHeight();
		this.painelDados.setBounds(5, posicaoAnterior, LARGURA, 200);
		this.painelFundo.add(this.painelDados, new Integer(camada++));
		
		posicaoAnterior += 120;
		this.painelBotoes.setBounds(5, posicaoAnterior, LARGURA, 200);
		this.painelFundo.add(this.painelBotoes, new Integer(camada++));
		
		this.labelImagemConfiguracoes.setBounds(LARGURA - this.imagemConfiguracoes.getIconWidth() - 10, 5, this.imagemConfiguracoes.getIconWidth(), this.imagemConfiguracoes.getIconHeight());
		this.painelFundo.add(this.labelImagemConfiguracoes, new Integer(camada++));
		
		//this.add(this.painelFundo);
		this.painelFundo.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}
	
	/** Adiciona eventos aos elementos da janela
	 * 
	 */
	public void addEventos(){
		
		this.botaoLogin.addMouseListener(tratadorEventosMouse);
		this.botaoNovaConta.addMouseListener(tratadorEventosMouse);
		
		this.labelImagemConfiguracoes.addMouseListener(tratadorEventosMouse);
		
		this.campoSenha.addActionListener(tratadorEventosMouse);
		this.campoUsuario.addActionListener(tratadorEventosMouse);
	}

	/** Seta o ultimo usuario logado
	 * @see Usuario
	 */
	public void setaUltimoLogin(){
		Usuario usuario = null;
		try {
			usuario = Usuario.recuperaUltimoUsuario();
		}
		catch (IOException e) {
			//e.printStackTrace();
			//nao seta nada
		}
		if(usuario != null){
			this.campoUsuario.setText(usuario.getUsername());
		}
		
	}

	/** Retorna o botao de login
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoLogin() {
		return botaoLogin;
	}

	/** Retorna o botao nova conta
	 * @return <code>JButton</code> com o botao
	 */
	public JButton getBotaoNovaConta() {
		return botaoNovaConta;
	}
	
	/** Retorna o nome de usuario informado
	 * @return <code>String</code> com o username
	 */
	public String getUsername(){
		return this.campoUsuario.getText();
	}
	
	/** Retorna a senha do usuario
	 * @return <code>String</code> com a senha
	 */
	public String getSenha(){
		return new String(this.campoSenha.getPassword());
	}
	
	/**
	 * @return the labelImagemConfiguracoes
	 */
	public JLabel getLabelImagemConfiguracoes() {
		return labelImagemConfiguracoes;
	}

	/** Retorna a imagem de configuracoes
	 * @return <code>ImageIcon</code> com a imagem
	 */
	public ImageIcon getImagemConfiguracoes() {
		return imagemConfiguracoes;
	}

	/** Retorna a imagem de configuracoes cinza
	 * @return <code>ImageIcon</code> com a imagem
	 */
	public ImageIcon getImagemConfiguracoesCinza() {
		return imagemConfiguracoesCinza;
	}
	
	/** Retorna o campo de texto Usuario
	 * @return <code>JTextField</code> com o campo
	 */
	public JTextField getCampoUsuario() {
		return campoUsuario;
	}

	/** Retorna o campo de texto Senha
	 * @return <code>JTextField</code> com o campo
	 */
	public JPasswordField getCampoSenha() {
		return campoSenha;
	}

	/** Verifica se os campos sao validos
	 * @return <code>boolean</code> com <code>true</code> se os campos estao validos e <code>false</code> senao
	 */
	public boolean verificaCampos(){
		if(this.campoUsuario.getText().trim().isEmpty()){
			this.addError("Informe o usuario");
		}
		if(this.getSenha().trim().isEmpty()){
			this.addError("Informe a senha");
		}
		if(!this.getErros().isEmpty()){
			return false;
		}
		//else
		return true;
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
		//nenhum evento a ser adicioando
	}
	
}
