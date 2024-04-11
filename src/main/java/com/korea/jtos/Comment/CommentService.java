package com.korea.jtos.Comment;

import com.korea.jtos.Answer.Answer;
import com.korea.jtos.DataNotFoundException;
import com.korea.jtos.Question.Question;
import com.korea.jtos.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(Question question, String content, SiteUser author){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setQuestion(question);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        return comment;
    }
    public Comment create(Answer answer, String content, SiteUser author){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setAnswer(answer);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        return comment;
    }
    public Comment getAnswerComment(Integer id){
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent()){
            return comment.get();
        }else{
            throw new DataNotFoundException("answer not found");
        }
    }
    public Comment getQuestionComment(Integer id){
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent()){
            return comment.get();
        }else{
            throw new DataNotFoundException("question not found");
        }
    }

}
