package org.com.health;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerHealth {

    @GetMapping("/api/health")
    public String healthCheck() {
        System.out.println("Health check called");  // Add this line
        return "OK";
    }
}