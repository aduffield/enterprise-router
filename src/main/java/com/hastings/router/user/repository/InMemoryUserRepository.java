package com.hastings.router.user.repository;

import com.hastings.router.user.model.RouterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Profile("dev")
public class InMemoryUserRepository implements UserRepository {

    private final static Logger LOG = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<String, RouterUser> users = new HashMap<>();

    /**
     * @param userIdentifier
     * @return
     */
    @Override
    public Optional<RouterUser> findByIdentifier(String userIdentifier) {
        Optional<RouterUser> optionalRouterUser = Optional.ofNullable(users.get(userIdentifier));
        if(!optionalRouterUser.isPresent()){
            throw new UserException("Could not retrieve user");
        }
        return optionalRouterUser;
    }

    /**
     * @param routerUser
     * @return
     */
    @Override
    public RouterUser save(RouterUser routerUser) {
        users.put(routerUser.getUserName(), routerUser);
        LOG.debug("SAVED {}", users);
        RouterUser newRouterUser = users.get(routerUser.getUserName());
        return new RouterUser(newRouterUser.getUserName(), newRouterUser.getFirstName(), newRouterUser.getLastName(),
                newRouterUser.getRoles());
    }

    @Override
    public List<RouterUser> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public boolean delete(String userName) {
        LOG.debug("Deleting routerUser {}", userName);
        if (users.containsKey(userName)) {
            LOG.debug("found routerUser, so deleting");
            users.remove(userName);
        } else {
            LOG.debug("Did not find routerUser");
        }
        return true;

    }
}
