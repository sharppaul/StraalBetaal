package nl.hr.project3_4.straalbetaal.gui;

import java.awt.Font;


public class Fonts {
	public static Font createFont(String fontname, float size) {
		
		try {
			Font t = Font.createFont(Font.TRUETYPE_FONT, Font.class.getResourceAsStream("/" + fontname));
			
			return t.deriveFont(size);
		} catch (Exception e) { 
			e.printStackTrace();
			System.err.println("Loading font failed, going back to default font..");
			return new Font("SansSerif", Font.BOLD, 24);
		}
	}

}
