package com.security.demo.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//guarantees to be just executed once per request"
//When a request is dispatched using a request dispatcher, it has to go through the filter chain again (or possibly a different one) before it gets to the servlet that is going to deal with it. The problem is that some of the security filter actions should only be performed once for a request. Hence the need for this filter.
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());
        if(Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            //When this is true the request will be rejected
            return;
        }
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            var authorities = (List<Map<String, String>>) body.get("authorities");

            //We are turning the authorities map into a list of granted authorities
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream().map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username, null, simpleGrantedAuthorities);
            //Associates a given SecurityContext with the current execution thread.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //The client that sent the token is now Authenticated
        }catch (JwtException e){
            //The token may been modified or expired
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        //We past the request down the chain
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
