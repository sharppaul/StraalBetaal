
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class LabelPrinter {

	private static String datum = "Donderdag 31 - 03 - 2016";
	private static String opgenomenSaldo = "€ 40,-";

	public static void main(String[] args) {
		System.setSecurityManager(null);
		PrinterJob printerJob = PrinterJob.getPrinterJob();

		PageFormat printerFormat = printerJob.defaultPage();
		Paper paper = printerFormat.getPaper();
		double width = vanCMNaarPPI(5.3);
		double height = vanCMNaarPPI(11);
		paper.setSize(width, height);
		paper.setImageableArea(vanCMNaarPPI(0.25), vanCMNaarPPI(0.5), width - vanCMNaarPPI(0.35), height - vanCMNaarPPI(1));

		printerFormat.setOrientation(PageFormat.PORTRAIT);
		printerFormat.setPaper(paper);

		dump(printerFormat);

		printerJob.setPrintable(new MyPrintable(), printerFormat);
		try {
			printerJob.print();
		} catch (PrinterException ex) {
			ex.printStackTrace();
		}
	}

	private static double vanCMNaarPPI(double cm) {
		return naarPPI(cm * 0.393700787);
	}
	private static double naarPPI(double inch) {
		return inch * 72d;
	}

	private static String dump(Paper paper) {
		StringBuilder sb = new StringBuilder(64);
		sb.append(paper.getWidth()).append("x").append(paper.getHeight()).append("/").append(paper.getImageableX())
				.append("x").append(paper.getImageableY()).append(" - ").append(paper.getImageableWidth()).append("x")
				.append(paper.getImageableHeight());
		return sb.toString();
	}
	private static String dump(PageFormat pf) {
		Paper paper = pf.getPaper();
		return dump(paper);
	}


	public static class MyPrintable implements Printable {

		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

			int result = NO_SUCH_PAGE;
			if(pageIndex < 1) {
				Graphics2D g2d = (Graphics2D) graphics;

				double width = pageFormat.getImageableWidth();
				double height = pageFormat.getImageableHeight();
				g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
				g2d.draw(new Rectangle2D.Double(4, 1, width - 7, height - 1));
				g2d.drawString("Straalbetaal", 10, 15);
				g2d.drawString("Datum: " + datum, 10, 45);
				g2d.drawString("Opgenomen bedrag: " + opgenomenSaldo, 10, 75);
				g2d.drawString("TEST 6", 10, 90);
				g2d.drawString("TEST 7", 10, 105);
				g2d.drawString("TEST 8", 10, 120);
				g2d.drawString("TEST 9", 10, 135);
				g2d.drawString("TEST 10", 10, 150);
				g2d.drawString("TEST 11", 10, 165);
				g2d.drawString("TEST 12", 10, 180);
				g2d.drawString("=====================", 10, 195);
				g2d.drawString("Tot ziens!", 50, 210);
				g2d.drawString("Tot de volgende keer!", 10, 225);
				result = PAGE_EXISTS;
			}
			return result;
		}
	}

}