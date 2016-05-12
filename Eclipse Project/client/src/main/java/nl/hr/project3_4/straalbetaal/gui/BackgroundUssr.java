package nl.hr.project3_4.straalbetaal.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class BackgroundUssr extends JComponent {
	private static final long serialVersionUID = 1L;
	private BufferedImage bgImage = null;

	public BackgroundUssr() {
		super();
		
		try {
			
			this.bgImage = ImageIO.read(BackgroundUssr.class.getResourceAsStream("/ussr.png"));
		} catch (IOException e) {
			System.out.println("Failed loading background image..");
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(204,0,0));
		g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(bgImage, getWidth()/20, getHeight()/20, this);   
	}
}
