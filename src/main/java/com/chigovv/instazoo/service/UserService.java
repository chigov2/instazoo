package com.chigovv.instazoo.service;

import com.chigovv.instazoo.dto.UserDTO;
import com.chigovv.instazoo.entity.User;
import com.chigovv.instazoo.entity.enums.ERole;
import com.chigovv.instazoo.exceptions.UserExistException;
import com.chigovv.instazoo.payload.request.SignupRequest;
import com.chigovv.instazoo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //создать нового юзера
    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRole().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving user {}", userIn.getEmail());

            //сохранить юзера
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + "already exist. Please check credentials");
        }
    }

    //обновить пользователя
    public User updateUser(UserDTO userDTO, Principal principal){

        //чтобы обновить пользователя - сначала возьмем пользователя из БД
        User user = getUserByPrincipal(principal);
        //обновление
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        //сохраним обновленного пользователя в БД
        return userRepository.save(user);
    }
    //метод позволяет взять текущего пользователя
    public User getCurrentUser(Principal principal){
        return getUserByPrincipal(principal);
    }

    //вспомогательный метод, который поможет доставть юзера из объекта Principal
    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("Username not found with username " + username));
    }
}

