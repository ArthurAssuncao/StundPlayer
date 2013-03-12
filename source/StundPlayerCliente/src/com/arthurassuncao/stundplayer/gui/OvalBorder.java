package com.arthurassuncao.stundplayer.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

// http://www.java2s.com/Code/Java/Swing-JFC/Ovalborder.htm
/** Classe para criar bordas ovais
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see Border
 */
public class OvalBorder implements Border {
	private int ovalWidth = 6;
	private int ovalHeight = 6;
	private Color lightColor = Color.WHITE;
	private Color darkColor = Color.GRAY;

	/** Cria um borda oval com valores padrao, 6 para a largura e altura
	 * 
	 */
	public OvalBorder() {
		ovalWidth = 6;
		ovalHeight = 6;
	}

	/** Cria um borda oval com largura e altura especificos
	 * @param largura <code>int</code> com a largura da borda
	 * @param altura <code>int</code> com a altura da borda
	 * 
	 */
	public OvalBorder(int largura, int altura) {
		ovalWidth = largura;
		ovalHeight = altura;
	}

	/** Cria um borda oval com largura, altura, cor do topo e cor de baixo especificos
	 * @param largura <code>int</code> com a largura da borda
	 * @param altura <code>int</code> com a altura da borda
	 * @param topColor <code>Color</code> com a cor do topo
	 * @param bottomColor <code>Color</code> com a cor de baixo
	 * 
	 */
	public OvalBorder(int largura, int altura, Color topColor, Color bottomColor) {
		ovalWidth = largura;
		ovalHeight = altura;
		lightColor = topColor;
		darkColor = bottomColor;
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
	 */
	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(ovalHeight, ovalWidth, ovalHeight, ovalWidth);
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#isBorderOpaque()
	 */
	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		width--;
		height--;

		g.setColor(lightColor);
		g.drawLine(x, y + height - ovalHeight, x, y + ovalHeight);
		g.drawArc(x, y, 2 * ovalWidth, 2 * ovalHeight, 180, -90);
		g.drawLine(x + ovalWidth, y, x + width - ovalWidth, y);
		g.drawArc(x + width - 2 * ovalWidth, y, 2 * ovalWidth, 2 * ovalHeight, 90, -90);

		g.setColor(darkColor);
		g.drawLine(x + width, y + ovalHeight, x + width, y + height - ovalHeight);
		g.drawArc(x + width - 2 * ovalWidth, y + height - 2 * ovalHeight, 2 * ovalWidth, 2 * ovalHeight, 0, -90);
		g.drawLine(x + ovalWidth, y + height, x + width - ovalWidth, y + height);
		g.drawArc(x, y + height - 2 * ovalHeight, 2 * ovalWidth, 2 * ovalHeight, -90, -90);
	}
}
