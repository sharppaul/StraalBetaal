package nl.hr.project3_4.straalbetaal.dispenser;

/*************************************************
 * amount = 50
 * 	options[0] = "1x €50";
 * 	options[1] = "1x €10 & 2x €20";
 * 	options[2]= "5x €10";
 *
 * amount = 100
 * 	options[0] = "1x €100"; BILJETTEN VAN 100 HEBBEN WIJ NIET, DUS 1*50 2*20 1*10
 * 	options[1] = "2x €50";
 * 	options[2] = "5x €20";
 *
 * amount = 200
 * 	options[0] = "2x €100"; DIT WORDT OP ZIJN BEURT 3*50 2*20 1*10 --> Beetje raar, zeg wat je het best vindt.
 * 	options[1] = "4x €50";
 * 	options[2] = "2x €50 & 5x €20";
 *************************************************/

public final class Gelddispenser {

	private void printBiljettenRemaining() {
		System.out.println("Biljetten remaining: " + availableBiljettenVanTien + " " + availableBiljettenVanTwintig
				+ " " + availableBiljettenVanVijftig);
	}
	public static void main(String[] args) {
		Gelddispenser gd = new Gelddispenser(5, 5, 5);

		String choice = "A";
		int amount = 50;

		
		
		try {
			gd.existAskedBiljetten(gd.keuzes50());
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		gd.setAmountGepindeBedrag(amount);
		gd.setBiljetKeuzeByGepindeBedrag(choice);

		System.out.println("Amount gepind: " + gd.getAmountGepindeBedrag());
		System.out.println("Biljet keuze: " + gd.getBiljetKeuzeByGepindeBedrag());
		System.out.println("Keuze naar arduino: " + gd.calculate());
		*/
	}

	private int availableBiljettenVanTien;
	private int availableBiljettenVanTwintig;
	private int availableBiljettenVanVijftig;

	private int wantedBiljettenVanTien;
	private int wantedBiljettenVanTwintig;
	private int wantedBiljettenVanVijftig;

	private String[][] allPossibleOptionsSentToArduino; // String with all 9 options
	private String[] optionsForSpecificAmount;

	private static Gelddispenser DISPENSER;
	
	private Gelddispenser(int tien, int twintig, int vijftig) {
		this.availableBiljettenVanTien = tien;
		this.availableBiljettenVanTwintig = twintig;
		this.availableBiljettenVanVijftig = vijftig;
		optionsForSpecificAmount = new String[3];
		allPossibleOptionsSentToArduino = new String[3][3];
	}

	public boolean existAskedOption(int choice) {
		if (choice == 50) 
			return existAskedBiljetten(keuzes50());
		else if (choice == 100)
			return existAskedBiljetten(keuzes100());
		else if (choice == 200)
			return existAskedBiljetten(keuzes200());
		else
			return false;
	}

	private boolean existAskedBiljetten(int[][] opties) {
		int counter = 0;
		for (int i = 0; i < opties.length; i++) {
			if (availableBiljettenVanTien >= opties[i][0]
					&& availableBiljettenVanTwintig >= opties[i][1]
					&& availableBiljettenVanVijftig >= opties[i][2]) {

				optionsForSpecificAmount[i] = "";
				if (opties[i][0] != 0) {
					optionsForSpecificAmount[i] = opties[i][0] + "x €10 ";
				}
				if (opties[i][1] != 0) {
					optionsForSpecificAmount[i] += opties[i][1] + "x €20 ";
				}
				if (opties[i][2] != 0) {
					optionsForSpecificAmount[i] += opties[i][2] + "x €50 ";
				}
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
		allPossibleOptionsSentToArduino[0][0] =  "1 2 1";
		optiesGenerated[1][1] = 5;
		allPossibleOptionsSentToArduino[0][1] = "0 5 0";
		optiesGenerated[2][2] = 2;
		allPossibleOptionsSentToArduino[0][2] = "0 0 2";
		return optiesGenerated;
	}

	private int[][] keuzes200() {
		int[][] optiesGenerated = new int[3][3];
		optiesGenerated[0][0] = 1;
		optiesGenerated[0][1] = 2;
		optiesGenerated[0][2] = 3;
		allPossibleOptionsSentToArduino[0][0] =  "1 2 3";
		optiesGenerated[1][2] = 4;
		allPossibleOptionsSentToArduino[0][1] = "0 0 4";
		optiesGenerated[2][1] = 5;
		optiesGenerated[2][2] = 2;
		allPossibleOptionsSentToArduino[0][2] = "0 5 2";
		return optiesGenerated;
	}

	private void removeChosenBillsFromDispenser() {
		String[] possibleAnswers = { "A", "B", "C" };

		for (int i = 0; i < possibleAnswers.length; i++) {
			if (allPossibleOptionsSentToArduino.equals(possibleAnswers[i])) {
				availableBiljettenVanTien -= optiesGenerated[i][0];
				availableBiljettenVanTwintig -= optiesGenerated[i][1];
				availableBiljettenVanVijftig -= optiesGenerated[i][2];
			}
		}
	}

	public static Gelddispenser getGelddispenser() {
		if (DISPENSER == null)
			DISPENSER = new Gelddispenser(5, 5, 5);
		
		return DISPENSER;
	}

	public String[] getOptionsForSpecificAmount() {
		return optionsForSpecificAmount;
	}


}
