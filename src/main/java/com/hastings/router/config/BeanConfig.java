package com.hastings.router.config;

import com.hastings.router.azure.service.AzureStorageService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Configuration
public class BeanConfig {

    private static final Logger LOG = LoggerFactory.getLogger(BeanConfig.class);

    @Autowired
    private Environment environment;

    @Value("${azure.storage.ConnectionString}")
    private String connectionString;

    @Value("${azure.storage.container.routeConfig.name}")
    private String routeConfigContainerName;

    @Value("${azure.storage.container.user.name}")
    private String userContainerName;

    @Bean
    public CloudBlobClient cloudBlobClient() throws URISyntaxException, StorageException, InvalidKeyException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        return storageAccount.createCloudBlobClient();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean("userContainer")
    @Profile("production")
    public AzureStorageService getUSerContainer() {
        LOG.debug("Creating Azure storage: {}", userContainerName);
        return new AzureStorageService(userContainerName);
    }

    @Bean("routeConfigContainer")
    @Profile("production")
    public AzureStorageService getRouteConfigContainer() {
        LOG.debug("Creating Azure storage: {}", routeConfigContainerName);
        return new AzureStorageService(routeConfigContainerName);
    }
}