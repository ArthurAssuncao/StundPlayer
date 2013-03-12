package com.arthurassuncao.stundplayer.player;

import javazoom.jl.decoder.JavaLayerException;

import com.arthurassuncao.stundplayer.player.JLayerPlayerPausable.PlaybackListener;

/** Player de musica que usa a biblioteca JLayer e permite o pause na execucao das musicas
 * @author Arthur Assuncao
 * @author http://thiscouldbebetter.wordpress.com/2011/07/04/pausing-an-mp3-file-using-jlayer/
 * 
 * @see JLayerPlayerPausable
 * @see Runnable
 */
public class SoundJLayer implements Runnable{
	protected String filePath;
	protected JLayerPlayerPausable player;
	protected Thread playerThread;
	protected String namePlayerThread = "AudioPlayerThread";
	private PlaybackListener playbackListener = new JLayerPlayerPausable.PlaybackAdapter();

	/** Cria o player com o endereco da musica
	 * @param filePath <code>String</code> com o endereco da musica
	 */
	public SoundJLayer(String filePath){
		this.filePath = filePath;
	}
	
	/** Cria o player com o endereco da musica
	 * @param filePath <code>String</code> com o endereco da musica
	 * @param namePlayerThread <code>String</code> com o nome da thread do player
	 * @see Thread
	 */
	public SoundJLayer(String filePath, String namePlayerThread){
		this.filePath = filePath;
		this.namePlayerThread = namePlayerThread;
	}

	/** Executa a musica, caso a musica esteja em execucao ou parada, a musica é reiniciada, caso esteja pausada, a musica é continuada
	 * 
	 */
	public void play(){
		if (this.player == null){
			this.playerInitialize();
		}
		else if(!this.player.isPaused() || this.player.isComplete() || this.player.isStopped()){
			this.stop();
			this.playerInitialize();
		}
		this.playerThread = new Thread(this, namePlayerThread);
		this.playerThread.setDaemon(true);

		this.playerThread.start();
	}

	/** Pausa a execucao da musica
	 * 
	 */
	public void pause(){
		if (this.player != null){
			this.player.pause();

			if(this.playerThread != null){
				//this.playerThread.stop(); //unsafe method
				this.playerThread = null;
			}
		}
	}

	/** Alterna entre pause e play na execucao da musica
	 * 
	 */
	public void pauseToggle(){
		if (this.player != null){
			if (this.player.isPaused() && !this.player.isStopped()){
				this.play();
			}
			else{
				this.pause();
			}
		}
	}

	/** Para a execucao da musica
	 * 
	 */
	public void stop(){
		if (this.player != null){
			this.player.stop();

			if(this.playerThread != null){
				//this.playerThread.stop(); //unsafe method
				this.playerThread = null;
			}
		}
	}
	
	/** Verifica se a musica esta completa, foi terminada
	 * @return <code>boolean</code> com <code>true</code> se a musica terminou de ser executada ou <code>false</code> senao
	 */
	public boolean isComplete(){
		return this.player.isComplete();
	}

	/*private void playerInitialize(){
		try{
			String urlAsString = 
					"file:///" 
							+ new java.io.File(".").getCanonicalPath() 
							+ "/" 
							+ this.filePath;

			this.player = new JLayerPlayerPausable(new java.net.URL(urlAsString));
			this.player.setPlaybackListener(this.playbackListener);
		}
		catch (JavaLayerException e){
			e.printStackTrace();
		}
	}*/
	
	/** Inicializa a execucao da musica
	 * 
	 */
	protected void playerInitialize(){
		try {
			this.player = new JLayerPlayerPausable(this.filePath);
			this.player.setPlaybackListener(this.playbackListener);
		}
		catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
	
	/*public FloatControl getFloatControl() throws JavaLayerException{
		return player.getFloatControl();
	}*/

	// IRunnable members
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		try{
			this.player.resume();
		}
		catch (javazoom.jl.decoder.JavaLayerException ex){
			ex.printStackTrace();
		}
	}
	
	/* Classe com os listeners de execucao da musica
	 * @author Arthur Assuncao
	 * @author Paulo Vitor
	 * @see JLayerPlayerPausable.PlaybackAdapter
	 */
	/*private static class PlaybackListener extends JLayerPlayerPausable.PlaybackAdapter {
		// PlaybackListener members
		@Override
		public void playbackStarted(JLayerPlayerPausable.PlaybackEvent playbackEvent){
			System.err.println("PlaybackStarted()");
		}
		
		@Override
		public void playbackPaused(JLayerPlayerPausable.PlaybackEvent playbackEvent){
			System.err.println("PlaybackPaused()");
		}

		@Override
		public void playbackFinished(JLayerPlayerPausable.PlaybackEvent playbackEvent){
			System.err.println("PlaybackStopped()");
		}
	}*/
}