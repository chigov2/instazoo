package com.chigovv.instazoo.service;

import com.chigovv.instazoo.dto.PostDTO;
import com.chigovv.instazoo.entity.Post;
import com.chigovv.instazoo.entity.User;
import com.chigovv.instazoo.exceptions.PostNotFoundException;
import com.chigovv.instazoo.repository.ImageRepository;
import com.chigovv.instazoo.repository.PostRepository;
import com.chigovv.instazoo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal){
        //нужно получить пользователя из объекта principal
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("Saving post for user: {}",user.getEmail());

        return postRepository.save(post);
    }
    //возвращать все посты из бд
    public List<Post> getAllPosts(){
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    //principal - принадлежит ли пост данному пользователю
    public Post getPostById(Long postId, Principal principal){
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user).orElseThrow(()->
        new PostNotFoundException("Post can not be found for username: "+user.getEmail()));
    }

    //все посты конкретного  юзера


    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("Username not found with username " + username));
    }

}
