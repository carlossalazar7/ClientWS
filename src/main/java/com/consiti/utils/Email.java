package com.consiti.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.net.InetAddress;
import java.util.Properties;

public class Email {

    public static void send(String messageText) throws Exception {

        String FROM = ReadProperties.getProperty("mail.from");
        String TO = ReadProperties.getProperty("mail.addresss");

        Properties proper = new Properties();
        proper.put("mail.smtp.host",  ReadProperties.getProperty("mail.smtp.host"));
        proper.put("mail.smtp.port",  ReadProperties.getProperty("mail.smtp.port"));
        proper.put("mail.smtp.auth",  ReadProperties.getProperty("mail.smtp.auth"));
        proper.put("mail.smtp.ssl.enable", ReadProperties.getProperty("mail.smtp.ssl.enable"));

        Session session = Session.getDefaultInstance(proper, null);
        session.setDebug(true);

        //define host
        System.out.println("Your current IP address : " + InetAddress.getLocalHost());
        System.out.println("Your current Hostname : " + InetAddress.getLocalHost().getHostName());

        // Define message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.setSubject( "("+InetAddress.getLocalHost().getHostName()+") "+ReadProperties.getProperty("mail.subject"));
        //message.setText(messageText);
        message.setContent(messageText,"text/html");
        if ((TO.split(",")).length > 1) {
            for (String mail : TO.split(","))
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.trim()));
        } else {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO.trim()));
        }

        // Envia el mensaje
        Transport.send(message);
        System.out.println("Correo enviado");
    }
}
