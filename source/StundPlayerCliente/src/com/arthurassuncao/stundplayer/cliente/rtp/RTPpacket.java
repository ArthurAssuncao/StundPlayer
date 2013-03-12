package com.arthurassuncao.stundplayer.cliente.rtp;

/** RTPpacket
 *
 */
public class RTPpacket{

	//size of the RTP header:
	private static int HEADER_SIZE = 12;

	//Fields that compose the RTP header
	private int Version;
	private int Padding;
	private int Extension;
	private int CC;
	private int Marker;
	private int PayloadType;
	private int SequenceNumber;
	private int TimeStamp;
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
		Version = 2;
		Padding = 0;
		Extension = 0;
		CC = 0;
		Marker = 0;
		Ssrc = 0;

		//fill changing header fields:
		SequenceNumber = frameNumber;
		TimeStamp = time;
		PayloadType = PType;

		//build the header bistream:
		//--------------------------
		header = new byte[HEADER_SIZE];

		//fill the header array of byte with RTP header fields
		header[0] = new Integer((Version<<6)|(Padding<<5)|(Extension<<4)|CC).byteValue();
		header[1] = new Integer((Marker<<7)|PayloadType).byteValue();

		header[2] = new Integer(SequenceNumber>>8).byteValue();
		header[3] = new Integer(SequenceNumber).byteValue();

		for (int i=0; i < 4; i++){
			header[7-i] = new Integer(TimeStamp>>(8*i)).byteValue();
		}

		for (int i=0; i < 4; i++){
			header[11-i] = new Integer(Ssrc>>(8*i)).byteValue();
		}

		//fill the payload bitstream:
		//--------------------------
		payloadSize = dataLength;
		payload = new byte[dataLength];

		//fill payload array of byte from data (given in parameter of the constructor)
		for (int i=0; i < dataLength; i++){
			payload[i] = data[i];
		}
	}

	/**
	 *Constructor of an RTPpacket object from the packet bistream
	 * @param packet <code>byte[]</code> with packet
	 * @param packet_size <code>int</code> with packet size
	 */
	public RTPpacket(byte[] packet, int packet_size){
		//fill default fields:
		Version = 2;
		Padding = 0;
		Extension = 0;
		CC = 0;
		Marker = 0;
		Ssrc = 0;

		//check if total packet size is lower than the header size
		if (packet_size >= HEADER_SIZE){
			//get the header bitsream:
			header = new byte[HEADER_SIZE];
			for (int i=0; i < HEADER_SIZE; i++){
				header[i] = packet[i];
			}

			//get the payload bitstream:
			payloadSize = packet_size - HEADER_SIZE;
			payload = new byte[payloadSize];
			for (int i=HEADER_SIZE; i < packet_size; i++){
				payload[i-HEADER_SIZE] = packet[i];
			}

			//interpret the changing fields of the header :
			PayloadType = header[1] & 127;
			SequenceNumber = unsignedInt(header[3]) + 256*unsignedInt(header[2]);
			TimeStamp = unsignedInt(header[7]) + 256*unsignedInt(header[6]) + 65536*unsignedInt(header[5]) + 16777216*unsignedInt(header[4]);
		}
	}

	/**
	 * getpayload : return the payload bistream of the RTPpacket and its size
	 * @param data <code>byte[]</code> with data packet
	 * @return <code>int</code> with payload
	 */
	public int getPayload(byte[] data) {

		for (int i=0; i < payloadSize; i++){
			data[i] = payload[i];
		}

		return(payloadSize);
	}

	/**
	 * getpayload_length : return the length of the payload
	 * @return <code>int</code> with payload length
	 */
	public int getPayloadLength() {
		return(payloadSize);
	}

	/**
	 * getlength : return the total length of the RTP packet
	 * @return <code>int</code> with packet length
	 */
	public int getLength() {
		return(payloadSize + HEADER_SIZE);
	}

	/**
	 *getpacket : returns the packet bitstream and its length
	 * @param packet <code>byte[]</code> with packet
	 * @return <code>int</code> with total packet size
	 */
	public int getPacket(byte[] packet){
		//construct the packet = header + payload
		for (int i=0; i < HEADER_SIZE; i++){
			packet[i] = header[i];
		}
		for (int i=0; i < payloadSize; i++){
			packet[i+HEADER_SIZE] = payload[i];
		}

		//return total size of the packet
		return(payloadSize + HEADER_SIZE);
	}

	/**
	 * gettimestamp
	 * @return <code>int</code> with timestamp
	 */
	public int getTimestamp() {
		return(TimeStamp);
	}

	/**
	 * getsequencenumber
	 * @return <code>int</code> with sequence number
	 */
	public int getSequenceNumber() {
		return(SequenceNumber);
	}

	/**
	 * getpayloadtype
	 * @return <code>int</code> with payload type
	 */
	public int getPayloadType() {
		return(PayloadType);
	}


	/**
	 * print headers without the SSRC
	 */
	public void printHeader(){
		//TO DO : uncomment

		/*for (int i=0; i < (HEADER_SIZE-4); i++){
			for (int j = 7; j>=0 ; j--){
				if (((1<<j) & header[i] ) != 0){
					System.out.print("1");
				}
				else{
					System.out.print("0");
				}
			}
			System.out.print(" ");
		}
		System.out.println();*/
	}

	/**return the unsigned value of 8-bit integer number
	 * 
	 * @param number
	 * @return <code>int</code> with unsigned number
	 */
	private static int unsignedInt(int number) {
		if (number >= 0){
			return(number);
		}
		else{
			return(256 + number);
		}
	}
}


