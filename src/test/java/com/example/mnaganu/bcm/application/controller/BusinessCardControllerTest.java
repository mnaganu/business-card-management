package com.example.mnaganu.bcm.application.controller;

import com.example.mnaganu.bcm.application.model.request.BusinessCardInsertRequest;
import com.example.mnaganu.bcm.application.model.request.BusinessCardUpdateRequest;
import com.example.mnaganu.bcm.common.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BusinessCardControllerTest {
  @Value(value = "${local.server.port}")
  private int port;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public BusinessCardControllerTest(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @BeforeEach
  public void initDB() {
    createTable(jdbcTemplate);
    createData(jdbcTemplate);
  }


  @Test
  void getTest_businessCard() {

    //パラメータ指定なし
    //offset が 0 limit が 5 で検索されること
    String endpoint = "api/v1/business-card";
    String responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":0,\"limit\":5,\"list\":[" +
            "{\"id\":1,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"総務部\",\"post\":\"部長\",\"name\":\"テスト 太郎\",\"postalCode\":\"000-0001\",\"address\":\"東京都〇〇区×× 9-99-001 △△ビル 8F\",\"phoneNumber\":\"03-0000-0001\",\"fax\":\"03-9999-0001\",\"note\":\"メモ1\",\"createTime\":\"2023-04-01T01:00:00.000+00:00\",\"updateTime\":\"2023-04-01T02:00:00.000+00:00\"}," +
            "{\"id\":3,\"companyName\":null,\"depaertmentName\":null,\"post\":null,\"name\":\"テスト 三郎\",\"postalCode\":null,\"address\":null,\"phoneNumber\":null,\"fax\":null,\"note\":null,\"createTime\":null,\"updateTime\":null}," +
            "{\"id\":4,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"管理部\",\"post\":\"課長\",\"name\":\"テスト 四郎\",\"postalCode\":\"000-0004\",\"address\":\"東京都〇〇区×× 9-99-004 △△ビル 8F\",\"phoneNumber\":\"03-0000-0004\",\"fax\":\"03-9999-0004\",\"note\":\"メモ4\",\"createTime\":\"2023-04-04T01:00:00.000+00:00\",\"updateTime\":\"2023-04-04T02:00:00.000+00:00\"}" +
            "]}"
    );

    //offset と limit の指定あり
    //3件中先頭の1件目が取得されること
    HashMap<String, String> queryParams = new HashMap<String, String>();
    queryParams.put("offset", "0");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":0,\"limit\":1,\"list\":[" +
            "{\"id\":1,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"総務部\",\"post\":\"部長\",\"name\":\"テスト 太郎\",\"postalCode\":\"000-0001\",\"address\":\"東京都〇〇区×× 9-99-001 △△ビル 8F\",\"phoneNumber\":\"03-0000-0001\",\"fax\":\"03-9999-0001\",\"note\":\"メモ1\",\"createTime\":\"2023-04-01T01:00:00.000+00:00\",\"updateTime\":\"2023-04-01T02:00:00.000+00:00\"}" +
            "]}"
    );

    //offset と limit の指定あり
    //3件中先頭から2件目が取得されること
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "1");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":1,\"limit\":1,\"list\":[" +
            "{\"id\":3,\"companyName\":null,\"depaertmentName\":null,\"post\":null,\"name\":\"テスト 三郎\",\"postalCode\":null,\"address\":null,\"phoneNumber\":null,\"fax\":null,\"note\":null,\"createTime\":null,\"updateTime\":null}" +
            "]}"
    );

    //offset と limit の指定あり
    //3件中先頭から3件目が取得されること
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "2");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":2,\"limit\":1,\"list\":[" +
            "{\"id\":4,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"管理部\",\"post\":\"課長\",\"name\":\"テスト 四郎\",\"postalCode\":\"000-0004\",\"address\":\"東京都〇〇区×× 9-99-004 △△ビル 8F\",\"phoneNumber\":\"03-0000-0004\",\"fax\":\"03-9999-0004\",\"note\":\"メモ4\",\"createTime\":\"2023-04-04T01:00:00.000+00:00\",\"updateTime\":\"2023-04-04T02:00:00.000+00:00\"}" +
            "]}"
    );

    //offset と limit の指定あり
    //検索結果 0件
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "3");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":3,\"limit\":1,\"list\":[]}"
    );

    //offset と limit の指定あり
    //offset が 0よりも小さい
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "-3");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1001\",\"message\":\"offset は0以上の数字で指定してください。\"}"
    );

    //offset と limit の指定あり
    //limit が 1よりも小さい
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "0");
    queryParams.put("limit", "0");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":0,\"offset\":0,\"limit\":0,\"list\":[]}"
    );

    //offset と limit の指定あり
    //offset が 0よりも小さい　
    //limit が 1よりも小さい
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "-1");
    queryParams.put("limit", "0");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1001\",\"message\":\"offset は0以上の数字で指定してください。\"}"
    );

    //offset と limit の指定あり
    //offset が 文字列
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "A");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1001\",\"message\":\"offset は0以上の数字で指定してください。\"}"
    );

    //offset と limit の指定あり
    //limit が 文字列
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "0");
    queryParams.put("limit", "B");
    endpoint = "api/v1/business-card";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1002\",\"message\":\"limit は1以上の数字で指定してください。\"}"
    );

    //サポートされていないパス
    endpoint = "api/v1/business-card/none";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:500 {\"status\":\"NG\",\"code\":\"E1005\",\"message\":\"Request method 'GET' is not supported\"}"
    );

    //存在しないエンドポイント
    endpoint = "api/v1/business";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:404 {\"status\":\"NG\",\"code\":\"E1006\",\"message\":\"URL not found [/api/v1/business]\"}"
    );

  }

  @Test
  void getTest_businessCard_Name() {

    //パラメータ指定なし
    //offset が 0 limit が 5 で検索されること
    String endpoint = "api/v1/business-card/name/テスト";
    String responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":0,\"limit\":5,\"list\":[" +
            "{\"id\":1,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"総務部\",\"post\":\"部長\",\"name\":\"テスト 太郎\",\"postalCode\":\"000-0001\",\"address\":\"東京都〇〇区×× 9-99-001 △△ビル 8F\",\"phoneNumber\":\"03-0000-0001\",\"fax\":\"03-9999-0001\",\"note\":\"メモ1\",\"createTime\":\"2023-04-01T01:00:00.000+00:00\",\"updateTime\":\"2023-04-01T02:00:00.000+00:00\"}," +
            "{\"id\":3,\"companyName\":null,\"depaertmentName\":null,\"post\":null,\"name\":\"テスト 三郎\",\"postalCode\":null,\"address\":null,\"phoneNumber\":null,\"fax\":null,\"note\":null,\"createTime\":null,\"updateTime\":null}," +
            "{\"id\":4,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"管理部\",\"post\":\"課長\",\"name\":\"テスト 四郎\",\"postalCode\":\"000-0004\",\"address\":\"東京都〇〇区×× 9-99-004 △△ビル 8F\",\"phoneNumber\":\"03-0000-0004\",\"fax\":\"03-9999-0004\",\"note\":\"メモ4\",\"createTime\":\"2023-04-04T01:00:00.000+00:00\",\"updateTime\":\"2023-04-04T02:00:00.000+00:00\"}" +
            "]}"
    );

    //offset と limit の指定あり
    //3件中先頭の1件目が取得されること
    HashMap<String, String> queryParams = new HashMap<String, String>();
    queryParams.put("offset", "0");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":0,\"limit\":1,\"list\":[" +
            "{\"id\":1,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"総務部\",\"post\":\"部長\",\"name\":\"テスト 太郎\",\"postalCode\":\"000-0001\",\"address\":\"東京都〇〇区×× 9-99-001 △△ビル 8F\",\"phoneNumber\":\"03-0000-0001\",\"fax\":\"03-9999-0001\",\"note\":\"メモ1\",\"createTime\":\"2023-04-01T01:00:00.000+00:00\",\"updateTime\":\"2023-04-01T02:00:00.000+00:00\"}" +
            "]}"
    );

    //offset と limit の指定あり
    //3件中先頭から2件目が取得されること
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "1");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":1,\"limit\":1,\"list\":[" +
            "{\"id\":3,\"companyName\":null,\"depaertmentName\":null,\"post\":null,\"name\":\"テスト 三郎\",\"postalCode\":null,\"address\":null,\"phoneNumber\":null,\"fax\":null,\"note\":null,\"createTime\":null,\"updateTime\":null}" +
            "]}"
    );

    //offset と limit の指定あり
    //3件中先頭から3件目が取得されること
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "2");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":2,\"limit\":1,\"list\":[" +
            "{\"id\":4,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"管理部\",\"post\":\"課長\",\"name\":\"テスト 四郎\",\"postalCode\":\"000-0004\",\"address\":\"東京都〇〇区×× 9-99-004 △△ビル 8F\",\"phoneNumber\":\"03-0000-0004\",\"fax\":\"03-9999-0004\",\"note\":\"メモ4\",\"createTime\":\"2023-04-04T01:00:00.000+00:00\",\"updateTime\":\"2023-04-04T02:00:00.000+00:00\"}" +
            "]}"
    );

    //offset と limit の指定あり
    //検索結果 0件
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "3");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":3,\"offset\":3,\"limit\":1,\"list\":[]}"
    );

    //offset と limit の指定あり
    //offset が 0よりも小さい
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "-3");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1001\",\"message\":\"offset は0以上の数字で指定してください。\"}"
    );

    //offset と limit の指定あり
    //limit が 1よりも小さい
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "0");
    queryParams.put("limit", "0");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":0,\"offset\":0,\"limit\":0,\"list\":[]}"
    );

    //offset と limit の指定あり
    //offset が 0よりも小さい　
    //limit が 1よりも小さい
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "-1");
    queryParams.put("limit", "0");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1001\",\"message\":\"offset は0以上の数字で指定してください。\"}"
    );

    //offset と limit の指定あり
    //offset が 文字列
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "A");
    queryParams.put("limit", "1");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1001\",\"message\":\"offset は0以上の数字で指定してください。\"}"
    );

    //offset と limit の指定あり
    //limit が 文字列
    queryParams = new HashMap<String, String>();
    queryParams.put("offset", "0");
    queryParams.put("limit", "B");
    endpoint = "api/v1/business-card/name/テスト";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1002\",\"message\":\"limit は1以上の数字で指定してください。\"}"
    );

    //サポートされていないパス
    endpoint = "api/v1/business-card/name/テスト/none";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:404 {\"status\":\"NG\",\"code\":\"E1006\",\"message\":\"URL not found [/api/v1/business-card/name/テスト/none]\"}"
    );

    //存在しないエンドポイント
    endpoint = "api/v1/business";
    responce = get(endpoint, queryParams);
    assertThat(responce).isEqualTo(
        "status:404 {\"status\":\"NG\",\"code\":\"E1006\",\"message\":\"URL not found [/api/v1/business]\"}"
    );
  }

  @Test
  void getTest_businessCard_Id() {

    //検索結果あり
    String endpoint = "api/v1/business-card/id/1";
    String responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":1,\"offset\":0,\"limit\":1,\"list\":[" +
            "{\"id\":1,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"総務部\",\"post\":\"部長\",\"name\":\"テスト 太郎\",\"postalCode\":\"000-0001\",\"address\":\"東京都〇〇区×× 9-99-001 △△ビル 8F\",\"phoneNumber\":\"03-0000-0001\",\"fax\":\"03-9999-0001\",\"note\":\"メモ1\",\"createTime\":\"2023-04-01T01:00:00.000+00:00\",\"updateTime\":\"2023-04-01T02:00:00.000+00:00\"}" +
            "]}"
    );

    //検索結果なし
    endpoint = "api/v1/business-card/id/100";
    responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":0,\"offset\":0,\"limit\":0,\"list\":[]}"
    );

    //id が -1
    endpoint = "api/v1/business-card/id/-1";
    responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1003\",\"message\":\"BusinessCardModel ID は1以上の数字で指定してください。\"}"
    );

    //id が 文字列
    endpoint = "api/v1/business-card/id/テスト";
    responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1003\",\"message\":\"BusinessCardModel ID は1以上の数字で指定してください。\"}"
    );

    //サポートされていないパス
    endpoint = "api/v1/business-card/name/id/1/none";
    responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:404 {\"status\":\"NG\",\"code\":\"E1006\",\"message\":\"URL not found [/api/v1/business-card/name/id/1/none]\"}"
    );

    //存在しないエンドポイント
    endpoint = "api/v1/business";
    responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:404 {\"status\":\"NG\",\"code\":\"E1006\",\"message\":\"URL not found [/api/v1/business]\"}"
    );
  }

  @Test
  void insertTest() {
    //登録できること
    BusinessCardInsertRequest request = new BusinessCardInsertRequest();
    request.setCompanyName("テスト株式会社");
    request.setDepaertmentName("営業部");
    request.setPost("部長");
    request.setName("テスト 五郎");
    request.setPostalCode("000-002");
    request.setAddress("埼玉県〇〇市×× 9-99-001 △△ビル 3F");
    request.setPhoneNumber("080-0000-0001");
    request.setFax("03-9999-9999");
    request.setNote("メモ5");

    String endpoint = "api/v1/business-card";
    String responce = post(endpoint, toJson(request));
    assertThat(responce).isEqualTo(
        "status:200 {\"status\":\"OK\",\"message\":\"登録完了\"}"
    );

    endpoint = "api/v1/business-card/name/五郎";
    responce = get(endpoint, null);
    System.out.println(responce);
    //createTime が実行時の日時が入るので、データ部分が含まれているかどうかで判断しております。
    assertThat(responce).contains(
        "status:200 {\"total\":1,\"offset\":0,\"limit\":5,\"list\":[" +
            "{\"id\":5,\"companyName\":\"テスト株式会社\",\"depaertmentName\":\"営業部\",\"post\":\"部長\",\"name\":\"テスト 五郎\",\"postalCode\":\"000-002\",\"address\":\"埼玉県〇〇市×× 9-99-001 △△ビル 3F\",\"phoneNumber\":\"080-0000-0001\",\"fax\":\"03-9999-9999\",\"note\":\"メモ5\""
    );

  }

  @Test
  void updateTest() {
    //存在しない ID を指定した場合
    //データは更新できないこと
    BusinessCardUpdateRequest request = new BusinessCardUpdateRequest();
    request.setId(99);
    request.setCompanyName("試験株式会社");
    request.setDepaertmentName("開発部");
    request.setPost("主任");
    request.setName("試験 太郎");
    request.setPostalCode("000-003");
    request.setAddress("東京都〇〇市×× 9-99-001 △△ビル 1F");
    request.setPhoneNumber("080-0000-0002");
    request.setFax("03-9999-8001");
    request.setNote("メモ6");

    String endpoint = "api/v1/business-card";
    String responce = put(endpoint, toJson(request));
    assertThat(responce).isEqualTo(
        "status:200 {\"status\":\"NG\",\"message\":\"登録に失敗しました。\"}"
    );

    endpoint = "api/v1/business-card/name/試験";
    responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:200 {\"total\":0,\"offset\":0,\"limit\":5,\"list\":[]}"
    );

    //既存 ID を指定した場合
    //データ更新されること
    request = new BusinessCardUpdateRequest();
    request.setId(1);
    request.setCompanyName("試験株式会社");
    request.setDepaertmentName("開発部");
    request.setPost("主任");
    request.setName("試験 太郎");
    request.setPostalCode("000-003");
    request.setAddress("東京都〇〇市×× 9-99-001 △△ビル 1F");
    request.setPhoneNumber("080-0000-0002");
    request.setFax("03-9999-8001");
    request.setNote("メモ6");

    endpoint = "api/v1/business-card";
    responce = put(endpoint, toJson(request));
    assertThat(responce).isEqualTo(
        "status:200 {\"status\":\"OK\",\"message\":\"登録完了\"}"
    );

    endpoint = "api/v1/business-card/name/試験";
    responce = get(endpoint, null);
    System.out.println(responce);
    //updateTime が実行時の日時が入るので、データ部分が含まれているかどうかで判断しております。
    assertThat(responce).contains(
        "status:200 {\"total\":1,\"offset\":0,\"limit\":5,\"list\":[" +
            "{\"id\":1,\"companyName\":\"試験株式会社\",\"depaertmentName\":\"開発部\",\"post\":\"主任\",\"name\":\"試験 太郎\",\"postalCode\":\"000-003\",\"address\":\"東京都〇〇市×× 9-99-001 △△ビル 1F\",\"phoneNumber\":\"080-0000-0002\",\"fax\":\"03-9999-8001\",\"note\":\"メモ6\",\"createTime\":\"2023-04-01T01:00:00.000+00:00\""
    );
  }

  @Test
  void deleteTest() {
    //未登録の場合、削除できないこと
    String endpoint = "api/v1/business-card/100";
    String responce = delete(endpoint);
    assertThat(responce).isEqualTo(
        "status:200 {\"status\":\"NG\",\"message\":\"削除に失敗しました。\"}"
    );

    //登録済みの場合、削除できること
    endpoint = "api/v1/business-card/1";
    responce = delete(endpoint);
    assertThat(responce).isEqualTo(
        "status:200 {\"status\":\"OK\",\"message\":\"削除完了\"}"
    );

    //id が -1
    endpoint = "api/v1/business-card/-1";
    responce = delete(endpoint);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1003\",\"message\":\"BusinessCardModel ID は1以上の数字で指定してください。\"}"
    );

    //id が 文字列
    endpoint = "api/v1/business-card/テスト";
    responce = delete(endpoint);
    assertThat(responce).isEqualTo(
        "status:400 {\"status\":\"NG\",\"code\":\"E1003\",\"message\":\"BusinessCardModel ID は1以上の数字で指定してください。\"}"
    );

    //サポートされていないパス
    endpoint = "api/v1/business-card/1/none";
    responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:404 {\"status\":\"NG\",\"code\":\"E1006\",\"message\":\"URL not found [/api/v1/business-card/1/none]\"}"
    );

    //存在しないエンドポイント
    endpoint = "api/v1/business";
    responce = get(endpoint, null);
    assertThat(responce).isEqualTo(
        "status:404 {\"status\":\"NG\",\"code\":\"E1006\",\"message\":\"URL not found [/api/v1/business]\"}"
    );

  }

  private String get(String path, HashMap<String, String> queryParams) {
    StringBuilder sb = new StringBuilder();

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromPath(path)
        .host("localhost")
        .port(port);

    URL url;
    try {
      if (queryParams == null) {
        url = new URL("http:" + builder.toUriString());
      } else {
        for (String key : queryParams.keySet()) {
          builder.queryParam(key, queryParams.get(key));
        }
        url = new URL("http:" + builder.toUriString());
      }

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      //HTTPのメソッドをGETに設定
      conn.setRequestMethod("GET");
      //リクエストボディへの書き込みを許可
      conn.setDoInput(true);
      //レスポンスボディの取得を許可
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("charset", "UTF-8");

      conn.connect();

      sb.append("status:");
      sb.append(conn.getResponseCode());
      sb.append(" ");

      if (conn.getResponseCode() == 200) {
        //HttpURLConnection から InputStream を取得し、読み出す
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        }
      } else {
        //Error の場合は HttpURLConnection から ErrorStream を取得し、読み出す
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        }
      }

    } catch (Exception e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }

    return sb.toString();
  }

  private String post(String path, String json) {
    StringBuilder sb = new StringBuilder();

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromPath(path)
        .host("localhost")
        .port(port);

    try {
      URL url = new URL("http:" + builder.toUriString());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      //HTTPのメソッドをPOSTに設定
      conn.setRequestMethod("POST");
      //リクエストボディへの書き込みを許可
      conn.setDoInput(true);
      //レスポンスボディの取得を許可
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("charset", "UTF-8");
      conn.setRequestProperty("Content-Length", Integer.toString(json.length()));

      conn.connect();

      //HttpURLConnectionからOutputStreamを取得し、json文字列を書き込む
      try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"))) {
        writer.write(json);
      }

      sb.append("status:");
      sb.append(conn.getResponseCode());
      sb.append(" ");

      if (conn.getResponseCode() == 200) {
        //HttpURLConnection から InputStream を取得し、読み出す
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        }
      } else {
        //Error の場合は HttpURLConnection から ErrorStream を取得し、読み出す
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        }
      }

    } catch (Exception e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }

    return sb.toString();
  }

  private String put(String path, String json) {
    StringBuilder sb = new StringBuilder();

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromPath(path)
        .host("localhost")
        .port(port);

    try {
      URL url = new URL("http:" + builder.toUriString());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      //HTTPのメソッドをPUTに設定
      conn.setRequestMethod("PUT");
      //リクエストボディへの書き込みを許可
      conn.setDoInput(true);
      //レスポンスボディの取得を許可
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("charset", "UTF-8");
      conn.setRequestProperty("Content-Length", Integer.toString(json.length()));

      conn.connect();

      //HttpURLConnectionからOutputStreamを取得し、json文字列を書き込む
      try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"))) {
        writer.write(json);
      }

      sb.append("status:");
      sb.append(conn.getResponseCode());
      sb.append(" ");

      if (conn.getResponseCode() == 200) {
        //HttpURLConnection から InputStream を取得し、読み出す
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        }
      } else {
        //Error の場合は HttpURLConnection から ErrorStream を取得し、読み出す
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        }
      }

    } catch (Exception e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }

    return sb.toString();
  }

  private String delete(String path) {
    StringBuilder sb = new StringBuilder();

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromPath(path)
        .host("localhost")
        .port(port);

    try {
      URL url = new URL("http:" + builder.toUriString());

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      //HTTP のメソッドを DELETE に設定
      conn.setRequestMethod("DELETE");
      //リクエストボディへの書き込みを許可
      conn.setDoInput(true);
      //レスポンスボディの取得を許可
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("charset", "UTF-8");

      conn.connect();

      sb.append("status:");
      sb.append(conn.getResponseCode());
      sb.append(" ");

      if (conn.getResponseCode() == 200) {
        //HttpURLConnection から InputStream を取得し、読み出す
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        }
      } else {
        //Error の場合は HttpURLConnection から ErrorStream を取得し、読み出す
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        }
      }

    } catch (Exception e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }

    return sb.toString();
  }

  private String toJson(BusinessCardInsertRequest insertRequest) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(insertRequest);
    } catch (JsonProcessingException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
    return null;
  }

  private String toJson(BusinessCardUpdateRequest updateRequest) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(updateRequest);
    } catch (JsonProcessingException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
    return null;
  }

  private static void createTable(JdbcTemplate jdbcTemplate) {
    String sql = "DROP TABLE IF EXISTS business_card";
    jdbcTemplate.execute(sql);

    sql = "CREATE TABLE `business_card` (\n" +
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
    jdbcTemplate.execute(sql);
  }

  private static void createData(JdbcTemplate jdbcTemplate) {
    //DBへの登録順に ID が振られる。
    //ID 1 のテストデータ
    //全ての項目に値が入っているデータ（削除状態でない）
    Map<String, String> map = new HashMap<>();
    map.put("company_name", "'テスト株式会社'");
    map.put("depaertment_name", "'総務部'");
    map.put("post", "'部長'");
    map.put("name", "'テスト 太郎'");
    map.put("postal_code", "'000-0001'");
    map.put("address", "'東京都〇〇区×× 9-99-001 △△ビル 8F'");
    map.put("phone_number", "'03-0000-0001'");
    map.put("fax", "'03-9999-0001'");
    map.put("note", "'メモ1'");
    map.put("create_time", "'2023-04-01 10:00:00'");
    map.put("update_time", "'2023-04-01 11:00:00'");
    map.put("delete_time", "null");

    jdbcTemplate.execute(CommonUtil.createInsertSql("business_card", map).get());

    //ID 2 のデータ
    //全ての項目に値が入っているデータ（削除状態）
    map = new HashMap<>();
    map.put("company_name", "'テスト株式会社'");
    map.put("depaertment_name", "'経理部'");
    map.put("post", "'部長補佐'");
    map.put("name", "'テスト 次郎'");
    map.put("postal_code", "'000-0002'");
    map.put("address", "'東京都〇〇区×× 9-99-002 △△ビル 8F'");
    map.put("phone_number", "'03-0000-0002'");
    map.put("fax", "'03-9999-0002'");
    map.put("note", "'メモ2'");
    map.put("create_time", "'2023-04-02 10:00:00'");
    map.put("update_time", "'2023-04-02 11:00:00'");
    map.put("delete_time", "'2023-04-02 12:00:00'");

    jdbcTemplate.execute(CommonUtil.createInsertSql("business_card", map).get());

    //ID 3 のデータ
    //not null の項目だけ値が入っているデータ
    map = new HashMap<>();
    map.put("company_name", "null");
    map.put("depaertment_name", "null");
    map.put("post", "null");
    map.put("name", "'テスト 三郎'");
    map.put("postal_code", "null");
    map.put("address", "null");
    map.put("phone_number", "null");
    map.put("fax", "null");
    map.put("note", "null");
    map.put("create_time", "null");
    map.put("update_time", "null");
    map.put("delete_time", "null");

    jdbcTemplate.execute(CommonUtil.createInsertSql("business_card", map).get());

    //ID 4 のテストデータ
    //全ての項目に値が入っているデータ（削除状態でない）
    map = new HashMap<>();
    map.put("company_name", "'テスト株式会社'");
    map.put("depaertment_name", "'管理部'");
    map.put("post", "'課長'");
    map.put("name", "'テスト 四郎'");
    map.put("postal_code", "'000-0004'");
    map.put("address", "'東京都〇〇区×× 9-99-004 △△ビル 8F'");
    map.put("phone_number", "'03-0000-0004'");
    map.put("fax", "'03-9999-0004'");
    map.put("note", "'メモ4'");
    map.put("create_time", "'2023-04-04 10:00:00'");
    map.put("update_time", "'2023-04-04 11:00:00'");
    map.put("delete_time", "null");

    jdbcTemplate.execute(CommonUtil.createInsertSql("business_card", map).get());
  }

}
