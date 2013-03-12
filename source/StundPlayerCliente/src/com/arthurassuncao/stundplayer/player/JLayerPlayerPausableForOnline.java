package com.arthurassuncao.stundplayer.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.Timer;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;

import com.arthurassuncao.stundplayer.player.JLayerPlayerPausable.PlaybackEvent.EventType;

/**
 * @author Arthur Assuncao
 *
 */
public class JLayerPlayerPausableForOnline extends JLayerPlayerPausable {
	
	private long fileSize; //tamanho do arquivo
	private int timeWait = 1; //tempo de espera em segundos
	private Timer timerWait;
	private boolean waiting;
	private final int maxBytesLost = 100; //maximo de bytes aceitados como perdidos
	//private PlaybackListenerForOnline listener;
	
	
	/** Cria um player com o endereco da musica num <code>URL</code>
	 * @param urlToStreamFrom <code>URL</code> com o caminho da musica
	 * @throws JavaLayerException caso ocorra um erro do JLayer
	 * @see URL
	 */
	public JLayerPlayerPausableForOnline(URL urlToStreamFrom, long fileSize, int timeForWait) throws JavaLayerException{
		super(urlToStreamFrom);
		this.fileSize = fileSize;
		this.timeWait = timeForWait;
		this.timerWait = new Timer(this.timeWait * 1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				timerWait.stop();
				waiting = false;
			}
		});
		this.timerWait.setCoalesce(false);
		this.timerWait.setInitialDelay(this.timeWait * 1000);
	}
	
	/** Cria um player com o endereco da musica num <code>URL</code>
	 * @param audioPath <code>String</code> com o caminho da musica
	 * @throws JavaLayerException caso ocorra um erro do JLayer
	 */
	public JLayerPlayerPausableForOnline(String audioPath, long fileSize, int timeForWait) throws JavaLayerException{
		super(audioPath);
		this.fileSize = fileSize;
		this.timeWait = timeForWait;
		this.timerWait = new Timer(this.timeWait * 1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evento) {
				timerWait.stop();
				waiting = false;
			}
		});
		this.timerWait.setCoalesce(false);
		this.timerWait.setInitialDelay(this.timeWait * 1000);
	}
	
	/** Seta o tratador de eventos do player
	 * @param newPlaybackListener <code>PlaybackListenerForOnline</code> com o tratador de eventos
	 * @throws NullPointerException caso o listener seja <code>null</code>
	 * @see PlaybackListener
	 * @see PlaybackListenerForOnline
	 */
	public void setPlaybackListener(PlaybackListenerForOnline newPlaybackListener) throws NullPointerException{
		super.setPlaybackListener(newPlaybackListener);
		if(newPlaybackListener != null){
			this.listener = (PlaybackListenerForOnline)newPlaybackListener;
		}
		else{
			throw new NullPointerException("PlaybackListener is null");
		}
	}
	
	/** Executa a musica
	 * @param frameIndexStart <code>int</code> com o frame inicial da execucao
	 * @param frameIndexFinal <code>int</code> com o frame final da execucao
	 * @param correctionFactorInFrames <code>int</code> com o numero de frames perdidos ao dar pause
	 * @return <code>boolean</code> com <code>true</code> se a musica foi executada e <code>false</code> senao
	 * @throws JavaLayerException caso ocorra um erro ao executar a musica
	 */
	@Override
	public boolean play(int frameIndexStart, int frameIndexFinal, int correctionFactorInFrames) throws JavaLayerException{ //aproveitamento de codigo == 0
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
		//this.waiting = false;
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
			if (this.paused || this.stopped || this.waiting){
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
		
		if(!shouldContinueReadingFrames && !this.stopped){ //chegou no fim do arquivo, mas sera q carregou tudo?
			long tamanhoArquivoAtual = this.getFileSize();
			if(tamanhoArquivoAtual + maxBytesLost < fileSize){
				//entra em modo de espera(waiting)
				System.out.println(tamanhoArquivoAtual);
				System.out.println(fileSize);
				this.waiting();
			}
		}

		// last frame, ensure all data flushed to the audio device.
		if (this.audioDevice != null && !this.paused && !this.waiting){
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
	
	/** Faz uma pausa na execucao da musica
	 * 
	 */
	public void waiting(){
		if(!this.stopped){
			this.waiting = true;
			if (this.listener != null) {
				((PlaybackAdapterForOnline)this.listener).playbackWaiting(new PlaybackEvent(this, EventTypeForOnline.InstancesForOnline.Waiting, this.audioDevice.getPosition()));
			}
			//this.close();
			this.timerWait.restart();
		}
	}
	
	/** Verifica se a musica esta em estado de espera
	 * @return <code>boolean</code> com <code>true</code> se a musica esta e </code>false</code> senao
	 */
	public boolean isWaiting(){
		return waiting;
	}
	
	/** Classe com os tipos de eventos
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 *
	 */
	protected static class EventTypeForOnline extends EventType{
		/** Cria instancia do tipo do evento
		 * @param name <code>String</code> com o nome do evento
		 */
		public EventTypeForOnline(String name){
			super(name);
		}
		
		/** Classe com as instancias dos tipos de eventos
		 * @author Arthur Assuncao
		 * @author Paulo Vitor
		 *
		 */
		public static class InstancesForOnline extends Instances{
			/** <code>EventType</code> representando o tipo de evento Waiting(esperando) */
			public static EventType Waiting = new EventType("Waiting");
		}
	}
	
	/** Classe com implementacoes dos eventos
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 *
	 */
	public static class PlaybackAdapterForOnline extends PlaybackAdapter implements PlaybackListenerForOnline{
		@Override
		public void playbackWaiting(PlaybackEvent event) {
			System.err.println("Playback waiting");
		}
	}

	/** Interface para tratadores de evento
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 * 
	 * @see PlaybackEvent
	 * @see PlaybackAdapter
	 */
	public static interface PlaybackListenerForOnline extends PlaybackListener{
		/** Metodo para tratar o evento playbackWaiting(Esperando)
		 * @param event <code>PlaybackEvent</code> com o evento
		 */
		public void playbackWaiting(PlaybackEvent event);
	}
	
}
