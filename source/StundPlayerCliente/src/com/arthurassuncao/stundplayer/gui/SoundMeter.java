package com.arthurassuncao.stundplayer.gui;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/** Classe para manipular as linhas de entrada e saida de som
 * @author Arthur Assuncao
 * @author Paulo Vitor
 *
 * @see AudioSystem
 * @see FloatControl
 */
public class SoundMeter {

	JFrame janela;

	/** Cria uma instancia exibindo as entrada e saidas de audio e seus respectivos volumes
	 * 
	 */
	public SoundMeter() {
		janela = new JFrame("SoundMeter");
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		janela.setLayout(new BoxLayout(janela.getContentPane(), BoxLayout.Y_AXIS));
		printMixersDetails();
		janela.setVisible(true);
	}

	/** Imprime os detalhes dos mixers
	 * 
	 */
	public void printMixersDetails(){
		javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		System.out.println("There are " + mixers.length + " mixer info objects");  
		for(int i=0;i<mixers.length;i++){
			Mixer.Info mixerInfo = mixers[i];
			System.out.println("Mixer Name:"+mixerInfo.getName());
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lineinfos = mixer.getTargetLineInfo();
			for(Line.Info lineinfo : lineinfos){
				System.out.println("line:" + lineinfo);
				try {
					Line line = mixer.getLine(lineinfo);
					line.open();
					if(line.isControlSupported(FloatControl.Type.VOLUME)){
						FloatControl control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
						System.out.println("Volume:"+control.getValue());   
						JProgressBar barraVolume = new JProgressBar();
						// if you want to set the value for the volume 0.5 will be 50%
						// 0.0 being 0%
						// 1.0 being 100%
						//control.setValue((float) 0.5);
						int value = (int) (control.getValue()*100);
						barraVolume.setValue(value);
						janela.add(new JLabel(lineinfo.toString()));
						janela.add(barraVolume);
						janela.pack();
					}
				}
				catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}//for
		}//for
	}

	/** Retorna o controle de volume do sistema
	 * @return <code>FloatControl</code> com o controle de volume do tipo speaker
	 * @see FloatControl
	 */
	public static FloatControl getControleVolumeSpeaker(){
		/*FloatControl controleVolume = null;
		javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for(int i=0; i<mixers.length; i++){
			Mixer.Info mixerInfo = mixers[i];
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lineinfos = mixer.getTargetLineInfo();
			for(Line.Info lineinfo : lineinfos){
				String linha = lineinfo.toString();
				try {
					Line line = mixer.getLine(lineinfo);
					line.open();
					if(line.isControlSupported(FloatControl.Type.MASTER_GAIN) && linha.contains("SPEAKER")){
						controleVolume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
						break;
					}
					else if(line.isControlSupported(FloatControl.Type.VOLUME) && linha.contains("SPEAKER")){
						controleVolume = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
						break;
					}
				}
				catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}//for
		}//for
		 */
		FloatControl controleVolume = null;
		try {
			Line line = AudioSystem.getLine(Port.Info.SPEAKER);                     
			line.open();
			if(line.isControlSupported(FloatControl.Type.MASTER_GAIN)){
				controleVolume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			}
			else if(line.isControlSupported(FloatControl.Type.VOLUME)){
				controleVolume = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
			}
			//line.close();
		}
		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e){
			System.err.println("Speaker nao suportado pelo Sistema Operacional");
		}
		return controleVolume;
	}


}