package nl.hr.project3_4.straalbetaal.printer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import nl.hr.project3_4.straalbetaal.gui.Fonts;

import javax.print.PrintService;

public class LabelWriter {
	public static final String PRINTERNAME = "Microsoft Print to PDF";
	//public static final String PRINTERNAME = "DYMO LabelWriter 400";
	public static final int FONTSIZE = 12;
	public static final boolean PRINTMENU = false;
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("dd-MM-yyyy");
	private static final DateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");

	private PrinterJob printerJob = PrinterJob.getPrinterJob();
	private PageFormat pageFormat = printerJob.defaultPage();
	private Paper paper = new Paper();

	public static void main(String[] args) {
		new LabelWriter().printLabel("6969", (long) 69.0, "123456789", true);
	}

	public void printLabel(String transNr, long bedrag, String rekeningNr, boolean didDonate) {
		double width = CMtoPPI(5.3);
		double height = CMtoPPI(11);
		paper.setSize(width, height);
		paper.setImageableArea(CMtoPPI(0.25), CMtoPPI(0.5), width - CMtoPPI(0.25), height - CMtoPPI(0.5));

		pageFormat.setPaper(paper);
		pageFormat.setOrientation(PageFormat.PORTRAIT);

		PrintService[] printService = PrinterJob.lookupPrintServices();

		Date now = new Date();

		for (int i = 0; i < printService.length; i++) {
			// To view available printServices, uncomment next line.
			// System.out.println(printService[i].getName());
			if (printService[i].getName().contains(PRINTERNAME)) {
				try {
					printerJob.setPrintService(printService[i]);
					printerJob.setPrintable(new Printable() {
						@Override
						public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
								throws PrinterException {
							if (pageIndex < 1) {
								Graphics2D g = (Graphics2D) graphics;
								g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
								g.setFont(Fonts.createFont("ubuntu.ttf", 18));
								g.drawString("StraalBetaal inc.", 5, 18);
								g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONTSIZE));
								g.drawString("==================", 5, FONTSIZE * 3);
								g.drawString("Datum: " + DATEFORMAT.format(now), 5, FONTSIZE * 5);
								g.drawString("Tijd:  " + TIMEFORMAT.format(now), 5, FONTSIZE * 6);
								g.drawString("==Pin Transactie==", 5, FONTSIZE * 8);
								g.drawString("Bedrag:", 5, FONTSIZE * 10);
								g.drawString("G " + formatAmount(bedrag*100), 5, FONTSIZE * 11);
								g.drawString("Transactie Nr.:", 5, FONTSIZE * 13);
								g.drawString(transNr, 5, FONTSIZE * 14);
								g.drawString("Rekening Nr.:", 5, FONTSIZE * 16);
								g.drawString("........" + rekeningNr.substring(rekeningNr.length() - 3), 5,
										FONTSIZE * 17);
								if (didDonate) {
									g.drawString("Gedoneerd: Ja", 5, FONTSIZE * 18);
								}
								g.drawString("==================", 5, FONTSIZE * 19);
								g.drawString("Bedankt voor het", 5, FONTSIZE * 21);
								g.drawString("pinnen & tot ziens.", 5, FONTSIZE * 22);

								// g.drawRect(0, 0,
								// (int)pageFormat.getImageableWidth(),
								// (int)pageFormat.getImageableHeight());
								return PAGE_EXISTS;
							} else {
								return NO_SUCH_PAGE;
							}
						}
					}, pageFormat);
					printerJob.print();
				} catch (PrinterException e) {
					System.err.println("Printing failed:");
					e.printStackTrace();
				}
			}
		}
	}

	public String formatAmount(long amount) {
		String amountStr = (amount / 100) + ",";
		long decimals = amount - ((amount / 100) * 100);
		if (decimals > 9) {
			amountStr += decimals;
		} else {
			amountStr += "0" + decimals;
		}

		return amountStr;
	}
	
	protected static double CMtoPPI(double cm) {
		return toPPI(cm * 0.393700787);
	}

	protected static double toPPI(double inch) {
		return inch * 72d;
	}
}