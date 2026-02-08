package com.example.userauthservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailTemplateDto {
    private String to;
    private String from;
    private String subject;
    private String body;
}
