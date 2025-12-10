package com.ssafy.zipcheck.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    @Qualifier("gmailSender")
    private final JavaMailSender gmailSender;

    @Qualifier("naverSender")
    private final JavaMailSender naverSender;

    /**
     * 이메일 주소에 따라 자동으로 SMTP 서버 선택
     * naver.com → 네이버 SMTP
     * 나머지 → Gmail SMTP
     */
    public void sendMail(String to, String subject, String msg) {
        if (to.endsWith("@naver.com")) {
            send(naverSender, to, subject, msg);
        } else {
            send(gmailSender, to, subject, msg);
        }
    }

    private void send(JavaMailSender sender, String to, String subject, String msg) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(msg);
        sender.send(mail);
    }
}


