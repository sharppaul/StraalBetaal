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

public class Gelddispenser {

	// Test Main Class
	public static void main(String[] args) {
		Gelddispenser gd = new Gelddispenser(5, 5, 5);

		String choice = "A";
		int amount = 50;

		gd.setAmountGepindeBedrag(amount);
		gd.setBiljetKeuzeByGepindeBedrag(choice);

		System.out.println("Amount gepind: " + gd.getAmountGepindeBedrag());
		System.out.println("Biljet keuze: " + gd.getBiljetKeuzeByGepindeBedrag());
		System.out.println("Keuze naar arduino: " + gd.calculate());

		System.out.println("Biljetten remaining: " + gd.getAantalBiljettenVanTien() + gd.getAantalBiljettenVanTwintig() + gd.getAantalBiljettenVanVijftig());
	}

	private int aantalBiljettenVanTien;
	private int aantalBiljettenVanTwintig;
	private int aantalBiljettenVanVijftig;

	private int amountGepindeBedrag;
	private String biljetKeuzeByGepindeBedrag;

	public Gelddispenser(int tien, int twintig, int vijftig) {
		this.aantalBiljettenVanTien = tien;
		this.aantalBiljettenVanTwintig = twintig;
		this.aantalBiljettenVanVijftig = vijftig;
	}

	/*
	 * De arduino neemt deze string in met de aantal biljetten van elke soort
	 * die moet worden afgegeven.
	 * 
	 * Gare methodes maar kon niks beter verzinnen. Hoe kon dit netter?
	 */
	public String calculate() {
		String arduinoBiljettenData;
		switch (amountGepindeBedrag) {
		case 50:
			arduinoBiljettenData = keuze50();
			break;
		case 100:
			arduinoBiljettenData = keuze100();
			break;
		case 200:
			arduinoBiljettenData = keuze200();
			break;
		default:
			arduinoBiljettenData = "0 0 0";
		}
		return arduinoBiljettenData;
	}

	private String keuze50() {
		if (biljetKeuzeByGepindeBedrag == "A") {
			aantalBiljettenVanVijftig -= 1;
			return "0 0 1";
		} else if (biljetKeuzeByGepindeBedrag == "B") {
			aantalBiljettenVanTien -= 1;
			aantalBiljettenVanTwintig -= 2;
			return "1 2 0";
		} else if (biljetKeuzeByGepindeBedrag == "C") {
			aantalBiljettenVanTien -= 5;
			return "5 0 0";
		} else
			return "0 0 0";
	}

	private String keuze100() {
		if (biljetKeuzeByGepindeBedrag == "A") {
			aantalBiljettenVanTien -= 1;
			aantalBiljettenVanTwintig -= 2;
			aantalBiljettenVanVijftig -= 1;
			return "1 2 1";
		} else if (biljetKeuzeByGepindeBedrag == "B") {
			aantalBiljettenVanVijftig -= 2;
			return "0 0 2";
		} else if (biljetKeuzeByGepindeBedrag == "C") {
			aantalBiljettenVanTwintig -= 5;
			return "0 5 0";
		} else
			return "0 0 0";
	}

	private String keuze200() {
		if (biljetKeuzeByGepindeBedrag == "A") {
			aantalBiljettenVanTien -= 1;
			aantalBiljettenVanTwintig -= 2;
			aantalBiljettenVanVijftig -= 3;
			return "1 2 3";
		} else if (biljetKeuzeByGepindeBedrag == "B") {
			aantalBiljettenVanVijftig -= 4;
			return "0 0 4";
		} else if (biljetKeuzeByGepindeBedrag == "C") {
			aantalBiljettenVanTwintig -= 5;
			aantalBiljettenVanVijftig -= 2;
			return "0 5 2";
		} else
			return "0 0 0";
	}

	public int getAantalBiljettenVanTien() {
		return aantalBiljettenVanTien;
	}

	public int getAantalBiljettenVanTwintig() {
		return aantalBiljettenVanTwintig;
	}

	public int getAantalBiljettenVanVijftig() {
		return aantalBiljettenVanVijftig;
	}

	public int getAmountGepindeBedrag() {
		return amountGepindeBedrag;
	}

	public void setAmountGepindeBedrag(int amountGepindeBedrag) {
		this.amountGepindeBedrag = amountGepindeBedrag;
	}

	public String getBiljetKeuzeByGepindeBedrag() {
		return biljetKeuzeByGepindeBedrag;
	}

	public void setBiljetKeuzeByGepindeBedrag(String biljetKeuzeByGepindeBedrag) {
		this.biljetKeuzeByGepindeBedrag = biljetKeuzeByGepindeBedrag;
	}

}
