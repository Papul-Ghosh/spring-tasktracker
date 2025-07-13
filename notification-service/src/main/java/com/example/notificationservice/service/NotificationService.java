package com.example.notificationservice.service;

import com.example.notificationservice.dto.ProjectEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class NotificationService {

    @Value("${cloud.aws.sender-email}")
    private String senderEmail;
    @Autowired
    private final SesClient sesClient;

    public NotificationService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail(String to, String subject, String bodyText) {
        try {
            Destination destination = Destination.builder().toAddresses(to).build();
            Content subjectContent = Content.builder().data(subject).build();
            Content bodyContent = Content.builder().data(bodyText).build();
            Body body = Body.builder().text(bodyContent).build();
            Message message = Message.builder().subject(subjectContent).body(body).build();

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .destination(destination)
                    .message(message)
                    .source(senderEmail)
                    .build();
            sesClient.sendEmail(emailRequest);
            System.out.println("Email sent successfully to " + to);

        } catch (Exception ex) {
            System.err.println("The email was not sent. Error message: " + ex.getMessage());
            throw new RuntimeException("Failed to send email via SES", ex);
        }
    }


    @KafkaListener(topics = "#{'${app.kafka.notification-topic}'}", groupId = "notification-service", containerFactory = "notificationProjectListener")
    public void handleProjectEvents(ProjectEventDto projectEventDto) {
//        switch (eventDto.getEventType()) {
//            case "TASK_CREATED" -> syncNewTask(eventDto.getTaskProjectDto());
//            case "TASK_DELETED" -> removeTask(eventDto.getTaskProjectDto());
//        }
        sendEmail(projectEventDto.getProjectNotificationDto().getEmail(),
                "SAMPLE HEADER",
                "SAMPLE MESSAGE");
    }
}
