package jarvis.controller.rfxtrx;

public class RFXtrxConfig {
    private String serialPortId;
    private byte[] controllerId;

    public String getSerialPortId() {
        return serialPortId;
    }

    public void setSerialPortId(String serialPortId) {
        this.serialPortId = serialPortId;
    }

	public byte[] getControllerId() {
		return controllerId;
	}

	public void setControllerId(byte[] controllerId) {
		this.controllerId = controllerId;
	}

	public void setControllerIdString(String controllerId) {
		if (controllerId == null || controllerId.length() != 6) {
			throw new IllegalArgumentException("ControllerId must be 6 chars: " + controllerId);
		}
		this.controllerId = new byte[3];
		this.controllerId[0] = (byte) Integer.parseInt(controllerId.substring(0, 2), 16);
		this.controllerId[1] = (byte) Integer.parseInt(controllerId.substring(2, 4), 16);
		this.controllerId[2] = (byte) Integer.parseInt(controllerId.substring(4, 6), 16);
	}
    
    
}
