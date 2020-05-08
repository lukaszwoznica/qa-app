package com.example.qaapp.payload.request;

import javax.validation.constraints.NotBlank;

public class PutAnswerRequest {
    @NotBlank
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
