package com.arthurassuncao.stundplayer.gui.temas;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

import com.arthurassuncao.stundplayer.gui.Janela;

/** Classe para manipular os temas(look and feel) do sistema
 * @author Arthur Assuncao
 * 
 * @see UIManager
 */
public class Temas {
	/*
	 * 		Temas Disponiveis
	 * Metal
	 * Nimbus
	 * CDE/Motif
	 * 		No Windows
	 * Windows
	 * Windows Classic
	 * 		No Linux(GKT)
	 * GTK+
	 */
	/** <code>String</code> com o nome do tema */
	public static final String METAL = "Metal" ;
	/** <code>String</code> com o nome do tema */
	public static final String NIMBUS = "Nimbus";
	/** <code>String</code> com o nome do tema */
	public static final String NIMBUS_DARK = "NimbusDark";
	/** <code>String</code> com o nome do tema */
	public static final String CDE_MOTIF = "CDE/Motif";
	/** <code>String</code> com o nome do tema */
	public static final String WINDOWS = "Windows";
	/** <code>String</code> com o nome do tema */
	public static final String WINDOWS_CLASSIC = "Windows Classic";
	/** <code>String</code> com o nome do tema */
	public static final String GTK = "GTK+";

	/** Muda o tema para o padrão do sistema. Caso não consiga usar o tema padrão do sistema, usa o tema Nimbus<br>
	 * Ex: se o sistema é windows, usará o tema Windows, se o sistema é linux rodando GTK, o tema será GTK+
	 * 
	 */
	public static void mudaTema(){ //Padrao é o do sistema
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			try{
				mudaTema(Temas.NIMBUS);
			}
			catch(Exception ee){
				//nao muda
			}
		}
	};
	
	/** Muda o tema para um dos temas passados como parametro no <code>var-args</code>. O tema é escolhido na ordem no <code>var-args</code>.
	 * 
	 * @param temas <code>String...</code> com a lista de temas.
	 */
	public static void mudaTema(String... temas){
		boolean darkNimbus = false;
		forExterno:
			for(String tema : temas){
				try {
					if(tema.equalsIgnoreCase(NIMBUS_DARK)){
						darkNimbus = true;
						tema = NIMBUS;
					}
					for (LookAndFeelInfo t : UIManager.getInstalledLookAndFeels()) {
						if (tema.equalsIgnoreCase(t.getName())) {
							UIManager.setLookAndFeel(t.getClassName());
							if(darkNimbus){
								Temas.setCoresBase();
							}

							break forExterno;
						} //fim if
					}// fim for
				}
				catch (Exception e) {
					//nao muda
				}
			}
	};

	/** Seta as cores base do programa
	 * 
	 */
	public static void setCoresBase(){
		Color corPadraoSistema = Janela.getCorPadraoPlayer(); //padrao inicial é R=249, G=127, B=16
		Color corFundoPadraoSistema = Janela.getCorFundoPadraoPlayer(); //padrao inicial é R=40, G=40, B=40

		UIManager.put("Nimbus.Overrides.InheritDefaults", true);
		//UIManager.put("nimbusBase", new ColorUIResource(10, 10, 10));
		UIManager.put("nimbusBase", new ColorUIResource(corFundoPadraoSistema.darker().darker()));
		UIManager.put("nimbusOrange", new ColorUIResource(corPadraoSistema));
		UIManager.put("nimbusFocus", new ColorUIResource(corPadraoSistema));
		UIManager.put("Slider.background", new ColorUIResource(corPadraoSistema));
		//UIManager.put("Button.background", Janela.COR_LARANJA.darker());
		UIManager.put("Button.background", new ColorUIResource(new Color(150, 150, 150)));
		UIManager.put("Label.foreground", new ColorUIResource(corPadraoSistema));
		
		System.out.println("Classe Temas: cor padrao do sistema modificada para:" +
				" R=" + corPadraoSistema.getRed() + 
				" G=" + corPadraoSistema.getGreen() + 
				" B=" + corPadraoSistema.getBlue()
				);
		System.out.println("Classe Temas: cor de fundo padrao do sistema modificada para: "+
				" R=" + corFundoPadraoSistema.getRed() + 
				" G=" + corFundoPadraoSistema.getGreen() + 
				" B=" + corFundoPadraoSistema.getBlue()
				);
		
		//UIManager.put("ProgressBar:ProgressBar[Enabled].foregroundPainter", Janela.COR_LARANJA);
		//UIManager.put("Slider:SliderTrack[Enabled].backgroundPainter", Janela.COR_LARANJA); 
	}

}
