package com.korea.jtos.Question;

import com.korea.jtos.Category.Category;
import com.korea.jtos.Comment.Comment;
import com.korea.jtos.Answer.Answer;
import com.korea.jtos.User.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @ManyToOne
    private Category category;

    private int hit = 0;

    // 최근 답변 시간을 나타내는 필드 추가
    private LocalDateTime lastAnsweredAt;

    // 최근 댓글 시간을 나타내는 필드 추가
    private LocalDateTime lastCommentedAt;

    // 최근 답변 시간을 업데이트하는 메서드
    public void updateLastAnsweredAt() {
        this.lastAnsweredAt = LocalDateTime.now();
    }

    // 최근 댓글 시간을 업데이트하는 메서드
    public void updateLastCommentedAt() {
        this.lastCommentedAt = LocalDateTime.now();
    }
}
