package com.coms309.helloworld.hellocontroller;

import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class HelloController {
        @GetMapping("/hello")
        public String hello(){
            return "Hello, from Spring Boot!";
        }

        @PostMapping("/endpoint")
        public String receiveData(@RequestParam String mydata){
            // Log the received data if you want
            System.out.println("Received data :"+mydata);

            // process the data as needed
            // here, we are just returning a confirmation message
            return "Data received successfullly :"+mydata;
        }

}
