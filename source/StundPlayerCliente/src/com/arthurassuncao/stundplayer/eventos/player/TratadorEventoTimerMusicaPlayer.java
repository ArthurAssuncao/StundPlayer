package com.arthurassuncao.stundplayer.eventos.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.arthurassuncao.stundplayer.gui.player.JanelaPlayer;
import com.arthurassuncao.stundplayer.player.SoundJLayer;

/** Classe para tratar os eventos do timer da janela do player
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see ActionListener
 */
public class TratadorEventoTimerMusicaPlayer implements ActionListener{

	private JanelaPlayer janela;

	/** Cria uma instancia do Tratador de eventos do timer da janela do player
	 * @param janela <code>JanelaPlayer</code> com a janela do player
	 */
	public TratadorEventoTimerMusicaPlayer(JanelaPlayer janela){
		this.janela = janela;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent evento) {
		if(this.janela.getClienteRTSP().isMusicaRodando()){
			/*if(this.janela.getControleVolume() == null){
				FloatControl controleVolume = this.janela.getClienteRTSP().getControleVolumeMusica();
				if(controleVolume != null){
					this.janela.setControleVolume(controleVolume);
				}
				System.out.println("Controle volume nulo");
			}*/
			this.janela.atualizaValorBarraProgressoMusica();
		}

		SoundJLayer musicaEmExecucao = this.janela.getClienteRTSP().getPlayer();
		if(musicaEmExecucao != null && musicaEmExecucao.isComplete()){
			this.janela.executaProximaMusica();
		}
	}

}
