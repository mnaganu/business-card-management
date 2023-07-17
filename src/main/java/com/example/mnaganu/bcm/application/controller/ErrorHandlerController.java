package com.example.mnaganu.bcm.application.controller;

import com.example.mnaganu.bcm.application.exception.BusinessCardIdFormatException;
import com.example.mnaganu.bcm.application.exception.BusinessCardNameNotFoundException;
import com.example.mnaganu.bcm.application.exception.LimitFormatException;
import com.example.mnaganu.bcm.application.exception.OffsetFormatException;
import com.example.mnaganu.bcm.application.model.ApiErrorCode;
import com.example.mnaganu.bcm.application.model.ApiStatus;
import com.example.mnaganu.bcm.application.model.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

;


@RestControllerAdvice
public class ErrorHandlerController {

  Logger log = LoggerFactory.getLogger(ErrorHandlerController.class);

  @ExceptionHandler({OffsetFormatException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(OffsetFormatException e) {
    log.error(e.getMessage());
    return new ErrorResponse(ApiStatus.NG, ApiErrorCode.E1001, e.getMessage());
  }

  @ExceptionHandler({LimitFormatException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(LimitFormatException e) {
    log.error(e.getMessage());
    return new ErrorResponse(ApiStatus.NG, ApiErrorCode.E1002, e.getMessage());
  }

  @ExceptionHandler({BusinessCardIdFormatException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(BusinessCardIdFormatException e) {
    log.error(e.getMessage());
    return new ErrorResponse(ApiStatus.NG, ApiErrorCode.E1003, e.getMessage());
  }

  @ExceptionHandler({BusinessCardNameNotFoundException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(BusinessCardNameNotFoundException e) {
    log.error(e.getMessage());
    return new ErrorResponse(ApiStatus.NG, ApiErrorCode.E1004, e.getMessage());
  }

  @ExceptionHandler({Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleException(Exception e) {
    e.printStackTrace();
    log.error(e.getMessage());
    return new ErrorResponse(ApiStatus.NG, ApiErrorCode.E1005, e.getMessage());
  }

}