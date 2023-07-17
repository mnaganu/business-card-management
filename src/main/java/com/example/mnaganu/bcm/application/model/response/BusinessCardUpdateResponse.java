package com.example.mnaganu.bcm.application.model.response;

import com.example.mnaganu.bcm.application.model.ApiStatus;

@lombok.Data
public class BusinessCardUpdateResponse {
  private final ApiStatus status;
  private final String message;
}
