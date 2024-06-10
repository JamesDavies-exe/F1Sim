package com.davies.F1Sim.Services;

import com.davies.F1Sim.DTO.LoginDTO;
import com.davies.F1Sim.Entities.GoogleUser;
import com.davies.F1Sim.Entities.User;
import com.davies.F1Sim.Exceptions.UserExistsException;
import com.davies.F1Sim.Repos.GoogleUserRepo;
import com.davies.F1Sim.Repos.UserRepo;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
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
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    GoogleUserRepo googleUserRepo;
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

    public String getGoogleRedirection() throws URISyntaxException, MalformedURLException {
        String uri = "https://accounts.google.com/o/oauth2/v2/auth";
        URIBuilder u = new URIBuilder(uri);
        u.addParameter("client_id", clientId);
        u.addParameter("redirect_uri", redirectUri);
        u.addParameter("scope", "https://www.googleapis.com/auth/userinfo.email");
        u.addParameter("access_type", "offline");
        u.addParameter("state", "state_parameter_passthrough_value");
        u.addParameter("response_type", "code");
        u.addParameter("prompt", "select_account");
        System.out.println(u.build().toURL().toString());
        return u.build().toURL().toString();
    }

    @Value("${client-secret}")
    String clientSecret;

    public String getGoogleUserEmail(String code) throws Exception {
        URL uri = new URL("https://oauth2.googleapis.com/token");
        Map<String, String> parameters = new HashMap<>();

        System.out.println("Code: " + code);

        parameters.put("client_id", clientId);
        parameters.put("redirect_uri", redirectUri);
        parameters.put("code", code);
        parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", "authorization_code");

        String resultado = doPost(uri, parameters);
        System.out.println("Resultado: " + resultado);
        Map<String, String> map = new Gson().fromJson(resultado, HashMap.class);
        String accessToken = map.get("access_token");

        System.out.println("Token: " + accessToken);

        // Reemplazar con el endpoint correcto
        URL userInfoEndpoint = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
        String respuesta = doGet(userInfoEndpoint);
        System.out.println("Respuesta: " + respuesta);
        Map<String, String> map2 = new Gson().fromJson(respuesta, HashMap.class);

        GoogleUser googleUser = new GoogleUser();
        googleUser.setMail(map2.get("email"));
        googleUser.setToken(accessToken);
        googleUserRepo.save(googleUser);
        return map2.get("email"); // Cambiar a la propiedad correcta si es diferente
    }

    private String doGet(URL url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url.toString());
        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        }
        String errorMessage = EntityUtils.toString(response.getEntity());
        throw new RuntimeException("Error in response GET: " + response.getStatusLine().getStatusCode() + " " + errorMessage);
    }

    private String doPost(URL uri, Map<String, String> parameters) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(uri.toString());
        List<NameValuePair> nvps = new ArrayList<>();
        for (String s : parameters.keySet()) {
            nvps.add(new BasicNameValuePair(s, parameters.get(s)));
        }
        post.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        }
        String errorMessage = EntityUtils.toString(response.getEntity());
        throw new RuntimeException("Error in response POST: " + response.getStatusLine().getStatusCode() + " " + errorMessage);
    }

    public List<User> getPlayers(String token) {
        User currentUser = tokenService.getUserFromToken(token);
        List<User> users = userRepo.findAll();
        users.remove(currentUser);
        return users;
    }
}