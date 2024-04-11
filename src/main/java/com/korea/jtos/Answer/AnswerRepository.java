package com.korea.jtos.Answer;

import com.korea.jtos.Answer.Answer;
import com.korea.jtos.Question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    @Query("SELECT a FROM Answer a JOIN a.voter v GROUP BY a.id ORDER BY COUNT(v) DESC")
    Page<Answer> findByQuestion(Pageable pageable, Question question);
    Page<Answer>findByQuestionId(Integer questionId, Pageable pageable);
}
