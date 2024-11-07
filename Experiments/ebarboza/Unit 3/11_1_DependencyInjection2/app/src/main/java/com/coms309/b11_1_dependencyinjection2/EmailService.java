package com.coms309.b11_1_dependencyinjection2;

public class EmailService {
    public void sendEmail(String message, String receiver){
        //logic to send email
        System.out.println("Email sent to "+receiver+ " with Message="+message);

    }
}
