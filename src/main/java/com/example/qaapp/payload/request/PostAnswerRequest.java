package com.example.qaapp.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostAnswerRequest {
    @NotBlank
    private String body;

    @NotNull
    private Long questionId;

    @NotNull
    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
