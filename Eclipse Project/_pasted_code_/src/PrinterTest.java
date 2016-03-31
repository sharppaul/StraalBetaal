import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class PrinterTest {

    public static void main(String[] args) {
        System.setSecurityManager(null);
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (true) {
            PageFormat pf = pj.defaultPage();
            Paper paper = pf.getPaper();
            double width = fromCMToPPI(5.3);
            double height = fromCMToPPI(11);
            paper.setSize(width, height);
            paper.setImageableArea(
                    fromCMToPPI(0.25),
                    fromCMToPPI(0.5),
                    width - fromCMToPPI(0.35),
                    height - fromCMToPPI(1));
            System.out.println("Before- " + dump(paper));
            pf.setOrientation(PageFormat.PORTRAIT);
            pf.setPaper(paper);
            System.out.println("After- " + dump(paper));
            System.out.println("After- " + dump(pf));
            dump(pf);
            PageFormat validatePage = pj.validatePage(pf);
            System.out.println("Valid- " + dump(validatePage));
            //Book book = new Book();
            //book.append(new MyPrintable(), pf);
            //pj.setPageable(book);
            pj.setPrintable(new MyPrintable(), pf);
            try {
                pj.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected static double fromCMToPPI(double cm) {
        return toPPI(cm * 0.393700787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

    protected static String dump(Paper paper) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(paper.getWidth()).append("x").append(paper.getHeight())
                .append("/").append(paper.getImageableX()).append("x").
                append(paper.getImageableY()).append(" - ").append(paper
                .getImageableWidth()).append("x").append(paper.getImageableHeight());
        return sb.toString();
    }

    protected static String dump(PageFormat pf) {
        Paper paper = pf.getPaper();
        return dump(paper);
    }

    public static class MyPrintable implements Printable {

        @Override
        public int print(Graphics graphics, PageFormat pageFormat,
                         int pageIndex) throws PrinterException {
            System.out.println(pageIndex);
            int result = NO_SUCH_PAGE;
            if (pageIndex < 1) {
                Graphics2D g2d = (Graphics2D) graphics;
                System.out.println("[Print] " + dump(pageFormat));
                double width = pageFormat.getImageableWidth();
                double height = pageFormat.getImageableHeight();
                g2d.translate((int) pageFormat.getImageableX(),
                        (int) pageFormat.getImageableY());
                g2d.draw(new Rectangle2D.Double(4, 1, width - 7, height - 1));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString("Straalbetaal", 10, 15);
                g2d.drawString("", 10, 30);
                g2d.drawString("TEST 3", 10, 45);
                g2d.drawString("TEST 4", 10, 60);
                g2d.drawString("TEST 5", 10, 75);
                g2d.drawString("TEST 6", 10, 90);
                g2d.drawString("TEST 7", 10, 105);
                g2d.drawString("TEST 8", 10, 120);
                g2d.drawString("TEST 9", 10, 135);
                g2d.drawString("TEST 10", 10, 150);
                g2d.drawString("TEST 11", 10, 165);
                g2d.drawString("TEST 12", 10, 180);
                g2d.drawString("TEST 13", 10, 195);
                g2d.drawString("TEST 14", 10, 210);
                g2d.drawString("TEST 15", 10, 225);
                g2d.drawString("TEST 16", 10, 240);
                result = PAGE_EXISTS;
            }
            return result;
        }
    }
}