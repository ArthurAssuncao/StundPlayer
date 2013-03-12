package com.arthurassuncao.stundplayer.gui.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import com.arthurassuncao.stundplayer.classes.MusicaDados;
import com.arthurassuncao.stundplayer.gui.JanelaInformacoesMusica;

/** A classe <code>MenuPopUp</code> cria um menu pop para a playlist do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see JPopupMenu
 */
public class MenuPopup extends JPopupMenu {

	private static final long serialVersionUID = -3337209329465813934L;
	private JMenuItem itemExecutar;
	private JMenuItem itemExibirInformacoes;
	private JMenuItem itemExcluir;

	/** Cria uma instancia do <code>MenuPopUp</code> 
	 * @param tabela <code>JTable</code> com a tabela que terá o menu popup
	 */
	public MenuPopup(JTable tabela){
		itemExecutar = new JMenuItem("Executar Musica");
		itemExibirInformacoes = new JMenuItem("Informações da Musica");
		itemExcluir = new JMenuItem("Excluir Musica");

		itemExecutar.addActionListener(new TratadorEventoItensMenuPopUp(tabela));
		itemExibirInformacoes.addActionListener(new TratadorEventoItensMenuPopUp(tabela));
		itemExcluir.addActionListener(new TratadorEventoItensMenuPopUp(tabela));

		this.add(itemExecutar);
		this.add(itemExibirInformacoes);
		this.add(itemExcluir);
	}

	/** Cria uma instancia do <code>MenuPopUp</code> 
	 * @param tabela <code>JTable</code> com a tabela que tera o menu popup
	 * @param janela <code>JanelaPesquisarCandidato</code> com a janela que será manipulada por acoes do menu popup
	 */
	public MenuPopup(JTable tabela, JanelaPlayer janela){

		itemExecutar = new JMenuItem("Executar Musica");
		itemExibirInformacoes = new JMenuItem("Informações da Musica");
		itemExcluir = new JMenuItem("Excluir Musica");

		itemExecutar.addActionListener(new TratadorEventoItensMenuPopUp(tabela, janela));
		itemExibirInformacoes.addActionListener(new TratadorEventoItensMenuPopUp(tabela, janela));
		itemExcluir.addActionListener(new TratadorEventoItensMenuPopUp(tabela, janela));

		this.add(itemExecutar);
		this.add(itemExibirInformacoes);
		this.add(itemExcluir);

	}

	/** Tratador de eventos dos itens da classe <code>MenuPopUp</code>
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 * 
	 * @see ActionListener
	 * @see JanelaPesquisarCandidato
	 * @see JTable
	 *
	 */
	private class TratadorEventoItensMenuPopUp implements ActionListener{

		private JTable tabela;
		private JanelaPlayer janela;

		/** Cria uma instancia do tratador de eventos da tabela
		 * @param tabela <code>JTable</code> com a tabela que terá os eventos tratados
		 */
		public TratadorEventoItensMenuPopUp(JTable tabela){
			this.tabela = tabela;
		}
		/** Cria uma instancia do tratador de eventos da tabela
		 * @param tabela <code>JTable</code> com a tabela que terá os eventos tratados
		 * @param janela <code>JanelaPlayer</code> com a janela que tem a tabela
		 */
		public TratadorEventoItensMenuPopUp(JTable tabela, JanelaPlayer janela){
			this.tabela = tabela;
			this.janela = janela;
		}
		/* Trata o evento de clique nos itens do menu popup.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent evento) {
			MusicaDados musicaSelecionada = null;
			int linha = this.tabela.getSelectedRow();
			if(linha != -1){
				int indexColunaMusica = -1;
				indexColunaMusica = this.tabela.getColumn(this.janela.COLUNA_NOME_MUSICAS).getModelIndex();
				musicaSelecionada = (MusicaDados)tabela.getValueAt(linha, indexColunaMusica );
				if(evento.getSource() == itemExecutar){ //trata item exibir
					//janela.executaMusica(musicaSelecionada.getNomeMusica());
					janela.executaMusicaSelecionada();
				}
				else if(evento.getSource() == itemExibirInformacoes){ //trata item alterar
					new JanelaInformacoesMusica(musicaSelecionada);
				}
				else if(evento.getSource() == itemExcluir){ //trata item excluir
					this.janela.excluirMusicaSelecionada();
				}
			}
		}
	}

}
