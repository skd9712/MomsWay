package com.momsway.controller;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AccessDeniedException.class)
    public String handlerAccessDeniedException(AccessDeniedException e) {
        return "error/403";
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public String handlerNotFoundException(ChangeSetPersister.NotFoundException e) {
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handlerInternalServerError(Exception e) {
        return "error/500";
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException e) {
        return "error/600";
    }
}



