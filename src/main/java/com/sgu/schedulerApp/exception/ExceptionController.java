package com.sgu.schedulerApp.exception;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomErrorException.class)
    public String handlerCustomErrorExceptions(CustomErrorException ex, Model model) {
        String msg = ex.getMessage();
        HttpStatus httpStatus = ex.getHttpStatus();
        model.addAttribute("msg", msg);
        model.addAttribute("httpStatus", httpStatus);
        return "errorpage/error";
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public String handleMissingPathVariable(MissingPathVariableException ex, Model model) {
        String msg = "The '" + ex.getParameter() + "' request parameter is missing";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        model.addAttribute("msg", msg);
        model.addAttribute("httpStatus", httpStatus);
        return "errorpage/error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleResourceNotFound(NoHandlerFoundException  ex, Model model) {
        String msg = ex.getMessage();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        model.addAttribute("msg", msg);
        model.addAttribute("httpStatus", httpStatus);
        return "errorpage/error";
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public String defaultErrorHandler(Exception exception, Model model) throws Exception {
        if (AnnotationUtils.findAnnotation(exception.getClass() ,ResponseStatus.class) !=null)
            throw exception;
        String msg = exception.getMessage();
        model.addAttribute("msg", msg);
        model.addAttribute("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);
        return "errorpage/error";
    }
}
