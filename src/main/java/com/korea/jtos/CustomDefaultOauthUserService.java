package com.korea.jtos;

import com.korea.jtos.User.SiteUser;
import com.korea.jtos.User.TestUser;
import com.korea.jtos.User.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomDefaultOauthUserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User user = delegate.loadUser(userRequest);
        // 구글 OAuth2 사용자의 정보를 가져옵니다.
        String email = user.getAttribute("email"); // 사용자 이메일을 가져옵니다. (사용자 이름으로 사용할 수 있음)

        // 이메일이나 기타 식별자를 사용하여 사용자 이름을 설정합니다.
        SiteUser target = userRepository.findByEmail(email);
        String username = email != null ? target.getUsername() : "Google User"; // 이메일이 없는 경우 "Google User"로 설정
        // 사용자 이름을 추가 또는 수정합니다.
        // 여기에서 필요한 다른 사용자 정보를 가져와서 추가적으로 처리할 수도 있습니다.

        return new TestUser(username);
    }
}