package com.arthurassuncao.stundplayer.RTP;

/**RTPpacket.java
 * Source Skeleton found from: http://www.csee.umbc.edu/~pmundur/courses/CMSC691C/RTPpacket.html
 * Modified by Maher Beg, Prudhvi Tella, and Alex Bednarczyk
 * 
 */

public class RTPpacket{

	//size of the RTP header:
	static int HEADER_SIZE = 12;
	/** <code>int</code> with mp3 code*/
	public static int PT_TYPE_MP3 = 14; //http://www.networksorcery.com/enp/protocol/rtp.htm (MPA RFC 2250, RFC 3551)

	//Fields that compose the RTP header
	private int version;
	private int padding;
	private int extension;
	private int CC;
	private int marker;
	private int payloadType;
	private int sequenceNumber;
	private int timeStamp;
	@SuppressWarnings("unused")
	private int Ssrc;

	//Bitstream of the RTP header
	private byte[] header;

	//size of the RTP payload
	private int payloadSize;
	//Bitstream of the RTP payload
	private byte[] payload;

	/**
	 * Constructor of an RTPpacket object from header fields and payload bitstream
	 * @param PType <code>int</code> with payload type
	 * @param frameNumber <code>int</code> with sequence frame number
	 * @param time <code>int</code> with time
	 * @param data <code>byte[]</code> with data packet
	 * @param dataLength <code>int</code> with data lenght
	 */
	public RTPpacket(int PType, int frameNumber, int time, byte[] data, int dataLength){
		//fill by default header fields:
		this.version = 2;
		this.padding = 0;
		this.extension = 0;
		this.CC = 0;
		this.marker = 0;
		this.Ssrc = 0;

		//fill changing header fields:
		this.sequenceNumber = frameNumber;
		this.timeStamp = time;
		this.payloadType = PType;

		//build the header bistream:
		//--------------------------
		this.header = new byte[HEADER_SIZE];
		for(int i=0; i < HEADER_SIZE; i++){ //initialize
			this.header[i] = 0;
		}

		//Source: http://www.networksorcery.com/enp/protocol/rtp.htm
		//version=2 bits, padding=1 bit, extension=1bit, CSRC count=4bits
		this.header[0] = (byte)((version << 6) + (padding << 5) + (extension << 4) + CC); //should equal 128 = 0x80
		//Marker=1 bit, Payload Type = 7 bits (14 for mp3)
		this.header[1] = (byte)((marker << 7) +payloadType&~0x80);
		//Sequence Number split over two bytes
		this.header[2] = (byte)((sequenceNumber & 0xFF00) >> 8);
		this.header[3] = (byte)((sequenceNumber&0xFF));
		//Timestamp = 32 bits
		this.header[4] = (byte)((timeStamp >> 24)&0xFF); header[5]=(byte)((timeStamp >> 16)&0xFF);
		this.header[6] = (byte)((timeStamp >> 8)&0xFF); header[7] = (byte)((timeStamp)&0xFF);

		//fill the payload bitstream:
		//--------------------------
		this.payloadSize = dataLength;
		this.payload = new byte[dataLength];
		for(int i=0; i < dataLength; i++){
			this.payload[i] = data[i];
		}


		// ! Do not forget to uncomment method printheader() below !

	}

	/**
	 * Constructor of an RTPpacket object from the packet bistream 
	 * @param packet <code>byte[]</code> with packet
	 * @param packetSize <code>int</code> with packet size
	 */
	public RTPpacket(byte[] packet, int packetSize){
		//fill default fields:
		this.version = 2;
		this.padding = 0;
		this.extension = 0;
		this.CC = 0;
		this.marker = 0;
		this.Ssrc = 0;

		//check if total packet size is lower than the header size
		if (packetSize >= HEADER_SIZE) {
			//get the header bitsream:
			this.header = new byte[HEADER_SIZE];
			for (int i=0; i < HEADER_SIZE; i++){
				this.header[i] = packet[i];
			}

			//get the payload bitstream:
			this.payloadSize = packetSize - HEADER_SIZE;
			this.payload = new byte[payloadSize];
			for (int i=HEADER_SIZE; i < packetSize; i++){
				this.payload[i-HEADER_SIZE] = packet[i];
			}

			//interpret the changing fields of the header:
			this.payloadType = header[1] & 127;
			this.sequenceNumber = unsignedInt(header[3]) + 256*unsignedInt(header[2]);
			this.timeStamp = unsignedInt(header[7]) + 256*unsignedInt(header[6]) + 65536*unsignedInt(header[5]) + 16777216*unsignedInt(header[4]);
		}
	}

	/**return the unsigned value of 8-bit integer number
	 * 
	 * @param number
	 * @return <code>int</code> with unsigned number
	 */
	private int unsignedInt(byte in){
		return (int)in & 0xFF;
	}

	/**
	 * getpayload: return the payload bistream of the RTPpacket and its size
	 * @param data <code>byte[]</code> with data packet
	 * @return <code>int</code> with payload
	 */
	public int getPayload(byte[] data) {

		for (int i=0; i < payloadSize; i++)
			data[i] = payload[i];

		return(payloadSize);
	}

	/**
	 * getpayload_length: return the length of the payload
	 * @return <code>int</code> with payload length
	 */
	public int getPayloadLength() {
		return(payloadSize);
	}

	/**
	 * getlength: return the total length of the RTP packet
	 * @return <code>int</code> with packet length
	 */
	public int getLength() {
		return(payloadSize + HEADER_SIZE);
	}

	/**
	 * getpacket: returns the packet bitstream and its length
	 * @param packet <code>byte[]</code> with packet
	 * @return <code>int</code> with total packet size
	 */
	public int getPacket(byte[] packet){
		//construct the packet = header + payload
		for (int i=0; i < HEADER_SIZE; i++)
			packet[i] = header[i];
		for (int i=0; i < payloadSize; i++)
			packet[i+HEADER_SIZE] = payload[i];

		//return total size of the packet
		return(payloadSize + HEADER_SIZE);
	}
	
	/**
	 * gettimestamp
	 * @return <code>int</code> with timestamp
	 */
	public int getTimestamp() {
		return(timeStamp);
	}

	/**
	 * getsequencenumber
	 * @return <code>int</code> with sequence number
	 */
	public int getSequenceNumber() {
		return(sequenceNumber);
	}

	/**
	 * getpayloadtype
	 * @return <code>int</code> with payload type
	 */
	public int getPayloadType() {
		return(payloadType);
	}


	/**
	 * print headers without the SSRC
	 */
	public void printHeader(){
		//TO DO: uncomment

		/*for (int i=0; i < (HEADER_SIZE-4); i++)
		{
			for (int j = 7; j>=0 ; j--)
				if (((1<= 0)
						return(nb);
				else
					return(256+nb);
		}*/

	}
}