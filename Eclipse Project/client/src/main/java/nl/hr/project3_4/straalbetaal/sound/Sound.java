package nl.hr.project3_4.straalbetaal.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class Sound {
	private String filename = "";
	private Clip clip;
	
	public static void main(String[] args) {
		Sound s = new Sound("anthem.wav");
		s.play();
		
		JOptionPane.showMessageDialog(null, "Playing music ...");
	}
		public Sound(String filename){
		this.filename = filename;
		
		try{
		    AudioInputStream audioInputStream =
		        AudioSystem.getAudioInputStream(Sound.class.getResource("/"+this.filename));
		    this.clip = AudioSystem.getClip();
		    this.clip.open(audioInputStream);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void play(){
		this.clip.start();
	}
	
	public void stop(){
		this.clip.stop();
	}
}
