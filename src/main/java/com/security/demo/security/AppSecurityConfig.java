package com.security.demo.security;

import com.security.demo.auth.ApplicationUserService;
import com.security.demo.jwt.JwtConfig;
import com.security.demo.jwt.JwtTokenVerifier;
import com.security.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

//notation to indicate is a security configuration
@Configuration
//Use @Configuration annotation on top of any class to declare that this class provides one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.
@EnableWebSecurity
//The @EnableWebSecurity is a marker annotation. It allows Spring to find (it's a @Configuration and, therefore, @Component) and automatically apply the class to the global WebSecurity.
@EnableGlobalMethodSecurity(prePostEnabled = true)
//EnableGlobalMethodSecurity provides AOP security on methods. Some of the annotations that it provides are PreAuthorize, PostAuthorize. It also has support for JSR-250.
//Without enabling the annotations mean nothing
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    //ctrl o on top of the extended class to see all the methods it has and also override then

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public AppSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, SecretKey secretKey, JwtConfig jwtConfig) {
        //The passwordEncoder requires a constructor
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //This is where you secure things
        // visit this page for examples of stuff you can use the http for -> https://www.codota.com/code/java/classes/org.springframework.security.config.annotation.web.builders.HttpSecurity
        // if you type http. you can see tons of stuff you can use to configure your security
        http
                .csrf().disable() // Spring boot security module enables CSRF by default.
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //We set the token repository {you need a csrf token to be able to do request other than get}
                //HttpOnlyFalse it means that the cookie will be unable to clients
                //.and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests() //Authorize request
                    .antMatchers("/", "index", "/css/*", "/js/*") // see readme for the rules of antmatchess
                    .permitAll() ////everything that's inside the antMatcher can be access by anybody without being logged in
                    .antMatchers("/api/**") //Everything that's inside this ant matcher
                    //Role base Authentication
                    .hasRole(UserRole.STUDENT.name()) //can be access by people with the role Student
                    //Permission based Authentication
                    //Only those who have the Write permissions can DELETE, POST and PUT
                    //But anybody with the role ADMIN or ADMIN_TRAINEE can do a get request
                    //The order you define this matchers matter it checks the permissions line by line
                    //.antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
                    //.antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
                    //.antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
                    //.antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.ADMIN_TRAINEE.name())
                    //We replaced the antMatchers with @PreAuthorize methods
                    .anyRequest()
                    .authenticated(); // Any request most be authenticated
                /*.and()
                 //   .httpBasic() // the mechanism that we want to enforce for authenticity of a client "basic auth" default is form
                //    .formLogin() // form based auth
                //        .loginPage("/login") //Custom login page
                //        .permitAll() // if you don't put this spring security will block the login
                //        .defaultSuccessUrl("/courses", true) // Redirect after success login
                        //.passwordParameter("password") //if you want to change the name of the inputs in the form for some reason here's where
                        //.usernameParameter("username") //if you want to change the name of the inputs in the form for some reason here's where
                .and() //By default the session id expires after 30 minutes of inactivity
                    .rememberMe() //thi sets the default to 2 weeks
                        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))//we change the default to 21 days
                        .key("somethingverysecured")
                        //.rememberMeParameter("remember-me") //if you want to change the name of the inputs in the form for some reason here's where
                .and()
                    .logout()
                        .logoutUrl("/logout") //Override the default log out
                        //If we have the csfr enable logout MOST be a post request, if we don't it can be any type of request
                        //Its best practice for the logout to always have a request type to protect from csfr attacks
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me") //The name of the response cookies
                        .logoutSuccessUrl("/login"); // Redirect after success logout
                */
    }

    @Override
    //AuthenticationManagerBuilder builds AuthenticationManager using which in-memory, JDBC and LDAP authentication is performed. To perform in-memory authentication AuthenticationManagerBuilder provides inMemoryAuthentication() method which returns InMemoryUserDetailsManagerConfigurer using which we can add user with the method withUser.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()); //Were we set which dao provider we use
    }

    @Bean
    //DaoAuthenticationProvider = An AuthenticationProvider implementation that retrieves user details from an UserDetailsService.
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder); // this allows passwords to be decoded
        provider.setUserDetailsService(applicationUserService); //Were we set which userService we will use implements UserDetailsService
        return provider;
    }

    /*
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
        return new InMemoryUserDetailsManager(student, admin, adminTrainee);
    }*/


}
