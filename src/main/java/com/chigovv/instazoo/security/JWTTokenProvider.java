package com.chigovv.instazoo.security;

import com.chigovv.instazoo.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

    //методы для генерации токена
    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();//хранит данные пользователя
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id",userId);
        claimsMap.put("username",user.getEmail());
        claimsMap.put("firstname",user.getName());
        claimsMap.put("lastname",user.getLastname());

        return Jwts.builder()//создаем токен
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.SECRET)
                .compact();
    }
    //проверка токена
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJwt(token);
            return true;
        }catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex){
            LOG.error(ex.getMessage());
        }
        return  false;
    }

    //метод будет брать данные из токена id
    public  Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJwt(token)
                .getBody();
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }
}