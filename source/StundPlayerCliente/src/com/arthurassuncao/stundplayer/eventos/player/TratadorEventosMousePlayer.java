package com.arthurassuncao.stundplayer.eventos.player;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.arthurassuncao.stundplayer.classes.MusicaDados;
import com.arthurassuncao.stundplayer.gui.player.JanelaPlayer;

/** Classe para tratar os eventos do mouse da janela do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see MouseAdapter
 */
public class TratadorEventosMousePlayer extends MouseAdapter {
	private JanelaPlayer janela;

	/** Cria uma instancia do Tratador de eventos do mouse da janela do player
	 * @param janela <code>JanelaPlayer</code> com a janela do player
	 */
	public TratadorEventosMousePlayer(JanelaPlayer janela){
		this.janela = janela;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent evento) {
		super.mouseClicked(evento);

		if(!(evento.getSource() instanceof JTextField) || evento.getSource() != janela.getCampoPesquisa()){
			JTextField campoPesquisa = janela.getCampoPesquisa();
			if(campoPesquisa.getText().equalsIgnoreCase("")){
				campoPesquisa.setText(janela.TEXTO_PADRAO_PESQUISAR);
			}
		}
		if(evento.getButton() == MouseEvent.BUTTON1){ //clicou com o botao esquerdo do mouse
			if(evento.getSource() instanceof JTextField && evento.getSource() == janela.getCampoPesquisa()){
				JTextField campoPesquisa = janela.getCampoPesquisa();
				if(campoPesquisa.getText().equalsIgnoreCase(janela.TEXTO_PADRAO_PESQUISAR)){
					campoPesquisa.setText("");
				}
			}
			else if(evento.getSource() instanceof JTable){
				JTable tabela = janela.getTabelaPlaylist();
				int linha = -1;
				int coluna = -1;
				linha = tabela.getSelectedRow();
				coluna = tabela.getColumn(this.janela.COLUNA_NOME_MUSICAS).getModelIndex();
				if(linha != -1){
					if(evento.getClickCount() >= 2){
						MusicaDados musicaSelecionada = (MusicaDados)tabela.getValueAt(linha, coluna);

						System.out.println("Selecionou: " + musicaSelecionada.getNomeMusica());
						//janela.executaMusica(musicaSelecionada.getNomeMusica());
						janela.executaMusicaSelecionada();
					}
					else{
						tabela.setColumnSelectionInterval(0, janela.NUMERO_COLUNAS_TABELA_PLAYLIST - 1);
						tabela.setRowSelectionInterval(linha, linha);
					}
				}

			}
			else if(evento.getSource() instanceof JButton){// botao
				JButton botao = (JButton)evento.getSource();
				if(botao == janela.getBotaoPesquisar()){
					List<MusicaDados> listaMusicas = null;
					if(janela.getCampoPesquisa().getText().isEmpty() || janela.getCampoPesquisa().getText().equalsIgnoreCase(janela.TEXTO_PADRAO_PESQUISAR)){
						janela.addTodasMusicas();
					}
					else{
						listaMusicas = janela.pesquisaMusicas(janela.getCampoPesquisa().getText());
						janela.addMusicasDadosTabelaPlaylist(listaMusicas);
					}
				}
				else if(botao == janela.getBotaoPlay()){
					System.out.println("PLAY");
					if(this.janela.getClienteRTSP().isMusicaRodando()){
						this.janela.executaMusica();
					}
					else{
						System.out.println("asdf");
						this.janela.reiniciaMusica();
					}
				}
				else if(botao == janela.getBotaoPause()){
					System.out.println("PAUSE");
					this.janela.pausaMusica();
				}
				else if(botao == janela.getBotaoStop()){
					System.out.println("STOP");
					this.janela.paraMusica();
				}
				else if(botao == janela.getBotaoProximaMusica()){
					System.out.println("Proxima musica");
					int linha = this.janela.proximaMusica();
					int coluna = this.janela.getTabelaPlaylist().getColumn(this.janela.COLUNA_NOME_MUSICAS).getModelIndex();
					MusicaDados musica = (MusicaDados)janela.getTabelaPlaylist().getValueAt(linha, coluna);
					janela.executaMusica(musica, linha);
				}
				else if(botao == janela.getBotaoMusicaAnterior()){
					System.out.println("Musica Anterior");
					int linha = this.janela.musicaAnterior();
					int coluna = this.janela.getTabelaPlaylist().getColumn(this.janela.COLUNA_NOME_MUSICAS).getModelIndex();
					MusicaDados musica = (MusicaDados)janela.getTabelaPlaylist().getValueAt(linha, coluna);
					janela.executaMusica(musica, linha);
				}
				else if(botao == janela.getBotaoAbrirMusica()){
					this.janela.abrirMusica();
				}
				else if(botao == janela.getBotaoRepetir()){
					janela.mudaRepetir();
				}
				else if(botao == janela.getBotaoShuffle()){
					janela.mudaShuffle();
				}
			}
		}//fim clicou num botao


	}//fim mouseClicked

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent evento) {
		super.mouseEntered(evento);
		if(evento.getSource() instanceof JSlider){ //barra de voluma
			this.janela.setValorVolume();
		}
		else if(evento.getSource() instanceof JProgressBar){
			this.janela.setBarraProgressoMusicaTooltipText();
		}
	}

}
