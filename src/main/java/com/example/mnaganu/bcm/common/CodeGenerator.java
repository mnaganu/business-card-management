package com.example.mnaganu.bcm.common;

import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

public class CodeGenerator {

  /**
   * UPDATE 文を生成します
   *
   * @param list     create table 文を解析したモデルリスト
   * @param isString 　出力結果を SQL ではなく Java の String で定義した形で出す場合は true を指定する
   * @param pk       　プライマリーキーとなるカラム名
   * @return UPDATE文
   */
  public static String generateUpdate(List<CreateTableModel> list, boolean isString, String pk) {
    StringBuffer sql = new StringBuffer();
    StringBuffer parameters = new StringBuffer();
    if (ObjectUtils.isEmpty(list) || ObjectUtils.isEmpty(pk)) {
      return sql.toString();
    }
    String tableName = "";
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        tableName = list.get(i).getTableName();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET \n");
        parameters.append("SqlParameterSource parameters = new MapSqlParameterSource(\"");
      } else {
        parameters.append(".addValue(\"");
      }
      if (!Objects.equals(list.get(i).getColumName().toLowerCase(), pk.toLowerCase())) {
        sql.append(list.get(i).getColumName().toLowerCase());
        sql.append(" = :");
        sql.append(list.get(i).getJavaMemberName());
        if (i != list.size() - 1) {
          sql.append(", \n");
        } else {
          sql.append(" \n");
        }
      }
      parameters.append(list.get(i).getJavaMemberName());
      parameters.append("\", model.");
      parameters.append(list.get(i).getJavaGetMethodName());
      parameters.append("())");
      if (i != list.size() - 1) {
        parameters.append("\n");
      } else {
        parameters.append(";");
      }
    }
    sql.append("WHERE ");
    sql.append(pk);
    sql.append(" = :");
    sql.append(toJavaMemberName(pk));
    sql.append(" \n");

    if (isString) {
      StringBuffer stringBuffer = new StringBuffer();
      String[] strArray = sql.toString().split("\n");
      stringBuffer.append("String sql = ");
      for (int i = 0; i < strArray.length; i++) {
        stringBuffer.append("\"");
        stringBuffer.append(strArray[i]);
        if (i == strArray.length - 1) {
          stringBuffer.append("\"; \n");
        } else {
          stringBuffer.append("\" + \n");
        }
      }
      stringBuffer.append(parameters);
      return stringBuffer.toString();
    }
    return sql.toString();
  }

  /**
   * INSERT 文を生成します
   *
   * @param list     create table 文を解析したモデルリスト
   * @param isString 　出力結果を SQL ではなく Java の String で定義した形で出す場合は true を指定する
   * @return
   */
  public static String generateInsert(List<CreateTableModel> list, boolean isString) {
    StringBuffer sql = new StringBuffer();
    StringBuffer values = new StringBuffer();
    StringBuffer parameters = new StringBuffer();
    if (ObjectUtils.isEmpty(list)) {
      return sql.toString();
    }
    String tableName = "";
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        tableName = list.get(i).getTableName();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" ( \n");
        parameters.append("SqlParameterSource parameters = new MapSqlParameterSource(\"");
      } else {
        parameters.append(".addValue(\"");
      }

      sql.append(list.get(i).getColumName().toLowerCase());
      values.append(":");
      values.append(list.get(i).getJavaMemberName());
      parameters.append(list.get(i).getJavaMemberName());
      parameters.append("\", model.");
      parameters.append(list.get(i).getJavaGetMethodName());
      parameters.append("())");

      if (i != list.size() - 1) {
        sql.append(", \n");
        values.append(", \n");
        parameters.append("\n");
      } else {
        sql.append(" \n");
        values.append(" \n");
        parameters.append(";");
      }
    }
    sql.append(" ) VALUES ( \n");
    sql.append(values);
    sql.append(" ) ");

    if (isString) {
      StringBuffer stringBuffer = new StringBuffer();
      String[] strArray = sql.toString().split("\n");
      stringBuffer.append("String sql = ");
      for (int i = 0; i < strArray.length; i++) {
        stringBuffer.append("\"");
        stringBuffer.append(strArray[i]);
        if (i == strArray.length - 1) {
          stringBuffer.append("\"; \n");
        } else {
          stringBuffer.append("\" + \n");
        }
      }
      stringBuffer.append(parameters);
      return stringBuffer.toString();
    }
    return sql.toString();
  }

  /**
   * SELECT 文を生成します
   *
   * @param prefix   　　カラム名の前につける文字
   * @param list     　create table 文を解析したモデルリスト
   * @param isString 　出力結果を SQL ではなく Java の String で定義した形で出す場合 true を指定する
   * @return INSERT 文
   */
  public static String generateSelect(String prefix, List<CreateTableModel> list, boolean isString) {
    StringBuffer sql = new StringBuffer();
    if (ObjectUtils.isEmpty(list)) {
      return sql.toString();
    }
    String tableName = "";
    sql.append("SELECT \n");
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        tableName = list.get(i).getTableName();
      }
      if (prefix != null && prefix.length() > 0) {
        sql.append(prefix);
        sql.append(".");
        sql.append(list.get(i).getColumName().toLowerCase());
        sql.append(" AS ");
        sql.append(prefix);
        sql.append("_");
        sql.append(list.get(i).getColumName().toLowerCase());
      } else {
        sql.append(list.get(i).getColumName().toLowerCase());
      }
      if (i != list.size() - 1) {
        sql.append(", \n");
      } else {
        sql.append(" \n");
      }
    }
    sql.append("FROM ");
    sql.append(tableName);
    if (prefix != null && prefix.length() > 0) {
      sql.append(" ");
      sql.append(prefix);
    }
    if (isString) {
      StringBuffer stringBuffer = new StringBuffer();
      String[] strArray = sql.toString().split("\n");
      stringBuffer.append("String sql = ");
      for (int i = 0; i < strArray.length; i++) {
        stringBuffer.append("\"");
        stringBuffer.append(strArray[i]);
        if (i == strArray.length - 1) {
          stringBuffer.append("\"; \n");
        } else {
          stringBuffer.append("\" + \n");
        }
      }
      return stringBuffer.toString();
    }
    return sql.toString();
  }

  /**
   * Model Class のソースコードを生成します
   *
   * @param list 　create table 文を解析したモデルリスト
   * @return Model Class のソースコード
   */
  public static String generateModel(List<CreateTableModel> list) {
    StringBuffer code = new StringBuffer();
    StringBuffer member = new StringBuffer();
    StringBuffer getMethod = new StringBuffer();
    StringBuffer createMethod = new StringBuffer();

    if (ObjectUtils.isEmpty(list)) {
      return code.toString();
    }
    for (int i = 0; i < list.size(); i++) {
      String defaultValue = getDefaultValue(list.get(i).getJavaType(), list.get(i).getDefaultValue());
      if (i == 0) {
        code.append("@lombok.Value\n");
        code.append("@lombok.Builder\n");
        code.append("public class ");
        code.append(list.get(i).getJavaClassName());
        code.append("Model");
        code.append(" {\n");

        createMethod.append("\n");
        createMethod.append("  public ");
        createMethod.append(list.get(i).getJavaClassName());
        createMethod.append("Model create(");
        createMethod.append("Map<String, Object> map) {\n");
        createMethod.append("    ");
        createMethod.append(list.get(i).getJavaClassName());
        createMethod.append("Model.");
        createMethod.append(list.get(i).getJavaClassName());
        createMethod.append("ModelBuilder builder = ");
        createMethod.append(list.get(i).getJavaClassName());
        createMethod.append("Model.builder();\n");
      }
      if (list.get(i).isNotNull() && !isDateType(list.get(i).getJavaType())) {
        //日付を扱うデータは null を許容する
        // "0000-00-00" を null に変換するため
        member.append("  @lombok.NonNull\n");
      } else {
        getMethod.append("\n");
        getMethod.append("  public Optional<");
        getMethod.append(list.get(i).getJavaType());
        getMethod.append("> ");
        getMethod.append(list.get(i).getJavaGetMethodName());
        getMethod.append("() {\n");
        getMethod.append("    return Optional.ofNullable(");
        getMethod.append(list.get(i).getJavaMemberName());
        getMethod.append(");\n");
        getMethod.append("  }\n");
      }
      if (defaultValue.length() > 0) {
        member.append("  @lombok.Builder.Default\n");
      }
      member.append("  private final ");
      member.append(list.get(i).getJavaType());
      member.append(" ");
      member.append(list.get(i).getJavaMemberName());
      if (defaultValue.length() > 0) {
        member.append(" = ");
        member.append(defaultValue);
      }
      member.append(";\n");

      createMethod.append("    if (ObjectUtils.isEmpty(map.get(\"");
      createMethod.append(list.get(i).getJavaMemberName());
      createMethod.append("\"))) {\n");

      createMethod.append("      builder.");
      createMethod.append(list.get(i).getJavaMemberName());
      createMethod.append("(this.");
      createMethod.append(list.get(i).getJavaGetMethodName());
      createMethod.append("()");
      if (!list.get(i).isNotNull()) {
        createMethod.append(".orElse(null)");
      }
      createMethod.append(");\n");
      createMethod.append("    } else {\n");
      createMethod.append("      builder.");
      createMethod.append(list.get(i).getJavaMemberName());
      createMethod.append("((");
      createMethod.append(list.get(i).getJavaType());
      createMethod.append(") map.get(\"");
      createMethod.append(list.get(i).getJavaMemberName());
      createMethod.append("\"));\n");
      createMethod.append("    }\n");

    }
    createMethod.append("    return builder.build();\n");
    createMethod.append("  }\n");

    code.append(member);
    code.append(getMethod);
    code.append(createMethod);
    code.append("}\n");
    return code.toString();
  }

  /**
   * RowCallbackHandler のソースコードを生成します。
   *
   * @param list create table 文を解析したモデルリスト
   * @return
   */
  public static String generateRowCallbackHandler(List<CreateTableModel> list) {
    return generateRowCallbackHandler(null, list);
  }

  /**
   * RowCallbackHandler のソースコードを生成します。
   *
   * @param prefix テーブルの別名（なければ null を指定してください。）
   * @param list   create table 文を解析したモデルリスト
   * @return
   */
  public static String generateRowCallbackHandler(String prefix, List<CreateTableModel> list) {
    StringBuffer code = new StringBuffer();
    if (ObjectUtils.isEmpty(list)) {
      return code.toString();
    }
    String javaClassName = "";
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        javaClassName = list.get(i).getJavaClassName();

        code.append("import org.springframework.jdbc.core.RowCountCallbackHandler;\n");
        code.append("import java.sql.ResultSet;\n");
        code.append("import java.sql.SQLException;\n");
        code.append("import java.util.ArrayList;\n");
        code.append("import java.util.List;\n");
        code.append("\n");
        code.append("public class ");
        code.append(list.get(i).getJavaClassName());
        code.append("RowCountCallbackHandler extends RowCountCallbackHandler {\n");
        code.append("  private List<");
        code.append(list.get(i).getJavaClassName());
        code.append("Model> ");
        code.append(toJavaMemberName(list.get(i).getJavaClassName()));
        code.append("ModelList; \n");
        code.append("  private final int offset;\n");
        code.append("  private final int limit;\n");
        code.append("\n");
        code.append("  public ");
        code.append(list.get(i).getJavaClassName());
        code.append("RowCountCallbackHandler (int offset, int limit) {\n");
        code.append("    super();\n");
        code.append("    ");
        code.append(toJavaMemberName(list.get(i).getJavaClassName()));
        code.append("ModelList = new ArrayList<");
        code.append(list.get(i).getJavaClassName());
        code.append("Model>();\n");
        code.append("    this.offset = offset;\n");
        code.append("    this.limit = limit;\n");
        code.append("  }\n");
        code.append("\n");
        code.append("  @Override\n");
        code.append("  protected void processRow(ResultSet rs, int rowNum) throws SQLException {\n");
        code.append("    if (rowNum >= offset & rowNum < offset + limit) {\n");
        code.append("      ");
        code.append(toJavaMemberName(list.get(i).getJavaClassName()));
        code.append("ModelList.add(\n");
        code.append("        ");
        code.append(list.get(i).getJavaClassName());
        code.append("Model.builder()\n");
      }

      if (Objects.equals(list.get(i).getJavaType(), "String")
          || Objects.equals(list.get(i).getJavaType(), "Date")
          || Objects.equals(list.get(i).getJavaType(), "Timestamp")) {
        code.append("          .");
        code.append(list.get(i).getJavaMemberName());
        code.append("(rs.get");
        code.append(list.get(i).getJavaType());
        code.append("(\"");
        if (prefix != null) {
          code.append(prefix);
          code.append("_");
        }
        code.append(list.get(i).getColumName().toLowerCase());
        code.append("\"))\n");
      } else {
        code.append("          .");
        code.append(list.get(i).getJavaMemberName());
        code.append("((");
        code.append(list.get(i).getJavaType());
        code.append(") rs.getObject(\"");
        if (prefix != null) {
          code.append(prefix);
          code.append("_");
        }
        code.append(list.get(i).getColumName().toLowerCase());
        code.append("\"))\n");
      }
    }
    code.append("          .build()\n");
    code.append("      );\n");
    code.append("    }\n");
    code.append("  }\n");
    code.append("\n");

    code.append("  public List<");
    code.append(javaClassName);
    code.append("Model> getList() {\n");
    code.append("    return ");
    code.append(toJavaMemberName(javaClassName));
    code.append("ModelList;\n");
    code.append("  }\n");
    code.append("}\n");
    return code.toString();
  }

  /**
   * RowMapper のソースコードを生成します。
   *
   * @param list create table 文を解析したモデルリスト
   * @return
   */
  public static String generateRowMapper(List<CreateTableModel> list) {
    return generateRowMapper(null, list);
  }

  /**
   * RowMapper のソースコードを生成します。
   *
   * @param prefix テーブルの別名（なければ null を指定してください。）
   * @param list   create table 文を解析したモデルリスト
   * @return
   */
  public static String generateRowMapper(String prefix, List<CreateTableModel> list) {
    StringBuffer code = new StringBuffer();
    if (ObjectUtils.isEmpty(list)) {
      return code.toString();
    }
    String javaClassName = "";
    for (int i = 0; i < list.size(); i++) {
      if (i == 0) {
        javaClassName = list.get(i).getJavaClassName();

        code.append("import org.springframework.jdbc.core.RowMapper;\n");
        code.append("import java.sql.ResultSet;\n");
        code.append("import java.sql.SQLException;\n");
        code.append("\n");
        code.append("public class ");
        code.append(list.get(i).getJavaClassName());
        code.append("RowMapper implements RowMapper<");
        code.append(list.get(i).getJavaClassName());
        code.append("Model> {\n");

        code.append("  @Override\n");
        code.append("  public ");
        code.append(list.get(i).getJavaClassName());
        code.append("Model mapRow(ResultSet rs, int rowNum) throws SQLException {\n");
        code.append("    return ");
        code.append(list.get(i).getJavaClassName());
        code.append("Model.builder()\n");
      }

      if (Objects.equals(list.get(i).getJavaType(), "String")
          || Objects.equals(list.get(i).getJavaType(), "Date")
          || Objects.equals(list.get(i).getJavaType(), "Timestamp")) {
        code.append("      .");
        code.append(list.get(i).getJavaMemberName());
        code.append("(rs.get");
        code.append(list.get(i).getJavaType());
        code.append("(\"");
        if (prefix != null) {
          code.append(prefix);
          code.append("_");
        }
        code.append(list.get(i).getColumName().toLowerCase());
        code.append("\"))\n");
      } else {
        code.append("      .");
        code.append(list.get(i).getJavaMemberName());
        code.append("((");
        code.append(list.get(i).getJavaType());
        code.append(") rs.getObject(\"");
        if (prefix != null) {
          code.append(prefix);
          code.append("_");
        }
        code.append(list.get(i).getColumName().toLowerCase());
        code.append("\"))\n");
      }
    }
    code.append("      .build();\n");
    code.append("  }\n");
    code.append("}\n");
    return code.toString();
  }

  private static String toJavaMemberName(String name) {
    StringBuffer javaMemberName = new StringBuffer();
    if (ObjectUtils.isEmpty(name)) {
      return javaMemberName.toString();
    }

    if (name.length() == 1) {
      javaMemberName.append(name.toLowerCase());
    } else {
      javaMemberName.append(name.substring(0, 1).toLowerCase());
      javaMemberName.append(name.substring(1));

    }
    return javaMemberName.toString();
  }

  private static String getDefaultValue(String type, String defaultVal) {
    if (ObjectUtils.isEmpty(type) || ObjectUtils.isEmpty(defaultVal)) {
      return "";
    }
    if (defaultVal.toLowerCase().equals("null")) {
      return "";
    }
    if (defaultVal.contains("0000-00-00")) {
      //特殊日付にする場合は省略(MySQL)
      return "";
    }
    if (type.equals("String")) {
      return defaultVal.replace("'", "\"");
    }
    if (type.equals("Integer")) {
      return defaultVal.replace("'", "");
    }
    return "";
  }

  private static boolean isDateType(String type) {
    if (Objects.equals(type, "Date")) {
      return true;
    }
    if (Objects.equals(type, "Timestamp")) {
      return true;
    }
    return false;
  }

}
