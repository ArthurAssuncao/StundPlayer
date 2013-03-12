package com.arthurassuncao.stundplayer.player;

/* *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *   
 *   Original by: http://thiscouldbebetter.wordpress.com/2011/07/04/pausing-an-mp3-file-using-jlayer/
 *   Last modified: 21-jul-2012 by Arthur Assuncao 
 *----------------------------------------------------------------------
 */

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//need JLayerPlayerPausable class
/** Classe para testar a classe <code>SoundJLayer</code> e <code>JLayerPlayerPausable</code>
 * @author Arthur Assuncao
 *
 * @see SoundJLayer
 * @see JLayerPlayerPausable
 */
public class JLayerPausableTest{
	private SoundJLayer soundToPlay;

	/** Metodo main para testar a classe
	 * @param args <code>String[]</code> com argumentos de linha de comando
	 */
	public static void main(String[] args){
		JLayerPausableTest jLayerPausable = new JLayerPausableTest();
		jLayerPausable.soundToPlay = new SoundJLayer("temp.mp3");

		JLayerPausableTest.interfaceGrafica(jLayerPausable);
	}

	//test only
	/** Cria a interface grafica para o teste
	 * @param jLayerPausable objeto para o teste
	 */
	public static void interfaceGrafica(JLayerPausableTest jLayerPausable){
		JFrame frame = new JFrame("Test JlayerPlayerPausable");

		JButton buttonPlay = new JButton("Play");
		JButton buttonPause = new JButton("Pause");
		JButton buttonStop = new JButton("Stop");
		JPanel panel = new JPanel(new GridBagLayout());

		panel.add(buttonPlay);
		panel.add(buttonPause);
		panel.add(buttonStop);

		buttonPlay.addActionListener(new ButtonListener(jLayerPausable));
		buttonPause.addActionListener(new ButtonListener(jLayerPausable));
		buttonStop.addActionListener(new ButtonListener(jLayerPausable));

		frame.add(panel);

		frame.setPreferredSize(new Dimension(200, 100));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}

	/** Tratador de eventos para os botoes da interface grafica
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 *@see ActionListener
	 */
	private static class ButtonListener implements ActionListener{
		JLayerPausableTest jLayerPausable;
		public ButtonListener(JLayerPausableTest jLayerPausable){
			this.jLayerPausable = jLayerPausable;
		}
		@Override
		public void actionPerformed(ActionEvent evento) {
			JButton botao = (JButton)evento.getSource();
			if(botao.getText().equalsIgnoreCase("PLAY")){
				this.jLayerPausable.soundToPlay.play();
			}
			else if(botao.getText().equalsIgnoreCase("PAUSE")){
				this.jLayerPausable.soundToPlay.pauseToggle();
			}
			else if(botao.getText().equalsIgnoreCase("STOP")){
				this.jLayerPausable.soundToPlay.stop();
			}
		}
	}

}