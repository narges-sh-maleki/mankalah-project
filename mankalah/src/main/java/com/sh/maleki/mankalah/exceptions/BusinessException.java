package com.sh.maleki.mankalah.exceptions;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public class BusinessException extends RuntimeException {

    private final ExceptionCodes exceptionCode;
    private List<String> extraInfo;


    public BusinessException(ExceptionCodes exceptionCode) {

        super(exceptionCode.getExceptionMessage());
        this.exceptionCode = exceptionCode;
    }

    public BusinessException(ExceptionCodes exceptionCode,String extraInfo) {

        super(exceptionCode.getExceptionMessage());
        this.exceptionCode = exceptionCode;
        this.extraInfo = Arrays.asList(extraInfo);
    }

    public BusinessException(ExceptionCodes exceptionCode,List<String> extraInfo) {

        super(exceptionCode.getExceptionMessage());
        this.exceptionCode = exceptionCode;
        this.extraInfo = extraInfo;
    }

    public BusinessException(ExceptionCodes exceptionCode, Throwable cause) {

        super(exceptionCode.getExceptionMessage(), cause);
        this.exceptionCode = exceptionCode;
    }
    public BusinessException(ExceptionCodes exceptionCode,String extraInfo, Throwable cause) {

        super(exceptionCode.getExceptionMessage(), cause);
        this.exceptionCode = exceptionCode;
        this.extraInfo =    Arrays.asList(extraInfo);

    }

    public BusinessException(ExceptionCodes exceptionCode,List<String> extraInfo, Throwable cause) {

        super(exceptionCode.getExceptionMessage(), cause);
        this.exceptionCode = exceptionCode;
        this.extraInfo= extraInfo;

    }
}
