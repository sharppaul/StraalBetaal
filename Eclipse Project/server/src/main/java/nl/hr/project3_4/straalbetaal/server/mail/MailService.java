package nl.hr.project3_4.straalbetaal.server.mail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {

	/*
	public static void main(String[] args) {
		new MailService("yosh.nahar@gmail.com").sendMailContainingTransactiebon(1, "123456789", 50L);
	}
	*/
	
	private final String emailSender = "straalbetaal@gmail.com";
	private final String passwordSender = "straalBetaalOP4";
	private String emailReceiver;

	private Properties props;

	private static final DateFormat DATEFORMAT = new SimpleDateFormat("dd-MM-yyyy");
	private static final DateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");
	
	public MailService(String emailReceiver) {
		this.emailReceiver = emailReceiver;

		props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // smtp server address
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.fallback", "false");
	}

	public void sendMailContainingTransactiebon(int transactieBon, String pasID, long amount) {
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailSender, passwordSender);
			}
		});
		session.setDebug(false);

		Date now = new Date();
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailReceiver));
			message.setSubject("StraalBetaal - Transactie: " + transactieBon);
			message.setContent("<body> "
					+ "<h1>StraalBetaal Inc.</h1>"
					+ "<hr></hr>"
					+ "<p>Datum: " + DATEFORMAT.format(now)
					+ "<br>Tijd: " + TIMEFORMAT.format(now)
					+ "<h2>==Pin Transactie==</h2>"
					+ "<br>Bedrag: G " + amount
					+ "<br>Transactie Nr.: " + transactieBon
					+ "<br>Rekening Nr. :" + pasID
					+ "</p>"
					+ "<p>========================</p>"
					+ "<p>Bedankt voor het pinnen & tot ziens!</p>"
					+ "</body>", "text/html; charset=utf-8");
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", emailSender, passwordSender);
			transport.sendMessage(message, message.getAllRecipients());
			System.out.println("Message Sent!");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
