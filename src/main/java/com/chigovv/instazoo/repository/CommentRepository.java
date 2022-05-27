package com.chigovv.instazoo.repository;

import com.chigovv.instazoo.entity.Comment;
import com.chigovv.instazoo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post); //находим все комментарии по посту

    Comment findByIdAndUserId(Long commentId, Long userId);

}
