package com.tkproject.email_writer.controller;

import com.tkproject.email_writer.entity.EmailDTO;
import com.tkproject.email_writer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
//@CrossOrigin(origins = "*")
public class EmailGenController {

    @Autowired
    private EmailService emailService;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @GetMapping("/home/url")
    public String homePage() {
        return "Hello from AI Email Reply Generator Spring Boot Application, url: " + geminiApiUrl;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailDTO emailDto) {
        String response = emailService.generateEmailReply(emailDto);
        return ResponseEntity.ok(response);
    }

}
