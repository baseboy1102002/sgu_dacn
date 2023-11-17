package com.sgu.schedulerApp.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sgu.schedulerApp.dto.EventDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private final String DYNAMIC_TEMPLATE_ID = "d-5aec434f0d9d4950a969fe787f563d19";
    private final String fromEmail = "sgu.event.scheduler@gmail.com";
    private final String API_KEY = "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";

    public void sendBulkEmails(List<String> tos, List<String> names, EventDto eventDto) {
        // specify the email details
        Mail mail = new Mail();
        mail.setFrom(new Email(this.fromEmail));
        mail.setSubject("Thông báo hủy bỏ sự kiện từ SGU_Event_Scheduler");
        mail.setTemplateId(DYNAMIC_TEMPLATE_ID);

        // add the multiple recipients to the email
        List<Personalization> personalizations = new ArrayList<>();
        for (int i=0; i< tos.size(); i++) {
            Personalization personalization = new Personalization();
            personalization.addTo(new Email(tos.get(i)));
            personalization.addDynamicTemplateData("fullName", names.get(i));
            personalization.addDynamicTemplateData("eventName", eventDto.getName());
            personalization.addDynamicTemplateData("eventDate", eventDto.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            personalization.addDynamicTemplateData("eventStartTime", eventDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            personalization.addDynamicTemplateData("eventEndTime", eventDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            personalizations.add(personalization);
        }
        for (Personalization personalization: personalizations) {
            mail.addPersonalization(personalization);
        }

        // send the bulk email
        sendEmail(mail);
    }

    private void sendEmail(Mail mail) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            // perform the request and send the email
            SendGrid sendGrid = new SendGrid(API_KEY);
            Response response = sendGrid.api(request);
            int statusCode = response.getStatusCode();
            // if the status code is not 2xx (network error)
            if (statusCode < 200 || statusCode >= 300) {
                throw new RuntimeException(response.getBody());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
