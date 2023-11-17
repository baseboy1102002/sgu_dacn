package com.sgu.schedulerApp.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomErrorException extends RuntimeException {
    private HttpStatus httpStatus = null;
    private Object data = null;

    public CustomErrorException() {
        super();
    }
    public CustomErrorException(String message) {
        super(message);
    }
    public CustomErrorException(HttpStatus httpStatus, String message) {
        this(message);
        this.httpStatus = httpStatus;
    }
    public CustomErrorException(HttpStatus httpStatus, String message, Object data) {
        this(httpStatus, message);
        this.data = data;
    }
}
