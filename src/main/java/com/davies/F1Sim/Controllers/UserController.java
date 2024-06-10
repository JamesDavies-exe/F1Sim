package com.davies.F1Sim.Controllers;

import com.davies.F1Sim.DTO.LoginDTO;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Exceptions.UserExistsException;
import com.davies.F1Sim.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    @CrossOrigin
    public void register(@RequestBody User newUser, HttpServletResponse response) {
        System.out.println(newUser.getMail());
        try {
            userService.registerUser(newUser);
        } catch (UserExistsException e) {
            System.out.println(e);
            response.setStatus(409);
        }
    }

    @PostMapping("/login")
    @CrossOrigin
    public Map<String, String> login(@RequestBody LoginDTO loginForm, HttpServletResponse response) throws UserExistsException {
        System.out.println(loginForm);
        Map<String, String> map = new HashMap<>();
        try {
            map = userService.loginUser(loginForm);
        } catch (UserExistsException e) {
            response.setStatus(409);
        }
        return map;
    }

    @GetMapping("/user")
    @CrossOrigin
    public String user(@RequestHeader("Authorization") String token){
        System.out.println(token);
        return "done";
    }

    @GetMapping("/logingoogle")
    @CrossOrigin
    public String logingoogle() throws URISyntaxException, MalformedURLException {
        return userService.getGoogleRedirection();
    }

    @GetMapping("/oauth2/callback")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Void> oauthCallback(@RequestParam String code) throws Exception {
        // Procesar el código de autorización y obtener el correo del usuario
        String userEmail = userService.getGoogleUserEmail(code);

        // Redirigir a la URL del cliente
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:5173/login"));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/getPlayers")
    @CrossOrigin
    public List<User> getPlayers(@RequestHeader("Authorization") String token){
        return userService.getPlayers(token);
    }
}