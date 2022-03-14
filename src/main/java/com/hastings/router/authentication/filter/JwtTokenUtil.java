package com.hastings.router.authentication.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class to assist in JWT operations.
 */
@Component
public class JwtTokenUtil implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.token.validity.seconds}")
    private long jwtTokenValidity;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Log token parameters.
     */
    @PostConstruct
    public void initialiseJwt() {
        LOG.debug("secret is {} and validity is {}", secret, jwtTokenValidity);
    }

    /**
     * retrieve username from jwt token. NB: this method does not currently work as the token is not stored with the user.
     *
     * @param token the token to search for
     * @return The Username,
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * get the expiration time from the token.
     *
     * @param token
     * @return
     */
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * get all Claims from the token
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    /**
     * for retrieving any information from token we will need the secret key
     *
     * @param token
     * @return
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


    /**
     * generate token for user
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        List<String> authorityList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return doGenerateToken(authorityList, userDetails.getUsername());
    }


    /**
     * Validates the token based
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Generate the token.
     *
     * @param claims
     * @param subject
     * @return
     */
    private String doGenerateToken(List<String> claims, String subject) {
        return Jwts.builder().claim("authorities", claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    /**
     * check if the token has expired
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
