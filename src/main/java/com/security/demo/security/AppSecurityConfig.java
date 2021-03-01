package com.security.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//notation to indicate is a security configuration
@Configuration
//Use @Configuration annotation on top of any class to declare that this class provides one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.
@EnableWebSecurity
//The @EnableWebSecurity is a marker annotation. It allows Spring to find (it's a @Configuration and, therefore, @Component) and automatically apply the class to the global WebSecurity.
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    //ctrl o on top of the extended class to see all the methods it has and also override then

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppSecurityConfig(PasswordEncoder passwordEncoder) {
        //The passwordEncoder requires a constructor
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //This is where you secure things
        // visit this page for examples of stuff you can use the http for -> https://www.codota.com/code/java/classes/org.springframework.security.config.annotation.web.builders.HttpSecurity
        // if you type http. you can see tons of stuff you can use to configure your security
        http
                .csrf().disable()// Spring boot security module enables CSRF by default.
                .authorizeRequests() //Authorize request
                .antMatchers("/", "index", "/css/*", "/js/*") // see readme for the rules of antmatchess
                .permitAll() ////everything that's inside the antMatcher can be access by anybody without being logged in
                .antMatchers("/api/**") //Everything that's inside this ant matcher
                //Role base Authentication
                .hasRole(UserRole.STUDENT.name()) //can be access by people with the role Student
                //Permission based Authentication
                //Only those who have the Write permissions can DELETE, POST and PUT
                //But anybody with the role ADMIN or ADMIN_TRAINEE can do a get request
                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.name())
                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.name())
                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.name())
                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.ADMIN_TRAINEE.name())
                .anyRequest()
                .authenticated() // Any request most be authenticated
                .and()
                .httpBasic(); // the mechanism that we want to enforce for authenticity of a client "basic auth" default is form
    }

    @Override
    @Bean //Spring @Bean annotation indicates that a method produces a bean to be managed by the Spring container
    protected UserDetailsService userDetailsService() {
        //UserDetailsService is an interface which is used to retrieve the user’s authentication and authorization information
        //This is how we retrieve our users from our database
        //UserDetails Implementations are not used directly by Spring Security for security purposes.
        // They simply store user information which is later encapsulated into Authentication objects. This allows non-security related user information (such as email addresses, telephone numbers etc) to be stored in a convenient location.
        UserDetails student = User.builder()
                .username("student")
                .password(passwordEncoder.encode("password"))//To encode the password
                //.roles(UserRole.STUDENT.name()) // ROLE_STUDENT
                .authorities(UserRole.STUDENT.getGrantedAuthorities())
                //Authorities sets the role automatically
                .build();
        UserDetails admin = User.builder()
                .username("root")
                //You Should enforce stronger passwords for the users
                .password(passwordEncoder.encode("root"))
                //.roles(UserRole.ADMIN.name()) //UserRole.Admin refers to our enum UserRole
                .authorities(UserRole.ADMIN.getGrantedAuthorities())
                .build();
        UserDetails adminTrainee = User.builder()
                .username("root2")
                .password(passwordEncoder.encode("root"))
                //.roles(UserRole.ADMIN_TRAINEE.name()) // ROLE_ADMIN_TRAINEE
                .authorities(UserRole.ADMIN_TRAINEE.getGrantedAuthorities())
                .build();
        //InMemoryUserDetailsManager Non-persistent implementation of UserDetailsManager which is backed by an in-memory map.
        //Mainly intended for testing and demonstration purposes, where a full blown persistent system isn't required.
        return new InMemoryUserDetailsManager(
                student, admin, adminTrainee);
    }
}
