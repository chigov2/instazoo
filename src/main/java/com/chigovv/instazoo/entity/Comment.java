package com.chigovv.instazoo.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;//владелец комментария
    @Column(nullable = false)
    private String username;//имя юзера, котрый сделал комментарий
    @Column(nullable = false)
    private Long userId;
    @Column(columnDefinition = "text",nullable = false)
    private String message;
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist// задает значение атрибута до записи в БД
    private void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
