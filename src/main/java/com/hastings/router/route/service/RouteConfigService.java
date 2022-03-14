package com.hastings.router.route.service;

import com.hastings.router.route.model.AppRoute;
import com.hastings.router.route.model.RouteConfig;

import java.util.List;
import java.util.Optional;

public interface RouteConfigService {

    RouteConfig saveRouteConfig(RouteConfig routeConfig);

    Optional<RouteConfig> getRouteConfig(String id);

    AppRoute calculateAppRoute(String policyNumber, RouteConfig routeConfig);

    List<RouteConfig> getRouteConfigs();

}
