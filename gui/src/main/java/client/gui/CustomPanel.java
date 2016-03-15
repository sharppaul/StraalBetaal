package client.gui;

import java.awt.Graphics;
import javax.swing.JPanel;

public class CustomPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
}
