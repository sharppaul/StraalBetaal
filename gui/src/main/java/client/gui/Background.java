package client.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Background extends JComponent {
	private static final long serialVersionUID = 1L;
	private BufferedImage bgImage = null;
	
	

	public Background() {
		super();
		
		try {
			
			this.bgImage = ImageIO.read(new File("resources/bg.png"));
			System.out.println("Loaded background image..");
		} catch (IOException e) {
			System.out.println("Failed loading background image..");
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth();  
        int height = getHeight();  
        for (int x = 0; x < width; x += bgImage.getWidth()) {  
            for (int y = 0; y < height; y += bgImage.getHeight()) {  
                g.drawImage(bgImage, x, y, this);  
            }  
        }  
	}
}
