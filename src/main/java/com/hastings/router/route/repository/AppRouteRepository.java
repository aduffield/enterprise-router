package com.hastings.router.route.repository;

import com.hastings.router.route.model.AppRoute;

import java.util.List;

public interface AppRouteRepository {

    AppRoute getAppRoute(String id);

    List<AppRoute> getAppRoutes();

    void save(AppRoute appRoute);

}
