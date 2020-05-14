package com.example.qaapp.controller;

import com.example.qaapp.payload.response.HomeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class HomeController {
    @GetMapping
    public ResponseEntity<HomeResponse> index(HttpServletRequest request) {
        Map<String, String> endpoints = new HashMap<>();
        String baseUrl = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri().toString();

        endpoints.put("home", baseUrl);
        endpoints.put("sing up", baseUrl.concat("/auth/signup"));
        endpoints.put("login", baseUrl.concat("/auth/login"));
        endpoints.put("categories", baseUrl.concat("/categories/{id}"));
        endpoints.put("questions", baseUrl.concat("/questions/{id}"));
        endpoints.put("answers", baseUrl.concat("/answers/{id}"));

        return ResponseEntity.ok(new HomeResponse("Welcome in Q&A Application API", endpoints));
    }
}
