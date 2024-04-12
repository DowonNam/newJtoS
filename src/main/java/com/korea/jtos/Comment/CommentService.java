package com.korea.jtos.Comment;

import com.korea.jtos.Answer.Answer;
import com.korea.jtos.DataNotFoundException;
import com.korea.jtos.Question.Question;
import com.korea.jtos.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createQuestionComment(Question question, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setQuestion(question);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        return comment;
    }

    public Comment getQuestionComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public Comment createAnswerComment(Answer answer, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setAnswer(answer);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
        return comment;
    }

    public Comment getAnswerComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public Comment getComment(Integer id){
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }
    public void modify(Comment comment,String content){
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }
    public void vote(Comment comment,SiteUser siteUser){
        comment.getVoter().add(siteUser);
        this.commentRepository.save(comment);
    }
}
