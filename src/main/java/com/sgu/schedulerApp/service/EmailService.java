package com.sgu.schedulerApp.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sgu.schedulerApp.dto.EventDto;
import com.sgu.schedulerApp.entity.Event;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private final String DELETE_EVENT_TEMPLATE_ID = "d-5aec434f0d9d4950a969fe787f563d19";
    private final String UPDATE_EVENT_TEMPLATE_ID = "d-3dd82ff3ce584f9a816cec0a2c5c6c89";
    private final String RESET_PASSWORD_TEMPLATE_ID = "d-a11391ecae53454e92088dd919c16847";
    private final String fromEmail = "sgu.event.scheduler@gmail.com";
    private final String API_KEY = "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";

    public void sendEmailsWhenDeleteEvent(List<String> tos, List<String> names, EventDto eventDto) {
        Mail mail = new Mail();
        mail.setFrom(new Email(this.fromEmail));
        mail.setTemplateId(DELETE_EVENT_TEMPLATE_ID);

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

        sendEmail(mail);
    }

    public void sendEmailsWhenUpdateEventSchedule(List<String> tos, List<String> names, EventDto oldEvent, EventDto newEvent) {
        Mail mail = new Mail();
        mail.setFrom(new Email(this.fromEmail));
        mail.setTemplateId(UPDATE_EVENT_TEMPLATE_ID);

        List<Personalization> personalizations = new ArrayList<>();
        for (int i=0; i< tos.size(); i++) {
            Personalization personalization = new Personalization();
            personalization.addTo(new Email(tos.get(i)));
            personalization.addDynamicTemplateData("fullName", names.get(i));
            personalization.addDynamicTemplateData("eventName", oldEvent.getName());
            personalization.addDynamicTemplateData("eventDateOld", oldEvent.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            personalization.addDynamicTemplateData("eventStartTimeOld", oldEvent.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            personalization.addDynamicTemplateData("eventEndTimeOld", oldEvent.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            personalization.addDynamicTemplateData("eventDateNew", newEvent.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            personalization.addDynamicTemplateData("eventStartTimeNew", newEvent.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            personalization.addDynamicTemplateData("eventEndTimeNew", newEvent.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            personalizations.add(personalization);
        }
        for (Personalization personalization: personalizations) {
            mail.addPersonalization(personalization);
        }

        sendEmail(mail);
    }

    public void sendResetPasswordEmail(String to, String resetLink) {
        Mail mail = new Mail();
        mail.setFrom(new Email(this.fromEmail));
        mail.setTemplateId(RESET_PASSWORD_TEMPLATE_ID);

        Personalization personalization = new Personalization();
        personalization.addTo(new Email(to));
        personalization.addDynamicTemplateData("reset-password-link", resetLink);

        mail.addPersonalization(personalization);
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
