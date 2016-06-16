package nl.hr.project3_4.straalbetaal.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.*;

import nl.hr.project3_4.straalbetaal.dispenser.Gelddispenser;
import nl.hr.project3_4.straalbetaal.language.Language;
import nl.hr.project3_4.straalbetaal.sound.Sound;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;

	private boolean fullScreen = true;

	private JFrame f;
	private CustomPanel mainPanel;
	private GridBagLayout l = new GridBagLayout();
	private GridBagConstraints c;
	private Font bigfont;
	private JLabel pin, customAmount;
	private final String[] modes = { "start", "login", "choice", "saldo", "pin", "billselect", "ticket", "success",
			"loading", "error", "donate" };
	private String mode = "loading";
	private String error = "wentwrong";
	private long saldo = 0;
	private boolean errored;
	private JLabel err = new JLabel("");
	private String[] billOption = { "biljet keuze 1", "biljet keuze 2", "biljet keuze 3" };
	private Language language = new Language(Language.EN);
	private boolean USSRTheme = false;

	public Frame() {

		f = new JFrame("StraalBetaal");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (fullScreen)
			f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		construct();
	}

	private void construct() {
		if (USSRTheme) {
			bigfont = Fonts.createFont("kremlin.ttf", (float) 24.0);
			f.setContentPane(new BackgroundUssr());
		} else {
			bigfont = Fonts.createFont("ubuntu.ttf", (float) 24.0);
			f.setContentPane(new Background());
		}

		f.getContentPane().setPreferredSize(new Dimension(1000, 1000));
		f.setUndecorated(fullScreen);

		f.setLayout(new GridBagLayout());

		mainPanel = new CustomPanel();
		mainPanel.setLayout(l);
		mainPanel.setSize(new Dimension(1000, 1000));

		if (USSRTheme) {
			mainPanel.setBackground(new Color(204, 0, 0));
		} else {
			mainPanel.setBackground(new Color(224, 224, 224));
		}

		f.getContentPane().add(mainPanel, new GridBagConstraints());
		f.pack();
		f.setVisible(true);
		// f.setResizable(false);

		loadMenu();
	}

	public void setLanguage(String language) {
		// only change to Language.EN/GER/NL!
		this.language = new Language(language);
		this.loadMenu();
	}

	public void goRussian() {
		f.dispose();
		USSRTheme = true;
		Sound s = new Sound("anthem.wav");
		s.play();
		construct();
	}

	public void goNormal() {
		f.dispose();
		USSRTheme = false;
		construct();
	}

	public static void main(String[] args) {
		Frame f = new Frame();
		// f.goRussian();

		// f.setMode("choice");
	}

	// MANAGES WHICH MENU LOADS:
	public void scrollMenus() {
		while (true) {

			for (int i = 0; i < this.modes.length; i++) {
				this.setMode(modes[i]);
				try {
					this.setLanguage(Language.EN);
					Thread.sleep(2000);

					this.setLanguage(Language.GER);
					Thread.sleep(2000);

					this.setLanguage(Language.NL);
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		}
	}

	public void loadMenu() {
		mainPanel.removeAll();
		if (!isErrored()) {
			if (mode.equals("start"))
				startMenu();
			if (mode.equals("login"))
				loginMenu();
			if (mode.equals("choice"))
				choiceMenu();
			if (mode.equals("saldo"))
				saldoMenu(getSaldo());
			if (mode.equals("pin"))
				pinMenu();
			if (mode.equals("billselect"))
				billMenu();
			if (mode.equals("donate"))
				donateMenu();
			if (mode.equals("ticket"))
				ticketMenu();
			if (mode.equals("success"))
				successMenu();
			if (mode.equals("loading"))
				loadingMenu();
			if (mode.equals("error"))
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

	public String formatAmount(long amount) {
		String amountStr = (amount / 100) + ",";
		long decimals = amount - ((amount / 100) * 100);
		if (decimals > 9) {
			amountStr += decimals;
		} else {
			amountStr += "0" + decimals;
		}

		return amountStr;
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
		customAmount.setText("G " + amount + ",-");
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

	public long getSaldo() {
		return this.saldo;
	}

	public void setSaldo(long saldo) {
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
		resetPin();
		for (int i = 0; i < dots; i++) {
			pin.setText(pin.getText() + " ● ");
		}

	}

	// BUTTON FUNCTION HANDLERS

	// MENU LAYOUTS:

	private void successMenu() {
		c = new GridBagConstraints();
		clearPanel();
		JLabel message = new JLabel(language.getString("done") + ". " + language.getString("thanks"));
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

	public void donateMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr = new JLabel(language.getString("donate"));
		JLabel desc = new JLabel(language.getString("donatedesc"));

		instr.setFont(bigfont);
		desc.setFont(bigfont);
		ImageButton ja = new ImageButton(language.getString("yes") + "     ", "ok.png");
		ja.setFont(bigfont);

		ImageButton nee = new ImageButton(language.getString("no") + "  ", "terug.png");
		nee.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 50, 10, 50);

		// INSTRUCTIES:
		c.gridy = 0;
		mainPanel.add(instr, c);
		c.gridy++;
		mainPanel.add(desc, c);

		c.gridy++;
		c.gridwidth = 1;
		c.ipady = 35;
		mainPanel.add(ja, c);
		c.gridy++;
		mainPanel.add(nee, c);
		c.gridy++;
	}

	public void ticketMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr = new JLabel(language.getString("receipt"));

		instr.setFont(bigfont);
		ImageButton ja = new ImageButton(language.getString("yes") + "    ", "ok.png");
		ja.setFont(bigfont);

		ImageButton nee = new ImageButton(language.getString("no") + "  ", "terug.png");
		nee.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);

		// INSTRUCTIES:
		c.gridy = 0;
		mainPanel.add(instr, c);

		c.gridy++;
		c.gridwidth = 1;
		c.ipady = 35;
		mainPanel.add(ja, c);
		c.gridy++;
		mainPanel.add(nee, c);
		c.gridy++;
	}

	public void saldoMenu(long saldo) {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr, saldotxt;
		instr = new JLabel(language.getString("balance") + ": ");
		instr.setFont(bigfont);

		saldotxt = new JLabel("G " + formatAmount(saldo));

		saldotxt.setFont(bigfont);

		instr.setFont(bigfont);
		ImageButton ja = new ImageButton(language.getString("cont") + " ", "ok.png");
		ja.setFont(bigfont);

		ImageButton nee = new ImageButton(language.getString("stop") + "    ", "stop.png");
		nee.setFont(bigfont);

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
		c.gridwidth = 2;
		c.gridy++;
		mainPanel.add(ja, c);
		c.gridy++;
		mainPanel.add(nee, c);

	}

	public void billMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr;
		instr = new JLabel(language.getString("bills") + ":");
		instr.setFont(bigfont);

		ImageButton a = new ImageButton(getBillOption()[0] + "  ", "a.png");
		a.setFont(bigfont);

		ImageButton b = new ImageButton(getBillOption()[1] + "  ", "b.png");
		b.setFont(bigfont);

		ImageButton cc = new ImageButton(getBillOption()[2] + "  ", "c.png");
		cc.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);
		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// LOTTA BUTTONS:
		c.ipady = 35;
		if (getBillOption()[0] != null) {
			c.gridy++;
			mainPanel.add(a, c);
		}
		if (getBillOption()[1] != null) {
			c.gridy++;
			mainPanel.add(b, c);
		}
		if (getBillOption()[2] != null) {
			c.gridy++;
			mainPanel.add(cc, c);
		}
		c.gridy++;
		c.gridwidth = 1;
		mainPanel.add(cancelButton(), c);
	}

	public void pinMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr;
		instr = new JLabel(language.getString("choice") + ":");
		instr.setFont(bigfont);

		Gelddispenser dispenser = Gelddispenser.getGelddispenser();

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);
		// customAmount

		ImageButton a = new ImageButton("G 50,-    ", "a.png");
		a.setFont(bigfont);

		ImageButton b = new ImageButton("G 100,-    ", "b.png");
		b.setFont(bigfont);
		
		ImageButton cc = new ImageButton("G 200,-    ", "c.png");
		cc.setFont(bigfont);
	
		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// LOTTA BUTTONS:
		c.ipady = 35;
		if (dispenser.existAskedOption(50)) {
			c.gridy++;
			mainPanel.add(a, c);
		}
		if (dispenser.existAskedOption(100)) {
			c.gridy++;
			mainPanel.add(b, c);
		}
		if (dispenser.existAskedOption(200)) {
			c.gridy++;
			mainPanel.add(cc, c);
		}

		c.gridy++;
		c.gridwidth = 1;
		mainPanel.add(cancelButton(), c);
	}

	public void choiceMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr = new JLabel(language.getString("choice") + ":");
		instr.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);

		ImageButton a = new ImageButton(language.getString("quickpin") + " (G70,-)   ", "a.png");
		a.setFont(bigfont);

		ImageButton b = new ImageButton(language.getString("seesaldo") + " ", "b.png");
		b.setFont(bigfont);

		ImageButton cc = new ImageButton(language.getString("withdraw") + "       ", "c.png");
		cc.setFont(bigfont);

		// INSTRUCTIES:
		c.gridwidth = 3;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// LOTTA BUTTONS:
		c.ipady = 30;
		c.gridy++;
		mainPanel.add(a, c);
		c.gridy++;
		mainPanel.add(b, c);
		c.gridy++;
		mainPanel.add(cc, c);

		c.gridy++;

		mainPanel.add(cancelButton(), c);

		c.ipady = 0;
		c.gridy++;
		mainPanel.add(languageRow("1", "2", "3"), c);
	}

	public void startMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel message = new JLabel();
		message.setFont(bigfont);
		message.setText(language.getString("welcome"));

		Image img = new Image("pinnen.png");

		// MESSAGE
		c.insets = new Insets(10, 50, 10, 50);
		c.gridwidth = 3;
		c.fill = GridBagConstraints.CENTER;
		c.gridy = 1;
		mainPanel.add(message, c);

		// IMAGE:
		c.gridy = 0;
		c.ipadx = img.getWidth();
		c.ipady = img.getHeight();
		mainPanel.add(img, c);

		c.ipady = 0;
		c.gridy = 2;
		mainPanel.add(languageRow("A", "B", "C"), c);
	}

	public void loadingMenu() {
		c = new GridBagConstraints();
		clearPanel();
		JLabel loadText = new JLabel();
		loadText.setFont(bigfont);
		loadText.setText(language.getString("loading") + "...");
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
		error2.setText(language.getString(message));
		JLabel error = new JLabel("", SwingConstants.CENTER);
		error.setFont(bigfont);
		error.setText(language.getString("removecard"));
		// MESSAGE:
		c.gridy = 0;
		c.fill = GridBagConstraints.CENTER;
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
		// pin.setFont(bigfont);
		pin.setHorizontalAlignment(JLabel.CENTER);
		pin.setBackground(Color.white);
		pin.setOpaque(true);

		JLabel instr = new JLabel(language.getString("enterpin") + ":");
		instr.setFont(bigfont);

		err.setFont(bigfont);
		err.setForeground(new Color(200, 0, 0));

		ImageButton a = new ImageButton(language.getString("stop") + "  ", "stop.png");
		a.setFont(bigfont);

		ImageButton b = new ImageButton(language.getString("cont") + "  ", "ok.png");
		b.setFont(bigfont);

		ImageButton corr = new ImageButton(language.getString("corr") + "  ", "terug.png");
		corr.setFont(bigfont);

		// GENERAL CONSTRAINTS:
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);

		// INSTRUCTIES:
		c.gridwidth = 3;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// PINBOX:
		c.gridy++;
		c.ipady = 80;
		c.gridx = 0;
		mainPanel.add(pin, c);

		// BUTTONS:
		c.ipady = 35;
		c.gridy++;
		c.gridwidth = 1;
		mainPanel.add(a, c);
		c.gridx = 2;
		mainPanel.add(b, c);

		// ERRORS:
		err.setText(" ");
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy++;
		mainPanel.add(err, c);
		c.gridx = 2;
		mainPanel.add(corr, c);

		c.gridwidth = 3;
		c.gridy++;
		c.gridx = 0;
		mainPanel.add(languageRow("A", "B", "C"), c);

	}

	public JPanel languageRow(String a, String b, String c) {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		ImageButton en = new ImageButton(" " + a, "us.png"), ger = new ImageButton(" " + b, "ger.png"),
				nl = new ImageButton(" " + c, "nl.png");
		en.setFont(bigfont);
		ger.setFont(bigfont);
		nl.setFont(bigfont);

		gbc.gridwidth = 1;

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridy = 0;
		gbc.gridx = 0;
		p.add(en, gbc);
		gbc.gridx = 1;
		p.add(ger, gbc);
		gbc.gridx = 2;
		p.add(nl, gbc);
		p.setBackground(new Color(0, 0, 0, 0));
		return p;
	}

	public ImageButton cancelButton() {
		ImageButton a = new ImageButton(language.getString("stop") + "  ", "stop.png");
		a.setFont(bigfont);
		return a;
	}

}
