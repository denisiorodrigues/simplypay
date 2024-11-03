package com.dentech.simplypay.services;

import com.dentech.simplypay.domain.user.User;
import com.dentech.simplypay.dtos.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    @Async
    public void sendNotification(User user, String message) {
        String email = user.getEmail();
        NotificationDto notificationRequest = new NotificationDto(email, message);
        String URL = "https://util.devi.tools/api/v1/notify";
        ResponseEntity<String> notificationResponse = this.restTemplate.postForEntity(URL, notificationRequest, String.class);

        System.out.println("sendNotification ---- >>  Status code: " + notificationResponse.getStatusCode());

        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)){
            System.out.println(" ----------- >>> Erro ao envia notificação <<<  ----------- ");
        }
    }
}
