package com.momsway.controller;

import com.momsway.exception.CustomException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AccessDeniedException.class)
    public String handlerAccessDeniedException(AccessDeniedException e) {
        return "error/403";
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public String handlerNotFoundException(NoResourceFoundException e) {
        return "error/404";
    }

    @ExceptionHandler(CustomException.class)
    public String handelCustomException(CustomException e){ return "error/404";}

    @ExceptionHandler(Exception.class)
    public String handlerInternalServerError(Exception e) {
        return "error/500";
    }

}



