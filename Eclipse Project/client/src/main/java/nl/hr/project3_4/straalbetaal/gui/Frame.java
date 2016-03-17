package nl.hr.project3_4.straalbetaal.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JFrame f;
	private CustomPanel mainPanel;
	private GridBagLayout l = new GridBagLayout();
	private GridBagConstraints c;
	private Font bigfont;
	private JLabel pin;
	private boolean ticket;

	public Frame() {
		bigfont = Fonts.createFont("ubuntu.ttf", (float) 24.0);
		f = new JFrame("GUI");
		f.setContentPane(new Background());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//f.setUndecorated(true);

		f.setLayout(new GridBagLayout());

		mainPanel = new CustomPanel();
		mainPanel.setLayout(l);
		mainPanel.setSize(new Dimension(1000, 1000));
		mainPanel.setBackground(new Color(100, 100, 100, 12));

		f.getContentPane().add(mainPanel, new GridBagConstraints());
		f.pack();
		f.setVisible(true);
		// f.setResizable(false);

		manageMenu();
	}

	public void clearPanel() {
		mainPanel.removeAll();
		mainPanel.updateUI();
	}

	public void addDotToPin() {
		System.out.println("addDotToPin()");
		if (pin.getText().equals(" ")) {
			pin.setText("●");
		} else {
			pin.setText(pin.getText() + " ●");
		}
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

	// MANAGES WHICH MENU LOADS:

	public void manageMenu() {
		mainPanel.removeAll();
		choiceMenu();
		// pinMenu();
		// saldoMenu((float) 2.1233);
		// bonMenu();
		// startMenu();
		mainPanel.updateUI();
	}

	// MENU LAYOUTS:

	public void bonMenu() {
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

		c.gridx = 0;
		mainPanel.add(cancelButton(), c);

	}

	public void saldoMenu(float saldo) {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr, saldotxt;
		instr = new JLabel("Saldo: ");
		instr.setFont(bigfont);

		saldotxt = new JLabel("€" + saldo);
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

	public void amountMenu() {
		c = new GridBagConstraints();
		clearPanel();

		JLabel instr, stuff;
		instr = new JLabel("Maak uw keuze:");
		instr.setFont(bigfont);
		stuff = new JLabel("Whole lot of buttons will come here. ");
		stuff.setFont(bigfont);

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
		mainPanel.add(stuff, c);

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
		c.fill = GridBagConstraints.CENTER;
		c.ipadx = img.getWidth();
		c.ipady = img.getHeight();
		mainPanel.add(img, c);
	}

	public void errorMenu(String message) {
		c = new GridBagConstraints();
		clearPanel();
		JLabel error = new JLabel("Vul uw pincode in:");
		error.setFont(bigfont);
		error.setText("Fout: \n" + message);
		// MESSAGE:
		c.gridwidth = 2;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 50, 10, 50);
		mainPanel.add(error);

		// BUTTONS:
		c.gridwidth = 1;
		c.ipady = 35;
		c.gridy = 1;
		c.gridwidth = 1;
		mainPanel.add(cancelButton(), c);

	}

	public void pinMenu() {
		c = new GridBagConstraints();
		clearPanel();
		pin = new JLabel();
		pin.setText(" ");
		pin.setFont(bigfont);
		pin.setHorizontalAlignment(JLabel.CENTER);
		pin.setBackground(Color.white);
		pin.setOpaque(true);

		JLabel instr = new JLabel("Vul uw pincode in:");
		instr.setFont(bigfont);

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
	}

	// BUTTONS AND BUTTON FUNCTIONS:

	public JButton ticketNoButton() {
		JButton btn = new JButton("Nee");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doesNotWantTicket();
			}
		});
		return btn;
	}

	public JButton ticketYesButton() {
		JButton btn = new JButton("Ja");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doesWantTicket();
			}
		});
		return btn;
	}

	public JButton closeButton() {
		JButton btn = new JButton("Close");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		return btn;
	}

	public JButton removeButton() {
		JButton btn = new JButton("remove");
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
}
