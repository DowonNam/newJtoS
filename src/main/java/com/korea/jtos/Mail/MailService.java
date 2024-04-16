package com.korea.jtos.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Password reset email sent to: {}", to);
        } catch (MailException e) {
            logger.error("Failed to send password reset email to {}: {}", to, e.getMessage());
            // 여기서 예외를 처리하거나 다시 던질 수 있습니다.
            // throw new MailSendException("Failed to send password reset email", e);
        }
    }
}
