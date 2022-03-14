package com.hastings.router.route.service;

import com.hastings.router.route.model.RouteConfig;
import com.hastings.router.route.repository.RouteConfigRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RouteConfigServiceImplTest {

    //Class under test
    private RouteConfigServiceImpl routeConfigService;

    @Mock
    private RouteConfigRepository routeConfigRepository;

    @BeforeEach
    public void setUp() throws Exception {
        routeConfigService = new RouteConfigServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void saveRouteConfig() {
        when(routeConfigRepository.save(any())).thenReturn(new RouteConfig());

    }

    @Test
    public void getRouteConfig() {

    }

    @Test
    public void calculateAppRoute() {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setPattern("0000000000##");
        routeConfig.setStatus(1);
        routeConfigService.calculateAppRoute("4274578868686867575", routeConfig);
    }

    @Test
    public void extractIndexFromPattern() {
        List<Integer> expected = new ArrayList<>();
        expected.add(3);
        expected.add(7);
        List<Integer> actual = routeConfigService.getIndexFromPattern("000#000#");
        assertEquals(expected, actual);
    }

    @Test
    public void extractIntegersToAdd() {
        List<Integer> indices = new ArrayList<>();
        indices.add(3);
        indices.add(5);

        int sum = 15;

        int actual = routeConfigService.getIntegerSum("76491643392", indices);
        assertEquals(sum, actual);
    }
}