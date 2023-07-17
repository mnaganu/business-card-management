package com.example.mnaganu.bcm.application.model.request;

import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@lombok.Data
public class BusinessCardInsertRequest {
  private String companyName;
  private String depaertmentName;
  private String post;
  private String name;
  private String postalCode;
  private String address;
  private String phoneNumber;
  private String fax;
  private String note;

  public static Optional<BusinessCardModel> convertToBusinessCardModel(BusinessCardInsertRequest request) {
    if (ObjectUtils.isEmpty(request)) {
      return Optional.empty();
    }
    BusinessCardModel model = BusinessCardModel.builder()
        .id(-1)
        .companyName(request.getCompanyName())
        .depaertmentName(request.getDepaertmentName())
        .post(request.getPost())
        .name(request.getName())
        .postalCode(request.getPostalCode())
        .address(request.getAddress())
        .phoneNumber(request.getPhoneNumber())
        .fax(request.getFax())
        .note(request.getNote())
        .build();

    return Optional.of(model);
  }
}
