package com.korea.jtos;

import com.korea.jtos.User.SiteUser;
import com.korea.jtos.User.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.xml.parsers.SAXParser;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    DefaultOAuth2UserService customDefaultOauthUserService;

//    private UserService userService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/user/login", "/oauth2/authorization/**").permitAll()
                        .requestMatchers("/user/signup", "/oauth2/authorization/**").permitAll()
                        .requestMatchers("/", "/oauth2/authorization/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"),
                                new AntPathRequestMatcher("/oauth2/authorization/**")))
                .headers(headers -> headers
                        .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", "frame-ancestors 'self'")))
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout", "GET"))  // GET 요청으로 로그아웃을 허용
                        .logoutSuccessUrl("/").invalidateHttpSession(true))
                .oauth2Login(oauthLogin -> oauthLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customDefaultOauthUserService)));
        return http.build();
    }

//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
//        return userRequest -> {
//            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
//            OAuth2User user = delegate.loadUser(userRequest);
//            // 구글 OAuth2 사용자의 정보를 가져옵니다.
//            String email = user.getAttribute("email"); // 사용자 이메일을 가져옵니다. (사용자 이름으로 사용할 수 있음)
//            String name = user.getAttribute("name");
//            // 이메일이나 기타 식별자를 사용하여 사용자 이름을 설정합니다.
//            String username = email != null ? this.userService.getUsernameByEmail(email) : "Google User"; // 이메일이 없는 경우 "Google User"로 설정
//            // 사용자 이름을 추가 또는 수정합니다.
//            // 여기에서 필요한 다른 사용자 정보를 가져와서 추가적으로 처리할 수도 있습니다.
//            return user;
//        };
//    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}