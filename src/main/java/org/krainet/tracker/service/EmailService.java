package org.krainet.tracker.service;

import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Data
@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    private String[] cc = {"stas.lisavoy@icloud.com"};
    private final String registrationBody = "You have successfully registered your account!";
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Boolean sendEmail(MultipartFile[] file, String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setCc(cc);
            helper.setSubject(subject);
            helper.setText(body);

            if (file.length != 0){
                for (int i = 0; i < file.length; i++) {
                    if (file[i] != null) {
                        helper.addAttachment(
                                file[i].getOriginalFilename(),
                                new ByteArrayResource(file[i].getBytes()));
                    }
                }
            }
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public String sendEmailNoAttachment(String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setCc(cc);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(mimeMessage);
            return "mail sent successfully";
        } catch (Exception e) {
            System.out.println(e);
        }
        return "mail sent failed";
    }
}
