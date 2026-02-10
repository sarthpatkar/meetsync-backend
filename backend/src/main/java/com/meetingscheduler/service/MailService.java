package com.meetingscheduler.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService {

    @Value("${sendgrid.api-key}")
private String sendGridApiKey;

@Value("${sendgrid.from-email}")
private String fromEmail;


    public void sendInvitation(String toEmail, String subject, String contentText) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("✅ Email sent to " + toEmail + " | Status code: " + response.getStatusCode());

        } catch (IOException ex) {
            System.out.println("❌ Failed to send email: " + ex.getMessage());
            throw new RuntimeException("Failed to send email", ex);
        }
    }
}
