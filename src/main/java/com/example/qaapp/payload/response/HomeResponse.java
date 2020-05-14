package com.example.qaapp.payload.response;

import java.util.Map;

public class HomeResponse {
    private String message;
    private Map<String, String> endpoints;

    public HomeResponse(String message, Map<String, String> endpoints) {
        this.message = message;
        this.endpoints = endpoints;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Map<String, String> endpoints) {
        this.endpoints = endpoints;
    }
}
