package com.example.demo_jwt.security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
     private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class); //to create a log
     
     //--------------------------------[start : Token Generation]---------------------------------------------------------
     //obtain jwt.secret and jwt.jwtExpiration from application.properties
    @Value("${demojwtapp.jwt.secret}") 
     private String jwtSecret;

     @Value("${demojwtapp.jwt.jwtExpiration}")
     private int jwtExpirationMs;

     public String generateJwtToken(Authentication authentication){ //from security core
          UserDetails userPrincipal = (UserDetails) authentication.getPrincipal(); //UserDetails object is in security core

          return (Jwts.builder()
            .setSubject((userPrincipal.getUsername()))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact() //build token as a string using compact method
          );
     }

     public Key key(){
          return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)); //cyptography -> BASE64
     }
     //--------------------------------[end : Token Generation]---------------------------------------------------------


     //--------------------------------[start : Token validation]-------------------------------------------------------
     public boolean validateJwtToken(String authToken) {
          try {
              Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
              return true;
          } catch (MalformedJwtException e) {
              logger.error("Invalid JWT Token: {}", e.getMessage());
          } catch (ExpiredJwtException e) {
              logger.error("JWT Token is Expired : {}", e.getMessage());
          } catch (UnsupportedJwtException e) {
              logger.error("Unsupported JWT : {}", e.getMessage());
          } catch (IllegalArgumentException e) {
              logger.error("JWT payload is empty : {}", e.getMessage());
          }
          return false;
     }
     //--------------------------------[end : Token validation]-------------------------------------------------------


     //--------------------------------[start : Extract username from Token]-----------------------------------------
     public String getUsernameFromJwtToken(String authToken) {
          return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody().getSubject();
     }
     //--------------------------------[end : Extract username from Token]-----------------------------------------     
}
