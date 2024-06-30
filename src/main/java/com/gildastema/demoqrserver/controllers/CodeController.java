package com.gildastema.demoqrserver.controllers;

import com.gildastema.demoqrserver.controllers.Responses.CodeResponse;
import com.gildastema.demoqrserver.controllers.requests.CodeRequest;
import com.gildastema.demoqrserver.entities.Code;
import com.gildastema.demoqrserver.services.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/codes")
@RequiredArgsConstructor
@CrossOrigin
public class CodeController {

    private final CodeService codeService;
    @PostMapping
    public String createCode(@RequestBody CodeRequest codeRequest) {
        return codeService.createCode(codeRequest.getContent());
    }

    @GetMapping
    public Page<CodeResponse> getCodes(Pageable pageable) {
        return codeService.getCodes(pageable).map(code -> {
            var codeResponse = new CodeResponse();
            codeResponse.setId(code.getId());
            codeResponse.setContent(code.getContent());
            codeResponse.setCodePath(code.getCodePath());
            return codeResponse;
        });
    }

    @GetMapping("/{id}")
    public CodeResponse getCode(@PathVariable Integer id) {
        Code code = codeService.getCodeById(id);
        var url = codeService.getCodeUrl(code);
        var codeResponse = new CodeResponse();
        codeResponse.setId(code.getId());
        codeResponse.setCodeLink(url);
        codeResponse.setContent(code.getContent());
        codeResponse.setCodePath(code.getCodePath());
        return codeResponse;
    }

    @GetMapping ("download/{codeId}")
    public ResponseEntity<Resource> downloadCode(@PathVariable Integer codeId) {
        var resource =  codeService.downloadCode(codeId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
