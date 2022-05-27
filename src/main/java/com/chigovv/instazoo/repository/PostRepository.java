package com.chigovv.instazoo.repository;

import com.chigovv.instazoo.entity.Post;
import com.chigovv.instazoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    //будем возвращать много постов
    List<Post> findAllByUserOrderByCreatedDateDesc(User user);//видеть только посты, принадлежащие юзеру

    //найтьи все посты
    List<Post> findAllByOrderByCreatedDateDesc();//на главной надо видеть все посты из базы данных

    Optional<Post> findPostByIdAndUser(Long id, User user);






}
