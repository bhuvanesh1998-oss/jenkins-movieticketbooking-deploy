package com.film.service;

import java.util.Properties;
import javax.mail.Session;

public class EmailConfiguration {
    public static Session getMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

       
        String username = "cinemassridevi@gmail.com";
        String password = "tctyxvdvlbflazdw";

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });
        return session;
    }
}

