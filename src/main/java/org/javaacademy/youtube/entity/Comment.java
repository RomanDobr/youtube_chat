package org.javaacademy.youtube.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "video_id")
    @ToString.Exclude
    private Video video;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    public Comment(Video video, User user, String text) {
        this.video = video;
        this.user = user;
        this.text = text;
    }
}
