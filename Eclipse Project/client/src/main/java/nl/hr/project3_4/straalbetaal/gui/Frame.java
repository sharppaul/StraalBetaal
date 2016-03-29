package nl.hr.project3_4.straalbetaal.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;

	private boolean fullScreen = false;

	private JFrame f;
	private CustomPanel mainPanel;
	private GridBagLayout l = new GridBagLayout();
	private GridBagConstraints c;
	private Font bigfont;
	private JLabel pin, customAmount;

	private final String[] modes = { "start", "login", "choice", "saldo", "pin", "billselect", "ticket", "success",
			"loading", "error" };
	private String mode = "start";
	private String error = "Er is iets misgegaan...";
	private boolean ticket;
	private float saldo = (float) 0.0;
	private boolean errored;
	private JLabel err = new JLabel("");
	private String[] billOption = { "biljet keuze 1", "biljet keuze 2", "biljet keuze 3" };

	public Frame() {
		bigfont = Fonts.createFont("ubuntu.ttf", (float) 24.0);
		f = new JFrame("GUI");
		f.setContentPane(new Background());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		if (fullScreen) {
			f.setUndecorated(true);
		}
		
		f.setLayout(new GridBagLayout());

		mainPanel = new CustomPanel();
		mainPanel.setLayout(l);
		mainPanel.setSize(new Dimension(1000, 1000));
		mainPanel.setBackground(new Color(224, 224, 224));

		f.getContentPane().add(mainPanel, new GridBagConstraints());
		f.pack();
		f.setVisible(true);
		// f.setResizable(false);

		loadMenu();
	}

	public static void main(String[] args) {
		Frame f = new Frame();
		f.scrollMenus(f);
	}

	// MANAGES WHICH MENU LOADS:
	public void scrollMenus(Frame f) {
		while (true) {
			for (int i = 0; i < this.modes.length; i++) {
				f.setMode(modes[i]);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		}
	}

	public void loadMenu() {
		mainPanel.removeAll();
		if (!isErrored()) {
			if (mode == "start")
				startMenu();
			if (mode == "login")
				loginMenu();
			if (mode == "choice")
				choiceMenu();
			if (mode == "saldo")
				saldoMenu(getSaldo());
			if (mode == "pin")
				pinMenu();
			if (mode == "billselect")
				billMenu();
			if (mode == "ticket")
				ticketMenu();
			if (mode == "success")
				successMenu();
			if (mode == "loading")
				loadingMenu();
			if (mode == "error")
				errorMenu(error);
		} else {
			mode = "error";
			errorMenu(error);
		}

		mainPanel.updateUI();
		mainPanel.repaint();
	}

	public void setMode(String mode) {
		if (Arrays.asList(modes).contains(mode)) {
			this.mode = mode;
			loadMenu();
		} else {
			System.err.println("setMode Error: Mode specified does not exist: " + mode + "\nAvailable modes: "
					+ Arrays.toString(modes));
		}
	}

	public String[] getBillOption() {
		return billOption;
	}

	public void setBillOption(String[] billOption) {
		this.billOption = billOption;
	}

	public void setPinErr(String error) {
		err.setText(error);
	}

	public void setPinAmount(int amount) {
		customAmount.setText("€" + amount + ",-");
	}

	public String getMode() {
		return this.mode;
	}

	public boolean isErrored() {
		return errored;
	}

	public void setErrored(boolean errored) {
		this.errored = errored;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return this.error;
	}

	public boolean wantsTicket() {
		return this.ticket;
	}

	public void setTicket(boolean ticket) {
		this.ticket = ticket;
	}

	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	// PANEL TOOLS:

	public void updatePanel() {
		mainPanel.updateUI();
	}

	public void clearPanel() {
		mainPanel.removeAll();
		mainPanel.updateUI();
	}

	// PIN TEXTFIELD TOOLS:

	public void resetPin() {
		pin.setText(" ");
	}

	public void addDotToPin(int dots) {
		pin.setText("");
		if (dots == 1) {
			pin.setText("●");
		} else {
			for(int i = 0; i < dots; i++){
				pin.setText(pin.getText() + " ●");
			}
		}
	}

	// BUTTON FUNCTION HANDLERS

	public void functie(String f) {

	}

	public void doesWantTicket() {
		System.out.println("doesWantTicket()");
		this.ticket = true;
		go();
	}

	public void doesNotWantTicket() {
		System.out.println("doesNotWantTicket()");
		this.ticket = false;
		go();
	}

	public void snelPin() {
		System.out.println("snelPin()");
		// start snelpinnen.
	}

	public void pinStart() {
		System.out.println("pinStart()");
		// start pinnen
	}

	public void viewSaldo() {
		System.out.println("viewSaldo()");
		// view saldo start.
	}

	public void cancel() {
		System.out.println("cancel()");
		System.exit(0);
	}

	public void go() {
		System.out.println("go()");
		// addDotToPin();
	}

	public void back() {
		System.out.println("back()");
	}

	// MENU LAYOUTS:

	private void successMenu() {
		c = new GridBagConstraints();
		clearPanel();
		JLabel message = new JLabel("Opdracht voltooid, verwijder uw pas.");
		message.setFont(bigfont);
		Image img = new Image("check.png");

		// MESSAGE:
		c.gridy = 0;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(40, 50, 40, 50);
		mainPanel.add(message);

		// check mark
		c.gridy++;
		c.ipadx = img.getWidth();
		c.ipady = img.getHeight();
		mainPanel.add(img, c);
	}

	public void ticketMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr = new JLabel("Wilt u een bon?");
		instr.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);

		// INSTRUCTIES:
		c.gridy = 0;
		mainPanel.add(instr, c);

		c.gridy++;
		c.gridwidth = 1;
		c.ipady = 35;
		mainPanel.add(ticketYesButton(), c);
		c.gridy++;
		mainPanel.add(ticketNoButton(), c);
		c.gridy++;

	}

	public void saldoMenu(float saldo) {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr, saldotxt;
		instr = new JLabel("Saldo: ");
		instr.setFont(bigfont);

		saldotxt = new JLabel("€" + Float.toString((float) Math.round(saldo * 100) / 100).replace(".", ","));
		saldotxt.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);

		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// SALDO:
		c.ipady = 35;
		c.gridy++;
		mainPanel.add(saldotxt, c);

		// OK BUTTON:
		c.gridy++;
		mainPanel.add(okButton(), c);

	}

	public void billMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr;
		instr = new JLabel("Kies biljetten:");
		instr.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);
		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// LOTTA BUTTONS:
		c.ipady = 35;
		c.gridy++;
		mainPanel.add(this.functionButton("billkeuzeA", getBillOption()[0] + "\t(A)"), c);
		c.gridy++;
		mainPanel.add(this.functionButton("billkeuzeB", getBillOption()[1] + "\t(B)"), c);
		c.gridy++;
		mainPanel.add(this.functionButton("billkeuzeC", getBillOption()[2] + "\t(C)"), c);

		c.gridy++;
		c.gridwidth = 1;
		mainPanel.add(cancelButton(), c);
	}

	public void pinMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr, other;
		instr = new JLabel("Maak uw keuze:");
		instr.setFont(bigfont);
		other = new JLabel("Of vul een bedrag in:");
		other.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);
		// customAmount
		customAmount = new JLabel();
		customAmount.setText(" ");
		customAmount.setFont(bigfont);
		customAmount.setHorizontalAlignment(JLabel.CENTER);
		customAmount.setBackground(Color.white);
		customAmount.setOpaque(true);
		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// LOTTA BUTTONS:
		c.ipady = 35;
		c.gridy++;
		mainPanel.add(this.functionButton("pinkeuzeA", "€ 50,- \t(A)"), c);
		c.gridy++;
		mainPanel.add(this.functionButton("pinkeuzeB", "€ 100,- \t(A)"), c);
		c.gridy++;
		mainPanel.add(this.functionButton("pinkeuzeC", "€ 200,- \t(A)"), c);

		c.ipady = 0;
		c.gridy++;

		mainPanel.add(other, c);

		c.ipady = 35;
		c.gridy++;

		mainPanel.add(customAmount, c);

		c.gridy++;
		c.gridwidth = 1;
		mainPanel.add(cancelButton(), c);
	}

	public void choiceMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr = new JLabel("Maak uw keuze:");
		instr.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);

		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// BUTTONS:
		c.ipady = 35;
		c.gridy++;
		mainPanel.add(snelPinButton(), c);
		c.gridy++;
		mainPanel.add(pinButton(), c);
		c.gridy++;
		mainPanel.add(saldoButton(), c);
		c.gridy++;
		c.gridwidth = 1;
		mainPanel.add(cancelButton(), c);
	}

	public void startMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel message = new JLabel("Vul uw pincode in:");
		message.setFont(bigfont);
		message.setText("Geef pinpas in");

		Image img = new Image("pinnen.png");

		// MESSAGE
		c.insets = new Insets(10, 50, 10, 50);
		c.gridwidth = 2;
		c.fill = GridBagConstraints.CENTER;
		c.gridy = 1;
		mainPanel.add(message, c);

		// IMAGE:
		c.gridy = 0;
		c.ipadx = img.getWidth();
		c.ipady = img.getHeight();
		mainPanel.add(img, c);
	}

	public void loadingMenu() {
		c = new GridBagConstraints();
		clearPanel();
		JLabel loadText = new JLabel();
		loadText.setFont(bigfont);
		loadText.setText("Laden...");
		loadText.setHorizontalAlignment(SwingConstants.CENTER);
		// MESSAGE:
		c.insets = new Insets(10, 50, 10, 50);
		c.gridwidth = 2;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		mainPanel.add(loadText, c);

		// Loading gif:
		Gif loading = new Gif("loading.gif");
		c.gridwidth = 2;
		c.ipady = loading.getHeight();
		c.ipadx = loading.getWidth();
		c.gridy++;
		mainPanel.add(loading, c);

	}

	public void errorMenu(String message) {
		c = new GridBagConstraints();
		clearPanel();
		JLabel error2 = new JLabel("", SwingConstants.CENTER);
		error2.setFont(bigfont);
		error2.setText(message);
		JLabel error = new JLabel("", SwingConstants.CENTER);
		error.setFont(bigfont);
		error.setText("Verwijder uw pas.");
		// MESSAGE:
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);
		mainPanel.add(error2, c);
		c.gridy++;
		mainPanel.add(error, c);

		// BUTTONS:
		Image img = new Image("error.png");
		c.ipady = img.getHeight();
		c.ipadx = img.getWidth();
		c.gridy++;
		mainPanel.add(img, c);

	}

	public void loginMenu() {
		c = new GridBagConstraints();
		clearPanel();
		pin = new JLabel();
		pin.setText(" ");
		//pin.setFont(bigfont);
		pin.setHorizontalAlignment(JLabel.CENTER);
		pin.setBackground(Color.white);
		pin.setOpaque(true);

		JLabel instr = new JLabel("Vul uw pincode in:");
		instr.setFont(bigfont);

		err.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);

		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// PINBOX:
		c.gridy++;
		c.ipady = 40;
		c.gridx = 0;
		mainPanel.add(pin, c);

		// BUTTONS:
		c.ipady = 35;
		c.gridy++;
		c.gridwidth = 1;
		mainPanel.add(cancelButton(), c);
		c.gridx = 1;
		mainPanel.add(okButton(), c);

		// ERRORS:
		err.setText("");
		c.gridwidth = 2;
		c.gridy++;
		mainPanel.add(err, c);
	}

	// BUTTONS AND BUTTON FUNCTIONS:

	public JButton ticketNoButton() {
		JButton btn = new JButton("Nee");
		btn.setFont(this.bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doesNotWantTicket();
			}
		});
		return btn;
	}

	public JButton ticketYesButton() {
		JButton btn = new JButton("Ja");
		btn.setFont(this.bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doesWantTicket();
			}
		});
		return btn;
	}

	public JButton closeButton() {
		JButton btn = new JButton("Close");
		btn.setFont(this.bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		return btn;
	}

	public JButton removeButton() {
		JButton btn = new JButton("remove");
		btn.setFont(this.bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPanel();
			}
		});
		return btn;
	}

	private JButton cancelButton() {
		JButton btn = new JButton("Stoppen");
		btn.setBackground(Color.red);
		btn.setFont(bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		return btn;
	}

	private JButton okButton() {
		JButton btn = new JButton("Doorgaan");
		btn.setBackground(Color.green);
		btn.setFont(bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				go();
			}
		});
		return btn;
	}

	private JButton snelPinButton() {
		JButton btn = new JButton("Snel Pinnen (€ 70,-)");
		btn.setFont(bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				snelPin();
			}
		});
		return btn;
	}

	private JButton pinButton() {
		JButton btn = new JButton("Bedrag Pinnen");
		btn.setFont(bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pinStart();
			}
		});
		return btn;
	}

	private JButton saldoButton() {
		JButton btn = new JButton("Saldo Bekijken");
		btn.setFont(bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewSaldo();
			}
		});
		return btn;
	}

	private JButton functionButton(String f, String inhoud) {
		JButton btn = new JButton(inhoud);
		btn.setFont(bigfont);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				functie(f);
			}
		});
		return btn;
	}
}
