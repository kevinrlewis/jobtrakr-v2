package com.kevinrlewis.domain;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {

    private HttpStatus status;
    private int statusCode;
    private String response;

    public Response(HttpStatus status, int statusCode, String response) {
        this.status = status;
        this.statusCode = statusCode;
        this.response = response;
    }
}