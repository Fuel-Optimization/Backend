package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.example.model.model.ModelAttributes;

import java.util.Map;

@Service
public class PredictionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskApiUrl = "http://127.0.0.1:5000/predict";

    public Map<String, Object> getPrediction(ModelAttributes modelAttributes) {

        Map<String, Object> inputData = modelAttributes.toMap();

        return callFlaskApi(inputData);
    }

    private Map<String, Object> callFlaskApi(Map<String, Object> inputData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(inputData, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, requestEntity, Map.class);

            return response.getBody();
        } catch (Exception e) {
            // Handle exceptions gracefully
            throw new RuntimeException("Error while calling Flask API: " + e.getMessage(), e);
        }
    }
}
