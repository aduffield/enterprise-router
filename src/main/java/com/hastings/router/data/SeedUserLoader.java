package com.hastings.router.data;


import com.hastings.router.user.model.RouterUser;
import com.hastings.router.user.model.UserRole;
import com.hastings.router.user.model.UserStatus;
import com.hastings.router.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeedUserLoader implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SeedUserLoader.class);

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String userName;

    @Value("${spring.security.user.password}")
    private String password;

    @Value("${spring.security.user.email}")
    private String email;


    @Override
    public void run(String... strings) {

        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(UserRole.ADMIN);
        RouterUser routerUser = new RouterUser(email, "Chuck", "Norris", userRoles);
        routerUser.setPassword(encoder.encode(password));
        routerUser.setUserStatus(UserStatus.ACTIVE);
        routerUser.setOrganisationName("Hastings");

        LOG.debug("Created seed routerUser: {}", routerUser);

        userRepository.save(routerUser);
    }

}
