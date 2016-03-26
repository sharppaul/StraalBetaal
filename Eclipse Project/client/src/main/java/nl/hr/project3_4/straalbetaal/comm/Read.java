package nl.hr.project3_4.straalbetaal.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Arrays;
import java.util.Enumeration;

import org.json.JSONException;
import org.json.JSONObject;

public class Read implements SerialPortEventListener {
	SerialPort serialPort;
	
	private static final String PORT_NAMES[] = { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7" };
	private BufferedReader input;
	@SuppressWarnings("unused")
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 57600;

	/** Stuff voor arduino data parsing: */
	ArduinoData data;

	public Read(ArduinoData data) {
		this.data = data;
		this.initialize();
	}

	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
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
			printFatalError(e);
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

				JSONObject incomingJson = new JSONObject(inputLine);

				if (Arrays.asList(incomingJson.keySet().toArray()).contains("event")) {
					// EVENT: pin received from arduino.
					if (incomingJson.getString("event").equals("pinsend")) {
						data.receivePin(incomingJson.getString("pin"), incomingJson.getString("card"));
					}

					// EVENT: error
					if (incomingJson.getString("event").equals("error")) {
						data.error(incomingJson.getString("error"));
					} else

					// EVENT: pin dot to be added.
					if (incomingJson.getString("event").equals("pindot")) {
						data.addToPinLength();
					} else

					// EVENT: choice from options
					if (incomingJson.getString("event").equals("choice")) {
						data.setChoice(incomingJson.getString("option"));
					} else

					// EVENT: amount key
					if (incomingJson.getString("event").equals("amountkey")) {
						data.amountKey(incomingJson.getString("key"));
					} else

					// EVENT: amount done
					if (incomingJson.getString("event").equals("amountdone")) {
						data.amountDone();
					} else

					// EVENT: amount reset
					if (incomingJson.getString("event").equals("amountreset")) {
						data.amountReset();
					} else

					// EVENT: chooses the bills
					if (incomingJson.getString("event").equals("billchoice")) {
						data.chooseBill(incomingJson.getString("option"));
					} else

					// EVENT: chooses the bon option. (receipt)
					if (incomingJson.getString("event").equals("bonchoice")) {
						data.setBon(Boolean.valueOf(incomingJson.getString("option")));
					} else

					// EVENT: reset
					if (incomingJson.getString("event").equals("reset")) {
						data.resetSession();
					} else

					// EVENT: reset pin digits.
					if (incomingJson.getString("event").equals("pinreset")) {
						data.resetPin();
					}

					// EVENT: reset pin digits.
					if (incomingJson.getString("event").equals("cardreceived")) {
						data.cardReceived();
					} else
						
					// EVENT: if no event matches...
					{
						System.err.println("Arduino event not recognized: "+incomingJson.getString("event"));
					}

				} else {
					System.err.println("Received filthy data from a filthy arduino:");
					System.err.println(incomingJson.toString());
				}
			} catch (IOException e) {
				printFatalError(e);
			} catch (JSONException e) {
				System.err.println("Received filthy data from a filthy arduino.");
				System.err.println(e.toString());
			}

		}
	}


	private boolean shouldError = true;

	public void printFatalError(Exception e) {
		if (shouldError) {
			printFatalError();
			System.err.println(e.toString());
		}
	}

	public void printFatalError() {
		if (shouldError) {
			System.err.println("Fatal error, arduino probably got disconnected. Exception:");
			shouldError = false;
		}
	}
}
