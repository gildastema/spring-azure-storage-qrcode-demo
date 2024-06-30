package com.gildastema.demoqrserver.controllers.Responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CodeResponse {
    private String codePath;
    private Integer id;
    private String content;
    private String codeLink;
}
