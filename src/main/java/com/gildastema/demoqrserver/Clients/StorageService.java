package com.gildastema.demoqrserver.Clients;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface StorageService {
    String store(String content, String fileName) throws IOException;

    byte[] load(String codePath);

    Resource getResource(String codePath);

    String getUrl(String disk, String fileName) throws IOException;
}
