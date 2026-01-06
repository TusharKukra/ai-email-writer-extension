package com.tkproject.email_writer.controller;

import com.tkproject.email_writer.entity.EmailDTO;
import com.tkproject.email_writer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@CrossOrigin(origins = "*")
public class EmailGenController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailDTO emailDto) {
        String response = emailService.generateEmailReply(emailDto);
        return ResponseEntity.ok(response);
    }

}
