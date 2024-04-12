package com.korea.jtos.Comment;

import com.korea.jtos.Answer.Answer;
import com.korea.jtos.Answer.AnswerForm;
import com.korea.jtos.Answer.AnswerService;
import com.korea.jtos.Question.Question;
import com.korea.jtos.Question.QuestionService;
import com.korea.jtos.User.SiteUser;
import com.korea.jtos.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {


    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    private final CommentService commentService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/createQuestion/{id}")
    public String createQuestionComment(@PathVariable("id") Integer id,
                                        @ModelAttribute("commentForm") @Valid CommentForm commentForm,
                                        BindingResult bindingResult,
                                        Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            Question question = questionService.getQuestion(id);
            model.addAttribute("question", question);
            model.addAttribute("commentForm", commentForm);
            model.addAttribute("org.springframework.validation.BindingResult.commentForm", bindingResult);
            return "question_detail";
        }
        SiteUser siteuser = userService.getUser(principal.getName());
        Comment comment = commentService.create(questionService.getQuestion(id), commentForm.getContent(), siteuser);
        return String.format("redirect:/question/detail/%s#comment_%s", comment.getQuestion().getId(), comment.getId());
    }


}
