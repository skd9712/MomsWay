package com.momsway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({AccessDeniedException.class})
    public String handlerAccessDeniedException(Exception e) {
        return "error/403";
    }

    @ExceptionHandler({NoResourceFoundException.class
            , MethodArgumentTypeMismatchException.class
    , HttpRequestMethodNotSupportedException.class})
    public String handlerNotFoundException(Exception e) {
        return "error/404";
    }

    @ExceptionHandler(CustomException.class)
    public String handelCustomException(CustomException e){
        log.info("handelCustomException...{}",e);
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handlerInternalServerError(Exception e) {
        log.error("handlerInternalServerError...{}",e);
        return "error/500";
    }

}



