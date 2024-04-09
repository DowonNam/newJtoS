package com.korea.jtos;


import com.korea.jtos.Answer.Answer;
import com.korea.jtos.Answer.AnswerRepository;
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

    @Test
    void testJpa() {
        for(int i = 0; i < 300; i++){
            String subject = String.format("테스터 데이터입니다.[%03d]",i);
            String content = "내용무";
            this.questionService.create(subject,content,null);
        }
    }
}

