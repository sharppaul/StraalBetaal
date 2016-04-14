package nl.hr.project3_4.straalbetaal.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Gif extends JLabel {
	private static final long serialVersionUID = 1L;
	
	
	public Gif(String file) {
		super(new ImageIcon(Gif.class.getResource("/"+file)));
	}

}
