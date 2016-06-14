package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.*;
import nl.hr.project3_4.straalbetaal.comm.*;
import nl.hr.project3_4.straalbetaal.dispenser.Gelddispenser;
import nl.hr.project3_4.straalbetaal.gui.*;
import nl.hr.project3_4.straalbetaal.language.Language;
import nl.hr.project3_4.straalbetaal.printer.LabelWriter;
import nl.hr.project3_4.straalbetaal.throwables.*;

@SuppressWarnings("unused")
public class Client {
	private Frame frame;
	private ArduinoData data;
	private Read reader;
	private ClientBackEnd backend;
	private long transNummer;
	private String language = "EN";
	private Gelddispenser dispenser;

	public static void main(String[] args) {

		Client client = new Client();

	}

	public Client() {
		frame = new Frame();
		data = new ArduinoData();
		reader = new Read(data);
		backend = new ClientBackEnd();
		dispenser = Gelddispenser.getGelddispenser();

		while (true) {
			try {
				this.transNummer = 0;
				frame.setMode("start");
				while (!cardInserted()) {
					sleep(100);
					checkLanguage();
				}
				
				this.checkPas();
				this.shouldReset();

				frame.setMode("login");
				int dots = 0;
				while (!data.isPinReceived()) {
					sleep(100);
					frame.addDotToPin(data.getPinLength());
					pinErrorOccured();
					shouldReset();
					checkLanguage();
				}

				frame.setMode("loading");
				backend = new ClientBackEnd();

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
						checkLanguage();
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

						shouldReset();
						frame.setMode("donate");
						while (!donateIsChosen()) { // user hasn't chosen ticket
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
						if (data.getBon()) {
							new LabelWriter().printLabel(Long.toString(transNummer), (long) data.getAmount(),
									data.getCard(), data.getDonate());
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

			} catch (Reset reset) {
				frame.setError(reset.getMessage());
				frame.setMode("error");
				this.sleep(5000);
			} catch (Success succes) {
				this.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
				this.sleep(500);
			}
		}
	}

	private void checkPas() throws Reset {
		CheckPasRequest rq = new CheckPasRequest(data.getBankID(), data.getCard());
		CheckPasResponse rs = backend.checkPas(rq);
		if (!rs.doesExist()) {
			data.reset();
			throw new Reset("unreadable");
		}

	}

	private void checkPinValid() throws Reset {
		CheckPinRequest rq = new CheckPinRequest(data.getBankID(), data.getCard(), data.getPin());
		CheckPinResponse rs = backend.checkPincode(rq);
		if (!rs.isCorrect()) {
			data.reset();
			throw new Reset("incorrect");
		}
		if (rs.isBlocked()) {
			data.reset();
			throw new Reset("blocked");
		}
	}

	private long requestSaldo() {
		BalanceResponse rs = backend.checkBalance(new BalanceRequest(data.getBankID(), data.getCard()));
		return rs.getBalance();
	}

	private void snelPinnen() throws Reset {
		WithdrawRequest rq = new WithdrawRequest(data.getBankID(), data.getCard(), 7000L);
		WithdrawResponse rs = backend.withdrawMoney(rq);
		if (rs.isSucceeded()) {
			// store transaction number and amount etc.
		} else {
			data.reset();
			throw new Reset("toolow");
		}
	}

	private void pinnen() throws Reset {
		long saldo = this.requestSaldo() - (data.getAmount() * 100);
		long donateAmount = saldo - (long) ((int) (saldo / 100) * 100);
		WithdrawRequest rq;

		if (donateAmount == 0)
			donateAmount = 50;

		if (saldo >= 0) {
			if (data.getDonate()) {
				rq = new WithdrawRequest(data.getBankID(), data.getCard(), (long) (data.getAmount() * 100) + donateAmount);
			} else {
				rq = new WithdrawRequest(data.getBankID(), data.getCard(), (long) (data.getAmount() * 100));
			}
			WithdrawResponse rs = backend.withdrawMoney(rq);
			System.out.println("Pinning succeeded: " + rs.isSucceeded());
			if (rs.isSucceeded()) {
				this.transNummer = rs.getTransactieNummer();
			} else {
				data.reset();
				throw new Reset("toolow");
			}
		} else {
			throw new Reset("toolow");
		}

	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	private void calculateBills() {
		dispenser.existAskedOption(data.getAmount());
		String[] options = dispenser.getOptionsForSpecificAmount();

		/*
		if (amount == 50) {
			options[0] = "1x €50";
			options[1] = "1x €10 & 2x €20";
			options[2] = "5x €10";
		}
		if (amount == 100) {
			options[0] = "1x €100";
			options[1] = "2x €50";
			options[2] = "5x €20";
		}
		if (amount == 200) {
			options[0] = "2x €100";
			options[1] = "4x €50";
			options[2] = "2x €50 & 5x €20";
		}
		*/

		frame.setBillOption(options);
	}

	private void finished() throws Success {
		if (data.isReset()) {
			throw new Success("done");
		}
	}

	private boolean userEnteredAmount() {
		// frame.setPinAmount(data.getAmount());
		
		if (data.isAmountDone()) {
			dispenser.existAskedOption(data.getAmount());
			return true;
		} else {
			return false;
		}
	}

	private void checkLanguage() {
		if (!this.language.equals(data.getLanguage())) {
			this.language = data.getLanguage();
			if (this.language.equals("EN"))
				frame.setLanguage(Language.EN);
			if (this.language.equals("GER"))
				frame.setLanguage(Language.GER);
			if (this.language.equals("NL"))
				frame.setLanguage(Language.NL);
		}
	}

	private boolean billSelected() {
		if (data.getBillOption() != null) {
			dispenser.removeChosenBillsFromDispenser(data.getBillOption());
			return true;
		}
		return false;
	}

	private boolean donateIsChosen() {
		if (data.getDonate() == null) {
			return false;
		} else {
			return true;
		}
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

	public void shouldReset() throws Reset {
		if (data.isReset()) {
			throw new Reset("abort");
		}
	}

}
