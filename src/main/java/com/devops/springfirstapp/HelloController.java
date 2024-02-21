package com.devops.springfirstapp;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
@RestController  
public class HelloController   
{  
@RequestMapping("/hello")  
public String hello()   
{  
return "Greetings from Spring Boot Successful - Excellent JOB VERSION 2";
}
}
