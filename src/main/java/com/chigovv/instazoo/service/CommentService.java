package com.chigovv.instazoo.service;

import com.chigovv.instazoo.dto.CommentDTO;
import com.chigovv.instazoo.entity.Comment;
import com.chigovv.instazoo.entity.Post;
import com.chigovv.instazoo.entity.User;
import com.chigovv.instazoo.exceptions.PostNotFoundException;
import com.chigovv.instazoo.repository.CommentRepository;
import com.chigovv.instazoo.repository.PostRepository;
import com.chigovv.instazoo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    //метод позволяет сохранить комментарий
    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        Post post = postRepository
                .findById(postId).orElseThrow(()-> new PostNotFoundException("Post can not be found for username: " +user.getEmail()));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUserName(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment for Post: {}",post.getId());

        return commentRepository.save(comment);
    }

    //возвращать все комментарии для поста
    public List<Comment> getAllCommentsForPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("Post can not be found"));

        //по посту находим все комментапии
        List<Comment> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteComment(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }


    //вспомогательный метод, который поможет доставть юзера из объекта Principal
    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("Username not found with username " + username));
    }
}
