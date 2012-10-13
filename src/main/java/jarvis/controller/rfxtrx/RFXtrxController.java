package jarvis.controller.rfxtrx;

import jarvis.device.ControlChannel;
import jarvis.device.ControlMessageBuilder;
import jarvis.device.ControlledDevice;
import jarvis.device.ReceiveEventListener;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RFXtrxController implements ControlChannel, Callable<Void>, SerialPortEventListener {
    private final Log log = LogFactory.getLog(RFXtrxController.class);

    private RFXtrxConfig config;
    private ControlMessageBuilder<RFXtrxMessage> messageBuilder;
    private List<ReceiveEventListener> listeners;

    // state vars
    private SerialPort port;
    private InputStream serialIn;
    
    public void sendPowerOn(ControlledDevice device) throws Exception {
        messageTx(this.messageBuilder.buildMessage(device, "powerOn"));
    }
    
    public void showOpenSerialPorts() {
    	log.info("Scanning for open serial ports");
        Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();
        while (e.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) e.nextElement();
            log.info("Port: " + port.getName());
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL && !port.isCurrentlyOwned()) {
                log.info("Serial port: " + port.getName());
            }
        }
    }
    
    public Void call() throws Exception {
        messageRx();
        return null;
    }
    
    private void messageTx(RFXtrxMessage message) throws Exception {
        ensureOpenPort();
        synchronized (port) {
        }
    }
    
    private void messageRx() throws Exception {
        ensureOpenPort();
        synchronized(port) {
            
        }
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.DATA_AVAILABLE:
            log.info("data received");
            break;
        default:
            break;
        }
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
                this.port.addEventListener(this);
            }
        }
    }
    
    public void setConfig(RFXtrxConfig config) {
        this.config = config;
    }

    public void setMessageBuilder(ControlMessageBuilder<RFXtrxMessage> messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    public void setListeners(List<ReceiveEventListener> listeners) {
        this.listeners = listeners;
    }
}
