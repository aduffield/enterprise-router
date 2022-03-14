package com.hastings.router.user.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastings.router.azure.service.AzureStorageService;
import com.hastings.router.azure.service.AzureStorageServiceException;
import com.hastings.router.user.model.RouterUser;
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
public class AzureStorageUserRepository implements UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AzureStorageUserRepository.class);

    @Autowired
    @Qualifier("userContainer")
    private AzureStorageService azureStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     *
     * @param userIdentifier
     * @return
     */
    @Override
    public Optional<RouterUser> findByIdentifier(String userIdentifier) {
        try{
        String blobText = azureStorageService.getBlobText(userIdentifier);
        RouterUser routerUser;
        routerUser = deserializeRouterUser(blobText);
        return Optional.ofNullable(routerUser);
        } catch (AzureStorageServiceException asse) {
            LOG.error("An error occurred with Azure Storage", asse);
            throw new UserException("Could not retrieve user", asse);
        }
    }

    /**
     *
     * @param routerUser
     * @return
     */
    @Override
    public RouterUser save(RouterUser routerUser) {
        InputStream targetStream = new ByteArrayInputStream(routerUser.asJsonString().getBytes());
        String blobText;
        try {
            azureStorageService.upload(routerUser.getUserName(), targetStream);
            blobText = azureStorageService.getBlobText(routerUser.getUserName());
        } catch (AzureStorageServiceException asse) {
            LOG.error("An error occurred with Azure Storage", asse);
           throw new UserException("Could not create or retrieve user", asse);
        }
        return deserializeRouterUser(blobText);
    }

    /**
     *
     * @return
     */
    @Override
    public List<RouterUser> findAll() {
        List<String> routeStrings = azureStorageService.getContainerBlobs();
        List<RouterUser> routeConfigs;
        routeConfigs = routeStrings.stream().map(this::deserializeRouterUser).collect(Collectors.toList());
        return routeConfigs;
    }

    /**
     *
     */
    @Override
    public void deleteAll() {
        azureStorageService.deleteBlobs();
    }


    /**
     *
     * @param userName
     */
    @Override
    public boolean delete(String userName) {
        return azureStorageService.deleteBlob(userName);
    }


    //Deserialise the Route User
    private RouterUser deserializeRouterUser(String blobText) {
        RouterUser savedRouterUser = null;
        try {
            savedRouterUser = objectMapper.readValue(blobText, RouterUser.class);
        } catch (JsonProcessingException jpe) {
            LOG.error("An error occurred deserializing the blob text", jpe);
            throw new UserException("An error occurred deserializing the blob text", jpe);
        }
        LOG.debug("getSavedRouterUser: {}", savedRouterUser);
        return savedRouterUser;
    }
}
