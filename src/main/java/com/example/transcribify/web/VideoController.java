package com.example.transcribify.web;

import com.example.transcribify.service.impl.SpeechmaticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RequestMapping("/api/v1/video")
public class VideoController {

    private final SpeechmaticsService speechmaticsService;

    public VideoController(SpeechmaticsService speechmaticsService) {
        this.speechmaticsService = speechmaticsService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            @RequestParam(value = "data_file", required = false) MultipartFile file,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "config", required = false) String config
    ) {
        try {
            String id;
            if (file != null) {
                id = speechmaticsService.processVideoFile(file, config);
            } else if (url != null && !url.isEmpty()) {
                id = speechmaticsService.processVideoUrl(url, config);
            } else {
                return ResponseEntity.badRequest().body("No file or URL provided.");
            }
            return ResponseEntity.ok(id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing video.");
        }
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<String> getVideoJob(@PathVariable String id){
        try {
            String jobDetails = speechmaticsService.getVideoJob(id);
            return ResponseEntity.ok(jobDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error fetching job details: " + e.getMessage());
        }
    }

    // TODO: Notification Configuration For Production
}
