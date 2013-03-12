package com.arthurassuncao.stundplayer.gui.player;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

import com.arthurassuncao.stundplayer.gui.Janela;

/** Botao do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * 
 * @see JButton
 */
//classe baseada em : http://www.jroller.com/DhilshukReddy/entry/customizing_jbuttons
public class BotaoPlayer extends JButton {
	private static final long serialVersionUID = 8325047424722406014L;

	private Cursor cursorMao = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	private Color corInicial = Janela.getCorPadraoPlayer().brighter(); //cor do gradiente, cor inicial, cima
	private Color corFinal = Janela.getCorPadraoPlayer(); //cor do gradiente, cor final, baixo
	private Color corRollOver = Janela.getCorPadraoPlayer().brighter(); //cor ao passar o mouse por cima
	private Color corPressionado = Janela.getCorPadraoPlayer().darker(); //cor ao pressionar
	private Color corTexto = new Color(150, 150, 150); //cinza
	private int outerRoundRectSize = 25; //arrendondamento da parte de baixo(cantos sudeste e sudoeste)
	private int innerRoundRectSize = 25; //arrendondamento da parte de cima(cantos nordeste e noroeste)
	private GradientPaint gradientPaint;

	/** Cria uma instancia do botao
	 *
	 */ 
	public BotaoPlayer(){
		super();

		int largura = 25;
		int altura = 25;

		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setCursor(cursorMao);

		this.setFocusable(false);

		this.setPreferredSize(new Dimension(largura , altura));

		this.setForeground(corTexto);
	} 

	/** Cria uma instancia do botao com imagem especifica
	 * @param imagem <code>Icon</code> com a imagem do botao
	 *
	 */ 
	public BotaoPlayer(Icon imagem){
		super();

		int largura = 25;
		int altura = 25;

		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setCursor(cursorMao);
		this.setIcon(imagem);

		this.setFocusable(false);

		this.setPreferredSize(new Dimension(largura , altura));

		this.setForeground(corTexto);
	} 

	/** Cria uma instancia do botao com altura e largura especifica
	 * @param largura <code>int</code> com a largura do botao
	 * @param altura <code>int</code> com a altura do botao
	 */
	public BotaoPlayer(int largura, int altura){
		super();

		if(largura > altura){
			this.outerRoundRectSize = largura;
			this.innerRoundRectSize = largura;
		}
		else{
			this.outerRoundRectSize = altura;
			this.innerRoundRectSize = altura;
		}

		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setCursor(cursorMao);

		this.setFocusable(false);

		this.setPreferredSize(new Dimension(largura , altura));

		this.setForeground(corTexto);
	} 

	/** Cria uma instancia do botao com altura, largura e imagem especifica
	 * @param largura <code>int</code> com a largura do botao
	 * @param altura <code>int</code> com a altura do botao
	 * @param imagem <code>Icon</code> com a imagem do botao
	 */
	public BotaoPlayer(int largura, int altura, Icon imagem){
		super();

		if(largura > altura){
			this.outerRoundRectSize = largura;
			this.innerRoundRectSize = largura;
		}
		else{
			this.outerRoundRectSize = altura;
			this.innerRoundRectSize = altura;
		}

		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setCursor(cursorMao);
		this.setIcon(imagem);

		this.setFocusable(false);

		this.setPreferredSize(new Dimension(largura , altura));

		this.setForeground(corTexto);
	} 

	/** Cria uma instancia do botao com altura, largura e imagem especifica
	 * @param largura <code>int</code> com a largura do botao
	 * @param altura <code>int</code> com a altura do botao
	 * @param texto <code>String</code> com o texto do botao
	 * @param arredondamento <code>int</code> com o arredondamento superior e inferior do botao
	 */
	public BotaoPlayer(int largura, int altura, String texto, int arredondamento){
		super();
		if(arredondamento >= 0){
			this.outerRoundRectSize = arredondamento;
			this.innerRoundRectSize = arredondamento;
		}

		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setCursor(cursorMao);
		this.setText(texto);

		this.setFocusable(false);

		this.setPreferredSize(new Dimension(largura , altura));

		this.setForeground(corTexto);
	} 


	/* Metodo para desenhar o botao
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int h = getHeight();
		int w = getWidth();
		ButtonModel model = getModel();
		if (!model.isEnabled()){
			setForeground(Color.GRAY);
			gradientPaint = new GradientPaint(0, 0, new Color(192,192,192), 0, h, new Color(192,192,192), true);
		}
		else{
			setForeground(corTexto);
			if (model.isRollover()){
				gradientPaint = new GradientPaint(0, 0, corRollOver, 0, h, corRollOver, true);
			}
			else {
				gradientPaint = new GradientPaint(0, 0, corInicial, 0, h, corFinal, true);
			}
		}
		g2d.setPaint(gradientPaint);
		GradientPaint p1;
		GradientPaint p2;
		if (model.isPressed()){
			gradientPaint = new GradientPaint(0, 0, corPressionado, 0, h, corPressionado, true);
			g2d.setPaint(gradientPaint);
			p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1, new Color(100, 100, 100));
			p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3, new Color(255, 255, 255, 100)); 
		}
		else {
			p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, h - 1, new Color(0, 0, 0));
			p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, h - 3, new Color(0, 0, 0, 50));
			gradientPaint = new GradientPaint(0, 0, corInicial, 0, h, corFinal, true);
		}
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, outerRoundRectSize, outerRoundRectSize);
		Shape clip = g2d.getClip();
		g2d.clip(r2d);
		g2d.fillRect(0, 0, w, h);
		g2d.setClip(clip);
		g2d.setPaint(p1);
		g2d.drawRoundRect(0, 0, w - 1, h - 1, outerRoundRectSize, outerRoundRectSize);
		g2d.setPaint(p2);
		g2d.drawRoundRect(1, 1, w - 3, h - 3, innerRoundRectSize, innerRoundRectSize);
		g2d.dispose();

		super.paintComponent(g);
	}
} 