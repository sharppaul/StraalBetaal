package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.*;
import nl.hr.project3_4.straalbetaal.comm.*;
import nl.hr.project3_4.straalbetaal.gui.*;
import nl.hr.project3_4.straalbetaal.print.LabelWriter;
import nl.hr.project3_4.straalbetaal.exceptions.*;

@SuppressWarnings("unused")
public class Client {
	private Frame frame;
	private ArduinoData data;
	private Read reader;
	private ClientBackEnd backend;
	private int transNummer;

	public static void main(String[] args) {

		Client client = new Client();

	}

	public Client() {
		frame = new Frame();
		data = new ArduinoData();
		reader = new Read(data);

		while (true) {
			try {
				this.transNummer = 0;
				frame.setMode("start");
				while (!cardInserted()) {
					sleep(100);
				}

				shouldReset();

				
				frame.setMode("login");
				int dots = 0;
				while (!data.isPinReceived()) {
					sleep(100);
					frame.addDotToPin(data.getPinLength());
					pinErrorOccured();
					shouldReset();
				}
				
				backend = new ClientBackEnd(data.getCard());
				System.out.println(data.getCard());
				frame.setMode("loading");
				checkPinValid();

				boolean userNotDone = true;
				while (userNotDone) {
					data.setPressedBack(false);
					shouldReset();
					frame.setMode("choice");

					while (!userMadeChoice()) {
						// WAIT FOR USER
						sleep(100);
						shouldReset();
					}

					frame.setMode("loading");
					shouldReset();

					if (data.getChoice().equals("a")) {
						snelPinnen();
						userNotDone = false;
					} else if (data.getChoice().equals("c")) {

						frame.setMode("pin");
						while (!userEnteredAmount()) { // user hasn't entered
														// amount
							sleep(100);
							shouldReset();
						}

						shouldReset();
						data.chooseBill(null);
						calculateBills();
						frame.setMode("billselect");
						while (!billSelected()) { // user hasn't chosen bills
							sleep(100);
							shouldReset();
						}
						frame.setMode("loading");

						pinnen();

						shouldReset();
						frame.setMode("ticket");
						while (!ticketIsChosen()) { // user hasn't chosen ticket
							sleep(100);
							shouldReset();
						}
						frame.setMode("loading");
						if(data.getBon()){
							new LabelWriter().printLabel(Integer.toString(transNummer), (long) data.getAmount(), data.getCard());
						}
						userNotDone = false;

					} else if (data.getChoice().equals("b")) {
						frame.setMode("loading");
						frame.setSaldo(this.requestSaldo());
						frame.setMode("saldo");
						while (!data.isPressedBack()) {
							sleep(100);
							shouldReset();
						}
						data.setChoice("none");
					}
				}

				shouldReset();
				frame.setMode("success");
				while (true) {
					sleep(100);
					finished();
				}

			} catch (ResetException e) {
				frame.setError(e.getMessage());
				frame.setMode("error");

				e.printStackTrace();
				try {
					Thread.sleep(2500);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			} catch (SuccessException e) {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public void checkPinValid() throws ResetException {
		CheckPinRequest rq = new CheckPinRequest(data.getPinEncrypted());
		CheckPinResponse rs = backend.checkPincode(rq);
		if (rs.getUserID().equals("wrong")) {
			data.reset();
			throw new ResetException("Pincode incorrect.");
		}
		if (rs.getUserID().equals("blocked")) {
			data.reset();
			throw new ResetException("Pinpas geblokkeerd.");
		}
	}

	public float requestSaldo() throws ResetException {
		BalanceResponse rs = backend.checkBalance();
		return rs.getBalance();
	}

	private void snelPinnen() throws ResetException {
		WithdrawRequest rq = new WithdrawRequest(70L);
		WithdrawResponse rs = backend.withdrawMoney(rq);
		if (rs.getResponse().equals("succes")) {
			// store transaction number and amount etc.
		} else {
			data.reset();
			throw new ResetException("Saldo te laag.");
		}
	}

	private void pinnen() throws ResetException {
		WithdrawRequest rq = new WithdrawRequest((long) data.getAmount());
		WithdrawResponse rs = backend.withdrawMoney(rq);
		System.out.println(rs.getResponse());
		if (rs.getResponse().equals("succes")) {
			this.transNummer = rs.getTransactionNumber();
		} else {
			data.reset();
			throw new ResetException("Saldo te laag.");
		}
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	private void calculateBills() {
		int amount = data.getAmount();
		// calculates some stuff, and will return a string with few amounts.
		// quite an interesting piece of software to make.
		String[] options = new String[3];

		options[0] = "Keuze 1";
		options[1] = "Keuze 2";
		options[2] = "Keuze 3";

		if (amount == 50) {
			options[0] = "1*€50";
			options[1] = "1*€10 & 2*€20";
			options[2] = "5*€10";
		}
		if (amount == 100) {
			options[0] = "1*€100";
			options[1] = "2*€50";
			options[2] = "5*€20";
		}
		if (amount == 200) {
			options[0] = "2*€100";
			options[1] = "4*€50";
			options[2] = "2*€50 & 5*€20";
		}

		frame.setBillOption(options);
	}

	private void finished() throws SuccessException {
		if (data.isReset()) {
			throw new SuccessException("Finished.");
		}
	}

	private boolean userEnteredAmount() {
		frame.setPinAmount(data.getAmount());
		if (data.isAmountDone()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean billSelected() {
		if (data.getBillOption() != null) {
			return true;
		}
		return false;
	}

	private boolean ticketIsChosen() {
		if (data.getBon() == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean cardInserted() {
		return data.isCardReceived();
	}

	public boolean userMadeChoice() {
		if (data.getChoice().equals("none")) {
			return false;
		} else {
			return true;
		}
	}

	public void pinErrorOccured() {
		if (data.isErrored()) {
			frame.setPinErr(data.getError());
			frame.addDotToPin(0);
			data.resetPin();
			data.setErrored(false);
		}
	}

	public void shouldReset() throws ResetException {
		if (data.isReset()) {
			throw new ResetException("Pinsessie afgebroken.");
		}
	}

}
