package com.arthurassuncao.stundplayer.eventos.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.arthurassuncao.stundplayer.classes.Usuario;
import com.arthurassuncao.stundplayer.cliente.Cliente;
import com.arthurassuncao.stundplayer.gui.JanelaMensagem;
import com.arthurassuncao.stundplayer.gui.player.JanelaPlayer;
import com.arthurassuncao.stundplayer.gui.sistema.JanelaConfiguracoes;
import com.arthurassuncao.stundplayer.gui.sistema.JanelaLogin;

/** Classe para tratar os eventos da janela de login
 * @author Arthur Assuncao
 * @author Paulo Vitor
 * @see MouseAdapter
 * @see ActionListener
 */
public class TratadorEventosMouseLogin extends MouseAdapter implements ActionListener{
	
	private JanelaLogin janela;
	
	/** Cria uma instancia do Tratador de eventos do mouse da janela de login
	 * @param janela <code>JanelaLogin</code> com a janela de login
	 */
	public TratadorEventosMouseLogin(JanelaLogin janela){
		this.janela = janela;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent evento) {
		super.mouseClicked(evento);
		if(evento.getSource() instanceof JButton){
			if(janela.verificaCampos()){
				
				String username;
				String senha;
				
				username = this.janela.getUsername();
				senha = this.janela.getSenha();
				
				if(evento.getSource() == janela.getBotaoLogin()){ //faz login
					Usuario usuario = Usuario.pesquisa(username, senha);
	
					if(usuario != null){ //faz login
						this.janela.dispose();
						abreJanelaPrograma(usuario);
					}
					else{
						JanelaMensagem.mostraMensagemErro(this.janela, "Login incorreto");
					}
						
				} //fim botao login
				else if(evento.getSource() == janela.getBotaoNovaConta()){ //cria uma nova conta
					Usuario novoUsuario = new Usuario(username, senha);
					String resuladoInsercao = novoUsuario.inserir();
					if(Cliente.RESULTADO_SUCESSO.equals(resuladoInsercao)){ //cadastrou
						JanelaMensagem.mostraMensagem(this.janela, "Nova Conta", "Novo usuario cadastrado");
						this.janela.dispose();
						abreJanelaPrograma(novoUsuario);
					}
					else if(Cliente.RESULTADO_EXISTE.equals(resuladoInsercao)){ //ja existe esse usuario
						JanelaMensagem.mostraMensagemErro(this.janela, "Usuario ja cadastrado");
					}
					else{
						JanelaMensagem.mostraMensagemErro(this.janela, "Erro ao cadastrar");
					}
				}
			}
			else{
				JanelaMensagem.mostraMensagemErro(this.janela, this.janela.getErros());
				this.janela.removeErros();
			}
		} // fim é botao
		else if(evento.getSource() instanceof JLabel){
			JLabel label = (JLabel)evento.getSource();
			if(label == janela.getLabelImagemConfiguracoes()){
				new JanelaConfiguracoes();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent evento) {
		super.mouseEntered(evento);
		if(evento.getSource() instanceof JLabel){
			JLabel label = (JLabel)evento.getSource();
			if(label == janela.getLabelImagemConfiguracoes()){
				label.setIcon(janela.getImagemConfiguracoes());
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent evento) {
		super.mouseExited(evento);
		if(evento.getSource() instanceof JLabel){
			JLabel label = (JLabel)evento.getSource();
			if(label == janela.getLabelImagemConfiguracoes()){
				label.setIcon(janela.getImagemConfiguracoesCinza());
			}
		}
	}
	
	/** Abre a janela do player e salva o usuario logado como ultimo logado
	 * @param usuario <code>Usuario</code> com o usuario
	 */
	public void abreJanelaPrograma(Usuario usuario){
		//new JanelaPrincipal(usuario).setVisible(true);
		this.salvaUltimoUsuario(usuario);
		new JanelaPlayer(usuario);
	}
	
	/** Salva o ultimo usuario logado
	 * @param usuario <code>Usuario</code> com o usuario
	 */
	public void salvaUltimoUsuario(Usuario usuario){
		try {
			Usuario.salvaUltimoUsuario(usuario);
		}
		catch (IOException e) {
			e.printStackTrace();
			JanelaMensagem.mostraMensagemErro(this.janela, "Não foi possivel salva o username do ultimo usuario");
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent evento) {
		String username;
		String senha;
		
		username = this.janela.getUsername();
		senha = this.janela.getSenha();
		if(evento.getSource() == this.janela.getCampoUsuario() || evento.getSource() == this.janela.getCampoSenha()){
			if(this.janela.verificaCampos()){
				Usuario usuario = Usuario.pesquisa(username, senha);
				
				if(usuario != null){ //faz login
					this.janela.dispose();
					TratadorEventosMouseLogin.this.abreJanelaPrograma(usuario);
				}
				else{
					JanelaMensagem.mostraMensagemErro(this.janela, "Login incorreto");
				}
			}
			else{
				JanelaMensagem.mostraMensagemErro(this.janela, this.janela.getErros());
				this.janela.removeErros();
			}
		}
	}
	
}
