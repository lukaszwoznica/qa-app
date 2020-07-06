package com.example.qaapp.service;

import com.example.qaapp.model.Category;
import com.example.qaapp.model.Question;
import com.example.qaapp.model.User;
import com.example.qaapp.payload.request.QuestionRequest;
import com.example.qaapp.repository.CategoryRepository;
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
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           CategoryRepository categoryRepository,
                           UserRepository userRepository,
                           JwtUtils jwtUtils) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    public Question create(QuestionRequest request, String token) throws NotFoundException {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found with id: " + request.getCategoryId()));

        String clearToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtUtils.getUserNameFromJwtToken(clearToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        Question question = new Question(request.getBody(), category, user);

        return questionRepository.save(question);
    }

    public Question update(Question question, QuestionRequest request) throws NotFoundException {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found with id: " + request.getCategoryId()));

        question.setBody(request.getBody());
        question.setCategory(category);

        return questionRepository.save(question);
    }

    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }

    public Set<Question> findAllByCategoryId(Integer categoryId) throws NotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category Not Found with id: " + categoryId));

        return category.getQuestions();
    }
}
