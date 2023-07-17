package com.example.mnaganu.bcm.common;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

  /**
   * 文字列で指定した日時で Timestamp を作成する
   *
   * @param datetime yyyy-MM-dd HH:mm:ss
   * @return Timestamp
   */
  static public Optional<Timestamp> stringToTimestamp(String datetime) {
    return stringToTimestamp(datetime, "-");
  }

  /**
   * 文字列で指定した日時で Timestamp を作成する
   *
   * @param datetime      yyyy-MM-dd HH:mm:ss など
   * @param dateDelimiter 日付の区切り文字
   * @return Timestamp
   */
  static public Optional<Timestamp> stringToTimestamp(String datetime, String dateDelimiter) {
    if (datetime == null || dateDelimiter == null) {
      return Optional.empty();
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy"
        + dateDelimiter
        + "MM"
        + dateDelimiter
        + "dd HH:mm:ss"
    );
    return Optional.ofNullable(Timestamp.valueOf(LocalDateTime.parse(datetime, dtf)));
  }

  /**
   * Timestamp を文字列（yyyy-MM-dd HH:mm:ss）に変換する
   *
   * @param timestamp Timestamp
   * @return yyyy-MM-dd HH:mm:ss
   */
  static public Optional<String> timestampToString(Timestamp timestamp) {
    return timestampToString(timestamp, "-");
  }

  /**
   * Timestamp を文字列（yyyy-MM-dd HH:mm:ss など）に変換する
   *
   * @param timestamp     Timestamp
   * @param dateDelimiter 日付の区切り文字
   * @return yyyy-MM-dd HH:mm:ss など
   */
  static public Optional<String> timestampToString(Timestamp timestamp, String dateDelimiter) {
    if (timestamp == null || dateDelimiter == null) {
      return Optional.empty();
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy"
        + dateDelimiter
        + "MM"
        + dateDelimiter
        + "dd HH:mm:ss"
    );
    return Optional.ofNullable(timestamp.toLocalDateTime().format(dtf).toString());
  }

  /**
   * 文字列で指定した日付で Date を作成する
   *
   * @param date yyyy-MM-dd など
   * @return Date
   */
  public static Optional<Date> stringToDate(String date) {
    return stringToDate(date, "-");
  }

  /**
   * 文字列で指定した日付で Date を作成する
   *
   * @param date          yyyy-MM-dd など
   * @param dateDelimiter 日付の区切り文字
   * @return Date
   */
  public static Optional<Date> stringToDate(String date, String dateDelimiter) {
    if (date == null || dateDelimiter == null) {
      return Optional.empty();
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy"
        + dateDelimiter
        + "MM"
        + dateDelimiter
        + "dd"
    );
    return Optional.ofNullable(Date.valueOf(LocalDate.parse(date, dtf)));
  }

  /**
   * Date を文字列（yyyy-MM-dd など）に変換する
   *
   * @param date 　         Date
   * @return
   */
  static public Optional<String> dateToString(Date date) {
    return dateToString(date, "-");
  }

  /**
   * Date を文字列（yyyy-MM-dd など）に変換する
   *
   * @param date          　         Date
   * @param dateDelimiter 日付の区切り文字
   * @return
   */
  static public Optional<String> dateToString(Date date, String dateDelimiter) {
    if (date == null || dateDelimiter == null) {
      return Optional.empty();
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy"
        + dateDelimiter
        + "MM"
        + dateDelimiter
        + "dd");
    return Optional.ofNullable(date.toLocalDate().format(dtf).toString());
  }

  /**
   * Insert 文を作成する
   *
   * @param tableName 　テーブル名
   * @param map       　key:カラム名とvalue:データのMap
   * @return
   */
  static public Optional<String> createInsertSql(String tableName, Map<String, String> map) {
    if (tableName == null || map == null) {
      return Optional.empty();
    }

    StringBuffer columns = new StringBuffer();
    StringBuffer values = new StringBuffer();

    for (String key : map.keySet()) {
      if (columns.length() != 0) {
        columns.append(", ");
        values.append(", ");
      }
      columns.append(key);
      values.append(map.get(key));
    }

    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO ");
    sql.append(tableName);
    sql.append(" (");
    sql.append(columns);
    sql.append(") VALUES (");
    sql.append(values);
    sql.append(")");

    return Optional.ofNullable(sql.toString());
  }

  public static boolean isNumeric(String str) {
    if (str != null) {
      String regex = "^[0-9]+$";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(str);
      return matcher.matches();
    }
    return false;
  }
  
}
