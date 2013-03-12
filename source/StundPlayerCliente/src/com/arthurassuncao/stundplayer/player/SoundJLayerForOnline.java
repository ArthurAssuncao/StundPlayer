package com.arthurassuncao.stundplayer.player;

import javazoom.jl.decoder.JavaLayerException;

import com.arthurassuncao.stundplayer.player.JLayerPlayerPausableForOnline.PlaybackListenerForOnline;

/**
 * @author Arthur Assuncao
 *
 */
public class SoundJLayerForOnline extends SoundJLayer {
	
	private long fileSize; //tamanho do arquivo
	private int timeWait; //tempo de espera
	private PlaybackListenerForOnline playbackListener = new JLayerPlayerPausableForOnline.PlaybackAdapterForOnline();
	
	/** Cria o player com o endereco da musica
	 * @param filePath <code>String</code> com o endereco da musica
	 */
	public SoundJLayerForOnline(String filePath, long fileSize, int timeWait){
		super(filePath);
		this.fileSize = fileSize;
		this.timeWait = timeWait;
	}
	
	/** Cria o player com o endereco da musica
	 * @param filePath <code>String</code> com o endereco da musica
	 * @param namePlayerThread <code>String</code> com o nome da thread do player
	 * @see Thread
	 */
	public SoundJLayerForOnline(String filePath, String namePlayerThread, long fileSize, int timeWait){
		super(filePath, namePlayerThread);
		this.fileSize = fileSize;
		this.timeWait = timeWait;
	}
	
	/** Inicializa a execucao da musica
	 * 
	 */
	protected void playerInitialize(){
		try {
			this.player = new JLayerPlayerPausableForOnline(this.filePath, this.fileSize, this.timeWait);
			this.player.setPlaybackListener(this.playbackListener);
		}
		catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		do{
			try{
				if(!((JLayerPlayerPausableForOnline)this.player).isWaiting()){
					System.err.println("INICIOU A MUSICA");
					this.player.resume();
				}
			}
			catch (javazoom.jl.decoder.JavaLayerException ex){
				ex.printStackTrace();
			}
			if( ((JLayerPlayerPausableForOnline)this.player).isWaiting() ){
				//evita q a musica feche
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
		}while( !this.player.isComplete() && !this.player.isPaused() && !this.player.isStopped() );
	}
	
	/** Verifica se a musica esta em estado de espera
	 * @return <code>boolean</code> com <code>true</code> se a musica esta e </code>false</code> senao
	 */
	public boolean isWaiting(){
		return ((JLayerPlayerPausableForOnline)this.player).isWaiting();
	}
	
}
