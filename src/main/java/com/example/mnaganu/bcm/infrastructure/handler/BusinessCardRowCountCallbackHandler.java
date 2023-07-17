package com.example.mnaganu.bcm.infrastructure.handler;

import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import org.springframework.jdbc.core.RowCountCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusinessCardRowCountCallbackHandler extends RowCountCallbackHandler {
  private List<BusinessCardModel> businessCardModelList;
  private final int offset;
  private final int limit;

  public BusinessCardRowCountCallbackHandler (int offset, int limit) {
    super();
    businessCardModelList = new ArrayList<BusinessCardModel>();
    this.offset = offset;
    this.limit = limit;
  }

  @Override
  protected void processRow(ResultSet rs, int rowNum) throws SQLException {
    if (rowNum >= offset & rowNum < offset + limit) {
      businessCardModelList.add(
          BusinessCardModel.builder()
              .id((Integer) rs.getObject("id"))
              .companyName(rs.getString("company_name"))
              .depaertmentName(rs.getString("depaertment_name"))
              .post(rs.getString("post"))
              .name(rs.getString("name"))
              .postalCode(rs.getString("postal_code"))
              .address(rs.getString("address"))
              .phoneNumber(rs.getString("phone_number"))
              .fax(rs.getString("fax"))
              .note(rs.getString("note"))
              .createTime(rs.getTimestamp("create_time"))
              .updateTime(rs.getTimestamp("update_time"))
              .deleteTime(rs.getTimestamp("delete_time"))
              .build()
      );
    }
  }

  public List<BusinessCardModel> getList() {
    return businessCardModelList;
  }
}
