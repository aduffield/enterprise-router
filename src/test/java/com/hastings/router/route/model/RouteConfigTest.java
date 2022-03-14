package com.hastings.router.route.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteConfigTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    public void whenConvertPostEntityToPostDto_thenCorrect() {
        RouteConfig post = new RouteConfig();
        post.setId("123");
        post.setPattern("#####456");
        post.setStatus(2);
        post.setAppPath("appPath");
        post.setArFlag("arrFlag");


        RouteConfigResponse routeConfigResponse = modelMapper.map(post, RouteConfigResponse.class);
        assertEquals(post.getId(), routeConfigResponse.getId());
        assertEquals(post.getStatus(), routeConfigResponse.getStatus());
        assertEquals(post.getAppPath(), routeConfigResponse.getAppPath());
    }

    @Test
    public void whenConvertPostDtoToPostEntity_thenCorrect() {
        RouteConfigRequest routeConfigRequest = new RouteConfigRequest();
        routeConfigRequest.setId("121");
        routeConfigRequest.setPattern("###00000");
        routeConfigRequest.setAppPath("appPath");

        RouteConfig routeConfig = modelMapper.map(routeConfigRequest, RouteConfig.class);
        assertEquals(routeConfigRequest.getId(), routeConfig.getId());
        assertEquals(routeConfigRequest.getAppPath(), routeConfig.getAppPath());
        assertEquals(routeConfigRequest.getBrand(), routeConfig.getBrand());
    }
}