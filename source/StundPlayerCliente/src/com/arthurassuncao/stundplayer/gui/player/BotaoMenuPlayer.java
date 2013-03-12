package com.arthurassuncao.stundplayer.gui.player;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;

import com.arthurassuncao.stundplayer.classes.Configuracoes;

/** Botao do menu do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see JButton
 */
public class BotaoMenuPlayer extends JButton {

	private static final long serialVersionUID = -7483470872289594611L;
	//private Color corFundo = new Color(50, 50, 50);
	private Color corFundo = Configuracoes.getInstance().getCorFundoPlayer().brighter();
	private Insets espacamento = new Insets(0, 0, 4, 0);
	
	/** Cria uma instancia do botao de menu do player
	 * 
	 */
	public BotaoMenuPlayer(){
		super();
		this.setFocusable(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.setPreferredSize(new Dimension(15, 15));
		this.setBackground(corFundo);
		this.setMargin(espacamento);
	}
	
	/** Cria uma instancia do botao de menu do player com texto especifico
	 * @param texto <code>String</code> com o texto do botao
	 * 
	 */
	public BotaoMenuPlayer(String texto){
		super();
		this.setFocusable(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.setPreferredSize(new Dimension(15, 15));
		this.setBackground(corFundo);
		this.setMargin(espacamento);
		if(texto != null){
			this.setText(texto);
		}
		else{
			throw new NullPointerException("Texto é null");
		}
	}
	
	/** Cria uma instancia do botao de menu do player com uma imagem especifica
	 * @param imagem <code>Icon</code> com a imagem do botao
	 * 
	 */
	public BotaoMenuPlayer(Icon imagem){
		super();
		this.setFocusable(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.setPreferredSize(new Dimension(15, 15));
		this.setBackground(corFundo);
		this.setMargin(new Insets(0, 0, 0, 0));
		if(imagem != null){
			this.setIcon(imagem);
		}
		else{
			throw new NullPointerException("Imagem é null");
		}
	}
	
}