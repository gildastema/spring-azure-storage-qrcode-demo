package com.gildastema.demoqrserver.services;

import com.gildastema.demoqrserver.Clients.AzureStorageService;
import com.gildastema.demoqrserver.Clients.CodeGeneratorService;
import com.gildastema.demoqrserver.Clients.StorageService;
import com.gildastema.demoqrserver.repositories.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gildastema.demoqrserver.entities.Code;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeService {

    private final CodeGeneratorService codeGeneratorService;
    private final CodeRepository codeRepository;
    private final StorageService storageService;

    public String createCode(String content) {
        var code =  codeRepository.save(new Code(content));
        String qrcodePath = null;
        try {
           var qrCodeContent = codeGeneratorService.generateQrCode(content);
            qrcodePath =  storageService.store(qrCodeContent, code.getId().toString()+".svg");
           code.setCodePath(qrcodePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrcodePath;

    }

    public Page<Code> getCodes(Pageable pageable) {
        return codeRepository.findAll(pageable);
    }

    public Resource downloadCode(Integer codeId) {
        var code = codeRepository.findById(codeId).orElseThrow();
        return storageService.getResource(code.getCodePath());
    }

    public Code getCodeById(Integer id) {
        return codeRepository.findById(id).orElseThrow();
    }

    public String getCodeUrl(Code code) {

        try {
            return storageService.getUrl("dev", code.getId()+".svg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
