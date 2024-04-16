package com.korea.jtos.User;

import com.korea.jtos.DataNotFoundException;
import com.korea.jtos.Mail.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @GetMapping("/findPassword")
    private String findPassword(UserPasswordModifyForm userPasswordModifyForm) {
        return "findPassword_form";
    }

    @PostMapping("/findPassword")
    public String findPassword(@Valid UserPasswordModifyForm userPasswordModifyForm,
                               Model model,
                               BindingResult bindingResult,
                               @RequestParam("username") String username,
                               @RequestParam("email") String email) {
        if (bindingResult.hasErrors()) {
            return "findPassword_form";
        }
        try {
            SiteUser user = this.userService.getUser(username);
            if (!user.getEmail().equals(email)) {
                model.addAttribute("error", "아이디와 이메일이 일치하지 않습니다. 다시 확인해 주세요.");
                return "findPassword_form";
            }
            String newPassword = this.userService.generateTemporaryPassword();
            this.userService.modifyPassword(user, newPassword);
            mailService.sendPasswordResetEmail(user.getEmail(), "임시 비밀번호",
                    "임시 비밀번호 : " + newPassword + " 로그인 후 비밀번호 변경 필수.");
            return "redirect:/user/login";
        } catch (DataNotFoundException e) {
            return "findPassword_form";
        }
    }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        userService.create(userCreateForm.getUsername(),
                userCreateForm.getEmail(), userCreateForm.getPassword1());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
