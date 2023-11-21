package com.ucu.topicos.config;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResendConfig {

    @Value("${resend.api-key}")
    private String apiKey;

    @Bean
    public Resend resend() {
        return new Resend(apiKey);
    }

    public void sendMail(String message, String mailTo) {
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("deres@resend.dev")
                .text(message)
                .addTo(mailTo)
                .subject("Invitacion a DERES")
                .build();

        try {
            SendEmailResponse data = this.resend().emails().send(sendEmailRequest);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

}
