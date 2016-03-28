package nl.hr.project3_4.straalbetaal.client;

public class SuccessException extends Exception {
	private static final long serialVersionUID = 1L;
	public SuccessException(){
		super();
	}
	public SuccessException(String s){
		super(s);
	}
}
