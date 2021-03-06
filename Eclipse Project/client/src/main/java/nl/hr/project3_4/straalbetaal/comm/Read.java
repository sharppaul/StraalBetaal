package nl.hr.project3_4.straalbetaal.comm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.json.JSONException;
import org.json.JSONObject;

public class Read implements SerialPortEventListener {
	SerialPort serialPort;

	private static final String PORT_NAMES[] = { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7" };
	private BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 57600;

	/** Stuff for arduino data parsing: */
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
					System.out.println("Arduino on " + portId.getName());
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

	public synchronized void serialWrite(String data) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			try {
				String inputLine = input.readLine();

				JSONObject incomingJson = new JSONObject(inputLine);
				System.out.println(incomingJson.toString());
				System.out.println(Arrays.toString(incomingJson.keySet().toArray()));

				if (Arrays.asList(incomingJson.keySet().toArray()).contains("event")) {
					// EVENT: pin received from arduino.
					if (incomingJson.getString("event").equals("pinsend")) {
						data.receivePin(incomingJson.getString("pin"));
						System.out.println("event OK");
					}

					// EVENT: back to choice menu.
					if (incomingJson.getString("event").equals("backtomenu")) {
						data.setPressedBack(true);
						System.out.println("event OK");
					}

					// EVENT: error
					if (incomingJson.getString("event").equals("error")) {
						data.error(incomingJson.getString("error"));
						System.out.println("event OK");
					}

					// EVENT: pin dot to be added.
					if (incomingJson.getString("event").equals("pindot")) {
						data.addToPinLength();
						System.out.println("event OK");
					}

					// EVENT: choice from options
					if (incomingJson.getString("event").equals("choice")) {
						data.setChoice(incomingJson.getString("option"));
						System.out.println("event OK");
					}

					// EVENT: amount key
					if (incomingJson.getString("event").equals("amountkey")) {
						data.amountKey(incomingJson.getString("key"));
						System.out.println("event OK");
					}

					// EVENT: amount done
					if (incomingJson.getString("event").equals("amountdone")) {
						data.amountDone();
						System.out.println("event OK");
					}

					// EVENT: amount reset
					if (incomingJson.getString("event").equals("amountreset")) {
						data.amountReset();
						System.out.println("event OK");
					}

					// EVENT: chooses the bills
					if (incomingJson.getString("event").equals("billchoice")) {
						data.chooseBill(incomingJson.getString("option"));
						System.out.println("event OK");
					}

					// EVENT: chooses the bon option. (receipt)
					if (incomingJson.getString("event").equals("bonchoice")) {
						data.setBon(Boolean.valueOf(incomingJson.getString("option")));
						System.out.println("event OK");
					}

					// EVENT: chooses the donation option.
					if (incomingJson.getString("event").equals("donatechoice")) {
						data.setDonate(Boolean.valueOf(incomingJson.getString("option")));
						System.out.println("event OK");
					}

					// EVENT: reset
					if (incomingJson.getString("event").toString().equals("reset")) {
						data.resetSession();
						System.out.println("event OK");
					}

					// EVENT: reset pin digits.
					if (incomingJson.getString("event").equals("pinreset")) {
						data.resetPin();
						System.out.println("event OK");
					}

					// EVENT: reset pin digits.
					if (incomingJson.getString("event").equals("cardreceived")) {
						data.receiveCard(incomingJson.getString("card"));
						data.setBankID(Integer.parseInt(incomingJson.getString("bankid")));
						System.out.println("event OK");
					}

					// EVENT: language.
					if (incomingJson.getString("event").equals("language")) {
						data.setLanguage(incomingJson.getString("option"));
						System.out.println("event OK");
					}

					// EVENT: if no event matches...
					// {
					// System.err.println("Arduino event not recognized:
					// "+incomingJson.getString("event"));
					// }

				} else {
					System.err.println("Received filthy data from a filthy arduino:");
					System.err.println(inputLine);
					System.err.println(incomingJson.toString());
				}
			} catch (IOException e) {
				e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	public void printFatalError() {
		if (shouldError) {
			System.err.println("Fatal error, arduino probably got disconnected. Exception:");
			shouldError = false;
			data.resetSession();
			selfClosingMessage("Error, verbinding met het keypad verbroken. Verwijder uw pas.");
		}
	}

	public void selfClosingMessage(String message) {
		JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = pane.createDialog(null, "Error");
		dialog.setModal(false);
		dialog.setVisible(true);

		new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		}).start();
	}

	public void dispense(String dispenserValues) {
		try {
			byte[] forArduino = dispenserValues.getBytes(StandardCharsets.US_ASCII);
			output.write(forArduino);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
