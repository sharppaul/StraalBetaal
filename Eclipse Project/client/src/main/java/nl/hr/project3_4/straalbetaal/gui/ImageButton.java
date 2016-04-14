package nl.hr.project3_4.straalbetaal.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	
	public ImageButton(String file) {
		super(new ImageIcon(Gif.class.getResource("/"+file)));
	}
	
	public ImageButton(String message, String file) {
		super(message, new ImageIcon(Gif.class.getResource("/"+file)));
	}

}
