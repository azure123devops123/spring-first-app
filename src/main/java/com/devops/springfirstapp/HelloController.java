package com.devops.springfirstapp;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;
@RestController  
public class HelloController   {
    @RequestMapping("/hello")  
    public String hello()   {  
    return "Hello World Greetings from Spring Boot Application - Version No. 4";
    }
}