package com.vaadin.timetable.Service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.timetable.EmailBean;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private EmailBean emailBean;

    public EmailService(EmailBean emailBean){
        this.emailBean = emailBean;
        sendEmail(emailBean);
    }

    public void sendEmail(EmailBean emailBean) {
        String username = "adhesh100@gmail.com";
        String password = "*****";
        Properties prop = new Properties();
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable","true");
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","587");
        try {
            Session session = Session.getInstance(prop,
                    new javax.mail.Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication(){
                            return new PasswordAuthentication(username,password);
                        }
                    });


            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username, false));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailBean.getTo()));
            msg.setSubject(emailBean.getSubject());
            InternetAddress[] internetAddresses = InternetAddress.parse(emailBean.getCc());
            msg.setRecipients(Message.RecipientType.CC,internetAddresses);
            msg.setContent(emailBean.getBody(), "text/html");
            Transport.send(msg);
        }catch (Exception e){
            Notification.show(e.getLocalizedMessage());
        }
    }

}
