package com.davies.F1Sim.Controllers;

import com.davies.F1Sim.DTO.ChangePasswordDTO;
import com.davies.F1Sim.DTO.LoginDTO;
import com.davies.F1Sim.DTO.UserDTO;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Exceptions.UserExistsException;
import com.davies.F1Sim.Services.TokenService;
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
    @Autowired
    TokenService tokenService;
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

    @GetMapping("/getUser")
    @CrossOrigin
    public UserDTO user(@RequestHeader("Authorization") String token) throws UserExistsException {
        return userService.getUserDTO(token);
    }


    @GetMapping("/getPlayers")
    @CrossOrigin
    public List<User> getPlayers(@RequestHeader("Authorization") String token){
        return userService.getPlayers(token);
    }

    @PutMapping("/changePassword")
    @CrossOrigin
    public String changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordDTO passwordDTO){
        System.out.println(passwordDTO.toString());
        String msg = "";
        msg = userService.changePassword(passwordDTO);
        return msg;
    }
}