package com.arthurassuncao.stundplayer.gui.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.arthurassuncao.stundplayer.gui.JanelaInformacoesMusica;
import com.arthurassuncao.stundplayer.gui.sistema.JanelaConfiguracoes;
import com.arthurassuncao.stundplayer.gui.sistema.JanelaSobre;

/** Botao com menuPopup do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see JButton
 */
public class BotaoPopupMenuPrincipal extends JButton {

	private static final long serialVersionUID = -7483470872289594611L;

	private JanelaPlayer janela;
	private final Color corFundo = new Color(50, 50, 50);
	private Insets espacamento = new Insets(0, 0, 4, 0);
	
	// Popup menu.
	private JPopupMenu menuPopup;
	
	private JMenuItem itemSobre;
	
	private JMenuItem itemEnviarMusica;
	private JMenuItem itemInformacoesMusica;
	
	private JMenuItem itemConfiguracoes;
	private JMenuItem itemDeletarConta;
	
	private JMenuItem itemSair;

	// tempo pra fechar o menuPopup.
	private long timeLastShown = 100;

	/** Cria uma instancia do botao com a janela do player especificada
	 * @param janela <code>JanelaPlayer</code> com a janela do player
	 */
	public BotaoPopupMenuPrincipal(JanelaPlayer janela){
		super();
		this.setFocusable(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.setPreferredSize(new Dimension(15, 15));
		this.setBackground(corFundo);
		this.setMargin(espacamento);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.setText("^");
		
		this.janela = janela;
		this.menuPopup = new JPopupMenu();
		
		itemSobre = new JMenuItem("Sobre");
		itemEnviarMusica = new JMenuItem("Enviar Musica");
		itemInformacoesMusica = new JMenuItem("Informações da Musica em Execucao");
		itemConfiguracoes = new JMenuItem("Configurações");
		itemDeletarConta = new JMenuItem("Deletar Conta");
		itemSair = new JMenuItem("Sair");
				
		itemSobre.setAccelerator(KeyStroke.getKeyStroke("A"));
		itemEnviarMusica.setAccelerator(KeyStroke.getKeyStroke("E"));
		itemInformacoesMusica.setAccelerator(KeyStroke.getKeyStroke("F4"));
		itemConfiguracoes.setAccelerator(KeyStroke.getKeyStroke("C"));
		itemDeletarConta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		itemSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		
		menuPopup.add(itemSobre);
		menuPopup.add(new JSeparator());
		menuPopup.add(itemEnviarMusica);
		menuPopup.add(itemInformacoesMusica);
		menuPopup.add(new JSeparator());
		menuPopup.add(itemConfiguracoes);
		menuPopup.add(itemDeletarConta);
		menuPopup.add(new JSeparator());
		menuPopup.add(itemSair);

		//Mostra e esconde o menuPopup
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
	
	/** Adiciona os eventos dos itens do menuPopup
	 * 
	 */
	private void addEventosItens(){
		this.itemSobre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JanelaSobre(BotaoPopupMenuPrincipal.this.janela);
			}
		});
		
		this.itemEnviarMusica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				janela.abrirMusica();
			}
		});
		
		this.itemInformacoesMusica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JanelaInformacoesMusica(janela.getMusicaEmExecucao());
			}
		});
		
		this.itemConfiguracoes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JanelaConfiguracoes();
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