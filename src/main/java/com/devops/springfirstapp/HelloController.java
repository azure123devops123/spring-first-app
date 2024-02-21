package com.devops.springfirstapp;  
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;
// import com.devops.springfirstapp.environment.InstanceInformationService;

@RestController  
public class HelloController   {
    // @Autowired
	// private InstanceInformationService service;

    @RequestMapping("/hello")  
    public String hello()   {  
    return "Hello World Greetings from Spring Boot Application" + " VERSION NO. 3 ";
    }
}