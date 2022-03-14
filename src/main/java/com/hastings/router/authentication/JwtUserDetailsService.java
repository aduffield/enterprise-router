package com.hastings.router.authentication;

import com.hastings.router.authentication.filter.JwtTokenUtil;
import com.hastings.router.user.model.RouterUser;
import com.hastings.router.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Responsible for converting Users to Spring Users.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Takes a username, finds the corresponding user, and creates a Spring RouterUser.
     *
     * @param userName the username to lookup
     * @return the
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<RouterUser> userResult = userRepository.findByIdentifier(userName);
        LOG.debug("user {}", userResult.get());
        if (userResult.isPresent()) {
            return getUserDetails(userResult.get());
        } else {
            throw new UsernameNotFoundException("RouterUser not found with username: " + userName);

        }
    }

    /**
     * @param routerUser
     * @return
     */
    public User getUserDetails(RouterUser routerUser) {
        LOG.debug("in getUserDetails...{}", routerUser);
        User newUser = new User(routerUser.getUserName(), routerUser.getPassword(),
                AuthorityUtils.createAuthorityList(routerUser.getRolesAsStringArray()));
        LOG.debug("NEW USER {}", newUser);
        return newUser;
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public RouterUser findRouterUser(String username, String password) {
        LOG.debug("in findRouterUser");
        Optional<RouterUser> userResult = userRepository.findByIdentifier(username);
        LOG.debug("User found {}", userResult);
        return userResult.orElseThrow(RuntimeException::new);

    }

    public String getAuthenticationToken(String username, String password, RouterUser routerUser) {
        LOG.debug("getAuthenticationToken {},{}", username, password);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = getUserDetails(routerUser);
        return jwtTokenUtil.generateToken(userDetails);
    }

    public String generateToken(UserDetails userDetails) {
        return jwtTokenUtil.generateToken(userDetails);
    }
}
