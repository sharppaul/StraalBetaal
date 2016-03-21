package nl.hr.project3_4.straalbetaal.comm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Arrays;
import java.util.Enumeration;

import org.json.JSONObject;

public class Read implements SerialPortEventListener {
	SerialPort serialPort;
	private static final String PORT_NAMES[] = { 
			"COM1",
			"COM2",
			"COM3",
			"COM4",
			"COM5",
			"COM6",
			"COM7"
	};
 	private BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	
	/** Stuff voor arduino data parsing: */
	ArduinoData data;
	
	public Read(ArduinoData data){
		this.data = data;
		this.initialize();
	}
	
	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					System.out.println("Activity on " + portId.getName());
					System.out.println("Waiting for card.");
					break;
				}
			}
		}
		
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			printFatalError();
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				System.out.println("Incoming data: " + inputLine);
				//JSONObject t = new JSONObject(inputLine);
				//System.out.println(Arrays.toString(t.keySet().toArray())); 
			} catch (Exception e) {
				printFatalError();
			}
		}
	}
	
	private boolean shouldError = true;
	public void printFatalError(){
		if(shouldError){
			System.err.println("Fatal error, arduino probably got disconnected. \nPlug in arduino and restard client to fix.");
			shouldError = false;
		}
	}
}
