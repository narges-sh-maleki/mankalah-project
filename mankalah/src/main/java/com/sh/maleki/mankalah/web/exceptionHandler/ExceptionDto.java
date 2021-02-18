package com.sh.maleki.mankalah.web.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {

    private HttpStatus status;
    @JsonFormat(shape =   JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime timeStamp;
    private String message;
    private List<String> extraInfo;


}
