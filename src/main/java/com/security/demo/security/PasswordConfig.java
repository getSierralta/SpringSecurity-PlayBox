package com.security.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//Use @Configuration annotation on top of any class to declare that this class provides one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.
public class PasswordConfig {

    @Bean//Spring @Bean annotation indicates that a method produces a bean to be managed by the Spring container
    public PasswordEncoder passwordEncoder(){
        //PasswordEncoder is an interface with 3 methods encode, matches and upgradeEncoding
        //This is what Spring Security uses to encode passwords internally
        return new BCryptPasswordEncoder(10); //receives how strong you want the password to be encrypted
    }
}
