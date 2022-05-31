package com.chigovv.instazoo.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String caption; //описание
    private String location;
    private Integer likes;

    @Column
    @ElementCollection(targetClass = String.class)//чтобы можно было сохранять как аррай коллекцию пользователей
    private Set<String> likedUsers = new HashSet<>();//пользователи, которые лайкнули данній пост

    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id")
    private User user;//пользователь, котроый создал пост

    @OneToMany(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER, orphanRemoval = true,mappedBy = "post")//fetch - сразу комент появляется
    private List<Comment> comments = new ArrayList<>();
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist// задает значение атрибута до записи в БД
    private void onCreate(){
        this.createdDate = LocalDateTime.now();
    }

}
