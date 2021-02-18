package com.sh.maleki.mankalah.web.exceptionHandler;

import com.sh.maleki.mankalah.exceptions.BusinessException;
import com.sh.maleki.mankalah.web.model.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(BusinessException e){

        log.error(e.getMessage() , e);

        ExceptionDto exceptionDto =  ExceptionDto.builder()
                .message(e.getMessage() + " " + e.getExceptionCode())
                .status(HttpStatus.BAD_REQUEST)
                .timeStamp(OffsetDateTime.now())
                .extraInfo(e.getExtraInfo())
                .build();

        return new ResponseEntity(exceptionDto,exceptionDto.getStatus());
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e){

        UUID logErrorId = UUID.randomUUID();
        log.error(logErrorId.toString(),e);

        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message("System Error! " +
                        "In order to track the error use the following code: " + logErrorId.toString())
                .timeStamp(OffsetDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        return new ResponseEntity(exceptionDto,exceptionDto.getStatus());
    }
}
