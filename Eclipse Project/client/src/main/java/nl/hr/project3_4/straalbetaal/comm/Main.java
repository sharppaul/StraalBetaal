package nl.hr.project3_4.straalbetaal.comm;

public class Main {
	private static ArduinoData data;
	private static Read reader;
	
	public static void main(String[] args){
		new Main();
	}
	
	public Main(){
		System.out.println("Started");
		
		data = new ArduinoData();
		
		reader = new Read(data);
		
		
	}
	
	public void receiveData(){
		System.out.println("Data received:");
		System.out.println(data.getPinCode() + "\n" + data.getCardID());
	}
}
