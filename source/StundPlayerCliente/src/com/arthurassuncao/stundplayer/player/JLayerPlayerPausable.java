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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

//use with JLayerPausableTest
/** Player que usa a biblioteca JLayer com a implementacao que permite o pause
 * @author Arthur Assuncao
 *
 * @see SoundJLayer
 */
public class JLayerPlayerPausable{
	// This class is loosely based on javazoom.jl.player.AdvancedPlayer.

	protected java.net.URL urlToStreamFrom;
	protected String audioPath;
	protected Bitstream bitstream;
	protected Decoder decoder;
	protected AudioDevice audioDevice;
	protected boolean closed;
	protected boolean complete;
	protected boolean paused;
	protected boolean stopped;
	protected PlaybackListener listener;
	protected int frameIndexCurrent;
	private final int lostFrames = 20; //some fraction of a second of the sound gets "lost" after every pause. 52 in original code

	/** Cria um player com o endereco da musica num <code>URL</code>
	 * @param urlToStreamFrom <code>URL</code> com o caminho da musica
	 * @throws JavaLayerException caso ocorra um erro do JLayer
	 * @see URL
	 */
	public JLayerPlayerPausable(URL urlToStreamFrom) throws JavaLayerException{
		this.urlToStreamFrom = urlToStreamFrom;
		this.listener = new PlaybackAdapter();
	}
	
	/** Cria um player com o endereco da musica num <code>URL</code>
	 * @param audioPath <code>String</code> com o caminho da musica
	 * @throws JavaLayerException caso ocorra um erro do JLayer
	 */
	public JLayerPlayerPausable(String audioPath) throws JavaLayerException{
		this.audioPath = audioPath;
		this.listener = new PlaybackAdapter();
	}

	/** Seta o tratador de eventos do player
	 * @param newPlaybackListener <code>PlaybackListener</code> com o tratador de eventos
	 * @throws NullPointerException caso o listener seja <code>null</code>
	 * @see PlaybackListener
	 */
	public void setPlaybackListener(PlaybackListener newPlaybackListener) throws NullPointerException{
		if(newPlaybackListener != null){
			this.listener = newPlaybackListener;
		}
		else{
			throw new NullPointerException("PlaybackListener is null");
		}
	}
	
	/** Retorna o tamanho atual do arquivo
	 * @return <code>long</code> com o tamanho do arquivo
	 */
	protected long getFileSize(){
		if(this.audioPath != null){
			return new File(this.audioPath).length();
		}
		else if(this.urlToStreamFrom != null){
			URLConnection conexaoURL;
			try {
				conexaoURL = this.urlToStreamFrom.openConnection();
			}
			catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
			return conexaoURL.getContentLength();
		}
		return -1;
	}

	/** Retorna o fluxo de entrada do audio a partir do caminho da musica
	 * @return <code>InputStream</code> com o fluxo de entrada da musica
	 * @see InputStream
	 * @throws IOException caso ocorra um erro ao acessar o arquivo
	 */
	protected InputStream getAudioInputStream() throws IOException{
		if(this.audioPath != null){
			return new FileInputStream(this.audioPath);
		}
		else if(this.urlToStreamFrom != null){
			this.urlToStreamFrom.openStream();
		}
		return null;
	}

	/** Executa a musica
	 * @return <code>boolean</code> com <code>true</code> se a musica foi executada e <code>false</code> senao
	 * @throws JavaLayerException caso ocorra um erro ao executar a musica
	 */
	public boolean play() throws JavaLayerException{
		return this.play(0);
	}

	/** Executa a musica
	 * @param frameIndexStart <code>int</code> com o frame inicial da execucao
	 * @return <code>boolean</code> com <code>true</code> se a musica foi executada e <code>false</code> senao
	 * @throws JavaLayerException caso ocorra um erro ao executar a musica
	 */
	public boolean play(int frameIndexStart) throws JavaLayerException {
		//return this.play(frameIndexStart, -1, 52); //original, mas voltava num ponto anterior ao do pause. 52 Sao os frames perdidos ao dar pause 
		return this.play(frameIndexStart, -1, lostFrames);
	}

	/** Executa a musica
	 * @param frameIndexStart <code>int</code> com o frame inicial da execucao
	 * @param frameIndexFinal <code>int</code> com o frame final da execucao
	 * @param correctionFactorInFrames <code>int</code> com o numero de frames perdidos ao dar pause
	 * @return <code>boolean</code> com <code>true</code> se a musica foi executada e <code>false</code> senao
	 * @throws JavaLayerException caso ocorra um erro ao executar a musica
	 */
	public boolean play(int frameIndexStart, int frameIndexFinal, int correctionFactorInFrames) throws JavaLayerException{
		try {
			this.bitstream = new Bitstream(this.getAudioInputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		this.audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
		this.decoder = new Decoder();
		this.audioDevice.open(this.decoder);

		boolean shouldContinueReadingFrames = true;

		this.paused = false;
		this.stopped = false;
		this.frameIndexCurrent = 0;

		while (shouldContinueReadingFrames == true && this.frameIndexCurrent < frameIndexStart - correctionFactorInFrames){
			shouldContinueReadingFrames = this.skipFrame();
			this.frameIndexCurrent++;
		}

		if (this.listener != null) {
			this.listener.playbackStarted(new PlaybackEvent(this, PlaybackEvent.EventType.Instances.Started, this.audioDevice.getPosition()));
		}

		if (frameIndexFinal < 0){
			frameIndexFinal = Integer.MAX_VALUE;
		}

		while (shouldContinueReadingFrames == true && this.frameIndexCurrent < frameIndexFinal){
			if (this.paused || this.stopped){
				shouldContinueReadingFrames = false;    
				try{
					Thread.sleep(1);
				}
				catch (Exception ex){
					ex.printStackTrace();
				}
			}
			else{
				shouldContinueReadingFrames = this.decodeFrame();
				this.frameIndexCurrent++;
			}
		}

		// last frame, ensure all data flushed to the audio device.
		if (this.audioDevice != null && !this.paused){
			this.audioDevice.flush();

			synchronized (this){
				this.complete = (this.closed == false);
				this.close();
			}

			// report to listener
			if (this.listener != null) {
				int audioDevicePosition = -1;
				if(this.audioDevice != null){
					audioDevicePosition = this.audioDevice.getPosition();
				}
				else{
					//throw new NullPointerException("attribute audioDevice in " + this.getClass() + " is NULL");
				}
				PlaybackEvent playbackEvent = new PlaybackEvent(this, PlaybackEvent.EventType.Instances.Stopped, audioDevicePosition);
				this.listener.playbackFinished(playbackEvent);
			}
		}

		return shouldContinueReadingFrames;
	}

	/** Continua a execucao da musica
	 * @return <code>boolean</code> com <code>true</code> se a musica foi executada e <code>false</code> senao
	 * @throws JavaLayerException caso ocorra um erro ao executar a musica
	 */
	public boolean resume() throws JavaLayerException{
		return this.play(this.frameIndexCurrent);
	}

	/** Termina a execucao da musica
	 * 
	 */
	public synchronized void close(){
		if (this.audioDevice != null){
			this.closed = true;

			this.audioDevice.close();

			this.audioDevice = null;

			try{
				this.bitstream.close();
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

	/** Decodifica os frames da musica
	 * @return <code>boolean</code> com <code>true</code> se houve frames decodificados e <code>false</code> senao
	 * @throws JavaLayerException caso houve erros na decodificacao
	 */
	protected boolean decodeFrame() throws JavaLayerException{
		boolean returnValue = false;
		if(this.stopped){ //nothing for decode
			return false;
		}

		try{
			if (this.audioDevice != null){
				Header header = this.bitstream.readFrame();
				if (header != null){
					// sample buffer set when decoder constructed
					SampleBuffer output = (SampleBuffer) this.decoder.decodeFrame(header, this.bitstream);

					synchronized (this){
						if (this.audioDevice != null){
							this.audioDevice.write(output.getBuffer(), 0, output.getBufferLength());
						}
					}

					this.bitstream.closeFrame();
					returnValue = true;
				}
				else{
					System.err.println("End of file"); //end of file
					//this.stop();
					returnValue = false;
				}
			}
		}
		catch (RuntimeException ex){
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}
		return returnValue;
	}

	/** Pausa a execucao da musica
	 * 
	 */
	public void pause(){
		if(!this.stopped){
			this.paused = true;
			if (this.listener != null) {
				this.listener.playbackPaused(new PlaybackEvent(this, PlaybackEvent.EventType.Instances.Paused, this.audioDevice.getPosition()));
			}
			this.close();
		}
	}

	/** Pula os bits do cabecalho
	 * @return <code>boolean</code> com <code>true</code> se pulou bits e <code>false</code> senao
	 * @throws JavaLayerException caso ocorra algum erro
	 */
	protected boolean skipFrame() throws JavaLayerException{
		boolean returnValue = false;
		Header header = this.bitstream.readFrame();

		if (header != null) {
			this.bitstream.closeFrame();
			returnValue = true;
		}

		return returnValue;
	}

	/** Para a execucao da musica
	 * 
	 */
	public void stop(){
		if(!this.stopped){
			if(!this.closed){
				this.listener.playbackFinished(new PlaybackEvent(this, PlaybackEvent.EventType.Instances.Stopped, this.audioDevice.getPosition()));
				this.close();
			}
			else if(this.paused){
				int audioDevicePosition = -1; //this.audioDevice.getPosition(), audioDevice is null
				this.listener.playbackFinished(new PlaybackEvent(this, PlaybackEvent.EventType.Instances.Stopped, audioDevicePosition));
			}
			this.stopped = true;
		}
	}

	/** Retorna se a musica esta fechada ou nao
	 * @return <code>boolean</code> com <code>true</code> se esta e <code>false</code> senao
	 */
	public boolean isClosed() {
		return closed;
	}
	
	/** Retorna se a musica esta completa
	 * @return <code>boolean</code> com <code>true</code> se esta e <code>false</code> senao
	 */
	public boolean isComplete() {
		return complete;
	}

	/** Retorna se a musica esta pausada
	 * @return <code>boolean</code> com <code>true</code> se esta e <code>false</code> senao
	 */
	public boolean isPaused() {
		return paused;
	}

	/** Retorna se a musica esta parada
	 * @return <code>boolean</code> com <code>true</code> se esta e <code>false</code> senao
	 */
	public boolean isStopped() {
		return stopped;
	}
	
	/*public FloatControl getFloatControl() throws JavaLayerException{
		FloatControl controleVolume = null;
		SourceDataLine source = null;
		AudioFormat fmt = null;
		if (fmt == null) {
			if(this.decoder != null){
				fmt = new AudioFormat(this.decoder.getOutputFrequency(), 16, this.decoder.getOutputChannels(), true, false);
			}
			else{
				//fmt = new AudioFormat(44100, 16, 2, true, false);
				return null;
			}
		}
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt);
        Throwable excecao = null;
        try {
            Line line = AudioSystem.getLine(info);
            if (line instanceof SourceDataLine) {
                source = (SourceDataLine) line;
                source.open(fmt);
                source.start();
            }
        }
        catch (RuntimeException ex) {
            excecao = ex;
        }
        catch (LinkageError ex) {
            excecao = ex;
        }
        catch (LineUnavailableException ex) {
            excecao = ex;
        }
        if (source == null) {
            throw new JavaLayerException("Nao foi possivel obter a linha de audio", excecao);
        }
        else{
        	float gain = 0.0F;
        	if(source.isControlSupported(FloatControl.Type.MASTER_GAIN)){
        		controleVolume = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);
        	}
        	else if(source.isControlSupported(FloatControl.Type.VOLUME)){
                controleVolume = (FloatControl) source.getControl(FloatControl.Type.VOLUME);
            }
        	if(controleVolume != null){
	            float newGain = Math.min(Math.max(gain, controleVolume.getMinimum()), controleVolume.getMaximum());
	            controleVolume.setValue(newGain);
        	}
        }
        return controleVolume;
	}*/


	// inner classes
	/** Classe para os eventos da execucao da musica
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 * @see JLayerPlayerPausable
	 * @see EventType
	 */
	public static class PlaybackEvent{
		private JLayerPlayerPausable source;
		private EventType eventType;
		private int frameIndex;

		/** Cria uma instancia do evento
		 * @param source <code>JLayerPlayerPausable</code> objeto onde houve o evento
		 * @param eventType <code>EventType</code> tipo do evento
		 * @param frameIndex <code>int</code> com o indice do frame onde ocorreu o evento
		 * @see JLayerPlayerPausable
		 * @see EventType
		 */
		public PlaybackEvent(JLayerPlayerPausable source, EventType eventType, int frameIndex){
			this.source = source;
			this.eventType = eventType;
			this.frameIndex = frameIndex;
		}
		
		/** Retorna a referencia do objeto onde ocorreu o evento
		 * @return <code>JLayerPlayerPausable</code> com a referencia ao objeto
		 */
		public JLayerPlayerPausable getSource() {
			return source;
		}

		/** Retorna o tipo do evento
		 * @return <code>EventType</code> com o tipo do evento
		 * @see EventType
		 */
		public EventType getEventType() {
			return eventType;
		}

		/** Retorna o indice do frame
		 * @return <code>int</code> com o indice do frame
		 */
		public int getFrameIndex() {
			return frameIndex;
		}

		/** Classe com os tipos de eventos
		 * @author Arthur Assuncao
		 * @author Paulo Vitor
		 *
		 */
		public static class EventType{
			protected String name;

			/** Cria instancia do tipo do evento
			 * @param name <code>String</code> com o nome do evento
			 */
			public EventType(String name){
				this.name = name;
			}
			
			/** Retorna o tipo do evento
			 * @return <code>String</code> com o tipo do evento
			 */
			public String getName() {
				return name;
			}

			/** Classe com as instancias dos tipos de eventos
			 * @author Arthur Assuncao
			 * @author Paulo Vitor
			 *
			 */
			public static class Instances{
				/** <code>EventType</code> representando o tipo de evento Started(iniciado) */
				public static EventType Started = new EventType("Started");
				/** <code>EventType</code> representando o tipo de evento Paused(pausado) */
				public static EventType Paused = new EventType("Paused");
				/** <code>EventType</code> representando o tipo de evento Stopped(parado) */
				public static EventType Stopped = new EventType("Stopped");
			}
		}
	}

	/** Classe com implementacoes dos eventos
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 *
	 */
	public static class PlaybackAdapter implements PlaybackListener{
		@Override
		public void playbackStarted(PlaybackEvent event){
			System.err.println("Playback started");
		}
		@Override
		public void playbackPaused(PlaybackEvent event){
			System.err.println("Playback paused");
		}
		@Override
		public void playbackFinished(PlaybackEvent event){
			System.err.println("Playback stopped");
		}
	}

	/** Interface para tratadores de evento
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 * 
	 * @see PlaybackEvent
	 * @see PlaybackAdapter
	 */
	public static interface PlaybackListener{
		/** Metodo para tratar o evento Started(Iniciado)
		 * @param event <code>PlaybackEvent</code> com o evento
		 */
		public void playbackStarted(PlaybackEvent event);
		/** Metodo para tratar o evento Paused(Pausado)
		 * @param event <code>PlaybackEvent</code> com o evento
		 */
		public void playbackPaused(PlaybackEvent event);
		/** Metodo para tratar o evento playbackFinished(Terminado)
		 * @param event <code>PlaybackEvent</code> com o evento
		 */
		public void playbackFinished(PlaybackEvent event);
	}
}