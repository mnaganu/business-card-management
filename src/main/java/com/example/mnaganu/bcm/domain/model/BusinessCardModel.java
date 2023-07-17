package com.example.mnaganu.bcm.domain.model;

import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

@lombok.Value
@lombok.Builder
public class BusinessCardModel {
  @lombok.NonNull
  private final Integer id;
  private final String companyName;
  private final String depaertmentName;
  private final String post;
  @lombok.NonNull
  private final String name;
  private final String postalCode;
  private final String address;
  private final String phoneNumber;
  private final String fax;
  private final String note;
  private final Timestamp createTime;
  private final Timestamp updateTime;
  private final Timestamp deleteTime;

  public Optional<String> getCompanyName() {
    return Optional.ofNullable(companyName);
  }

  public Optional<String> getDepaertmentName() {
    return Optional.ofNullable(depaertmentName);
  }

  public Optional<String> getPost() {
    return Optional.ofNullable(post);
  }

  public Optional<String> getPostalCode() {
    return Optional.ofNullable(postalCode);
  }

  public Optional<String> getAddress() {
    return Optional.ofNullable(address);
  }

  public Optional<String> getPhoneNumber() {
    return Optional.ofNullable(phoneNumber);
  }

  public Optional<String> getFax() {
    return Optional.ofNullable(fax);
  }

  public Optional<String> getNote() {
    return Optional.ofNullable(note);
  }

  public Optional<Timestamp> getCreateTime() {
    return Optional.ofNullable(createTime);
  }

  public Optional<Timestamp> getUpdateTime() {
    return Optional.ofNullable(updateTime);
  }

  public Optional<Timestamp> getDeleteTime() {
    return Optional.ofNullable(deleteTime);
  }

  public BusinessCardModel create(Map<String, Object> map) {
    BusinessCardModel.BusinessCardModelBuilder builder = BusinessCardModel.builder();
    if (ObjectUtils.isEmpty(map.get("id"))) {
      builder.id(this.getId());
    } else {
      builder.id((Integer) map.get("id"));
    }
    if (ObjectUtils.isEmpty(map.get("companyName"))) {
      builder.companyName(this.getCompanyName().orElse(null));
    } else {
      builder.companyName((String) map.get("companyName"));
    }
    if (ObjectUtils.isEmpty(map.get("depaertmentName"))) {
      builder.depaertmentName(this.getDepaertmentName().orElse(null));
    } else {
      builder.depaertmentName((String) map.get("depaertmentName"));
    }
    if (ObjectUtils.isEmpty(map.get("post"))) {
      builder.post(this.getPost().orElse(null));
    } else {
      builder.post((String) map.get("post"));
    }
    if (ObjectUtils.isEmpty(map.get("name"))) {
      builder.name(this.getName());
    } else {
      builder.name((String) map.get("name"));
    }
    if (ObjectUtils.isEmpty(map.get("postalCode"))) {
      builder.postalCode(this.getPostalCode().orElse(null));
    } else {
      builder.postalCode((String) map.get("postalCode"));
    }
    if (ObjectUtils.isEmpty(map.get("address"))) {
      builder.address(this.getAddress().orElse(null));
    } else {
      builder.address((String) map.get("address"));
    }
    if (ObjectUtils.isEmpty(map.get("phoneNumber"))) {
      builder.phoneNumber(this.getPhoneNumber().orElse(null));
    } else {
      builder.phoneNumber((String) map.get("phoneNumber"));
    }
    if (ObjectUtils.isEmpty(map.get("fax"))) {
      builder.fax(this.getFax().orElse(null));
    } else {
      builder.fax((String) map.get("fax"));
    }
    if (ObjectUtils.isEmpty(map.get("note"))) {
      builder.note(this.getNote().orElse(null));
    } else {
      builder.note((String) map.get("note"));
    }
    if (ObjectUtils.isEmpty(map.get("createTime"))) {
      builder.createTime(this.getCreateTime().orElse(null));
    } else {
      builder.createTime((Timestamp) map.get("createTime"));
    }
    if (ObjectUtils.isEmpty(map.get("updateTime"))) {
      builder.updateTime(this.getUpdateTime().orElse(null));
    } else {
      builder.updateTime((Timestamp) map.get("updateTime"));
    }
    if (ObjectUtils.isEmpty(map.get("deleteTime"))) {
      builder.deleteTime(this.getDeleteTime().orElse(null));
    } else {
      builder.deleteTime((Timestamp) map.get("deleteTime"));
    }
    return builder.build();
  }
}
