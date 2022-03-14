package com.hastings.router.user.repository;

import com.hastings.router.user.model.RouterUser;
import com.hastings.router.user.model.UserStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryUserRepositoryTest {

    //Class under test
    private InMemoryUserRepository inMemoryUserRepository;

    @BeforeEach
    public void setUp() throws Exception {
        inMemoryUserRepository = new InMemoryUserRepository();
    }

    @AfterEach
    public void tearDown() throws Exception {
        inMemoryUserRepository.deleteAll();
        List<RouterUser> actuals = inMemoryUserRepository.findAll();
        assertEquals(0, actuals.size());
    }

    @Test
    public void save() {
        inMemoryUserRepository.save(createUser("user1@hello.com"));
        inMemoryUserRepository.save(createUser("user2@hello.com"));
        inMemoryUserRepository.save(createUser("user3@hello.com"));
        List<RouterUser> actuals = inMemoryUserRepository.findAll();
        assertEquals(3, actuals.size());
    }

    @Test
    public void findAll() {
        List<RouterUser> actuals = inMemoryUserRepository.findAll();
        assertEquals(0,actuals.size());
        inMemoryUserRepository.save(createUser("user1@hello.com"));
        inMemoryUserRepository.save(createUser("user2@hello.com"));

        List<RouterUser> actuals2 = inMemoryUserRepository.findAll();
        assertEquals(2, actuals2.size());
    }

    @Test
    public void delete() {
        inMemoryUserRepository.save(createUser("user1@hello.com"));
        inMemoryUserRepository.save(createUser("user2@hello.com"));
        List<RouterUser> actuals = inMemoryUserRepository.findAll();
        assertEquals(2, actuals.size());

        inMemoryUserRepository.delete("user2@hello.com");
        List<RouterUser> actuals2 = inMemoryUserRepository.findAll();
        assertEquals(1, actuals2.size());
        assertEquals("user1@hello.com", actuals2.get(0).getUserName());
    }

    private RouterUser createUser(String userName) {
        RouterUser routerUser = new RouterUser();
        routerUser.setUserStatus(UserStatus.PENDING);
        routerUser.setToken("6789");
        routerUser.setUserName(userName);
        return  routerUser;
    }
}