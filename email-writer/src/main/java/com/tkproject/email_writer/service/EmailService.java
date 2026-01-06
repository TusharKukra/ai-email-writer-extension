package com.tkproject.email_writer.service;

import com.tkproject.email_writer.entity.EmailDTO;

public interface EmailService {
    public String generateEmailReply(EmailDTO emailDto);
}
