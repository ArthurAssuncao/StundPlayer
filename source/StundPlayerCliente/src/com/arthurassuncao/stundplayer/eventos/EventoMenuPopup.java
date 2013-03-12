package com.arthurassuncao.stundplayer.eventos;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

/** Classe para tratar os eventos dos <code>JPopupMenu</code> usados nas tabelas de algumas janelas do sistema.
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see MouseAdapter
 */
public class EventoMenuPopup extends MouseAdapter {

	private JTable tabela;
	//private Component janela;
	private JPopupMenu menuPopUp;

	/*public EventoMenuPopUp(JTable tabela){
                this.tabela = tabela;
        }*/

	/** Cria uma instancia do Tratador de eventos do mouse das tabelas que usam <code>JPopupMenu</code>
	 * @param tabela <code>JTable</code> tabela que tera o <code>JPopupMenu</code>
	 * @param menu <code>JPopupMenu</code> com o menu popup da tabela
	 * 
	 */
	public EventoMenuPopup(JTable tabela, JPopupMenu menu){
		this.tabela = tabela;
		this.menuPopUp = menu;
	}

	//private MenuPopUp menu;
	/** Trata o evento de pressionar o botao do mouse na tabela
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent evento){
		int linha = evento.getY() / tabela.getRowHeight();
		this.tabela.clearSelection(); //limpa selecao
		if(linha < this.tabela.getRowCount()){
			this.tabela.changeSelection(linha, 0, false, false);
			if (evento.isPopupTrigger()){
				doPop(evento);
			}
		}
	}

	/** Trata o evento de soltar o botao do mouse apos pressionar o botao na tabela
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent evento){
		if (evento.isPopupTrigger())
			doPop(evento);
	}

	/** Exibe o menu popup nas coordenadas do cursor do mouse
	 * @param evento um <code>MouseEvent</code> com o evento gerado pelo mouse
	 */
	private void doPop(MouseEvent evento){
		//MenuPopUp menu = new MenuPopUp(this.tabela, this.janela);
		this.menuPopUp.show(evento.getComponent(), evento.getX(), evento.getY());
	}
}
