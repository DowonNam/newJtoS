package com.korea.jtos.Answer;

import com.korea.jtos.Answer.Answer;
import com.korea.jtos.Question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer>findByQuestionId(Integer questionId, Pageable pageable);
}
