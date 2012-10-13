package jarvis.controller.rfxtrx;

import junit.framework.TestCase;

public class RFXtrxControllerTest extends TestCase {

	public void testShowSerialPorts() throws Exception {
		RFXtrxConfig config = new RFXtrxConfig();
		config.setSerialPortId("COM1");
		
		RFXtrxController controller = new RFXtrxController();
		controller.setConfig(config);
		controller.showOpenSerialPorts();
	} 
}
