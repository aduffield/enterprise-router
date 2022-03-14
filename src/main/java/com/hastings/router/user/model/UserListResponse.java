package com.hastings.router.user.model;

import java.util.ArrayList;
import java.util.List;

public class UserListResponse extends ArrayList<RouterUser> {
    public UserListResponse(List<RouterUser> routerUsers) {
        super();

        this.addAll(routerUsers);
    }
}
