package com.film.service;

import java.time.LocalDate;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    private static final String GMAIL_USERNAME = "cinemassridevi@gmail.com"; 
    private static final String GMAIL_PASSWORD = "tctyxvdvlbflazdw"; 

    public static void sendTicketDetails(String recipientEmail, String date, String time, String seatNumbers, double totalPrice, String movieName, String theaterName) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_USERNAME, GMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(GMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Ticket Details");
            
            String emailContent = "<html><body>" +
                    "<h2>Ticket Details</h2>" +
                    "<table border=\"1\" style=\"border-collapse: collapse; width: 100%;\">" +
                    "<tr><td style=\"padding: 8px;\">Theater Name:</td><td style=\"padding: 8px;\">" + theaterName +"</td></tr>" +
                    "<tr><td style=\"padding: 8px;\">Movie Name:</td><td style=\"padding: 8px;\">" + movieName + "</td></tr>" +
                    "<tr><td style=\"padding: 8px;\">Date:</td><td style=\"padding: 8px;\">" + date + "</td></tr>" +
                    "<tr><td style=\"padding: 8px;\">Time:</td><td style=\"padding: 8px;\">" + time + "</td></tr>" +
                    "<tr><td style=\"padding: 8px;\">Seat Numbers:</td><td style=\"padding: 8px;\">" + seatNumbers + "</td></tr>" +
                    "<tr><td style=\"padding: 8px;\">Total Price:</td><td style=\"padding: 8px;\">" + totalPrice + "</td></tr>" + 
                    "</table>" +
                    "</body></html>";




            message.setContent(emailContent, "text/html");

            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Email sending failed: " + e.getMessage());
        }
    }

	public static void sendTicketDetails(String recipientEmail, LocalDate bookingDate, String time1, String seatNumbers,
			double totalPrice, String movieName1) {
		// TODO Auto-generated method stub
		
	}
}
