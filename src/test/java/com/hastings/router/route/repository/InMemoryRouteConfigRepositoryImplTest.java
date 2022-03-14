package com.hastings.router.route.repository;

import com.hastings.router.route.model.RouteConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class InMemoryRouteConfigRepositoryImplTest {

    //Class under test
    private InMemoryRouteConfigRepositoryImpl inMemoryRouteConfigRepository;

    @BeforeEach
    public void setUp() throws Exception {
        inMemoryRouteConfigRepository = new InMemoryRouteConfigRepositoryImpl();
    }

    @AfterEach
    public void tearDown() throws Exception {
        inMemoryRouteConfigRepository = new InMemoryRouteConfigRepositoryImpl();
    }

    @Test
    public void save() {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setId("12345");
        routeConfig.setPattern("000000000##");
        Object actual = inMemoryRouteConfigRepository.save(routeConfig);
    }

    @Test
    public void getRouteConfig() {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setId("98765");
        routeConfig.setPattern("000000000##");
        inMemoryRouteConfigRepository.save(routeConfig);
        List<RouteConfig> actual = inMemoryRouteConfigRepository.getAllRouteConfigs();
        assertEquals(1, actual.size());

    }
}