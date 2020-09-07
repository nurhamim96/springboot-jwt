package com.learn.spring.springbootauthenticationwithjwt.security;

import com.learn.spring.springbootauthenticationwithjwt.entity.User;
import com.learn.spring.springbootauthenticationwithjwt.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String JWT_HEADER = "Authorization";
    private static final String JWT_TOKEN_PREFIX = "Bearer";

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJwt(jwt);
                User user = customUserDetailsService.loadUserById(userId);

                Object principal;
                Object credentials;
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWT_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JWT_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
