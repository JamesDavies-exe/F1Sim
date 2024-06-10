package com.davies.F1Sim.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    @Value("${token.secret}")
    String tokenSecret;
    @Value("${token.expiration}")
    int tokenExpiration;
    @Autowired
    UserRepo userRepo;

    public String getToken(User user) {
        Map<String, Object> map = new HashMap<>();

        //Agregamos los atributos del token
        map.put("_id", user.getId());
        map.put("email", user.getMail());
        map.put("name", user.getName());
        map.put("id", user.getId());
        map.put("iat", new Date(System.currentTimeMillis()));

        //Creamos el token
        String token = JWT.create()
                .withPayload(map)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration))
                .sign(Algorithm.HMAC512(tokenSecret.getBytes()));

        return token;
    }

    public User getUserFromToken(String token) {
        token = token.replace("Bearer: ", "");
        String email = JWT.require(Algorithm.HMAC512(tokenSecret.getBytes()))
                .build()
                .verify(token)
                .getClaim("email")
                .asString();
        return userRepo.findByMail(email);
    }
}
