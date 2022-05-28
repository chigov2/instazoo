package com.chigovv.instazoo.web;

import com.chigovv.instazoo.payload.response.JWTTokenSuccessResponse;
import com.chigovv.instazoo.payload.response.MessageResponse;
import com.chigovv.instazoo.payload.request.LoginRequest;
import com.chigovv.instazoo.security.JWTTokenProvider;
import com.chigovv.instazoo.security.SecurityConstants;
import com.chigovv.instazoo.service.UserService;
import com.chigovv.instazoo.payload.request.SignupRequest;
import com.chigovv.instazoo.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult){
        //первое - можем получить ошибки
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);

        //если есть ошибки - надо вернуть ошибки
        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));

    }
}
