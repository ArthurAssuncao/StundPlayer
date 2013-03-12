package com.arthurassuncao.stundplayer.eventos.player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import com.arthurassuncao.stundplayer.gui.player.JanelaPlayer;

/** Classe para tratar os eventos do teclado da janela do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see KeyAdapter
 */
public class TratadorEventosTecladoPlayer extends KeyAdapter {
	private JanelaPlayer janela;
	
	/** Cria uma instancia do Tratador de eventos do teclado da janela do player
	 * @param janela <code>JanelaPlayer</code> com a janela do player
	 */
	public TratadorEventosTecladoPlayer(JanelaPlayer janela){
		this.janela = janela;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent evento) {
		super.keyPressed(evento);
		
		if(!(evento.getSource() instanceof JTextField) || evento.getSource() != janela.getCampoPesquisa()){ //teclas de atalho
			switch(evento.getKeyCode()){
				case KeyEvent.VK_A: //abre arquivos para upar
					System.out.println("Tecla A pressionada");
					janela.abrirMusica();
					break;
				case KeyEvent.VK_INSERT: //abre arquivos para upar
					System.out.println("Tecla INSERT pressionada");
					janela.abrirMusica();
					break;
				case KeyEvent.VK_DELETE: //delete uma musica
					System.out.println("Tecla DELETE pressionada " + janela.getMusicaSelecionada());
					this.janela.excluirMusicaSelecionada();
					break;
			}
		}
		
	}
	
	
	
}
