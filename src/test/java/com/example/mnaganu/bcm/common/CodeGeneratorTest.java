package com.example.mnaganu.bcm.common;

import org.junit.jupiter.api.Test;

import java.util.List;

public class CodeGeneratorTest {
  @Test
  void test() {
    String str = "CREATE TABLE `business_card` (\n" +
        " `id` int NOT NULL AUTO_INCREMENT,\n" +
        "`company_name` varchar(255) DEFAULT NULL COMMENT '会社名',\n" +
        "`depaertment_name` varchar(255) DEFAULT NULL COMMENT '部署名',\n" +
        "`post` varchar(255) DEFAULT NULL COMMENT '役職',\n" +
        "`name` varchar(255) NOT NULL COMMENT '名前',\n" +
        "`postal_code` varchar(8) DEFAULT NULL COMMENT '郵便番号',\n" +
        "`address` varchar(255) DEFAULT NULL COMMENT '住所',\n" +
        "`phone_number` varchar(255) DEFAULT NULL COMMENT '電話番号',\n" +
        "`fax` varchar(255) DEFAULT NULL COMMENT 'fax',\n" +
        "`note` varchar(255) DEFAULT NULL COMMENT '備考',\n" +
        "`create_time` datetime DEFAULT NULL COMMENT 'データ作成日',\n" +
        "`update_time` datetime DEFAULT NULL COMMENT 'データ更新日',\n" +
        "`delete_time` datetime DEFAULT NULL COMMENT 'データ削除日',\n" +
        "PRIMARY KEY (`id`)\n" +
        ")\n";
    List<CreateTableModel> modelList = CreateTableModel.build(str);
    for (CreateTableModel m : modelList) {
      System.out.println(m);
    }

    str = CodeGenerator.generateUpdate(modelList, false, "id");
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateUpdate(modelList, true, "id");
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateInsert(modelList, false);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateInsert(modelList, true);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateSelect("", modelList, false);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateSelect("", modelList, true);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateSelect("ts", modelList, false);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateSelect("ts", modelList, true);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateModel(modelList);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateRowCallbackHandler(null, modelList);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateRowCallbackHandler("ts", modelList);
    System.out.println(str + "\n\n");

    str = CodeGenerator.generateRowMapper(null, modelList);
    System.out.println(str);

    str = CodeGenerator.generateRowMapper("ts", modelList);
    System.out.println(str);
  }
}
