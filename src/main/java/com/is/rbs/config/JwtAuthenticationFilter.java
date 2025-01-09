package com.is.rbs.config;

import com.is.rbs.model.ResponseAnswers.Response;
import com.is.rbs.utils.Utils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

//    private static final String secretKey = "823a5b472e8540ae900c7471f3487b263103acf310fd27951s"; // Замените на ваш секретный ключ
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        request.setAttribute("clientIp", request.getRemoteAddr());
        request.setAttribute("url", request.getRequestURI());
        request.setAttribute("method", request.getMethod());
        if (request.getAttribute("Request-Id") != null) {
            request.setAttribute("Request-Id", request.getAttribute("Request-Id"));
        } else {
            String requestId = UUID.randomUUID().toString();
            request.setAttribute("Request-Id", requestId);
        }
        request.setAttribute("clientHost", request.getRemoteHost());
        request.setAttribute("startTime", startTime);
        chain.doFilter(request, response);
    }

    public Claims extractClaims(String token) throws SignatureException {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String generateToken(String username, Authentication authentication) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 3600000); // Токен на 1 час
//        System.out.println("Sau brat");
        try{
        return Jwts.builder()
                .setSubject(username)
                .claim("role", authentication.getAuthorities().toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
        }catch (Exception e){

        return "Error";

            }
         }

    public static String generateRefreshToken(String username,Authentication authentication) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 604800000); // Токен на 7 дней
        return Jwts.builder()
                .setSubject(username)
                .claim("role", authentication.getAuthorities().toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }


}
