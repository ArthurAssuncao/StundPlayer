package com.arthurassuncao.stundplayer.gui.sistema;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JTabbedPane;

import com.arthurassuncao.stundplayer.gui.Janela;
import com.arthurassuncao.stundplayer.gui.LabelRotulo;
import com.arthurassuncao.stundplayer.gui.Painel;

/** Janela para exibir as sobre o programa
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see Janela
 */
public class JanelaSobre extends Janela {
	private static final long serialVersionUID = 4732415723111043080L;
	private static final int LARGURA = 450;
	private static final int ALTURA = 250;
	
	private JTabbedPane painelGuias;
	private Painel painelSobre, painelCreditos;
	
	private LabelRotulo labelSobre1, labelSobre2;
	private LabelRotulo labelCreditosTitulo, labelCreditos1, labelCreditos2;
	
	/** Cria uma instancia da janela com o componente que sera usado para a localizacao relativa da janela
	 * @param componente <code>Component</code> com o componente pai desta janela
	 */
	public JanelaSobre(Component componente) {
		super("Sobre", LARGURA, ALTURA);
		painelGuias = new JTabbedPane();
		
		labelSobre1 = new LabelRotulo("Player online para a disciplina de Redes de Computadores");
		labelSobre2 = new LabelRotulo("Este player executa a musica do servidor usando os protocolos RTP e RTSP");
				
		labelCreditosTitulo = new LabelRotulo("Programadores:");
		labelCreditos1 = new LabelRotulo("Arthur Assuncao");
		labelCreditos2 = new LabelRotulo("Paulo Vitor");
		
		painelSobre = new Painel(new GridBagLayout());
		painelCreditos = new Painel(new GridBagLayout());
		
		painelGuias.setBackground(Color.BLACK);
		
		GridBagConstraints grid = new GridBagConstraints();
		grid.anchor = GridBagConstraints.LINE_START;
		painelCreditos.add(labelCreditosTitulo, grid);
		grid.anchor = GridBagConstraints.CENTER;
		grid.gridy = 1;
		painelCreditos.add(labelCreditos1, grid);
		grid.gridy = 2;
		painelCreditos.add(labelCreditos2, grid);
		
		grid.gridy = 0;
		painelSobre.add(labelSobre1, grid);
		grid.gridy = 1;
		painelSobre.add(labelSobre2, grid);
		
		painelGuias.add(painelSobre, "Sobre", 0);
		painelGuias.add(painelCreditos, "Creditos", 1);
		
		this.add(painelGuias);

		setLocationRelativeTo(componente);
		
		setVisible(true);
		
	}

	/* (non-Javadoc)
	 * @see br.java.redes.gui.Janela#addItensPopupMenu()
	 */
	@Override
	protected void addItensPopupMenu() {
		//nenhum item a ser adicionado
	}

	/* (non-Javadoc)
	 * @see br.java.redes.gui.Janela#addEventoItens()
	 */
	@Override
	protected void addEventoItens() {
		//nenhum evento a ser adicionado
	}
}
