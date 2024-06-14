package com.davies.F1Sim.Services;

import com.davies.F1Sim.DTO.ChangePasswordDTO;
import com.davies.F1Sim.DTO.LoginDTO;
import com.davies.F1Sim.DTO.UserDTO;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Exceptions.UserExistsException;
import com.davies.F1Sim.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    TokenService tokenService;
    @Value("${client-id}")
    String clientId;
    @Value("${redirect-uri}")
    String redirectUri;

    public void registerUser(User user) throws UserExistsException {
        if (userRepo.existsByMail(user.getMail())) {
            throw new UserExistsException("Este usuario ya existe");
        }
        if (user.getPassword().length() < 6) {
            throw new UserExistsException("La contraseña debe tener al menos 6 caracteres");
        }
//        Encriptamos la contraseña
        user.setPassword(hashPassword(user.getPassword()));
        // Al crear el usuario no estará online al no haber iniciado sesión todavia
        user.setOnline(false);
        user.setRole("user");
        userRepo.save(user);
    }

    public Map<String, String> loginUser(LoginDTO loginForm) throws UserExistsException {
        User user = userRepo.findByMailAndPassword(loginForm.email(), hashPassword(loginForm.password()));
        Map<String, String> map = new HashMap<>();
        if (user == null) {
            throw new UserExistsException("User or password incorrect");
        }
        user.setOnline(true);
        userRepo.save(user);
        map.put("username", user.getName());
        map.put("token", tokenService.getToken(user));
        return map;
    }

    public static String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] encodedhash = digest.digest(password.getBytes());

            // Convert byte array into signum representation
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getPlayers(String token) {
        User currentUser = tokenService.getUserFromToken(token);
        List<User> users = userRepo.findAll();
        users.remove(currentUser);
        return users;
    }

    public UserDTO getUserDTO(String token) throws UserExistsException {
        User user = tokenService.getUserFromToken(token);
        System.out.println(user.getName());
        return new UserDTO(user.getName(), user.getRole());
    }

    public String changePassword(ChangePasswordDTO passwordDTO) {
        String msg = "";
        User user = userRepo.findByPassword(hashPassword(passwordDTO.currentPassword()));
        if(user != null){
            user.setPassword(hashPassword(passwordDTO.newPassword()));
            userRepo.save(user);
            System.out.println("YES");
            msg = "La contraseña se ha cambiado";
        }else {
            if (user.getPassword() != hashPassword(passwordDTO.currentPassword())) msg = "Error: la contraseña no es correcta";
            if (passwordDTO.newPassword().length() < 6) msg = "Error: la contraseña deber tener al menos 6 caracteres";
        }
        return msg;
    }
}