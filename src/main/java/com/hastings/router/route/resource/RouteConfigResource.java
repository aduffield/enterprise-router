package com.hastings.router.route.resource;

import com.hastings.router.error.EntityNotFoundException;
import com.hastings.router.route.model.AppRoute;
import com.hastings.router.route.model.RouteConfig;
import com.hastings.router.route.model.RouteConfigRequest;
import com.hastings.router.route.model.RouteConfigResponse;
import com.hastings.router.route.service.RouteConfigService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Responsible for route configuration and app-route retrieval. Could argue that these two responsibilities should be
 * split into separate classes. I might get around to doing this.
 */
@RestController
public class RouteConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(RouteConfigResource.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RouteConfigService routeConfigService;

    /**
     * Creates a route configuration.
     *
     * @param routeConfigRequest The Route Configuration request as a deserialised object
     * @return The response entity containing the route configuration response.
     */
    @RequestMapping(value = "/route-config", method = RequestMethod.POST)
    public ResponseEntity<RouteConfigResponse> createRouteConfig(@RequestBody RouteConfigRequest routeConfigRequest) {

        LOG.debug("Got routeConfig Request: {}", routeConfigRequest);
        RouteConfig routeConfig = convertToEntity(routeConfigRequest);
        LOG.debug("Converter to routeConfig: {}", routeConfig);
        RouteConfig savedRouteConfig = routeConfigService.saveRouteConfig(routeConfig);
        LOG.debug("Saved Route Config: {}", savedRouteConfig);
        RouteConfigResponse routeConfigResponse = convertToDto(savedRouteConfig);
        LOG.debug("routeConfigResponse {}", routeConfigResponse);

        return new ResponseEntity<>(routeConfigResponse, HttpStatus.CREATED);
    }

    /**
     * Gets all the configured route configurations.
     *
     * @return The list of Route Configurations
     */
    @RequestMapping(value = "/route-config", method = RequestMethod.GET)
    public ResponseEntity<List<RouteConfigResponse>> getAllRouteConfig() {
        List<RouteConfigResponse> resp = convertToDtos(routeConfigService.getRouteConfigs());

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    /**
     * Gets the app-route for the particular route configuration and policy number combination
     *
     * @param id           tbe Route Configuration id.
     * @param policyNumber the policy number
     * @return the application route based on the configured rules
     */
    @RequestMapping(value = "/app-route/{id}/{policyNumber}", method = RequestMethod.GET)
    public ResponseEntity<AppRoute> getAppRoute(@PathVariable("id") String id, @PathVariable("policyNumber") String policyNumber) throws EntityNotFoundException {

        LOG.debug("In getAppRoute()");
        Optional<RouteConfig> optionalRouteConfig = routeConfigService.getRouteConfig(id);
        RouteConfig routeConfig = optionalRouteConfig.orElseThrow(() -> new EntityNotFoundException(RouteConfig.class, "id", id));

        return ResponseEntity.ok(routeConfigService.calculateAppRoute(policyNumber, routeConfig));
    }

    /**
     * Converts the Dto request to a service entity
     *
     * @param routeConfigRequest the requeest object to convert
     * @return the entity after conversion
     */
    private RouteConfig convertToEntity(RouteConfigRequest routeConfigRequest) {
        return modelMapper.map(routeConfigRequest, RouteConfig.class);
    }

    private RouteConfigResponse convertToDto(RouteConfig routeConfig) {
        return modelMapper.map(routeConfig, RouteConfigResponse.class);
    }

    private List<RouteConfigResponse> convertToDtos(List<RouteConfig> routeConfig) {
        return modelMapper.map(routeConfig, List.class);
    }

}
