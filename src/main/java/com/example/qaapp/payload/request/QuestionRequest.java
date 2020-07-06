package com.example.qaapp.payload.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class QuestionRequest {
    @NotBlank
    private String body;

    @NotNull
    private Integer categoryId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
