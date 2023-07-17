package com.example.mnaganu.bcm.application.model;

import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Optional;

@lombok.Data
public class BusinessCardData {
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
  private Timestamp createTime;
  private Timestamp updateTime;

  public static Optional<BusinessCardData> convertTo(BusinessCardModel model) {
    if (ObjectUtils.isEmpty(model)) {
      return Optional.empty();
    }
    BusinessCardData data = new BusinessCardData();
    data.setId(model.getId());
    data.setCompanyName(model.getCompanyName().orElse(null));
    data.setDepaertmentName(model.getDepaertmentName().orElse(null));
    data.setPost(model.getPost().orElse(null));
    data.setName(model.getName());
    data.setPostalCode(model.getPostalCode().orElse(null));
    data.setAddress(model.getAddress().orElse(null));
    data.setPhoneNumber(model.getPhoneNumber().orElse(null));
    data.setFax(model.getFax().orElse(null));
    data.setNote(model.getNote().orElse(null));
    data.setCreateTime(model.getCreateTime().orElse(null));
    data.setUpdateTime(model.getUpdateTime().orElse(null));

    return Optional.of(data);
  }

}
