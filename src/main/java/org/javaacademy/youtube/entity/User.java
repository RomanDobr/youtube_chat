package org.javaacademy.youtube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user_youtube")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nick_name")
    private String nickName;

    @OneToMany(mappedBy = "user")
    private List<Video> videoList;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList;

    public User(String nickName) {
        this.nickName = nickName;
    }
}
