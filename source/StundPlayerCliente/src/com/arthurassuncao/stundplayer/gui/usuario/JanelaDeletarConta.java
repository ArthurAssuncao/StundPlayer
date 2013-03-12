package com.arthurassuncao.stundplayer.gui.usuario;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.arthurassuncao.stundplayer.classes.Usuario;
import com.arthurassuncao.stundplayer.gui.Janela;
import com.arthurassuncao.stundplayer.gui.JanelaMensagem;
import com.arthurassuncao.stundplayer.gui.LabelRotulo;
import com.arthurassuncao.stundplayer.gui.Painel;
import com.arthurassuncao.stundplayer.gui.player.JanelaPlayer;
import com.arthurassuncao.stundplayer.gui.sistema.JanelaLogin;

/** Janela para deletar a conta do usuario
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see Janela
 */
public class JanelaDeletarConta extends Janela {
	
	private static final long serialVersionUID = 6497442250579382902L;
	private static final int LARGURA = 400;
	private static final int ALTURA = 200;
	
	private Painel painelTotal;
	
	private LabelRotulo labelUsuario;
	private LabelRotulo labelSenha;
	
	private JTextField campoUsuario;
	private JPasswordField campoSenha;
	
	private JButton botaoCancelar;
	private JButton botaoExcluir;
	
	private JanelaPlayer janela;
	
	/** Cria a janela para deletar a conta do usuario especificado
	 * @param janela <code>JanelaPlayer</code> com a janela do player
	 * @param username <code>String</code> com o nome de usuario do usuario
	 */
	public JanelaDeletarConta(JanelaPlayer janela, String username){
		super("Deletar Conta", LARGURA, ALTURA);
		
		this.janela = janela;
		
		this.iniciaElementos();
		
		this.campoUsuario.setText(username);
		
		this.addElementos();
		
		this.addEventos();
		
		this.pack();
		
		this.setLocationRelativeTo(janela);
		
		this.setVisible(true);
	}
	
	/** Inicializa os elementos da janela
	 * 
	 */
	public void iniciaElementos(){
		
		this.painelTotal = new Painel(new GridBagLayout());
		
		this.labelUsuario = new LabelRotulo("Usuario: ");
		this.labelSenha = new LabelRotulo("Senha: ");
		
		this.campoUsuario = new JTextField(10);
		this.campoSenha = new JPasswordField(10);
		
		this.botaoCancelar = new JButton("Cancelar");
		this.botaoExcluir = new JButton("Excluir");
		
		//seta propriedades
		this.botaoCancelar.setFocusable(false);
		this.botaoExcluir.setFocusable(false);
		
		this.campoUsuario.setEditable(false);
	}
	
	/** Adiciona os elementos a janela
	 * 
	 */
	public void addElementos(){
		GridBagConstraints grid = new GridBagConstraints();
		grid.insets = new Insets(5, 5, 5, 5);
		
		grid.gridx = 0;
		grid.gridy = 0;
		painelTotal.add(labelUsuario, grid);
		grid.gridx = 1;
		painelTotal.add(campoUsuario, grid);
		grid.gridy = 1;
		grid.gridx = 0;
		painelTotal.add(labelSenha, grid);
		grid.gridx = 1;
		painelTotal.add(campoSenha, grid);
		grid.gridy = 2;
		grid.gridx = 0;
		painelTotal.add(botaoCancelar, grid);
		grid.gridx = 1;
		painelTotal.add(botaoExcluir, grid);
		
		this.add(painelTotal);
		
	}
	
	/** Adiciona eventos aos elementos da janela
	 * 
	 */
	public void addEventos(){
		this.botaoCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				JanelaDeletarConta.this.dispose();
			}
		});
		
		this.botaoExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				String username = JanelaDeletarConta.this.campoUsuario.getText();
				String senha = new String(JanelaDeletarConta.this.campoSenha.getPassword());
				if(senha.isEmpty()){
					JanelaMensagem.mostraMensagemErro(JanelaDeletarConta.this, "Informe a senha");
				}
				else{
					Usuario usuario = new Usuario(username, senha);
					boolean deletarConta = JanelaMensagem.mostraMensagemConfirmaWarning(JanelaDeletarConta.this, "Deletar Conta", "Deseja realmente excluir esta conta?\n\n" +
							"Ao excluir a sua conta, todas as suas musicas sao excluidas." +
							"\nPara usar o servico novamente tera que criar uma nova conta.");
					if(deletarConta){
						if(usuario.excluir()){
							JanelaMensagem.mostraMensagem(JanelaDeletarConta.this, "Deletar Conta", "Conta excluida com sucesso");
							JanelaDeletarConta.this.janela.logout();
							JanelaDeletarConta.this.janela.dispose();
							new JanelaLogin();
						}
						else{
							JanelaMensagem.mostraMensagem(JanelaDeletarConta.this, "Deletar Conta", "Não foi possivel excluir a conta\n" +
									"Verifique a senha e tente novamente");
						}
					}
					else{
						JanelaMensagem.mostraMensagem(JanelaDeletarConta.this, "Deletar Conta", "A conta não foi excluida");
						JanelaDeletarConta.this.dispose();
					}
				}
				
			}
		});
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
