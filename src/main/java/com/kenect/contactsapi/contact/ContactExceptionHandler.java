package com.kenect.contactsapi.contact;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ContactExceptionHandler {

  @ExceptionHandler(ContactUpstreamException.class)
  public ProblemDetail handleContactUpstreamException(ContactUpstreamException exception) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
    problemDetail.setTitle("Upstream contact service failure");
    problemDetail.setDetail(exception.getMessage());
    return problemDetail;
  }
}
