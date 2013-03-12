package com.arthurassuncao.stundplayer.RTP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Creates a UDP socket in a separate thread, and transmits data based on the file specified
 * @author maherbeg
 * @see MP3Object
 * @see Runnable
 *
 * Created: Apr 16, 2009
 * Modified: Apr 16, 2009
 */
public class RTPStream implements Runnable {
	public static final int FREQUENCE_SLEEP = (int)(1000*1000/90000.0*1000);
	private MP3Object tostream;
	private DatagramSocket socket;
	private int sequence = 0;

	private InetAddress destinationIP;
	private int destinationPort;

	private boolean sending = false;

	/** Verified if file is sending
	 * @return <code>boolean</code> with <code>true</code> if sending or <code>false</code> else.
	 */
	public boolean isSending() {
		return sending;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if(sending && sequence < tostream.getSize()){
			int curtime = (int)(System.currentTimeMillis()&0xFFFFFFFF); 
			//retrieve lower 32 bits of the system timer

			RTPpacket tosend = new RTPpacket(RTPpacket.PT_TYPE_MP3, sequence, curtime, 
					tostream.getFrame(sequence), tostream.getFrame(sequence).length);

			sequence++;

			byte payload[] = new byte[tosend.getLength()];
			tosend.getPacket(payload);

			DatagramPacket p = new DatagramPacket(payload, payload.length);
			p.setAddress(destinationIP);
			p.setPort(destinationPort);

			try {
				socket.send(p);
			}
			catch (IOException e1) {
				System.out.println("RTPStream.run(): Error sending frame " + (sequence-1) + "...skipping it");
				e1.printStackTrace();
			}
			
			try { //90khz timing
				Thread.sleep(0, FREQUENCE_SLEEP);
			}
			catch (InterruptedException e) {
				System.out.println("RTPStream.run(): Interrupted while waiting...");
				e.printStackTrace();
			}
		}//while

		//socket.close();
		//System.out.println("RTPStream.run(): Finished sending mp3 file");

	}

	/** Create a RTSP file stream
	 * @param file <code>String</code> file for send
	 * @param ip <code>InetAddress</code> destination ip
	 * @param port <code>int</code> destination port
	 */
	public RTPStream(String file, InetAddress ip, int port){
		destinationIP = ip;
		destinationPort = port;

		try {
			tostream = new MP3Object(file);
		}
		catch (IOException e) {
			System.out.println("RTPStream.RTPStream(): Can not parse MP3 File, abandoning stream.");
			e.printStackTrace();
			return;
		}

		try {
			socket = new DatagramSocket();//port, InetAddress.getByName(ip));
			//successfully read the file, let's connect
			socket.connect(ip, port);
		}
		catch (SocketException e) {
			System.out.println("RTPStream.RTPStream(): Error creating socket...abandoning stream.");
			e.printStackTrace();
			socket = null;
			tostream = null;
			return;
		}

		sending = true;
		//Thread t = new Thread(this);
		//t.start();
	}

	/** Return a object mp3
	 * @return <code>MP3Object</code>
	 */
	public MP3Object getTostream() {
		return tostream;
	}
	
	/*public static void main(String args[])
	{
		RTPStream rtp = new RTPStream("musica.mp3", "10.0.0.74", 1234);
	}*/
}
