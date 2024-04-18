package com.korea.jtos.Comment;

import com.korea.jtos.User.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByAuthor_Id(Long userId);

}
