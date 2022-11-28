package com.techelevator.tenmo.services;

import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public double viewCurrentBalance(int id) {
        //TODO change double to wrapper class so we can return null when not found.
        double currentBalance = 0;
        try {
            ResponseEntity<Double> response = restTemplate.exchange(API_BASE_URL + "balance/" + id, HttpMethod.GET, makeAuthEntity(), Double.class);
            currentBalance = response.getBody();
        }
        catch(Exception e) {
            BasicLogger.log(e.getMessage());
        }
        return currentBalance;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
