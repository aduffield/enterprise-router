package com.hastings.router.azure.service;

import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class AzureStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(AzureStorageService.class);
    private final String azureContainerName;

    @Autowired
    private CloudBlobClient cloudBlobClient;

    public AzureStorageService(String azureContainerName) {
        this.azureContainerName = azureContainerName;
    }

    @PostConstruct
    protected boolean createContainer() {
        LOG.debug("in create");
        boolean containerCreated = false;
        CloudBlobContainer container = null;
        try {
            container = cloudBlobClient.getContainerReference(azureContainerName);
        } catch (URISyntaxException | StorageException e) {
            LOG.error(e.getMessage());
            throw new AzureStorageServiceException("Could get container reference" , e);
        }
        try {

            BlobRequestOptions blobRequestOptions = new BlobRequestOptions();
            blobRequestOptions.setTimeoutIntervalInMs(3000);
            containerCreated = container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER,
                    blobRequestOptions,
                    new OperationContext());
        } catch (StorageException e) {
            LOG.error(e.getMessage());
            throw new AzureStorageServiceException("Could not Create " + azureContainerName , e);
        }
        return containerCreated;
    }

    public URI upload(MultipartFile multipartFile) {
        URI uri = null;
        CloudBlockBlob blob;
        try {
            blob = cloudBlobClient.getContainerReference(azureContainerName).getBlockBlobReference(multipartFile.getName());
            blob.upload(multipartFile.getInputStream(), -1);
            uri = blob.getUri();
        } catch (URISyntaxException | IOException | StorageException e) {
            LOG.error(e.getMessage());
        }
        return uri;
    }

    /**
     * @param fileName
     * @param inputStream
     * @return
     */
    public URI upload(String fileName, InputStream inputStream) {
        URI uri = null;
        CloudBlockBlob blob;
        try {
            blob = cloudBlobClient.getContainerReference(azureContainerName).getBlockBlobReference(fileName);
            blob.upload(inputStream, -1);
            uri = blob.getUri();
            LOG.debug("URI {}", uri);
        } catch (URISyntaxException | StorageException | IOException e) {
            LOG.error(e.getMessage());
            throw new AzureStorageServiceException("Could not Save", e);
        }
        return uri;
    }


    /**
     * @return
     */
    public List<String> getContainerBlobs() {
        LOG.debug("Getting all blobs for {}", azureContainerName);
        Iterable<ListBlobItem> blobs;
        List<String> blobStrings = new ArrayList<>();
        try {
            blobs = cloudBlobClient.getContainerReference(azureContainerName).listBlobs();
            for (ListBlobItem blobItem : blobs) {
                blobStrings.add(((CloudBlockBlob) blobItem).downloadText());
            }
        } catch (URISyntaxException | StorageException | IOException e) {
            LOG.error(e.getMessage());
            throw new AzureStorageServiceException("Could not get data", e);
        }
        return blobStrings;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public String getBlobText(String fileName) {
        CloudBlockBlob blob;
        String json = null;
        try {
            blob = cloudBlobClient.getContainerReference(azureContainerName).getBlockBlobReference(fileName);
            json = blob.downloadText();
            LOG.debug("json {}", json);
        } catch (URISyntaxException | StorageException | IOException e) {
            LOG.error(e.getMessage());
            throw new AzureStorageServiceException("Could not get data for " + fileName, e);
        }
        return json;
    }

    public boolean deleteBlob(String blobName) {
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(azureContainerName);
            CloudBlockBlob blobToBeDeleted = container.getBlockBlobReference(blobName);
            blobToBeDeleted.deleteIfExists();
        } catch (URISyntaxException | StorageException e) {
            LOG.error(e.getMessage());
            throw new AzureStorageServiceException("Could not delete " + blobName, e);
        }
        return true;
    }

    public boolean deleteContainer() {
        boolean deleted = false;
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(azureContainerName);
            deleted = container.deleteIfExists();
            LOG.debug("Deleted container {}", azureContainerName);
        } catch (StorageException | URISyntaxException e) {
            LOG.error(e.getMessage());
        }
        return deleted;
    }

    /**
     *
     */
    public void deleteBlobs() {

        CloudBlobContainer container;
        try {
            container = cloudBlobClient.getContainerReference(azureContainerName);
            Iterable<ListBlobItem> blobs = container.listBlobs();

            LOG.debug("All Blobs: {}", blobs.iterator().next().getUri());

            blobs.forEach(e -> {
                deleteBlob(container, e);
            });

        } catch (URISyntaxException | StorageException e) {
            LOG.error(e.getMessage());
            throw new AzureStorageServiceException("Could not delete blobs", e);
        }
    }

    //Deletes a blob from the container.
    private void deleteBlob(CloudBlobContainer container, ListBlobItem e) {
        try {
            String bb = getLastPart(e.getUri());
            container.getBlockBlobReference(bb).deleteIfExists();
        } catch (URISyntaxException | StorageException ex) {
            LOG.error(ex.getMessage());
            throw new AzureStorageServiceException("Could not delete blobs", ex);
        }
    }


    //Gets the last part of a URI - this is the name of the container.
    private String getLastPart(URI uri) {
        String urw2 = uri.toString();
        return urw2.substring(urw2.lastIndexOf('/') + 1);

    }

}