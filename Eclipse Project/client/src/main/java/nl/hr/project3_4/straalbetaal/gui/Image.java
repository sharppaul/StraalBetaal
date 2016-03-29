package nl.hr.project3_4.straalbetaal.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Image extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image = null;
	
	

	public Image(String i) {
		super();
		
		try {
			this.image = ImageIO.read(new File("resources/"+i));
		} catch (IOException e) {
			System.err.println("Failed loading image: " + i);
		}
		this.setOpaque(false);
	}
	
	@Override
	public int getHeight(){
		return image.getHeight();
	}
	
	@Override
	public int getWidth(){
		return image.getWidth();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}
}
