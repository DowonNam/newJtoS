package com.korea.jtos.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileModifyForm {

    private String usernickname;

    private String email;

    private MultipartFile profileImage;
}
