package nl.hr.project3_4.straalbetaal.comm;

public class ArduinoData {
	//fields here. 
	
	public ArduinoData(){
		//stuff here
	}
	
	public void reset(){
		//RESETS THE ARDUINO DATA AND ALL FIELDS, WILL REMOVE ALL INFORMATION. ONLY CALL WHEN ARDUINO IS AT "START" MODE, SO THE PROGRAMS GROW UP TOGETHER
	}
	
	public boolean shouldReset(){
		return false;
		//add smart code that checks if something went wrong with arduino, or something. Can also be reached from client to trigger reset. (e.g. when pin is wrong)
	}
}
