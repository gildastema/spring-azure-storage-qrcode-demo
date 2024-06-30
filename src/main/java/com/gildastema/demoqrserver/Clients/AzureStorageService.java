package com.gildastema.demoqrserver.Clients;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.SasProtocol;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.azure.spring.cloud.core.resource.AzureStorageBlobProtocolResolver;


@Service
public class AzureStorageService implements StorageService{
    static final String BLOB_RESOURCE_PATTERN = "azure-blob://%s/%s";
    private  BlobServiceClient blobServiceClient;
    @Value("${spring.cloud.azure.storage.connection-string}")
    private String connectionStr;

    @Qualifier("azureStorageBlobProtocolResolver")
    private final ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionStr).buildClient();

    }

    public AzureStorageService( ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public String store(String content, String fileName) throws IOException {
                String blobUrl = String.format("azure-blob://%s/%s", "dev", fileName);
        Resource resource = resourceLoader.getResource(blobUrl);
        if (resource instanceof WritableResource) {
            try (OutputStream outputStream = ((WritableResource) resource).getOutputStream()) {
                outputStream.write(content.getBytes(StandardCharsets.UTF_8));
            }
        }
        return blobUrl;
    }

    /**
     *  download from azure storage
     * @param codePath
     * @return
     */
    @Override
    public byte[] load(String codePath) {
        Resource resource = resourceLoader.getResource(codePath);
        try {
            return resource.getInputStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public Resource getResource(String codePath) {
        return resourceLoader.getResource(codePath);
    }

    @Override
    public String getUrl(String disk, String fileName) throws IOException {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(disk).getBlobClient(fileName);
        BlobSasPermission permissions = new BlobSasPermission().setReadPermission(true);

        OffsetDateTime expiryTime = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1); // SAS will be valid for 1 day

        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, permissions)
                .setProtocol(SasProtocol.HTTPS_ONLY); // Allow only HTTPS

        String sasToken = blobClient.generateSas(sasSignatureValues);

        return blobClient.getBlobUrl() + "?" + sasToken;

    }
}
