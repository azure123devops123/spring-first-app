package com.devops.springfirstapp;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;
@RestController  
public class HelloController   {
    @RequestMapping("/hello")  
    public String hello() throws UnknownHostException   {  
    var hostname = InetAddress.getLocalHost().getHostAddress();
    return "JAVA SPRING BOOT APPLICATION RESPONSE FROM HOST => " + hostname;
    // var hostname = InetAddress.getLocalHost().getHostName();
    // System.out.println(hostname);
    }
}