package com.hastings.router.user.service;

import com.hastings.router.data.FakerService;
import com.hastings.router.user.model.RouterUser;
import com.hastings.router.user.repository.UserRepository;
import com.hastings.router.user.repository.UserRepositoryIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FakerService fakerService;

    //Class under test
    @InjectMocks
    private UserService userService = new UserService();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUser() {
        RouterUser routerUser = new RouterUser();
        routerUser.setFirstName("Andy");
        routerUser.setLastName("Smith");
        routerUser.setUserName("classClown");

        when(fakerService.generate()).thenReturn("fakepwd");
        when(userRepository.save(routerUser)).thenReturn(routerUser);
        when(encoder.encode("fakepwd")).thenReturn("encPwd");

        RouterUser actual = userService.createUser(routerUser);
        assertEquals(routerUser,actual);
    }

    @Test
    void findUsers() {
        RouterUser routerUser = new RouterUser();
        routerUser.setFirstName("Andy");
        routerUser.setLastName("Smith");
        routerUser.setUserName("classClown");
        RouterUser routerUser2 = new RouterUser();
        routerUser2.setFirstName("Andy");
        routerUser2.setLastName("Smith");
        routerUser2.setUserName("classClown");
        List<RouterUser> expected = new ArrayList<>();
        expected.add(routerUser);
        expected.add(routerUser2);

        when(userRepository.findAll()).thenReturn(expected);

        List<RouterUser> actual = userService.findUsers();
        LOG.debug("Actual {}", actual);
    }

    @Test
    void findUser() {
    }

    @Test
    void approveUser() {
    }

    @Test
    void updatePassword() {
    }
}