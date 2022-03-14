package com.hastings.router.azure.service;

import com.hastings.router.azure.service.AzureStorageService;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
public class AzureStorageServiceDisabled {

    //Class under test
//    @InjectMocks
    private AzureStorageService azureStorageService;

//    @Mock
    private CloudBlobClient cloudBlobClient;

//    @BeforeEach
    public void setUp() throws Exception {
        azureStorageService = new AzureStorageService("tester");
        MockitoAnnotations.initMocks(this);
    }

//    @AfterEach
//    public void tearDown() throws Exception {
//        azureStorageService.deleteContainer();
//    }

//    @Test
    public void createContainer() throws Exception {

//        CloudBlobContainer container = new CloudBlobContainer(URI.create("http://www.nnn.mon/hew/mhr"));
        when(cloudBlobClient.getContainerReference(anyString())).thenReturn(any(CloudBlobContainer.class));
        boolean created = azureStorageService.createContainer();
        System.out.println("created = " + created);

        String file = "my jsons string";
        byte[] b = file.getBytes();
        MultipartFile multipartFile = new MockMultipartFile("xbname", b);
        azureStorageService.upload(multipartFile);

//        List<URI> blobs = azureStorageService.listBlobs();
//        System.out.println("blobs = " + blobs.get(0).getFragment());

    }

    @Test
    public void upload() {
    }

    @Test
    public void listBlobs() {
    }

    @Test
    public void deleteBlob() {
    }
}