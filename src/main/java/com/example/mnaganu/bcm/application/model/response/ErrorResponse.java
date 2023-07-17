package com.example.mnaganu.bcm.application.model.response;

import com.example.mnaganu.bcm.application.model.ApiErrorCode;
import com.example.mnaganu.bcm.application.model.ApiStatus;

@lombok.Data
public class ErrorResponse {
  private final ApiStatus status;
  private final ApiErrorCode code;
  private final String message;
}
