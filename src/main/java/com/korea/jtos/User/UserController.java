package com.korea.jtos.User;

import com.korea.jtos.DataNotFoundException;
import com.korea.jtos.Mail.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/profile/{userId}")
    public String userProfile(Model model, Principal principal) {
        // 현재 로그인된 사용자의 유저네임을 Principal 객체에서 추출
        String username = principal.getName();

        // 유저네임을 사용하여 사용자 정보 조회
        SiteUser user = this.userService.getUserByUsername(username);

        // 사용자 정보가 null인지 확인
        if (user == null) {
            return "userNotFound"; // 사용자가 존재하지 않을 경우 적절한 처리 필요
        }

        // 모델에 사용자 정보 추가
        model.addAttribute("userNickname",user.getUsernickname());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        // 프로필 페이지 뷰 이름 반환
        return "userProfile_form";
    }

    @GetMapping("/modifyPassword")
    private String modifyPassword(PasswordModifyForm passwordModifyForm) {
        return "modifyPassword_form";
    }

    @PostMapping("/modifyPassword")
    public String modifyPassword(@Valid PasswordModifyForm passwordModifyForm,
                                 BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "modifyPassword_form";
        }

        // 비밀번호와 비밀번호 확인이 일치하는지 검사
        if (!passwordModifyForm.getPassword1().equals(passwordModifyForm.getPassword2())) {
            model.addAttribute("error", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "modifyPassword_form";
        }

        try {
            String username = principal.getName(); // 현재 로그인한 사용자의 username
            SiteUser user = this.userService.getUser(username);
            if (!passwordEncoder.matches(passwordModifyForm.getPassword(), user.getPassword())) {
                model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
                return "modifyPassword_form";
            }

            String newPassword = passwordModifyForm.getPassword1();
            this.userService.modifyPassword(user, newPassword);
            return "redirect:/user/login";
        } catch (DataNotFoundException e) {
            model.addAttribute("error", "사용자가 존재하지 않습니다.");
            return "modifyPassword_form";
        }
    }

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
            String password = this.userService.generateTemporaryPassword();
            this.userService.modifyPassword(user, password);
            mailService.sendPasswordResetEmail(user.getEmail(), "임시 비밀번호",
                    "임시 비밀번호 : " + password + " 로그인 후 비밀번호 변경 필수.");
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
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1(), userCreateForm.getUsernickname());
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
                userCreateForm.getEmail(), userCreateForm.getPassword1(),userCreateForm.getUsernickname());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
