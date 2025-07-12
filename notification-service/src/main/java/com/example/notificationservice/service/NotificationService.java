package com.example.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
//    private final UserClient userClient;

    public NotificationService(
//            UserClient userClient,
            ) {
//        this.userClient = userClient;
    }


    @KafkaListener(topics = "#{'${app.kafka.notification-topic}'}", groupId = "notification-service", containerFactory = "notificationProjectListener")
    public void handleProjectEvents(String str) {
//        switch (eventDto.getEventType()) {
//            case "TASK_CREATED" -> syncNewTask(eventDto.getTaskProjectDto());
//            case "TASK_DELETED" -> removeTask(eventDto.getTaskProjectDto());
//        }
        System.out.println(str);
    }


//    public Long getActiveUserId() {
//        return userClient.getUserIdFromUserService();
//    }
//
}
