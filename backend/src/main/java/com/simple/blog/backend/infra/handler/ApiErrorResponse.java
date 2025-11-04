package com.simple.blog.backend.infra.handler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record ApiErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors) {


    public static ApiErrorResponse of(int status, String error, String message, String path){

        ZonedDateTime zonedDateTime = Instant.now().atZone(ZoneId.of("America/Sao_Paulo"));
        String formattedDate = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return new ApiErrorResponse(
                formattedDate,
                status,
                error,
                message,
                path,
                null
        );
    }

    public static ApiErrorResponse of(int status, String error, String message, String path, List<FieldError> fieldErrors){

        ZonedDateTime zonedDateTime = Instant.now().atZone(ZoneId.of("America/Sao_Paulo"));
        String formattedDate = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return new ApiErrorResponse(
                formattedDate,
                status,
                error,
                message,
                path,
                fieldErrors
        );
    }

    public record FieldError(String field, String message){}
}
