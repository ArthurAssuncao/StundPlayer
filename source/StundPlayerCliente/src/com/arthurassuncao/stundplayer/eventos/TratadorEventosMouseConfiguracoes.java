package com.arthurassuncao.stundplayer.eventos;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import com.arthurassuncao.stundplayer.classes.Configuracoes;
import com.arthurassuncao.stundplayer.gui.Janela;
import com.arthurassuncao.stundplayer.gui.JanelaMensagem;
import com.arthurassuncao.stundplayer.gui.sistema.JanelaConfiguracoes;

/** Classe para tratar os eventos do mouse da janela de configuracao
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see MouseAdapter
 */
public class TratadorEventosMouseConfiguracoes extends MouseAdapter {

	private JanelaConfiguracoes janela = null;

	/** Cria uma instancia do Tratador de eventos do mouse da janela de configuracoes
	 * @param janela <code>JanelaConfiguracoes</code> com a janela de configuracoes
	 */
	public TratadorEventosMouseConfiguracoes(JanelaConfiguracoes janela){
		this.janela = janela;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent evento) {
		super.mouseClicked(evento);
		if (evento.getButton() == MouseEvent.BUTTON1){ //botao esquerdo do mouse
			if(this.janela != null && evento.getSource() instanceof JButton){
				if ((JButton)evento.getSource() == janela.getBotaoLimparCampos()){
					//limpa campos
					this.janela.limpaCamposJanela();
				}
				else if ((JButton)evento.getSource() == janela.getBotaoSalvar()){
					if(this.janela.verificaCampos()){ //campos estao validos
						String ip 	= this.janela.getCampoIpServidor().getText();
						int ftp 	= Integer.parseInt(this.janela.getCampoPortaFTP().getText());
						int rtp 	= Integer.parseInt(this.janela.getCampoPortaRTP().getText());
						int rtsp 	= Integer.parseInt(this.janela.getCampoPortaRTSP().getText());
						Color corPlayer = this.janela.getCampoCorPlayer().getBackground();
						Color corFundoPlayer = this.janela.getCampoCorFundoPlayer().getBackground();

						Configuracoes configuracoes = Configuracoes.getInstance();
						configuracoes.setIpServidor(ip);
						configuracoes.setPortaFTP(ftp);
						configuracoes.setPortaRTP(rtp);
						configuracoes.setPortaRTSP(rtsp);
						configuracoes.setCorPlayer(corPlayer);
						configuracoes.setCorFundoPlayer(corFundoPlayer);
						boolean salvou = configuracoes.escreveConfiguracoesArquivo();
						if(salvou){
							JanelaMensagem.mostraMensagem(this.janela, "Configurações", "Configurações salvas com sucesso");
							Janela.repintaTodasJanelas();
							this.janela.limpaCamposJanela();
							this.janela.dispose(); //fecha janela
						}
						else{
							JanelaMensagem.mostraMensagemErro(this.janela, "Não foi possivel salvar as configurações.");
						}

					}
					else{
						JanelaMensagem.mostraMensagemErro(this.janela, this.janela.getErros());
						this.janela.removeErros();
					}
				}
				else if ((JButton)evento.getSource() == janela.getBotaoPadraoIpServidor()){
					this.janela.getCampoIpServidor().setText(Configuracoes.IP_SERVIDOR_PADRAO);
				}
				else if ((JButton)evento.getSource() == janela.getBotaoPadraoPortaServidor()){
					this.janela.getCampoPortaServidor().setText(String.valueOf(Configuracoes.PORTA_SERVIDOR_PADRAO));
				}
				else if ((JButton)evento.getSource() == janela.getBotaoPadraoPortaFTP()){
					this.janela.getCampoPortaFTP().setText(String.valueOf(Configuracoes.PORTA_FTP_PADRAO));
				}
				else if ((JButton)evento.getSource() == janela.getBotaoPadraoPortaRTP()){
					this.janela.getCampoPortaRTP().setText(String.valueOf(Configuracoes.PORTA_RTP_PADRAO));
				}
				else if ((JButton)evento.getSource() == janela.getBotaoPadraoPortaRTSP()){
					this.janela.getCampoPortaRTSP().setText(String.valueOf(Configuracoes.PORTA_RTSP_PADRAO));
				}
				else if ((JButton)evento.getSource() == janela.getBotaoPadraoCorPlayer()){
					this.janela.getCampoCorPlayer().setBackground(Configuracoes.COR_PLAYER_PADRAO);
				}
				else if ((JButton)evento.getSource() == janela.getBotaoPadraoCorFundoPlayer()){
					this.janela.getCampoCorFundoPlayer().setBackground(Configuracoes.COR_FUNDO_PLAYER_PADRAO);
				}

			}// fim botao salvar
		} //fim tratamento botoes
	}// fim if getButton
}// fim metodo