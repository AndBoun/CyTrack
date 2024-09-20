package com.coms309.helloworld.hellocontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
        @GetMapping("/hello")
        public String hello(){
            return "Hello, from Spring Boot!";
        }
}
