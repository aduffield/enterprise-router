package com.hastings.router.user.repository;

import com.hastings.router.user.model.RouterUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<RouterUser> findByIdentifier(String userIdentifier);

    RouterUser save(RouterUser routerUser);

    List<RouterUser> findAll();

    void deleteAll();

    boolean delete(String userName);
}