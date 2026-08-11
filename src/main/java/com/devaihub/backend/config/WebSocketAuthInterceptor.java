package com.devaihub.backend.config;

import com.devaihub.backend.jwt.JwtService;
import com.devaihub.backend.security.CustomUserDetailsService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public WebSocketAuthInterceptor(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader =
                    accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null ||
                    !authHeader.startsWith("Bearer ")) {

                System.out.println(
                        "WEBSOCKET: No Authorization header"
                );

                return message;
            }

            String token =
                    authHeader.substring(7);

            String username =
                    jwtService.extractUsername(token);

            if (username == null) {

                System.out.println(
                        "WEBSOCKET: Could not extract username"
                );

                return message;
            }

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

            if (jwtService.isTokenValid(
                    token,
                    userDetails
            )) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                accessor.setUser(authentication);

                System.out.println(
                        "===================================="
                );

                System.out.println(
                        "JWT USERNAME = " + username
                );
                System.out.println(
                        "USERDETAILS USERNAME = "
                                + userDetails.getUsername()
                );
                System.out.println(
                        "PRINCIPAL NAME = "
                                + authentication.getName()
                );
                System.out.println(
                        "ACCESSOR USER = "
                                + accessor.getUser().getName()
                );
                System.out.println(
                        "===================================="
                );

            } else {

                System.out.println(
                        "WEBSOCKET: Invalid JWT"
                );
            }
        }

        return message;
    }
}
