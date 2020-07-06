package com.example.qaapp.service;

import com.example.qaapp.model.Answer;
import com.example.qaapp.model.Question;
import com.example.qaapp.model.User;
import com.example.qaapp.payload.request.PostAnswerRequest;
import com.example.qaapp.repository.AnswerRepository;
import com.example.qaapp.repository.QuestionRepository;
import com.example.qaapp.repository.UserRepository;
import com.example.qaapp.security.jwt.JwtUtils;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public AnswerService(AnswerRepository answerRepository,
                         QuestionRepository questionRepository,
                         UserRepository userRepository,
                         JwtUtils jwtUtils) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    public Optional<Answer> findById(Long id) {
        return answerRepository.findById(id);
    }

    public Answer create(PostAnswerRequest request, String token) throws NotFoundException {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new NotFoundException("Question Not Found with id: " + request.getQuestionId()));

        String clearToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtUtils.getUserNameFromJwtToken(clearToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        Answer answer = new Answer(request.getBody(), question, user);

        return answerRepository.save(answer);
    }

    public Answer update(Answer answer) {
        return answerRepository.save(answer);
    }

    public void deleteById(Long id) {
        answerRepository.deleteById(id);
    }

    public Set<Answer> findAllByQuestionId(Long questionId) throws NotFoundException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Category Not Found with id: " + questionId));

        return question.getAnswers();
    }
}
