package nl.hr.project3_4.straalbetaal.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class Fonts {
	public static Font createFont(String fontname, float size) {
		
		try {
			Font t = Font.createFont(Font.TRUETYPE_FONT, new File("resources/"+ fontname));
			return t.deriveFont(size);
		} catch (IOException e) { 
			System.err.println("Loading font failed, going back to default font..");
			return new Font("SansSerif", Font.BOLD, 20);
		} catch (FontFormatException e) {
			System.err.println("Loading font failed, going back to default font..");
			return new Font("SansSerif", Font.BOLD, 20);
		}
	}

}
