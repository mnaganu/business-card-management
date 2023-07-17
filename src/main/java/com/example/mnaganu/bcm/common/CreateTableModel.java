package com.example.mnaganu.bcm.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@lombok.Value
@lombok.Builder
public class CreateTableModel {
  private final String tableName;
  private final String columName;
  private final String type;
  private final boolean isNotNull;
  private final String defaultValue;
  private final String javaClassName;
  private final String javaType;
  private final String javaMemberName;
  private final String javaGetMethodName;

  @Override
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("CreateTableModel{");
    stringBuffer.append("tableName=");
    stringBuffer.append(tableName);
    stringBuffer.append(", columName=");
    stringBuffer.append(columName);
    stringBuffer.append(", type=");
    stringBuffer.append(type);
    stringBuffer.append(", isNotNull=");
    stringBuffer.append(isNotNull);
    stringBuffer.append(", defaultValue=");
    stringBuffer.append(defaultValue);
    stringBuffer.append(", javaClassName=");
    stringBuffer.append(javaClassName);
    stringBuffer.append(", javaType=");
    stringBuffer.append(javaType);
    stringBuffer.append(", javaMemberName=");
    stringBuffer.append(javaMemberName);
    stringBuffer.append(", javaGetMethodName=");
    stringBuffer.append(javaGetMethodName);
    stringBuffer.append("}");

    return stringBuffer.toString();
  }

  public static List<CreateTableModel> build(String sqlCreateTable) {
    List<CreateTableModel> list = new ArrayList<>();
    if (sqlCreateTable == null) {
      return list;
    }
    String[] strArray = sqlCreateTable.split("\n");

    String regTable = "CREATE TABLE `(.*)`.+";
    Pattern pTable = Pattern.compile(regTable);
    String regColumn = "\\s*`(.+)`\\s{1}(\\w+).+";
    Pattern pColumn = Pattern.compile(regColumn);
    String regNotNull = "\\s{1}NOT NULL\\s{1}.+";
    Pattern pNotNull = Pattern.compile(regNotNull);
    String regDefault = "\\s{1}DEFAULT\\s{1}([\\w']+)[\\s,]+";
    Pattern pDefault = Pattern.compile(regDefault);

    String tableName = "";

    for (int i = 0; i < strArray.length; i++) {
      String columnName = null;
      String type = null;
      boolean isNotNull = false;
      String defaultValue = null;

      Matcher m = pTable.matcher(strArray[i]);
      if (m.find()) {
        tableName = m.group(1);
      }
      m = pColumn.matcher(strArray[i]);
      if (m.find()) {
        columnName = m.group(1);
        type = m.group(2);
      }
      m = pNotNull.matcher(strArray[i]);
      if (m.find()) {
        isNotNull = true;
      }
      m = pDefault.matcher(strArray[i]);
      if (m.find()) {
        defaultValue = m.group(1);
      }
      if (columnName != null) {
        list.add(
            CreateTableModel.builder()
                .tableName(tableName)
                .columName(columnName)
                .type(type)
                .isNotNull(isNotNull)
                .defaultValue(defaultValue)
                .javaType(toJavaType(type))
                .javaClassName(toJavaClassName(tableName))
                .javaMemberName(toJavaMemberName(columnName))
                .javaGetMethodName(toJavaGetMethodName(columnName))
                .build()
        );
      }
    }

    return list;
  }

  private static String toJavaMemberName(String columName) {
    StringBuffer javaMemberName = new StringBuffer();
    if (columName == null) {
      return javaMemberName.toString();
    }
    if (columName.startsWith("_")) {
      javaMemberName.append("_");
    }

    List<String> list = toList(columName.toLowerCase(), "_");
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        javaMemberName.append(list.get(i));
      } else {
        if (list.get(i).length() == 1) {
          javaMemberName.append(list.get(i).toUpperCase());
        } else {
          javaMemberName.append(list.get(i).substring(0, 1).toUpperCase());
          javaMemberName.append(list.get(i).substring(1));
        }
      }
    }
    return javaMemberName.toString();
  }

  private static String toJavaGetMethodName(String columnName) {
    StringBuffer javaGetMethodName = new StringBuffer();
    String javaMemberName = toJavaMemberName(columnName);
    javaGetMethodName.append("get");
    if (javaMemberName != null) {
      if (javaMemberName.length() > 0) {
        if (javaMemberName.startsWith("_")) {
          javaGetMethodName.append(javaGetMethodName);
        } else {
          if (javaMemberName.length() == 1) {
            javaGetMethodName.append(javaMemberName.toUpperCase());
          } else {
            javaGetMethodName.append(javaMemberName.substring(0, 1).toUpperCase());
            javaGetMethodName.append(javaMemberName.substring(1));
          }
        }
      }
    }
    return javaGetMethodName.toString();
  }

  private static String toJavaClassName(String tableName) {
    StringBuffer javaClassName = new StringBuffer();
    if (tableName == null) {
      return javaClassName.toString();
    }
    String[] strArray = tableName.toLowerCase().split("_");
    for (int i = 0; i < strArray.length; i++) {
      if (strArray[i].length() > 0) {
        if (strArray[i].length() == 1) {
          javaClassName.append(strArray[i].toUpperCase());
        } else {
          javaClassName.append(strArray[i].substring(0, 1).toUpperCase());
          javaClassName.append(strArray[i].substring(1));
        }
      }
    }
    return javaClassName.toString();
  }

  private static String toJavaType(String type) {
    String lowerCaseType = type.toLowerCase();
    if (lowerCaseType.contains("varchar")) {
      return "String";
    }
    if (lowerCaseType.contains("text")) {
      return "String";
    }
    if (lowerCaseType.contains("datetime")) {
      return "Timestamp";
    }
    if (lowerCaseType.contains("date")) {
      return "Date";
    }
    if (lowerCaseType.contains("int")) {
      return "Integer";
    }
    return "";
  }

  private static List<String> toList(String data, String delimiter) {
    List<String> list = new ArrayList<String>();
    if (data == null || delimiter == null) {
      return list;
    }
    String[] strArray = data.split(delimiter);
    for (String str : strArray) {
      if (str.length() != 0) {
        list.add(str);
      }
    }
    return list;
  }


}
