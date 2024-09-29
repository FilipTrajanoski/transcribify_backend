package com.example.transcribify.service.impl;

import com.example.transcribify.config.MultipartInputStreamFileResource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SpeechmaticsService {

    private final RestTemplate restTemplate;

    @Value("${speechmatics.api.url}")
    private String speechmaticsApiUrl;

    @Value("${speechmatics.api.key}")
    private String speechmaticsApiKey;

    public SpeechmaticsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders setupHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(speechmaticsApiKey);

        return headers;
    }

    private MultiValueMap<String, Object> prepareRequestBody(String config) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("config", config);

        return body;
    }

    private String prepareRequestSendRequestHandleResponse(MultiValueMap<String, Object> body, HttpHeaders headers) throws IOException {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send the request to Speechmatics API
        ResponseEntity<String> response = restTemplate.postForEntity(speechmaticsApiUrl, requestEntity, String.class);

        // Check and return the response
        if (response.getStatusCode() == HttpStatus.CREATED) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            return jsonObject.getString("id");
        } else {
            throw new IOException("Failed to process video file with Speechmatics: " + response.getStatusCode());
        }
    }

    public String processVideoFile(MultipartFile file, String config) throws IOException {
        // Prepare the request headers
        HttpHeaders headers = setupHttpHeaders();

        // Prepare the body with the file and other configurations
        MultiValueMap<String, Object> body = prepareRequestBody(config);
        body.add("data_file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

        return prepareRequestSendRequestHandleResponse(body, headers);
    }

    public String processVideoUrl(String url, String config) throws IOException {
        // Prepare the request headers
        HttpHeaders headers = setupHttpHeaders();

        // Prepare the body with the file and other configurations
        MultiValueMap<String, Object> body = prepareRequestBody(config);

        return prepareRequestSendRequestHandleResponse(body, headers);
    }

    public String getVideoJob(String id) {
        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(speechmaticsApiKey);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Build the URL with the provided ID
        String url = String.format("%s/%s", speechmaticsApiUrl, id);

        while (true) {
            // Send the GET request to Speechmatics API
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            // Check and return the response
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonObject = new JSONObject(response.getBody());
                JSONObject jobObject = jsonObject.getJSONObject("job");
                String status = jobObject.getString("status");
                if (status.equals("done")) {
                    break;
                }
                // Wait for some time before polling again
                try {
                    Thread.sleep(5000); // Poll every 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

            } else {
                throw new RuntimeException("Failed to fetch job details from Speechmatics: " + response.getStatusCode());
            }
        }

        return getVideoTranscript(id);
    }

    public String getVideoTranscript(String id) {
        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(speechmaticsApiKey);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Build the URL with the provided ID
        String url = String.format("%s/%s/transcript", speechmaticsApiUrl, id);

        // Send the GET request to Speechmatics API
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // Check and return the response
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch transcription from Speechmatics: " + response.getStatusCode());
        }
    }
}
