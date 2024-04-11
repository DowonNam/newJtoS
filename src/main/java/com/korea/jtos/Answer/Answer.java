package com.korea.jtos.Answer;

import com.korea.jtos.Question.Question;
import com.korea.jtos.User.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter;

    @Transient  // 데이터베이스에 저장되지 않음
    private int voteCount;

    // Getter와 Setter
    public int getVoteCount() {
        return voter.size();
    }
}
