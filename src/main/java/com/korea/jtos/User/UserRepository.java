package com.korea.jtos.User;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.plaf.OptionPaneUI;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByusername(String username);
}