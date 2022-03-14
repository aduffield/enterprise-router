package com.hastings.router.route.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastings.router.azure.service.AzureStorageService;
import com.hastings.router.azure.service.AzureStorageServiceException;
import com.hastings.router.route.model.RouteConfig;
import com.hastings.router.user.repository.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Profile("production")
public class AzureStorageRouteConfigRepository implements RouteConfigRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AzureStorageRouteConfigRepository.class);

    @Autowired
    @Qualifier("routeConfigContainer")
    private AzureStorageService azureStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public RouteConfig save(RouteConfig routeConfig) {
        RouteConfig rc;
        try {
            InputStream targetStream = new ByteArrayInputStream(routeConfig.asJsonString().getBytes());
            azureStorageService.upload(routeConfig.getId(), targetStream);
            String blobText = azureStorageService.getBlobText(routeConfig.getId());
            LOG.debug("Blob text: {}", blobText);
            rc = deserializeRouterUser(blobText);
        } catch (AzureStorageServiceException asse) {
            LOG.error("An error occurred with Azure Storage", asse);
            throw new UserException("Could not create or retrieve Route Config", asse);
        }
        return rc;
    }


    @Override
    public Optional<RouteConfig> getRouteConfig(String id) {
        String bbb = azureStorageService.getBlobText(id);
        RouteConfig rc;
        rc = deserializeRouterUser(bbb);
        return Optional.ofNullable(rc);
    }

    @Override
    public List<RouteConfig> getAllRouteConfigs() {
        List<String> routeStrings = azureStorageService.getContainerBlobs();
        List<RouteConfig> routeConfigs;
        routeConfigs = routeStrings.stream().map(this::deserializeRouterUser).collect(Collectors.toList());
        return routeConfigs;
    }


    //Deserialise the Route User
    private RouteConfig deserializeRouterUser(String blobText) {
        RouteConfig savedRouterUser = null;
        try {
            savedRouterUser = objectMapper.readValue(blobText, RouteConfig.class);
        } catch (JsonProcessingException jpe) {
            LOG.error("An error occurred deserializing the blob text", jpe);
            throw new UserException("An error occurred deserializing the blob text", jpe);
        }
        LOG.debug("getSavedRouterUser: {}", savedRouterUser);
        return savedRouterUser;
    }

}
