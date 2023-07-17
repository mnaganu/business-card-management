package com.example.mnaganu.bcm.common;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonUtilTest {
  @Test
  void stringToTimestamp() {
    String testDateTime = "2023-04-02 11:11:11";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String testDateTime2 = "2023/04/02 11:11:11";
    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    //Timestamp が取得できること
    Optional<Timestamp> timestamp = CommonUtil.stringToTimestamp(testDateTime);
    assertThat(timestamp.isPresent()).isTrue();
    assertThat(timestamp.get().toLocalDateTime().format(dtf).toString()).isEqualTo(testDateTime);

    timestamp = CommonUtil.stringToTimestamp(testDateTime2, "/");
    assertThat(timestamp.isPresent()).isTrue();
    assertThat(timestamp.get().toLocalDateTime().format(dtf2).toString()).isEqualTo(testDateTime2);

    //null の場合
    timestamp = CommonUtil.stringToTimestamp(null);
    assertThat(timestamp.isEmpty()).isTrue();

    timestamp = CommonUtil.stringToTimestamp(null, "-");
    assertThat(timestamp.isEmpty()).isTrue();

    timestamp = CommonUtil.stringToTimestamp(testDateTime2, null);
    assertThat(timestamp.isEmpty()).isTrue();

    timestamp = CommonUtil.stringToTimestamp(null, null);
    assertThat(timestamp.isEmpty()).isTrue();

  }

  @Test
  void timestampToString() {
    String testDateTime = "2023-04-02 11:11:11";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(testDateTime, dtf));
    String testDateTime2 = "2023/04/02 11:11:11";
    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    Timestamp timestamp2 = Timestamp.valueOf(LocalDateTime.parse(testDateTime2, dtf2));

    //String が取得できること
    Optional<String> datetime = CommonUtil.timestampToString(timestamp);
    assertThat(datetime.isPresent()).isTrue();
    assertThat(datetime.get()).isEqualTo(testDateTime);

    datetime = CommonUtil.timestampToString(timestamp2, "/");
    assertThat(datetime.isPresent()).isTrue();
    assertThat(datetime.get()).isEqualTo(testDateTime2);

    //null の場合
    datetime = CommonUtil.timestampToString(null);
    assertThat(datetime.isEmpty()).isTrue();

    datetime = CommonUtil.timestampToString(null, "/");
    assertThat(datetime.isEmpty()).isTrue();

    datetime = CommonUtil.timestampToString(timestamp2, null);
    assertThat(datetime.isEmpty()).isTrue();

    datetime = CommonUtil.timestampToString(null, null);
    assertThat(datetime.isEmpty()).isTrue();

  }

  @Test
  void stringToDate() {
    String testDate = "2023-04-03";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    String testDate2 = "2023/04/03";
    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    //Date が取得できること
    Optional<Date> date = CommonUtil.stringToDate(testDate);
    assertThat(date.isPresent()).isTrue();
    assertThat(date.get().toLocalDate().format(dtf).toString()).isEqualTo(testDate);

    date = CommonUtil.stringToDate(testDate2, "/");
    assertThat(date.isPresent()).isTrue();
    assertThat(date.get().toLocalDate().format(dtf2).toString()).isEqualTo(testDate2);

    //null の場合
    date = CommonUtil.stringToDate(null);
    assertThat(date.isEmpty()).isTrue();

    date = CommonUtil.stringToDate(null, "/");
    assertThat(date.isEmpty()).isTrue();

    date = CommonUtil.stringToDate(testDate2, null);
    assertThat(date.isEmpty()).isTrue();

    date = CommonUtil.stringToDate(null, null);
    assertThat(date.isEmpty()).isTrue();

  }

  @Test
  void dateToString() {
    String testDate = "2023-04-03";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Date date = Date.valueOf(LocalDate.parse(testDate, dtf));

    String testDate2 = "2023/04/03";
    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    Date date2 = Date.valueOf(LocalDate.parse(testDate2, dtf2));

    //String が取得できること
    Optional<String> dateString = CommonUtil.dateToString(date);
    assertThat(dateString.isPresent()).isTrue();
    assertThat(dateString.get()).isEqualTo(testDate);

    dateString = CommonUtil.dateToString(date2, "/");
    assertThat(dateString.isPresent()).isTrue();
    assertThat(dateString.get()).isEqualTo(testDate2);

    //null の場合
    dateString = CommonUtil.dateToString(null);
    assertThat(dateString.isEmpty()).isTrue();

    dateString = CommonUtil.dateToString(null, "/");
    assertThat(dateString.isEmpty()).isTrue();

    dateString = CommonUtil.dateToString(date2, null);
    assertThat(dateString.isEmpty()).isTrue();

    dateString = CommonUtil.dateToString(null, null);
    assertThat(dateString.isEmpty()).isTrue();

  }

  @Test
  void createInsertSql() {
    String testData = "INSERT INTO employee (name, age) VALUES ('Taro', 20)";

    Map<String, String> map = new HashMap<>();
    map.put("name", "'Taro'");
    map.put("age", "20");

    //insert 文が生成されること
    Optional<String> sql = CommonUtil.createInsertSql("employee", map);
    assertThat(sql.isPresent()).isTrue();
    assertThat(sql.get()).isEqualTo(testData);

    //null の場合
    sql = CommonUtil.createInsertSql(null, map);
    assertThat(sql.isEmpty()).isTrue();

    sql = CommonUtil.createInsertSql("employee", null);
    assertThat(sql.isEmpty()).isTrue();

    sql = CommonUtil.createInsertSql(null, null);
    assertThat(sql.isEmpty()).isTrue();

  }

  @Test
  void isNumeric() {
    //数字のみの場合
    assertThat(CommonUtil.isNumeric("1234567890")).isTrue();
    //数字以外の文字が混ざっている場合
    assertThat(CommonUtil.isNumeric("1,234,567,890")).isFalse();
    //null の場合
    assertThat(CommonUtil.isNumeric(null)).isFalse();
  }
}
