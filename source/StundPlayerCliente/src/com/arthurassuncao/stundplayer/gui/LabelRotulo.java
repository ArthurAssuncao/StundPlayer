package com.arthurassuncao.stundplayer.gui;

import javax.swing.Icon;
import javax.swing.JLabel;

/** Um <code>LabelRotulo</code> pode exibir texto, imagem ou os dois. Com a fonte da classe <code>Fonte</code>.
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see JLabel
 */
public class LabelRotulo extends JLabel {

	/** @serial
	 * 
	 */
	private static final long serialVersionUID = -3370430013421476281L;
	{
		this.setForeground(Janela.getCorPadraoPlayer());
	}
	
	/** Cria um <code>LabelRotulo</code> sem uma imagem e com o texto vazio no titulo.
	 * 
	 */
	public LabelRotulo() {
		super();
		this.setFont(Fonte.FONTE_NORMAL);
	}

	/** Cria um <code>LabelRotulo</code> com texto especifico.
	 * @param texto <code>String</code> com o texto do label.
	 */
	public LabelRotulo(String texto) {
		super(texto);
		this.setFont(Fonte.FONTE_NORMAL);
	}

	/** Cria um <code>LabelRotulo</code> com imagem especifico.
	 * @param imagem <code>Icon</code> com a imagem do label.
	 */
	public LabelRotulo(Icon imagem) {
		super(imagem);
		this.setFont(Fonte.FONTE_NORMAL);
	}
	
}
