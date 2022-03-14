package com.hastings.router.route.repository;

import com.hastings.router.route.model.RouteConfig;

import java.util.List;
import java.util.Optional;


public interface RouteConfigRepository {

    RouteConfig save(RouteConfig routeConfig);

    Optional<RouteConfig> getRouteConfig(String id);

    List<RouteConfig> getAllRouteConfigs();


}
