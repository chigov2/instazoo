package com.chigovv.instazoo.facade;

import com.chigovv.instazoo.dto.UserDTO;
import com.chigovv.instazoo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDTO userToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setBio(user.getBio());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }
}
