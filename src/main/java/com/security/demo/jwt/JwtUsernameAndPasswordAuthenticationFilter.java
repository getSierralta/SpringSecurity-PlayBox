package com.security.demo.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

//Processes an authentication form. We need authentication to make sure that the user is really who they claim to be.
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final AuthenticationManager authenticationManager;


    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.authenticationManager = authenticationManager;
    }


    @Override
    //Were we are sending the credentials and we validate them

    //The attemptAuthentication function runs when the user tries to log in to our application. It reads the credentials, creates a user POJO from them, and then checks the credentials to authenticate.
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // The Jackson ObjectMapper can parse JSON from a string, stream or file, and create a Java object or object graph representing the parsed JSON.
        try {
            UserNameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    //The getInputStream() method of Java Socket class returns an input stream for the given socket.
                    .readValue(request.getInputStream(), UserNameAndPasswordAuthenticationRequest.class);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                    //name
                    authenticationRequest.getUsername(),
                    //credentials
                    authenticationRequest.getPassword()
            );
            //AuthenticationManager makes sure the user name exist and it checks the credentials
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    //This method will be invoke after the attemptAuthentication is successful
    //Here we will create a token and send it to our client
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //This should be very secure
        String token = Jwts.builder()
                //Header
                .setSubject(authResult.getName())
                //body
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                //Sing
                // HMACSHA1 algorithm
                .signWith(secretKey)
                .compact();
        //We add the token to the response header
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix()+token);
    }


}
