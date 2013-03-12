package com.arthurassuncao.stundplayer.gui;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/** Classe para os paineis dos interfaces graficas do sistema.
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see JPanel
 */
public class Painel extends JPanel{

	private static final long serialVersionUID = -4672547415570830951L;
	private Color corFundo = Janela.getCorFundoPadraoPlayer();
	//bloco inicializador
	/** Bloco inicializador, executado após a execucao do construtor da superclasse e antes do construtor desta classe
	 * 
	 */
	{
		this.setBackground(corFundo);
		this.setFont(Fonte.FONTE_NORMAL);
	}
	
	/** Cria uma instancia do <code>Painel</code> com fundo branco e fonte normal da classe <code>Fonte</code>
	 * 
	 */
	public Painel(){
		super();
	}
	/** Cria uma instancia do <code>Painel</code> com fundo branco e fonte normal da classe <code>Fonte</code> e com uma cor de fundo especifica.
	 * @param cor <code>Color</code> com a cor de fundo do painel
	 * 
	 */
	public Painel(Color cor){
		super();
		this.setBackground(cor);
	}
	/** Cria uma instancia do <code>Painel</code> com fundo branco e fonte normal da classe <code>Fonte</code> e com layout especifico.
	 * @param layout <code>LayoutManager</code> com o layout do painel.
	 * 
	 */
	public Painel(LayoutManager layout){
		super();
		this.setLayout(layout);
	}
	/** Cria uma instancia do <code>Painel</code> com fundo branco e fonte normal da classe <code>Fonte</code> e com layout e cor especifico.
	 * @param cor  <code>Color</code> com a cor de fundo do painel.
	 * @param layout  <code>LayoutManager</code> com o layout do painel.
	 * 
	 */
	public Painel(Color cor, LayoutManager layout){
		super();
		this.setBackground(cor);
		this.setLayout(layout);
	}
}

