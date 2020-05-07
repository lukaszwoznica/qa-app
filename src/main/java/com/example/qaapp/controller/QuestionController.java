package com.example.qaapp.controller;

import com.example.qaapp.model.Question;
import com.example.qaapp.payload.request.PostQuestionRequest;
import com.example.qaapp.payload.request.PutQuestionRequest;
import com.example.qaapp.payload.response.MessageResponse;
import com.example.qaapp.repository.QuestionRepository;
import com.example.qaapp.service.QuestionService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            List<Question> questions = questionService.findAll();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") Long id) {
        Optional<Question> maybeQuestion = questionService.findById(id);

        return maybeQuestion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createQuestion(@Valid @RequestBody PostQuestionRequest request) {
        try {
            return ResponseEntity.ok(questionService.create(request));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable("id") Long id, @Valid @RequestBody PutQuestionRequest request) {
        Optional<Question> maybeQuestion = questionService.findById(id);

        if (maybeQuestion.isPresent()) {
            try {
                return ResponseEntity.ok(questionService.update(maybeQuestion.get(), request));
            } catch (NotFoundException e) {
                return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable("id") Long id) {
        try {
            questionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}