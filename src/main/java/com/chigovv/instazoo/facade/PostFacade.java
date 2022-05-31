package com.chigovv.instazoo.facade;

import com.chigovv.instazoo.dto.PostDTO;
import com.chigovv.instazoo.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDTO postToPostDTO(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setId(post.getId());
        postDTO.setCaption(post.getCaption());
        postDTO.setLikes(post.getLikes());
        postDTO.setUserLiked(post.getLikedUsers());
        postDTO.setLocation(post.getLocation());
        postDTO.setTitle(post.getTitle());
        return postDTO;
    }
}
