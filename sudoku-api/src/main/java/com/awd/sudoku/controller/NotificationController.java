package com.awd.sudoku.controller;

import com.awd.sudoku.models.AppUser;
import com.awd.sudoku.service.FirebaseMessagingService;
import com.awd.sudoku.service.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/notifications")
public class NotificationController {

    private final FirebaseMessagingService firebaseMessagingService;
    private final UserService userService;

    public NotificationController(FirebaseMessagingService firebaseMessagingService, UserService userService) {
        this.firebaseMessagingService = firebaseMessagingService;
        this.userService = userService;
    }

    @GetMapping("/subscribe")
    public void subscribe(@RequestParam String token) {
        try {
            firebaseMessagingService.subscribeToTopic(token);
        } catch (FirebaseMessagingException e) {
            System.out.println("Error subscribing to topic");
        }
    }

    @GetMapping("/setToken")
    public void setToken(@RequestParam String token) {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setNotificationToken(token);
        userService.save(user);
    }
}
