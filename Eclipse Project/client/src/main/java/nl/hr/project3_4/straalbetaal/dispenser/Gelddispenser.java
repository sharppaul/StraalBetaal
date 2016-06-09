package nl.hr.project3_4.straalbetaal.dispenser;

public class Gelddispenser {

	// Testing
	private void printBiljettenRemaining() {
		System.out.println("Biljetten remaining: " + availableBiljettenVanTien + " " + availableBiljettenVanTwintig
				+ " " + availableBiljettenVanVijftig);
	}
	public static void main(String[] args) {
		Gelddispenser gd = new Gelddispenser(10, 10, 10);
		try {
			gd.setAmountWanted(300);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		System.out.println(gd.getBiljetOptiesBijWantedAmount()[0] + gd.getBiljetOptiesBijWantedAmount()[1]
				+ gd.getBiljetOptiesBijWantedAmount()[2]);
		gd.setChosenOption("A");
		gd.printBiljettenRemaining();
	}

	private int amountWanted;
	private int[][] optiesGenerated;
	private String[] biljetOptiesBijWantedAmount;
	private String chosenOption;

	private int availableBiljettenVanTien;
	private int availableBiljettenVanTwintig;
	private int availableBiljettenVanVijftig;

	private int wantedBiljettenVanTien;
	private int wantedBiljettenVanTwintig;
	private int wantedBiljettenVanVijftig;

	public Gelddispenser(int tien, int twintig, int vijftig) {
		this.availableBiljettenVanTien = tien;
		this.availableBiljettenVanTwintig = twintig;
		this.availableBiljettenVanVijftig = vijftig;
		this.biljetOptiesBijWantedAmount = new String[3];
		optiesGenerated = new int[3][3];
	}

	private void generateBillOptions() throws Exception {
		if (amountWanted > 100)
			optiesBijBedragBoven100();
		else
			optiesBijBedragOnder100();

		existAskedBiljetten();
	}

	private void optiesBijBedragBoven100() {
		for (int i = 0; i < optiesGenerated.length; i++) {
			int resterendBedrag = amountWanted;

			wantedBiljettenVanVijftig = (resterendBedrag / 50) * (i % 2);
			resterendBedrag -= wantedBiljettenVanVijftig * 50;

			wantedBiljettenVanTwintig = (resterendBedrag / 20);
			resterendBedrag -= wantedBiljettenVanTwintig * 20;

			wantedBiljettenVanTien = (resterendBedrag / 10);
			resterendBedrag -= wantedBiljettenVanTien * 10;

			if (wantedBiljettenVanTwintig > 2 && i == 2) {
				wantedBiljettenVanTien += 2;
				wantedBiljettenVanTwintig -= 1;
			}
			while (wantedBiljettenVanTien > 2) {
				wantedBiljettenVanTien -= 2;
				wantedBiljettenVanTwintig += 1;
			}
			while (wantedBiljettenVanTwintig > 4) {
				wantedBiljettenVanTwintig -= 5;
				wantedBiljettenVanVijftig += 2;
			}

			optiesGenerated[i][0] = wantedBiljettenVanTien;
			optiesGenerated[i][1] = wantedBiljettenVanTwintig;
			optiesGenerated[i][2] = wantedBiljettenVanVijftig;
		}
	}

	private void optiesBijBedragOnder100() {
		if (amountWanted == 10)
			optiesGenerated[0][0] = 1;

		for (int i = 0; i < optiesGenerated.length; i++) {
			int resterendBedrag = amountWanted;

			wantedBiljettenVanVijftig = (resterendBedrag / 50) * (i % 2);
			resterendBedrag -= wantedBiljettenVanVijftig * 50;

			wantedBiljettenVanTwintig = (resterendBedrag / 20);
			resterendBedrag -= wantedBiljettenVanTwintig * 20;

			wantedBiljettenVanTien = (resterendBedrag / 10);
			resterendBedrag -= wantedBiljettenVanTien * 10;

			while (wantedBiljettenVanTien > 4) {
				wantedBiljettenVanTien -= 2;
				wantedBiljettenVanTwintig += 1;
			}
			if (wantedBiljettenVanTwintig > 2 && i == 2) {
				wantedBiljettenVanTien += 2;
				wantedBiljettenVanTwintig -= 1;
			}
			if (amountWanted < 50 && i > 0) {
				optiesGenerated[i][0] = resterendBedrag / 10;
			}

			optiesGenerated[i][0] = wantedBiljettenVanTien;
			optiesGenerated[i][1] = wantedBiljettenVanTwintig;
			optiesGenerated[i][2] = wantedBiljettenVanVijftig;
		}
	}

	private void existAskedBiljetten() throws Exception {
		for (int i = 0; i < biljetOptiesBijWantedAmount.length; i++) {
			if (availableBiljettenVanTien >= optiesGenerated[i][0]
					&& availableBiljettenVanTwintig >= optiesGenerated[i][1]
					&& availableBiljettenVanVijftig >= optiesGenerated[i][2]) {
				int counter = 0;
				biljetOptiesBijWantedAmount[i] = "";
				if (optiesGenerated[i][0] != 0) {
					biljetOptiesBijWantedAmount[i] = optiesGenerated[i][0] + "x €10 ";
					counter++;
				}
				if (optiesGenerated[i][1] != 0) {
					biljetOptiesBijWantedAmount[i] += optiesGenerated[i][1] + "x €20 ";
					counter++;
				}
				if (optiesGenerated[i][2] != 0) {
					biljetOptiesBijWantedAmount[i] += optiesGenerated[i][2] + "x €50 ";
					counter++;
				}
				if (counter == 0)
					biljetOptiesBijWantedAmount[i] = "No Bill Option";
			} else
				throw new Exception("Amount not available at dispenser!");
		}
	}

	private void removeChosenBillsFromDispenser() {
		String[] possibleAnswers = { "A", "B", "C" };

		for (int i = 0; i < possibleAnswers.length; i++) {
			if (chosenOption.equals(possibleAnswers[i])) {
				availableBiljettenVanTien -= optiesGenerated[i][0];
				availableBiljettenVanTwintig -= optiesGenerated[i][1];
				availableBiljettenVanVijftig -= optiesGenerated[i][2];
			}
		}
	}

	public void setAmountWanted(int amountWanted) throws Exception {
		this.amountWanted = amountWanted;
		generateBillOptions();
	}

	public String[] getBiljetOptiesBijWantedAmount() {
		return biljetOptiesBijWantedAmount;
	}

	public void setChosenOption(String chosenOption) {
		this.chosenOption = chosenOption;
		removeChosenBillsFromDispenser();
	}

}
