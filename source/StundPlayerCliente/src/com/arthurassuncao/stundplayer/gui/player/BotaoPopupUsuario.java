package com.arthurassuncao.stundplayer.gui.player;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/** Botao com menu Popup e nome do usuario 
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see JButton
 */
public class BotaoPopupUsuario extends JButton {

	private static final long serialVersionUID = -7483470872289594611L;

	private JanelaPlayer janela;
	// Popup menu.
	private JPopupMenu menuPopup;
	
	private JMenuItem itemEnviarMusica;
	private JMenuItem itemDeletarConta;
	private JMenuItem itemSair;

	// Last time the popup closed.
	private long timeLastShown = 100;

	/** Cria uma instancia do botao com janela do player, largura e altura especificos
	 * @param janela <code>JanelaPlayer</code> com a janela do player
	 * @param largura <code>int</code> com a largura do botao
	 * @param altura <code>int</code> com a altura do botao
	 * @throws IllegalArgumentException caso a largura ou altura sejam menores ou iguais a 0
	 */
	public BotaoPopupUsuario(JanelaPlayer janela, int largura, int altura) throws IllegalArgumentException{
		super();
		if(largura <= 0){
			throw new IllegalArgumentException("Parametro Largura nao pode ser menor ou igual a 0");
		}
		else if(altura <= 0){
			throw new IllegalArgumentException("Parametro Altura nao pode ser menor ou igual a 0");
		}
		this.setPreferredSize(new Dimension(largura, altura));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		this.janela = janela;
		this.menuPopup = new JPopupMenu();
		
		itemEnviarMusica = new JMenuItem("Enviar Musica");
		itemDeletarConta = new JMenuItem("Deletar Conta");
		itemSair = new JMenuItem("Sair");
		
		menuPopup.add(itemEnviarMusica);
		menuPopup.add(itemDeletarConta);
		menuPopup.add(itemSair);

		// Show and hide popup on left click.
		menuPopup.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent evento) {
				timeLastShown = System.currentTimeMillis();
			}
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent evento){
				//abre menu popup
			}
			@Override
			public void popupMenuCanceled(PopupMenuEvent evento){
				//fecha menu popup
			}
		});
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((System.currentTimeMillis() - timeLastShown) > 300) {
					Component c = (Component) e.getSource();
					menuPopup.show(c, -1, c.getHeight());
				}
			}
		});

		this.addEventosItens();
	}
	
	/** Adiciona os eventos dos itens do menu popup
	 * 
	 */
	private void addEventosItens(){
		this.itemEnviarMusica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				janela.abrirMusica();
			}
		});
		
		this.itemDeletarConta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				janela.deletarConta();
			}
		});
		
		this.itemSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.exit(0);
				janela.logout();
			}
		});
	}
}