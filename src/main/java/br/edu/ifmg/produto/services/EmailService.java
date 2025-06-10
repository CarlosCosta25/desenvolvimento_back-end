package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.EmailDTO;
import br.edu.ifmg.produto.exceptions.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendEmail(EmailDTO emailDTO){

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(emailFrom);
            message.setTo(emailDTO.getTo());
            message.setSubject(emailDTO.getSubject());
            message.setText(emailDTO.getBody());

            mailSender.send(message);
        }
        catch (MailSendException e){
            throw  new EmailException(e.getMessage());
        }

    }
}
