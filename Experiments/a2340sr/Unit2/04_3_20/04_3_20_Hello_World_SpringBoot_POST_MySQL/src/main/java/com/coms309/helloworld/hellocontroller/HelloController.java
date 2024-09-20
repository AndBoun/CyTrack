package com.coms309.helloworld.hellocontroller;

import com.coms309.helloworld.entity.Message;
import com.coms309.helloworld.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class HelloController {

    @Autowired
    private MessageRepository messageRepository;

        @GetMapping("/hello")
        public String hello(){
            return "Hello, from Spring Boot!";
        }

        @PostMapping("/endpoint")
        public String receiveData(@RequestParam String mydata){
            // Log the received data if you want
            System.out.println("Received data :"+mydata);

            // Save the message to the database
            Message message = new Message(mydata);
            messageRepository.save(message);


            // process the data as needed
            // here, we are just returning a confirmation message
            return "Data received successfullly :"+mydata;
        }

}
