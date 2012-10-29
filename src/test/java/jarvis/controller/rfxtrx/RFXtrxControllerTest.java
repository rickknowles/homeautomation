package jarvis.controller.rfxtrx;

import jarvis.device.ControlledDevice;
import junit.framework.TestCase;

public class RFXtrxControllerTest extends TestCase {
//	private final Log log = LogFactory.getLog(RFXtrxControllerTest.class);
	
	public void testShowSerialPorts() throws Exception {
		RFXtrxConfig config = new RFXtrxConfig();
		config.setSerialPortId("COM6");
		config.setControllerIdString("f0de13");
		
		ControlledDevice light = new ControlledDevice();
		light.setDeviceId(1);
		light.setLabel("Cinema room light");
		
		RFXtrxControlMessageBuilder messageBuilder = new RFXtrxControlMessageBuilder();
		messageBuilder.setConfig(config);
		
		RFXtrxController controller = new RFXtrxController();
		controller.setConfig(config);
		controller.setMessageBuilder(messageBuilder);
		controller.afterPropertiesSet();
		controller.showOpenSerialPorts();
		while (true) {
			controller.sendPowerOff(light);
			Thread.sleep(5000);
			controller.sendPowerOn(light);
			Thread.sleep(5000);
		}
	} 
}
