package com.example.mnaganu.bcm.application.model.response;

import com.example.mnaganu.bcm.application.model.BusinessCardData;

import java.util.List;

@lombok.Data
public class BusinessCardSelectResponse {
  private final int total;
  private final int offset;
  private final int limit;
  private final List<BusinessCardData> list;
}
