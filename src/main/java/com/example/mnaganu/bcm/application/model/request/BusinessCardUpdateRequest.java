package com.example.mnaganu.bcm.application.model.request;

import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@lombok.Data
public class BusinessCardUpdateRequest {
  private Integer id;
  private String companyName;
  private String depaertmentName;
  private String post;
  private String name;
  private String postalCode;
  private String address;
  private String phoneNumber;
  private String fax;
  private String note;

  public static Optional<BusinessCardModel> convertToBusinessCardModel(BusinessCardUpdateRequest request) {
    if (ObjectUtils.isEmpty(request)) {
      return Optional.empty();
    }
    BusinessCardModel model = BusinessCardModel.builder()
        .id(request.getId())
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
