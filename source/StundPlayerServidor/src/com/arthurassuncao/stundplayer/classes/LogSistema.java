package com.arthurassuncao.stundplayer.classes;

/** Classe para exibir ou gravar as mensagens do servidor
 * @author Arthur Assuncao
 * @author Paulo Vitor
 */
public abstract class LogSistema {
	/** Mostra um erro de banco de dados
	 * @param mensagem <code>String</code> com a mensagem
	 */
	public static void mostraMensagemErroBD(Object mensagem){
		System.err.println("Erro: " + mensagem.toString());
	}
	/** Mostra uma mensagem de banco de dados
	 * @param mensagem <code>String</code> com a mensagem
	 */
	public static void mostraMensagemBD(Object mensagem){
		System.out.println(mensagem.toString());
	}
	/** Mostra um erro do servidor
	 * @param mensagem <code>String</code> com a mensagem
	 */
	public static void mostraMensagemErroServidor(Object mensagem){
		System.err.println("Erro: " + mensagem.toString());
	}
	/** Mostra uma mensagem
	 * @param mensagem <code>String</code> com a mensagem
	 */
	public static void mostraMensagem(Object mensagem){
		System.out.println(mensagem.toString());
	}
}
