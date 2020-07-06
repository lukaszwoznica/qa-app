package com.example.qaapp.controller;

import com.example.qaapp.model.Answer;
import com.example.qaapp.payload.request.PostAnswerRequest;
import com.example.qaapp.payload.request.PutAnswerRequest;
import com.example.qaapp.payload.response.MessageResponse;
import com.example.qaapp.service.AnswerService;
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
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/answers")
    public ResponseEntity<List<Answer>> getAllAnswers() {
        try {
            List<Answer> answers = answerService.findAll();
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable("id") Long id) {
        Optional<Answer> maybeAnswer = answerService.findById(id);

        return maybeAnswer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/questions/{id}/answers")
    public ResponseEntity<Set<Answer>> getAllAnswersFromQuestion(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(answerService.findAllByQuestionId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/answers")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createAnswer(@Valid @RequestBody PostAnswerRequest request,
                                          @RequestHeader("Authorization") String token) {
        try {
            Answer answer = answerService.create(request, token);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(answer.getId())
                    .toUri();

            return ResponseEntity.created(location).body(answer);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/answers/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Answer> updateAnswer(@PathVariable("id") Long id, @RequestBody PutAnswerRequest request) {
        Optional<Answer> maybeAnswer = answerService.findById(id);

        if (maybeAnswer.isPresent()) {
            Answer answer = maybeAnswer.get();
            answer.setBody(request.getBody());

            return ResponseEntity.ok(answerService.update(answer));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/answers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAnswer(@PathVariable("id") Long id) {
        try {
            answerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
