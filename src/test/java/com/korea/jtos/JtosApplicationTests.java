package com.korea.jtos;


import com.korea.jtos.Answer.Answer;
import com.korea.jtos.Answer.AnswerRepository;
import com.korea.jtos.Answer.AnswerService;
import com.korea.jtos.Category.Category;
import com.korea.jtos.Category.CategoryService;
import com.korea.jtos.Question.Question;
import com.korea.jtos.Question.QuestionRepository;
import com.korea.jtos.Question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JtosApplicationTests {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private CategoryService categoryService;

    @Test
    void testJpa() {
        for(int i = 0; i < 3; i++){
//            Question question = this.questionService.getQuestion(912);;
//            String content = String.format("내용무입니다:[%03d]", i);
//            this.answerService.create(question, content, null);
        }
    }
}

