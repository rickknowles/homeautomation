package jarvis.controller.rfxtrx;

public class RFXtrxControlMessage {
	
//	BYTE packetlength; 
//	 BYTE packettype; 
//	 BYTE subtype; 
//	 BYTE seqnbr; 
//	 BYTE id1; 
//	 BYTE id2; 
//	 BYTE id3; 
//	 BYTE unitcode; 
//	 BYTE cmnd; 
//	 BYTE level; 
//	 BYTE filler  : 4; 
//	 BYTE rssi : 4; 
	
	public byte packettype = 0x14;
	public byte subtype = 0x00;
	public byte seqnbr;
	public byte id1;
	public byte id2;
	public byte id3;
	public byte unitcode;
	public byte cmnd;
	public byte level = 0x10; // 0x0 to 0x1F for 0-100%
	
	public byte[] toByteArray() {
		byte[] out = new byte[11];
		out[0] = 10; // length
		out[1] = packettype;
		out[2] = subtype;
		out[3] = seqnbr;
		out[4] = id1;
		out[5] = id2;
		out[6] = id3;
		out[7] = unitcode;
		out[8] = cmnd;
		out[9] = level;
		out[10] = (byte) 0x70;
		return out;
	}
}
