package com.awd.sudoku.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void sendNotificationToAll(String title, String body) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message
                .builder()
                .setTopic("all")
                .setNotification(notification)
                .build();

        firebaseMessaging.send(message);
    }

    public void sendNotificationToUser(String title, String body, String token) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();


        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        firebaseMessaging.send(message);
    }

    public void subscribeToTopic(String token) throws FirebaseMessagingException {
        firebaseMessaging.subscribeToTopic(Collections.singletonList(token), "all");
    }


}
