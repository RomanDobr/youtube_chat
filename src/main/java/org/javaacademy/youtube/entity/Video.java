package org.javaacademy.youtube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "video")
@Data
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name_video")
    private String nameVideo;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "video")
    @ToString.Exclude
    private List<Comment> commentList;

    public Video(String nameVideo, String content, User user) {
        this.nameVideo = nameVideo;
        this.content = content;
        this.user = user;
    }
}
