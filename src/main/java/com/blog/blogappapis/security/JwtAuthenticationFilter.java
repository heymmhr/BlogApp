package com.blog.blogappapis.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // get token

        String requestToken = request.getHeader("Authorization");

        // bearer 23424

        logger.info("Header : {}", requestToken);

        String username = null;
        String token = null;

        if (requestToken != null && requestToken.startsWith("Bearer")){

             token = requestToken.substring(7);

             try {
                 username = this.jwtTokenHelper.getUsernameFromToken(token);
             }
             catch (IllegalArgumentException e){
                 System.out.println("Unable to get Jwt token");
             }
             catch (ExpiredJwtException e){
                 System.out.println("Jwt token has expired");
             }
             catch (MalformedJwtException e){
                 System.out.println("Invalid jwt");
             }


        }else {
           logger.info("Invalid Token value ");
        }

        //once we get the token, now validate

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //fetch user detail from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtTokenHelper.validateToken(token, userDetails);
            if (validateToken) {

                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);


            } else {
                logger.info("Validation fails !!");
            }

        }else {
            System.out.println("username is null or context is not null");

        }
        filterChain.doFilter(request,response);

    }
}
