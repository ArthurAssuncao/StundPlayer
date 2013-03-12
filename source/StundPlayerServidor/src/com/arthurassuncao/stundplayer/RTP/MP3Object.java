package com.arthurassuncao.stundplayer.RTP;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/** Reads and MP3 file and splits it up into frames.
 *  You can use ReadNext to read the next set of bytes in the queue
 *  Or you can use getFrame(int) to get a specific byte frame.
 * @author maherbeg
 *
 * Created: Apr 14, 2009
 * Modified: Apr 14, 2009
 */
public class MP3Object{
	private byte[] mp3;
	//Throw an exception about not being able to read the MP3 Object
	//     which passes it on to the parent, who can handle the stream cancel
	//     and notify the user
	//Let's go ahead and open an mp3 file
	private ArrayList<byte[]> frames;

	/** Create a MP3Object with specified file
	 * @param file <code>String</code> with mp3 file
	 * @throws IOException in file reader
	 */
	@SuppressWarnings("resource")
	public MP3Object(String file) throws IOException{
		DataInputStream buffer = null;
		buffer = new DataInputStream(new FileInputStream(file));
		mp3 = new byte[buffer.available()];
		buffer.readFully(mp3);

		//Now let's split that thing up into manageable frames
		frames = new ArrayList<byte[]>();

		System.out.println("MP3 file length: " + (mp3.length / 1024.0) / 1024 + " MB");
		//Let's iterate through and split this baby up
		for(int i=0; i < mp3.length; i++){

			if((i+1) < mp3.length && //make sure we won't go over the limit
					((int)mp3[i]&0xFF) == 0xFF && ((int)mp3[i+1] & 0xF0) == 0xF0) {//First 11 bits of frame are 1's
				int curframe = frames.size()+1;

				//We have found the frame sync, let's check the next few id's
				if((i+3) >= mp3.length){
					System.out.println("MP3Object.MP3Object(" + curframe + "): MP3 header went out of bounds");
					break;
				}

				int mp3header[] = {mp3[i], mp3[i+1], mp3[i+2], mp3[i+3]}; //32 bit mp3 header
				for(int x = 0; x < mp3header.length; x++)
					mp3header[x] = ((int)mp3header[x])&0xFF;

				if(((int)mp3header[2] & 0xF0) == 0xF0){ //uh oh, string of 11 1's possibly happened, this is illegal
					//System.out.println("MP3Object.MP3Object(" + curframe + "): Bad header, bit rate determined as bad.");
					continue;
				}
				i+=3; //move the counter forward 3 bytes

				short checksum = 0;
				boolean checksumfound = false;
				if( (mp3header[1] & 0x01) == 0){ //Check sum bytes, check sum exists
					//System.out.println("MP3Object.MP3Object(" + curframe + "): CRC Found for this frame");
					i++; //move past mp3 header
					checksum = (short)(((short)mp3[i] << 8) + mp3[i+1]);
					checksumfound = true;
					i+=1; //move 1 byte (i increases by one at the end
				}

				//Find the bit rate, indexes from a table which is based on the layer and version
				// Source: http://www.mpgedit.org/mpgedit/mpeg_format/MP3Format.html
				//Our table is slightly different, mostly because i wanted to do fast look ups
				//row is the value in the mp3 header [15,12], then index columns 
				//by (mpeg v2=1,mpegv1=0)<<2+(Layer I=11, Layer2=10, Layer3=01, Reserved=00)
				//which should give you a value from 0-8, if somehow it is anything bigger, you
				//will go out of bounds
				//      V1,RV V1,L3, V1,L2, V1,L1, V2,RV, V2,L3, V2,L2, V2,L1
				short bitratetable[][] = {      {0,             0xFFF,0xFFF,0xFFF,      0,      0xFFF,  0xFFF,0xFFF}, //first row is free
						{0,             32,             32,       32,   0,              8,              8,       32},
						{0,             40,             48,       64,   0,              16,             16,      48},
						{0,             48,         56,   96,   0,              24,             24,  56},
						{0,             56,             64,      128,   0,              32,             32,      64},
						{0,             64,             80,      160,   0,              40,             40,      80},
						{0,             80,             96,      192,   0,              48,             48,  96},
						{0,             96,             112, 224,       0,              56,             56,     112},
						{0,             112,    128, 256,       0,              64,             64,     128},
						{0,             128,    160, 288,       0,              80,             80, 144},
						{0,             160,    192, 320,       0,              96,             96, 160},
						{0,             192,    224, 352,       0,              112,   112, 176},
						{0,             224,    256, 384,       0,              128,   128, 192},
						{0,             256,    320, 416,       0,              144,   144, 224},
						{0,     320,    384, 448,       0,              160,   160, 256},
						{0,             0,              0,         0,   0,                0,     0,   0} //bad values
				};
				byte MPEGVersion = (byte)((mp3header[1] >> 3) & 0x03);
				if(MPEGVersion == 2) MPEGVersion = 1; //Helps to create the bit index, does not represent actual version, check table on website
				else                             MPEGVersion = 0;
				byte layer = (byte)((mp3header[1] >> 1) & 0x03);
				byte bitrateindex = (byte)((MPEGVersion << 2) + layer);
				if(bitrateindex > 8){
					System.out.println("MP3Object.MP3Object(" + curframe + "): Erroneous frame, can't find bit rate");
					continue;
				}

				short BitRate = bitratetable[(mp3header[2] >> 4)&0x0F][bitrateindex];
				if(BitRate == 0){
					System.out.println("MP3Object.MP3Object(" + curframe + "): Invalid bit rate of 0");
					continue;
				}

				if(BitRate == 0xFFF){
					System.out.println("MP3Object.MP3Object(" + curframe + "): BIT RATE IS FREE, CAN NOT HANDLE THIS TYPE OF FRAME.");
					continue;
				}

				//sampling frequency is indexed by row = bits[11,10] in the mp3 header, and 
				//columns = MPEGV2.5=00, reserved=01, MPEGV2=10, MPEGV1=11
				int  sampleratetable[][] = { {11025, 0, 22050, 44100},
						{12000, 0, 24000, 48000},
						{ 8000, 0, 16000, 32000},
						{    0, 0,             0,         0} //reserved state
				};

				int SampleRate = sampleratetable[ (mp3header[2] >> 2 & 0x03)][ (mp3header[1] >> 3) & 0x03];
				if(SampleRate == 0){
					System.out.println("MP3Object.MP3Object(" + curframe + "): Invalid sample rate...");
					continue;
				}
				int Padding = (mp3header[2] >> 1) & 0x01;
				if(Padding == 1){
					if(layer == 3){ //Layer I files have a 32 bit padding
						Padding = 4;
					} else{
						Padding = 1; //Layer II & III have an 8 bit padding
					}
				}
				//FrameSize = 144 * BitRate / (SampleRate + Padding)
				//source: http://www.hydrogenaudio.org/forums/lofiversion/index.php/t5956.html
				if( (SampleRate + Padding) == 0){
					System.out.println("MP3Object.MP3Object(" + curframe + "): Divide by zero calculation Frame size");
					continue;
				}
				//                              MPEG 1  MPEG 2  MPEG 2.5
				//                              Layer I         384     384     384
				//                              Layer II        1152    1152    1152
				//                              Layer III       1152    576     576
				short samplesPerFrameTable[][] = {      {384,           384,            384}, //first row is free
						{1152,          1152,           1152},
						{1152,          576,            576}
				};

				int samplesPerFrame = samplesPerFrameTable[3-layer][MPEGVersion];
				int slotSize;
				if(samplesPerFrame == 384)              slotSize = 4;
				else                                                    slotSize = 1;


				//Subtract four for the header
				int FrameSize = (int)(((samplesPerFrame / 8 * (BitRate*1000)) / SampleRate) + Padding) * slotSize-4;
				System.out.println("MP3Object.MP3Object(): Sample Rate: " + SampleRate + ", Bit Rate: " + BitRate + ", Frame Size: "+FrameSize);

				CRC check = new CRC();

				byte mp3frame[] = new byte[FrameSize];
				i++; //start byte from after checksum
				int EndFrame = i+FrameSize;
				for(int j=0; i < EndFrame && i < mp3.length; i++,j++){
					mp3frame[j] = mp3[i];
					check.addBits(mp3frame[j], 8);
				}
				i--; //we went past the end of the frame to exit the loop, so let's start earlier.

				int calculated = check.checksum();
				System.out.print("MP3Object.MP3Object(" + curframe + "): Frame Header: " + 
						Integer.toHexString(mp3header[0]) + Integer.toHexString(mp3header[1])
						+ Integer.toHexString(mp3header[2]) + Integer.toHexString(mp3header[3])
						+ " Length of MP3 File " + frames.size() * 1152.0 /SampleRate 
						+ " s, Calculated CRC: " + calculated + " - header checksum: "+ checksum);

				if(checksumfound && (checksum != calculated)){
					System.out.println("...Erroneous check sum found...skipping frame.");
					continue;
				}

				//copy header into a full frame
				byte fullframe[] = new byte[FrameSize+4];
				for(int index=0; index<4; index++)
					fullframe[index] = (byte)mp3header[index];

				for(int index=4; index < fullframe.length; index++)
					fullframe[index] = mp3frame[index-4];

				frames.add(fullframe); //just for seeing number of frames
				//calculate the track length for testing purposes
				//http://www.hydrogenaudio.org/forums/lofiversion/index.php/t43172.html
				//numberOfFrames * noOfSamplesPerFrame / SamplingRate
				System.out.println(" we're legit.");
			}//if
		}//for
		//System.out.println("MP3Object.MP3Object(): Number of good frames found: " + frames.size());
	}

	/** Return the frame
	 * @param index <code>int</code> with frame
	 * @return <code>byte[]</code> with frame bits
	 */
	public byte[] getFrame(int index){
		return frames.get(index);
	}

	/** Return the frame size
	 * @return <code>int</code> with frame size
	 */
	public int getSize(){
		return frames.size();
	}

	/*public static void main(String args[]) throws IOException, BitstreamException
	{
		MP3Object yo = new MP3Object("C:/Users/Paulo/Desktop/musica.mp3");
		Bitstream bs = new Bitstream(new FileInputStream("C:/Users/Paulo/Desktop/musica.mp3"));

		Header h = bs.readFrame();
		byte frame[] = null;
		int frameCount = 0;

		do{
			if(frameCount % 10 == 0)
				System.out.println(frameCount + ": " + h.bitrate_string() + " " + h.sample_frequency_string());
			frameCount++;
			//System.out.println(h.nSlots + " " + h.framesize);
			//all bytes of the frame
			for(int i=0; i < h.slots(); i++){
				bs.get_bits(8);
			}
			bs.closeFrame();
		}while( ( h = bs.readFrame()) != null);
	}*/

}
