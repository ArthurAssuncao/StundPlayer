package com.arthurassuncao.stundplayer.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.arthurassuncao.stundplayer.recursos.Recursos;

/** Classe para manipular fontes, usa a fonte true type Segoe Ui
 * @author Arthur Assuncao
 *
 */
public class Fonte {

	/** <code>int</code> para representar o tamanho da fonte padrão de titulos */
	public static final float TAMANHO_TITULO = 18F;
	/** <code>int</code> para representar o tamanho da fonte padrão */
	public static final float TAMANHO_PADRAO = 11F;
	private final String DIRETORIO_FONTES	= "fontes/";
	private final String ENDERECO_FONTE	= "fontes/freesans";
	private final String ENDERECO_FONTE_BOLD = "fontes/freesans";
	private final String ENDERECO_FONTE_ITALIC = "fontes/freesans";
	private final String ENDERECO_FONTE_BOLD_ITALIC = "fontes/freesans";
	
	private final String EXTENSAO_FONTE			= ".ttf";

	/** <code>int</code> para representar o estilo de fonte normal */
	public static final int ESTILO_NORMAL					= Font.PLAIN;
	/** <code>int</code> para representar o estilo de fonte negrito */
	public static final int ESTILO_NEGRITO					= Font.BOLD;
	/** <code>int</code> para representar o estilo de fonte italico */
	public static final int ESTILO_ITALICO 					= Font.ITALIC;
	/** <code>int</code> para representar o estilo de fonte negrito italico */
	public static final int ESTILO_NEGRITO_ITALICO 	= Font.BOLD + Font.ITALIC;

	/** <code>Font</code> normal usando o tamanho padrao e estilo normal, com a fonte padrao da classe, Segoe Ui */
	public static final Font FONTE_NORMAL = new Fonte(TAMANHO_PADRAO, ESTILO_NORMAL).getFont();
	/** <code>Font</code> normal usando o tamanho padrao e estilo normal, com a fonte padrao da classe, Segoe Ui */
	public static final Font FONTE_TITULO = new Fonte(TAMANHO_TITULO, ESTILO_NEGRITO).getFont();

	private Font fonte;

	/** Cria uma instancia da classe com estilo normal e tamanho padrao
	 *  @see Font
	 *  @see Fonte#ESTILO_NORMAL
	 *  @see Fonte#TAMANHO_PADRAO
	 */
	public Fonte(){
		try{
			String fonteName = ENDERECO_FONTE + EXTENSAO_FONTE;
			//InputStream is = new BufferedInputStream(new FileInputStream(fonteName));
			InputStream is = new BufferedInputStream(Recursos.getResourceAsStream(fonteName));
			fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_NORMAL, TAMANHO_PADRAO);
			is.close();
		}
		catch(FontFormatException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


	/**Cria uma fonte de acordo com a fonte informada, tamanho e estilo
	 * @param nomeFonte <code>String</code> com o nome da fonte, pega a fonte do diretorio de fontes
	 * @param tamanho <code>int</code> tamanho da fonte
	 * @param estilo <code>int</code> representando o estilo da fonte. Normal, italico, negrito e negrito-italico
	 * @throws NullPointerException se o nome da fonte for nulo
	 */
	public Fonte(String nomeFonte, int tamanho, int estilo) throws NullPointerException{
		try{
			if(nomeFonte == null){
				throw new NullPointerException("Nome da fonte é nulo");
			}
			String enderecoFonte = DIRETORIO_FONTES + nomeFonte;
			InputStream is = new BufferedInputStream(Recursos.getResourceAsStream(enderecoFonte));
			fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(estilo, tamanho);
			is.close();
		}
		catch(FontFormatException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/** Cria uma instancia da classe com estilo especificado e tamanho padrao, se o estilo não for reconhecido, for diferente dos estilos definidos como constante, é atribuido o estilo normal
	 *  @see Font
	 *  @see Fonte#TAMANHO_PADRAO
	 *  @param estiloFonte <code>int</code> com o estilo da fonte
	 */
	public Fonte(int estiloFonte){
		String fonteName;
		InputStream is;
		try{
			switch (estiloFonte) {
			case ESTILO_NORMAL:
				fonteName = ENDERECO_FONTE + EXTENSAO_FONTE;
				is = new BufferedInputStream(Recursos.getResourceAsStream(fonteName));
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_NORMAL, TAMANHO_PADRAO);
				break;
			case ESTILO_NEGRITO:
				fonteName = ENDERECO_FONTE_BOLD + EXTENSAO_FONTE;
				is = new BufferedInputStream(Recursos.getResourceAsStream(fonteName));
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_NEGRITO, TAMANHO_PADRAO);
				break;
			case ESTILO_ITALICO:
				fonteName = ENDERECO_FONTE_ITALIC + EXTENSAO_FONTE;
				is = new BufferedInputStream(Recursos.getResourceAsStream(fonteName));
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_ITALICO, TAMANHO_PADRAO);
				break;
			case ESTILO_NEGRITO_ITALICO:
				fonteName = ENDERECO_FONTE_BOLD_ITALIC + EXTENSAO_FONTE;
				is = new BufferedInputStream(Recursos.getResourceAsStream(fonteName));
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_NEGRITO_ITALICO, TAMANHO_PADRAO);
				break;
			default:
				fonteName = ENDERECO_FONTE + EXTENSAO_FONTE;
				is = new BufferedInputStream(Recursos.getResourceAsStream(fonteName));
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(TAMANHO_PADRAO);
				break;
			}
			is.close();
		}
		catch(FontFormatException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/** Cria uma instancia da classe com estilo normal e tamanho especifico, se o tamanho for invalido, é atribuido o tamanho padrao
	 *  @see Font
	 *  @see Fonte#TAMANHO_PADRAO
	 *  @param tamanho <code>float</code> com o tamanho da fonte
	 */
	public Fonte(float tamanho){
		try{
			String fonteName = ENDERECO_FONTE + EXTENSAO_FONTE;
			InputStream is = new BufferedInputStream(Recursos.getResourceAsStream(fonteName));
			if(tamanho > 0){
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(tamanho);
			}
			else{
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(TAMANHO_PADRAO);
			}
			is.close();
		}
		catch(FontFormatException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/** Cria uma instancia da classe com estilo especifico e tamanho especifico, se o tamanho for invalido, é atribuido o tamanho padrao.
	 *  Se o estilo não for reconhecido, for diferente dos estilos definidos como constante, é atribuido o estilo normal
	 *  @see Font
	 *  @see Fonte#TAMANHO_PADRAO
	 *  @param tamanho <code>float</code> com o tamanho da fonte
	 *  @param estiloFonte <code>int</code> com o estilo da fonte
	 */
	@SuppressWarnings("resource")
	public Fonte(float tamanho, int estiloFonte){
		try{
			if(tamanho <= 0){
				tamanho = TAMANHO_PADRAO;
			}
			String fonteName = ENDERECO_FONTE + EXTENSAO_FONTE;
			InputStream is = new BufferedInputStream(Recursos.getResourceAsStream(fonteName));
			switch(estiloFonte){
			case ESTILO_NORMAL:{
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_NORMAL, tamanho);
				break;
			}
			case ESTILO_NEGRITO:{
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_NEGRITO, tamanho);
				break;
			}
			case ESTILO_ITALICO:{
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_ITALICO, tamanho);
				break;
			}
			case ESTILO_NEGRITO_ITALICO:{
				fonteName = ENDERECO_FONTE + "z" + EXTENSAO_FONTE;
				is = Recursos.getResourceAsStream(fonteName);
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(ESTILO_NEGRITO_ITALICO, tamanho);
				break;
			}
			default:{
				fonte = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(tamanho);
				break;
			}
			}
			is.close();
		}
		catch(FontFormatException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/** Retorna um objeto <code>Font</code> com o estilo e tamanho definidos ao criar a fonte
	 * @return <code>Font</code> com a fonte instanciada
	 */
	public Font getFont(){
		return this.fonte;
	}
}
