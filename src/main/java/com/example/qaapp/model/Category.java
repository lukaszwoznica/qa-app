package com.example.qaapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 255)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Question> questions = new HashSet<>();;

    public Category() {

    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }
}
