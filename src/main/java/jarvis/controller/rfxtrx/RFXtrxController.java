package jarvis.controller.rfxtrx;

import jarvis.device.ControlChannel;
import jarvis.device.ControlMessageBuilder;
import jarvis.device.ControlMessageType;
import jarvis.device.ControlledDevice;
import jarvis.device.ReceiveEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class RFXtrxController implements ControlChannel, SerialPortEventListener, InitializingBean {
    private final Log log = LogFactory.getLog(RFXtrxController.class);

    private RFXtrxConfig config;
    private ControlMessageBuilder<RFXtrxControlMessage> messageBuilder;
    private List<ReceiveEventListener> listeners;

    // state vars
    private SerialPort port;
    private InputStream serialIn;
    
    @Override
	public void afterPropertiesSet() throws Exception {
    	ensureOpenPort();
	}

	public void sendPowerOn(ControlledDevice device) throws Exception {
        messageTx(this.messageBuilder.buildMessage(device, ControlMessageType.POWER_ON));
    }

	public void sendPowerOff(ControlledDevice device) throws Exception {
        messageTx(this.messageBuilder.buildMessage(device, ControlMessageType.POWER_OFF));
    }
    
    public void showOpenSerialPorts() {
    	log.info("Scanning for open serial ports");
        Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();
        while (e.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) e.nextElement();
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL && !port.isCurrentlyOwned()) {
                log.info("Serial port: " + port.getName());
            }
        }
    }
    
    private void messageTx(RFXtrxControlMessage message) throws Exception {
    	byte[] msg = message.toByteArray();
    	log.info("Sending data packet = " + hexConvert(msg, 0, msg.length));
    	
    	
        synchronized (port) {
        	OutputStream outStream = null;
        	try {
            	outStream = port.getOutputStream();
            	outStream.write(msg);
            	outStream.flush();
        	} finally {
        		if (outStream != null) {
        			outStream.close();
        		}
        	}
        }
    }

    public void serialEvent(SerialPortEvent event) {
        synchronized (port) {
	    	try {
	            switch(event.getEventType()) {
	            case SerialPortEvent.DATA_AVAILABLE:
	            	int len = this.serialIn.available();
	            	log.info("Incoming data packet of length " + len);
	            	byte[] buf = new byte[len];
	            	int pos = 0;
	            	int read;
	            	while ((read = this.serialIn.read(buf, pos, buf.length - pos)) > 0) {
	            		pos += read;
	            	}
	            	log.info("Incoming data packet = " + hexConvert(buf, 0, pos));
	                break;
	            default:
	                break;
	            }    		
	    	} catch (IOException err) {
	    		log.error(err);
	    		throw new RuntimeException("IOError", err);
	    	}
        }
    }

    private static String hexConvert(byte[] packet, int off, int len) {
    	String out = "";
    	for (int n = 0; n < len; n++) {
    		out += Integer.toHexString(((int) packet[off + n]) & 0xff) + " ";
    	}
    	
    	return "[ " + out + "]";
    }
    
    private void ensureOpenPort() throws Exception {
        synchronized (this) {
            if (this.port == null) {
                CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(config.getSerialPortId());
                this.port = (SerialPort) portId.open("jarvis", 2000);
                this.port.setSerialPortParams(38400, SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                this.serialIn = this.port.getInputStream(); 
                this.port.notifyOnDataAvailable(true);
                this.port.addEventListener(this);
                log.info("Opened serial port to RFXtrx");
            }
        }
    }
    
    public void setConfig(RFXtrxConfig config) {
        this.config = config;
    }

    public void setMessageBuilder(ControlMessageBuilder<RFXtrxControlMessage> messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    public void setListeners(List<ReceiveEventListener> listeners) {
        this.listeners = listeners;
    }
}
