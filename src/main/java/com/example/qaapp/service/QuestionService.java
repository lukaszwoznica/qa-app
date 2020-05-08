package com.example.qaapp.service;

import com.example.qaapp.model.Category;
import com.example.qaapp.model.Question;
import com.example.qaapp.model.User;
import com.example.qaapp.payload.request.PostQuestionRequest;
import com.example.qaapp.payload.request.PutQuestionRequest;
import com.example.qaapp.repository.CategoryRepository;
import com.example.qaapp.repository.QuestionRepository;
import com.example.qaapp.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           CategoryRepository categoryRepository,
                           UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    public Question create(PostQuestionRequest request) throws NotFoundException {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found with id: " + request.getCategoryId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User Not Found with id: " + request.getUserId()));

        Question question = new Question(request.getBody(), category, user);

        return questionRepository.save(question);
    }

    public Question update(Question question, PutQuestionRequest request) throws NotFoundException {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found with id: " + request.getCategoryId()));

        question.setBody(request.getBody());
        question.setCategory(category);

        return questionRepository.save(question);
    }

    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }
}
