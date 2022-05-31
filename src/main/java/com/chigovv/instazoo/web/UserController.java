package com.chigovv.instazoo.web;

import com.chigovv.instazoo.dto.UserDTO;
import com.chigovv.instazoo.entity.User;
import com.chigovv.instazoo.facade.UserFacade;
import com.chigovv.instazoo.service.UserService;
import com.chigovv.instazoo.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    //возврат пользователя по его ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId){
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    //update user profile
    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO,
                                             BindingResult bindingResult,
                                             Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUser(userDTO,principal);
        //вернуть обновленного пользователя в клиент
        UserDTO userUpdated = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userUpdated,HttpStatus.OK);
    }
}
