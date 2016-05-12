package nl.hr.project3_4.straalbetaal.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.*;

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
			"loading", "error" };
	private String mode = "loading";
	private String error = "Er is iets misgegaan...";
	private float saldo = (float) 0.0;
	private boolean errored;
	private JLabel err = new JLabel("");
	private String[] billOption = { "biljet keuze 1", "biljet keuze 2", "biljet keuze 3" };

	private boolean USSRTheme = false;

	public Frame() {
		bigfont = Fonts.createFont("ubuntu.ttf", (float) 24.0);
		f = new JFrame("StraalBetaal");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		construct();
	}

	private void construct() {
		if (USSRTheme) {
			f.setContentPane(new BackgroundUssr());
		} else {
			f.setContentPane(new Background());
		}

		

		if (fullScreen) {
			f.setUndecorated(true);
		}

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
		f.goRussian();
		f.scrollMenus();
		// f.setMode("login");
	}

	// MANAGES WHICH MENU LOADS:
	public void scrollMenus() {
		while (true) {
			for (int i = 0; i < this.modes.length; i++) {
				this.setMode(modes[i]);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					System.out.println(e);
				}
				if (USSRTheme) {
					//goNormal();
				} else {
					goRussian();
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
		JLabel message = new JLabel("De opdracht is voltooid, verwijder alstublieft uw pas."
				+ " Bedankt voor het pinnen bij straalbetaal en tot ziens!");
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
		ImageButton ja = new ImageButton("Ja     ", "ok.png");
		ja.setFont(bigfont);
		ja.setHorizontalTextPosition(SwingConstants.LEFT);

		ImageButton nee = new ImageButton("Nee  ", "terug.png");
		nee.setFont(bigfont);
		nee.setHorizontalTextPosition(SwingConstants.LEFT);

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

	public void saldoMenu(float saldo) {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr, saldotxt;
		instr = new JLabel("Saldo: ");
		instr.setFont(bigfont);

		saldotxt = new JLabel("€" + Float.toString((float) Math.round(saldo * 100) / 100).replace(".", ","));
		saldotxt.setFont(bigfont);

		instr.setFont(bigfont);
		ImageButton ja = new ImageButton("Doorgaan ", "ok.png");
		ja.setFont(bigfont);
		ja.setHorizontalTextPosition(SwingConstants.LEFT);

		ImageButton nee = new ImageButton("Stoppen    ", "stop.png");
		nee.setFont(bigfont);
		nee.setHorizontalTextPosition(SwingConstants.LEFT);

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
		instr = new JLabel("Kies biljetten:");
		instr.setFont(bigfont);

		ImageButton a = new ImageButton(getBillOption()[0] + "  ", "a.png");
		a.setFont(bigfont);
		a.setHorizontalTextPosition(SwingConstants.LEFT);
		a.setHorizontalAlignment(SwingConstants.RIGHT);

		ImageButton b = new ImageButton(getBillOption()[1] + "  ", "b.png");
		b.setFont(bigfont);
		b.setHorizontalTextPosition(SwingConstants.LEFT);
		b.setHorizontalAlignment(SwingConstants.RIGHT);

		ImageButton cc = new ImageButton(getBillOption()[2] + "  ", "c.png");
		cc.setFont(bigfont);
		cc.setHorizontalTextPosition(SwingConstants.LEFT);
		cc.setHorizontalAlignment(SwingConstants.RIGHT);

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
		mainPanel.add(a, c);
		c.gridy++;
		mainPanel.add(b, c);
		c.gridy++;
		mainPanel.add(cc, c);

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

		ImageButton a = new ImageButton("€ 50,-    ", "a.png");
		a.setFont(bigfont);
		a.setHorizontalTextPosition(SwingConstants.LEFT);
		a.setHorizontalAlignment(SwingConstants.RIGHT);

		ImageButton b = new ImageButton("€ 100,-    ", "b.png");
		b.setFont(bigfont);
		b.setHorizontalTextPosition(SwingConstants.LEFT);
		b.setHorizontalAlignment(SwingConstants.RIGHT);

		ImageButton cc = new ImageButton("€ 200,-    ", "c.png");
		cc.setFont(bigfont);
		cc.setHorizontalTextPosition(SwingConstants.LEFT);
		cc.setHorizontalAlignment(SwingConstants.RIGHT);

		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// LOTTA BUTTONS:
		c.ipady = 35;
		c.gridy++;
		mainPanel.add(a, c);
		c.gridy++;
		mainPanel.add(b, c);
		c.gridy++;
		mainPanel.add(cc, c);

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

		ImageButton a = new ImageButton("Snelpinnen (€70,-)   ", "a.png");
		a.setFont(bigfont);
		a.setHorizontalTextPosition(SwingConstants.LEFT);
		a.setHorizontalAlignment(SwingConstants.RIGHT);

		ImageButton b = new ImageButton("Saldo Bekijken      ", "b.png");
		b.setFont(bigfont);
		b.setHorizontalTextPosition(SwingConstants.LEFT);
		b.setHorizontalAlignment(SwingConstants.RIGHT);

		ImageButton cc = new ImageButton("Pinnen             ", "c.png");
		cc.setFont(bigfont);
		cc.setHorizontalTextPosition(SwingConstants.LEFT);
		cc.setHorizontalAlignment(SwingConstants.RIGHT);

		// INSTRUCTIES:
		c.gridwidth = 2;
		c.gridy = 0;
		mainPanel.add(instr, c);

		// LOTTA BUTTONS:
		c.ipady = 35;
		c.gridy++;
		mainPanel.add(a, c);
		c.gridy++;
		mainPanel.add(b, c);
		c.gridy++;
		mainPanel.add(cc, c);

		c.gridy++;
		c.gridwidth = 1;
		mainPanel.add(cancelButton(), c);
	}

	public void startMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel message = new JLabel();
		message.setFont(bigfont);
		message.setText("Welkom bij Straalbetaal, voer alstublieft uw pinpas in.");

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
		error.setText("Verwijder alstublieft uw pas.");
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

		JLabel instr = new JLabel("Vul uw pincode in:");
		instr.setFont(bigfont);

		err.setFont(bigfont);
		err.setForeground(new Color(200, 0, 0));

		ImageButton a = new ImageButton("Stoppen  ", "stop.png");
		a.setFont(bigfont);
		a.setHorizontalTextPosition(SwingConstants.LEFT);
		a.setHorizontalAlignment(SwingConstants.RIGHT);

		ImageButton b = new ImageButton("Doorgaan  ", "ok.png");
		b.setFont(bigfont);
		b.setHorizontalTextPosition(SwingConstants.LEFT);
		b.setHorizontalAlignment(SwingConstants.RIGHT);

		ImageButton corr = new ImageButton("Correctie  ", "terug.png");
		corr.setFont(bigfont);
		corr.setHorizontalTextPosition(SwingConstants.LEFT);
		corr.setHorizontalAlignment(SwingConstants.RIGHT);

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
	}

	public ImageButton cancelButton() {
		ImageButton a = new ImageButton("Stoppen  ", "stop.png");
		a.setFont(bigfont);
		a.setHorizontalTextPosition(SwingConstants.LEFT);
		a.setHorizontalAlignment(SwingConstants.RIGHT);
		return a;
	}

}
