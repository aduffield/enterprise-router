package com.hastings.router.user.repository;

import com.hastings.router.AbstractIntegrationIT;
import com.hastings.router.EnterpriseRouterApplication;
import com.hastings.router.user.model.RouterUser;
import com.hastings.router.user.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {EnterpriseRouterApplication.class, AbstractIntegrationIT.Initializer.class}, webEnvironment = RANDOM_PORT)
public class UserRepositoryIT extends AbstractIntegrationIT {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryIT.class);

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    protected TableCreator tableCreator;

    @BeforeEach
    public void before() {
        LOG.debug("Removing all users");
        userRepository.deleteAll();
        List<RouterUser> showUsers = userRepository.findAll();
        LOG.debug("Show Users {}", showUsers);
    }

    @Test
    public void createUserSuccessfullyCreated() {

        RouterUser routerUser;

        List<UserRole> role = new ArrayList<>();
        role.add(UserRole.STANDARD_USER);

        routerUser = new RouterUser("hello@world.com", "firstName", "lastName", role);
        routerUser.setOrganisationId(1234L);
        routerUser.setOrganisationName("TUI");
        routerUser.setPassword("hello");

        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.STANDARD_USER);

        routerUser.setRoles(roles);

        LOG.debug("Saving RouterUser {}", routerUser);
        userRepository.save(routerUser);
        LOG.debug("Saved RouterUser {}", routerUser);
        List<RouterUser> result = userRepository.findAll();

        assertEquals(result.size(), 1);
    }


    //    @Test
    public void deleteUserSuccessfullyDeleted() {

        List<UserRole> role = new ArrayList<>();
        role.add(UserRole.STANDARD_USER);

        RouterUser firstRouterUser = new RouterUser("hello@world.com", "Harry", "Green", role);
        firstRouterUser.setOrganisationId(1234L);
        firstRouterUser.setOrganisationName("TUI");
        firstRouterUser.setPassword("hello");
        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.STANDARD_USER);
        firstRouterUser.setRoles(roles);


        List<UserRole> role2 = new ArrayList<>();
        role2.add(UserRole.STANDARD_USER);
        RouterUser secondRouterUser = new RouterUser("hello@world.com", "Carl", "deWiele", role2);
        secondRouterUser.setOrganisationId(1234L);
        secondRouterUser.setOrganisationName("TUI");
        secondRouterUser.setPassword("hello");
        secondRouterUser.setRoles(roles);

        LOG.debug("Saving Users");
        userRepository.save(firstRouterUser);
        userRepository.save(secondRouterUser);
        LOG.debug("Saved RouterUser");
        List<RouterUser> result = userRepository.findAll();
        assertEquals(result.size(), 2);
        userRepository.delete(firstRouterUser.getUserName());

        result = userRepository.findAll();
        assertEquals(result.size(), 1);

        Optional<RouterUser> notFoundUser = userRepository.findByIdentifier(firstRouterUser.getUserName());
        Optional<RouterUser> foundUser = userRepository.findByIdentifier(secondRouterUser.getUserName());

//        assertFalse(" user no longer present", notFoundUser.isPresent());
        assertEquals(foundUser.get().getUserName(), "CarldeW");
    }

    //    @Test
    public void userSuccessUpdated() {

        RouterUser routerUser;

        List<UserRole> role = new ArrayList<>();
        role.add(UserRole.STANDARD_USER);
        routerUser = new RouterUser("hello@world.com", "Andy", "Duffield", role);
        routerUser.setOrganisationId(1234L);
        routerUser.setOrganisationName("TUI");
        routerUser.setPassword("hello");

        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.STANDARD_USER);

        routerUser.setRoles(roles);

        LOG.debug("Saving RouterUser");
        userRepository.save(routerUser);
        LOG.debug("Saved RouterUser");

        RouterUser result = userRepository.findByIdentifier("ad").get();
        assertEquals(result.getUserName(), "ad");

        routerUser.setFirstName("Julie");
        userRepository.save(routerUser);
        result = userRepository.findByIdentifier("ad").get();
        assertEquals(result.getFirstName(), "Julie");

    }
}