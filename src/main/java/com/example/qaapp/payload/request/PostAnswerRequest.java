package com.example.qaapp.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostAnswerRequest {
    @NotBlank
    private String body;

    @NotNull
    private Long questionId;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

}
