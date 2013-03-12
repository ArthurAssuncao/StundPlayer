package com.arthurassuncao.stundplayer.gui.player;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;

import com.arthurassuncao.stundplayer.classes.Configuracoes;
import com.arthurassuncao.stundplayer.gui.Cor;
import com.arthurassuncao.stundplayer.gui.Janela;

/** Tabela para a playlist do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see JXTable
 */
public class TabelaPlaylist extends JXTable {
	private static final long serialVersionUID = 3199113205367806818L;
	
	private int linhaEmExecucao = -1;
	private int linhaEmExecucaoAnterior = -1;
	private final Color corFundo = Configuracoes.getInstance().getCorFundoPlayer();
	private final Color corFundoEmExecucao = Cor.getBrilho(corFundo) < 100 ? corFundo.brighter() : corFundo.darker();

	/** Cria uma tabela com modelo de tabela especifico
	 * @param modeloTabela <code>DefaultTableModel</code> com o modelo da tabela
	 * @see DefaultTableModel
	 */
	public TabelaPlaylist(DefaultTableModel modeloTabela){
		super(modeloTabela);
		
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setShowGrid(false);
		this.setShowVerticalLines(false);
		this.setShowHorizontalLines(false);
		this.setForeground(Janela.getCorPadraoPlayer());
		this.setBackground(corFundo);
		this.setSelectionBackground(corFundo.brighter().brighter());
		this.setFocusable(true);
		this.setBorder(null);
		this.setSortable(false);
		//this.setSortOrder(this.tabelaPlaylist.getColumn(COLUNA_NOME_MUSICAS).getModelIndex(), SortOrder.ASCENDING);
		//this.setUpdateSelectionOnSort(true);
		//this.setHorizontalScrollEnabled(true);
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.JXTable#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.JXTable#prepareRenderer(javax.swing.table.TableCellRenderer, int, int)
	 */
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component componente = super.prepareRenderer(renderer, row, column);
		JComponent jcomponente = (JComponent)componente;
		if (isRowSelected(row)){
			int larguraBorda = 1;
			int top = (row > 0 && isRowSelected(row-1)) ? 1 : larguraBorda;
			int left =( column == 0) ? larguraBorda : 0;
			int bottom = ((row < getRowCount()-1) && (isRowSelected(row + 1))) ? 1 : larguraBorda;
			int right = (column == getColumnCount()-1) ? larguraBorda : 0;

			jcomponente.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Janela.getCorPadraoPlayer())); 
		}
		else{
			jcomponente.setBorder(null);
		}
		
		if(row == this.linhaEmExecucao){
			jcomponente.setBackground(corFundoEmExecucao);
		}
		else if(row == this.linhaEmExecucaoAnterior){
			jcomponente.setBackground(corFundo);
		}

		
		return componente;
	}
	
	/** Seta a linha correspondente a musica que esta em execucao
	 * @param linha <code>int</code> com a linha da musica em execucao
	 */
	public void setLinhaEmExecucao(int linha){
		this.linhaEmExecucaoAnterior = this.linhaEmExecucao;
		this.linhaEmExecucao = linha;
	}
	
}
