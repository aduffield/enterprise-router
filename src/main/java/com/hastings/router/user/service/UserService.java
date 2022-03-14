package com.hastings.router.user.service;

import com.hastings.router.data.FakerService;
import com.hastings.router.user.model.RouterUser;
import com.hastings.router.user.model.UserStatus;
import com.hastings.router.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A service class to handle all operations on user's of the system.
 */
@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FakerService fakerService;


    /**
     *  Creates a router user with the provided Roles and returns the details of the created user with a generated password.
     *
     * @param routerUser The Router User to create
     * @return the created Router user, with generated password to be provided to the user ( and should be changed via the
     * updatePassword method
     */
    public RouterUser createUser(RouterUser routerUser) {

        String generatedPassword = fakerService.generate();
        routerUser.setUserStatus(UserStatus.ACTIVE);
        String encodedPassword = encoder.encode(generatedPassword);
        routerUser.setPassword(encodedPassword);
        LOG.debug("About to save: {}", routerUser);
        RouterUser ru =userRepository.save(routerUser);
        ru.setPassword(generatedPassword);
        ru.setUserStatus(UserStatus.ACTIVE);

        return ru;
    }


    /**
     * Returns all users in the system
     *
     * @return a list of all of the valid Router Users.
     */
    public List<RouterUser> findUsers() {
        List<RouterUser> routerUserList = new ArrayList<>();
        Iterable<RouterUser> userResultList = userRepository.findAll();
        userResultList.forEach(routerUserList::add);
        return routerUserList;
    }


    /**
     * Finds a Router User.
     *
     * @param userName the Unique username to search for a Router User
     * @return the Router User or null if a Router User is not found
     */
    public RouterUser findUser(String userName) {
        return userRepository.findByIdentifier(userName).orElse(null);
    }

    /**
     * Finds a Router User.
     *
     * @param userName the Unique username to search for a Router User
     * @return the Router User or null if a Router User is not found
     */
    public boolean deleteUser(String userName) {
        return userRepository.delete(userName);
    }

    /**
     *  Approves a Router User, i.e. sets the user status of the user to active.
     * @param userName the Unique username to search for a Router User
     * @return true if the status was updated, otherwise false.
     */
    public Boolean approveUser(String userName) {
        Optional<RouterUser> userResult = userRepository.findByIdentifier(userName);
        if (userResult.isPresent()) {
            RouterUser routerUser = userResult.get();
            routerUser.setUserStatus(UserStatus.ACTIVE);

            userRepository.save(routerUser);

            return true;
        } else {
            return false;
        }
    }

    /**
     *  Allows a Router User to update their password.
     * @param userName the username of the Router User which requires the password updating
     * @param password the password o update to
     * @return true is the update is successful, otherwise false
     */
    public Boolean updatePassword(String userName, String password) {
        Optional<RouterUser> userResult = userRepository.findByIdentifier(userName);
        if (userResult.isPresent()) {
            RouterUser routerUser = userResult.get();
            routerUser.setPassword(encoder.encode(password));
            routerUser.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(routerUser);

            return true;
        }
        return false;
    }
}
