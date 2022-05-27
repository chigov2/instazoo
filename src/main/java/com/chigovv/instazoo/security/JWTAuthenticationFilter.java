package com.chigovv.instazoo.security;

import com.chigovv.instazoo.entity.User;
import com.chigovv.instazoo.service.CustomUserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    //1 добавить JWT provider
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    //2
    @Autowired
    private CustomUserDetailService customUserDetailService;

    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //4 как только постувает запрос на сервер - будет вызываться этот метод

        try {
            String jwt = getJWTFromRequest(request);//дудем брать данные из этого запроса-
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
                //берем данные из токена
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                User userDetails = customUserDetailService.loadUserById(userId);

                //Пытаемся найти пользователя в БД
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,null,
                                Collections.emptyList()
                );
                //авторизация - задать детали
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            LOG.error("Could not set user authentication");
        }

        //5
        filterChain.doFilter(request,response);
    }

    //3 написать метод, который будет получать json web token прямо из запроса,
    // который будет поступать на сервер
    private String getJWTFromRequest(HttpServletRequest request){
        //каждый раз, как будет передаваться запрос на сервер - будет передаваться token внутри header
        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)){
            return bearToken.split(" ")[1];
        }
        return null;
    }

}
