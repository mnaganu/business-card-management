package com.example.mnaganu.bcm.infrastructure.mapper;

import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessCardRowMapper implements RowMapper<BusinessCardModel> {
  @Override
  public BusinessCardModel mapRow(ResultSet rs, int rowNum) throws SQLException {
    return BusinessCardModel.builder()
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
        .build();
  }
}
