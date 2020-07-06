package com.example.qaapp.controller;

import com.example.qaapp.model.Question;
import com.example.qaapp.payload.request.QuestionRequest;
import com.example.qaapp.payload.response.MessageResponse;
import com.example.qaapp.service.QuestionService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            List<Question> questions = questionService.findAll();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") Long id) {
        Optional<Question> maybeQuestion = questionService.findById(id);

        return maybeQuestion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/categories/{id}/questions")
    public ResponseEntity<Set<Question>> getAllQuestionsFromCategory(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(questionService.findAllByCategoryId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/questions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionRequest request,
                                            @RequestHeader("Authorization") String token) {
        try {
            Question question = questionService.create(request, token);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(question.getId())
                    .toUri();

            return ResponseEntity.created(location).body(question);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/questions/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateQuestion(@PathVariable("id") Long id, @Valid @RequestBody QuestionRequest request) {
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

    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
