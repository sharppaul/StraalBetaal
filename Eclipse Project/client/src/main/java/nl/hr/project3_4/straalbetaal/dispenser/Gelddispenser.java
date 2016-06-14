package nl.hr.project3_4.straalbetaal.dispenser;

public final class Gelddispenser {

	public void currectBalanceInDispenser() {
		System.out.println("Tien: " + availableBiljettenVanTien + " Twintig: " + availableBiljettenVanTwintig + " Vifjtig: " + availableBiljettenVanVijftig); 
	}
	public static void main(String[] args) {
		Gelddispenser dispenser = Gelddispenser.getGelddispenser();
		dispenser.existAskedOption(50);
		for (String i : dispenser.getOptionsForSpecificAmount())
			System.out.println(i);
		dispenser.removeChosenBillsFromDispenser("C");
		System.out.println(dispenser.getFinalArduinoChoice());
		dispenser.currectBalanceInDispenser();
	}

	private int availableBiljettenVanTien;
	private int availableBiljettenVanTwintig;
	private int availableBiljettenVanVijftig;

	private int finalAmountChoice;
	private String finalArduinoChoice;

	// String with all 9 options
	private String[][] allPossibleOptionsSentToArduino;
	private String[] optionsForSpecificAmount;
	private int[][] finalAmountOpties;

	private static Gelddispenser DISPENSER = new Gelddispenser(5, 5, 5);

	private Gelddispenser(int tien, int twintig, int vijftig) {
		this.availableBiljettenVanTien = tien;
		this.availableBiljettenVanTwintig = twintig;
		this.availableBiljettenVanVijftig = vijftig;
		this.optionsForSpecificAmount = new String[3];
		this.allPossibleOptionsSentToArduino = new String[3][3];
		this.finalAmountOpties = new int[3][3];
	}

	public boolean existAskedOption(int choice) {
		this.finalAmountChoice = choice;
		switch (choice) {
		case 50:
			return existAskedBiljetten(keuzes50());
		case 100:
			return existAskedBiljetten(keuzes100());
		case 200:
			return existAskedBiljetten(keuzes200());
		default:
			return false;
		}
	}

	private boolean existAskedBiljetten(int[][] opties) {
		finalAmountOpties = opties;
		int counter = 0;
		for (int i = 0; i < opties.length; i++) {
			if (availableBiljettenVanTien >= opties[i][0] && availableBiljettenVanTwintig >= opties[i][1]
					&& availableBiljettenVanVijftig >= opties[i][2]) {

				optionsForSpecificAmount[i] = "";
				if (opties[i][0] != 0)
					optionsForSpecificAmount[i] = opties[i][0] + "x €10 ";
				if (opties[i][1] != 0)
					optionsForSpecificAmount[i] += opties[i][1] + "x €20 ";
				if (opties[i][2] != 0)
					optionsForSpecificAmount[i] += opties[i][2] + "x €50 ";
				counter++;

			}
		}

		if (counter == 0) {
			return false;
		}
		return true;
	}

	private int[][] keuzes50() {
		int[][] optiesGenerated = new int[3][3];
		optiesGenerated[0][2] = 1;
		allPossibleOptionsSentToArduino[0][0] = "0 0 1";
		optiesGenerated[1][0] = 1;
		optiesGenerated[1][1] = 2;
		allPossibleOptionsSentToArduino[0][1] = "1 2 0";
		optiesGenerated[2][0] = 5;
		allPossibleOptionsSentToArduino[0][2] = "5 0 0";
		return optiesGenerated;
	}

	private int[][] keuzes100() {
		int[][] optiesGenerated = new int[3][3];
		optiesGenerated[0][0] = 1;
		optiesGenerated[0][1] = 2;
		optiesGenerated[0][2] = 1;
		allPossibleOptionsSentToArduino[1][0] = "1 2 1";
		optiesGenerated[1][1] = 5;
		allPossibleOptionsSentToArduino[1][1] = "0 5 0";
		optiesGenerated[2][2] = 2;
		allPossibleOptionsSentToArduino[1][2] = "0 0 2";
		return optiesGenerated;
	}

	private int[][] keuzes200() {
		int[][] optiesGenerated = new int[3][3];
		optiesGenerated[0][0] = 1;
		optiesGenerated[0][1] = 2;
		optiesGenerated[0][2] = 3;
		allPossibleOptionsSentToArduino[2][0] = "1 2 3";
		optiesGenerated[1][2] = 4;
		allPossibleOptionsSentToArduino[2][1] = "0 0 4";
		optiesGenerated[2][1] = 5;
		optiesGenerated[2][2] = 2;
		allPossibleOptionsSentToArduino[2][2] = "0 5 2";
		return optiesGenerated;
	}

	public void removeChosenBillsFromDispenser(String choice) {
		String[] possibleAnswers = { "A", "B", "C" };

		for (int i = 0; i < possibleAnswers.length; i++) {
			if (choice.equals(possibleAnswers[i])) {
				availableBiljettenVanTien -= finalAmountOpties[i][0];
				availableBiljettenVanTwintig -= finalAmountOpties[i][1];
				availableBiljettenVanVijftig -= finalAmountOpties[i][2];
				System.out.println(
						finalAmountOpties[i][0] + " " + finalAmountOpties[i][1] + " " + finalAmountOpties[i][2]);
			}
			switch (this.finalAmountChoice) {
			case 50:
				finalArduinoChoice = allPossibleOptionsSentToArduino[0][i];
			case 100:
				finalArduinoChoice = allPossibleOptionsSentToArduino[1][i];
			case 200:
				finalArduinoChoice = allPossibleOptionsSentToArduino[2][i];
			default:
				finalArduinoChoice = "";
			}
		}
	}

	public static Gelddispenser getGelddispenser() {
		return DISPENSER;
	}

	public String[] getOptionsForSpecificAmount() {
		return optionsForSpecificAmount;
	}

	public String getFinalArduinoChoice() {
		return finalArduinoChoice;
	}

}
