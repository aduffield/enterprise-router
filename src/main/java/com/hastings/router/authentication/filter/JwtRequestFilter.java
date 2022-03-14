package com.hastings.router.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastings.router.error.ApiError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Validate and checks the JWT token coming in on all requests.
 */
@Component
@Order(2)
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        LOG.debug("In doFilterInternal()");

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken;
        Claims claims = null;
        List<String> authorities = new ArrayList<>();

        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
                authorities = (List) claims.get("authorities");

            } catch (IllegalArgumentException e) {
                LOG.error("Unable to get JWT Token");
                createErrorResponse(new ApiError(UNAUTHORIZED, "An error has occurred authenticating", e), response);
            } catch (ExpiredJwtException e) {
                LOG.error("JWT Token has expired");
                createErrorResponse(new ApiError(UNAUTHORIZED, "An error has occurred authenticating", e), response);
                throw new JWTUserException("JWT Token has expired", e);
            } catch (JwtException e) {
                LOG.error("JWT Token has a problem", e);
                createErrorResponse(new ApiError(UNAUTHORIZED, "An error has occurred authenticating", e), response);
                throw new JWTUserException("JWT Token has a problem", e);
            }

        } else {
            LOG.debug("JWT Token does not begin with Bearer String");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            List<SimpleGrantedAuthority> cdc = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null, cdc);
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }
        chain.doFilter(request, response);
    }

    private void createErrorResponse(ApiError apiError, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}
