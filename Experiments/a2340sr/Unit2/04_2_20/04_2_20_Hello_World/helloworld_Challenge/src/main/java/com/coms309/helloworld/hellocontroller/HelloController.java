package com.coms309.helloworld.hellocontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("coms309")
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello, Spring Boot!";
    }
    @GetMapping("/helloworld")
    public String hello2(){
        return "What a Wonderful World.";
    }
}
