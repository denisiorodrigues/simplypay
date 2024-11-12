package com.dentech.simplypay.services;

import com.dentech.simplypay.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        String url = "https://util.devi.tools/api/v2/authorize";
        ResponseEntity<Map> authorizeResponse = this.restTemplate.getForEntity(url, Map.class);

        if(authorizeResponse.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> data = (Map<String, Object>) authorizeResponse.getBody().get("data");
            return (boolean) data.containsKey("authorization");
        }

        return false;
    }
}
