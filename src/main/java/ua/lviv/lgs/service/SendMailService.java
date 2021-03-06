package ua.lviv.lgs.service;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendMailService {

	private final static Logger log = LogManager.getLogger(SendMailService.class.getName());
	private final static Properties prop = new Properties();

	private static SendMailService ms;

	public static SendMailService getMailSender() {
		if (ms == null) {
			ms = new SendMailService();
		}

		return ms;
	}

	private SendMailService() {
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("mail.properties"));
		} catch (IOException e) {
			log.error("Cant load properties file: ", e);
		}
	}

	public void sendMail(String to, String subject, String text) throws AddressException, MessagingException {
		Session session = Session.getInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(prop.getProperty("username"), prop.getProperty("password"));
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(prop.getProperty("from")));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(subject);
		message.setText(text);

		Transport.send(message);
	}

}
