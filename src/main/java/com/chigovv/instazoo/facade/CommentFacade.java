package com.chigovv.instazoo.facade;

import com.chigovv.instazoo.dto.CommentDTO;
import com.chigovv.instazoo.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

    public CommentDTO commentToCommentDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUsername(comment.getUserName());
        commentDTO.setMessage(comment.getMessage());

        return commentDTO;
    }
}
