package com.hastings.router.route.repository;

import com.hastings.router.route.model.RouteConfig;
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
public class InMemoryRouteConfigRepositoryImpl implements RouteConfigRepository {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryRouteConfigRepositoryImpl.class);

    private final Map<String, RouteConfig> routeConfigurations = new HashMap<>();

    @Override
    public RouteConfig save(RouteConfig routeConfig) {
        LOG.debug("Saving Route Config {}", routeConfig);
        routeConfigurations.put(routeConfig.getId(), routeConfig);
        return getRouteConfig(routeConfig.getId()).get();
    }

    @Override
    public Optional<RouteConfig> getRouteConfig(String id) {
        return Optional.ofNullable(routeConfigurations.get(id));
    }

    @Override
    public List<RouteConfig> getAllRouteConfigs() {
        return new ArrayList<>(routeConfigurations.values());

    }
}
