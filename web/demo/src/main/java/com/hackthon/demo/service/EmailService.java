package com.hackthon.demo.service;

import com.hackthon.demo.clzs.Loc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendSimpleEmail(String toAddr, Loc loc, double priority){
        int pInt = (int) Math.floor(priority / 0.33);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("never-loast@gmail.com");
        message.setTo(toAddr);
        message.setSubject("Alert: Out of Range");
        message.setText("You subject is in abnormal status. The alert level is " + pInt + ".\nPlease visit http://localhost:8080/viewActivity");
        mailSender.send(message);
        System.out.println("Email Sent\n");
    }
}
