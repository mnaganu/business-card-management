package com.example.mnaganu.bcm.infrastructure.repository;

import com.example.mnaganu.bcm.common.CommonUtil;
import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import com.example.mnaganu.bcm.domain.model.SelectModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BusinessCardRepositoryImplTest {

  private final BusinessCardRepositoryImpl businessCardRepositoryImpl;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public BusinessCardRepositoryImplTest(BusinessCardRepositoryImpl businessCardRepositoryImpl, JdbcTemplate jdbcTemplate) {
    this.businessCardRepositoryImpl = businessCardRepositoryImpl;
    this.jdbcTemplate = jdbcTemplate;
  }

  @BeforeEach
  public void initDB() {
    createTable(jdbcTemplate);
    createData(jdbcTemplate);
  }

  @Test
  void selectAllById_検索結果0件() {
    //削除状態のデータが取れないこと
    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectAllById(99);
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  void selectAllById_検索結果1件() {

    //全ての項目に値が入っているデータ
    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectAllById(1);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(1);
    assertThat(actual.get().getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.get().getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.get().getPost().get()).isEqualTo("部長");
    assertThat(actual.get().getName()).isEqualTo("テスト 太郎");
    assertThat(actual.get().getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.get().getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.get().getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.get().getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.get().getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.get().getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.get().getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.get().getDeleteTime().isEmpty()).isTrue();

    //削除状態のデータ
    actual = businessCardRepositoryImpl.selectAllById(2);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(2);
    assertThat(actual.get().getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.get().getDepaertmentName().get()).isEqualTo("経理部");
    assertThat(actual.get().getPost().get()).isEqualTo("部長補佐");
    assertThat(actual.get().getName()).isEqualTo("テスト 次郎");
    assertThat(actual.get().getPostalCode().get()).isEqualTo("000-0002");
    assertThat(actual.get().getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-002 △△ビル 8F");
    assertThat(actual.get().getPhoneNumber().get()).isEqualTo("03-0000-0002");
    assertThat(actual.get().getFax().get()).isEqualTo("03-9999-0002");
    assertThat(actual.get().getNote().get()).isEqualTo("メモ2");
    assertThat(CommonUtil.timestampToString(actual.get().getCreateTime().get()).get()).isEqualTo("2023-04-02 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.get().getUpdateTime().get()).get()).isEqualTo("2023-04-02 11:00:00");
    assertThat(CommonUtil.timestampToString(actual.get().getDeleteTime().get()).get()).isEqualTo("2023-04-02 12:00:00");

    //not null だけに値が入っているデータ
    actual = businessCardRepositoryImpl.selectAllById(3);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(3);
    assertThat(actual.get().getCompanyName().isEmpty()).isTrue();
    assertThat(actual.get().getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.get().getPost().isEmpty()).isTrue();
    assertThat(actual.get().getName()).isEqualTo("テスト 三郎");
    assertThat(actual.get().getPostalCode().isEmpty()).isTrue();
    assertThat(actual.get().getAddress().isEmpty()).isTrue();
    assertThat(actual.get().getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.get().getFax().isEmpty()).isTrue();
    assertThat(actual.get().getNote().isEmpty()).isTrue();
    assertThat(actual.get().getCreateTime().isEmpty()).isTrue();
    assertThat(actual.get().getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.get().getDeleteTime().isEmpty()).isTrue();
  }

  @Test
  void selectAll_検索結果0件() {
    //offset が 0よりも小さい
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.selectAll(-1, 1);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //limit が 1よりも小さい
    selectModel = businessCardRepositoryImpl.select(0, 0);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //offset が 0よりも小さい　
    //limit が 1よりも小さい
    selectModel = businessCardRepositoryImpl.select(-1, 0);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //テーブルを作り直してデータを全て消す。
    createTable(jdbcTemplate);
    selectModel = businessCardRepositoryImpl.select(1, 10);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(1);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(0);
  }

  @Test
  void selectAll_検索結果1件() {
    //先頭の1件
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.selectAll(0, 1);
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(1);
    assertThat(selectModel.getTotal()).isEqualTo(4);

    List<BusinessCardModel> list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    BusinessCardModel actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.getPost().get()).isEqualTo("部長");
    assertThat(actual.getName()).isEqualTo("テスト 太郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    //中間の1件
    selectModel = businessCardRepositoryImpl.selectAll(1, 1);
    assertThat(selectModel.getOffset()).isEqualTo(1);
    assertThat(selectModel.getLimit()).isEqualTo(1);
    assertThat(selectModel.getTotal()).isEqualTo(4);

    list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(2);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("経理部");
    assertThat(actual.getPost().get()).isEqualTo("部長補佐");
    assertThat(actual.getName()).isEqualTo("テスト 次郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0002");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-002 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0002");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0002");
    assertThat(actual.getNote().get()).isEqualTo("メモ2");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-02 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-02 11:00:00");
    assertThat(CommonUtil.timestampToString(actual.getDeleteTime().get()).get()).isEqualTo("2023-04-02 12:00:00");

    //最後の1件
    selectModel = businessCardRepositoryImpl.selectAll(3, 10);
    assertThat(selectModel.getOffset()).isEqualTo(3);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(4);

    list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(4);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("管理部");
    assertThat(actual.getPost().get()).isEqualTo("課長");
    assertThat(actual.getName()).isEqualTo("テスト 四郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0004");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-004 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0004");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0004");
    assertThat(actual.getNote().get()).isEqualTo("メモ4");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-04 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-04 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();
  }

  @Test
  void selectAll_検索結果が複数件() {
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.selectAll(0, 10);
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(4);

    List<BusinessCardModel> list = selectModel.getList();
    assertThat(list.size()).isEqualTo(4);

    BusinessCardModel actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.getPost().get()).isEqualTo("部長");
    assertThat(actual.getName()).isEqualTo("テスト 太郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    actual = list.get(1);
    assertThat(actual.getId()).isEqualTo(2);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("経理部");
    assertThat(actual.getPost().get()).isEqualTo("部長補佐");
    assertThat(actual.getName()).isEqualTo("テスト 次郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0002");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-002 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0002");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0002");
    assertThat(actual.getNote().get()).isEqualTo("メモ2");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-02 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-02 11:00:00");
    assertThat(CommonUtil.timestampToString(actual.getDeleteTime().get()).get()).isEqualTo("2023-04-02 12:00:00");

    actual = list.get(2);
    assertThat(actual.getId()).isEqualTo(3);
    assertThat(actual.getCompanyName().isEmpty()).isTrue();
    assertThat(actual.getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.getPost().isEmpty()).isTrue();
    assertThat(actual.getName()).isEqualTo("テスト 三郎");
    assertThat(actual.getPostalCode().isEmpty()).isTrue();
    assertThat(actual.getAddress().isEmpty()).isTrue();
    assertThat(actual.getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.getFax().isEmpty()).isTrue();
    assertThat(actual.getNote().isEmpty()).isTrue();
    assertThat(actual.getCreateTime().isEmpty()).isTrue();
    assertThat(actual.getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    actual = list.get(3);
    assertThat(actual.getId()).isEqualTo(4);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("管理部");
    assertThat(actual.getPost().get()).isEqualTo("課長");
    assertThat(actual.getName()).isEqualTo("テスト 四郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0004");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-004 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0004");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0004");
    assertThat(actual.getNote().get()).isEqualTo("メモ4");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-04 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-04 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();
  }

  @Test
  void selectById_検索結果0件() {
    //削除状態のデータが取れないこと
    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectById(2);
    assertThat(actual.isEmpty()).isTrue();
    //存在しないID
    actual = businessCardRepositoryImpl.selectById(99);
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  void selectById_検索結果1件() {

    //全ての項目に値が入っているデータ
    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectById(1);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(1);
    assertThat(actual.get().getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.get().getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.get().getPost().get()).isEqualTo("部長");
    assertThat(actual.get().getName()).isEqualTo("テスト 太郎");
    assertThat(actual.get().getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.get().getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.get().getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.get().getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.get().getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.get().getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.get().getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.get().getDeleteTime().isEmpty()).isTrue();

    //not null だけに値が入っているデータ
    actual = businessCardRepositoryImpl.selectById(3);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(3);
    assertThat(actual.get().getCompanyName().isEmpty()).isTrue();
    assertThat(actual.get().getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.get().getPost().isEmpty()).isTrue();
    assertThat(actual.get().getName()).isEqualTo("テスト 三郎");
    assertThat(actual.get().getPostalCode().isEmpty()).isTrue();
    assertThat(actual.get().getAddress().isEmpty()).isTrue();
    assertThat(actual.get().getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.get().getFax().isEmpty()).isTrue();
    assertThat(actual.get().getNote().isEmpty()).isTrue();
    assertThat(actual.get().getCreateTime().isEmpty()).isTrue();
    assertThat(actual.get().getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.get().getDeleteTime().isEmpty()).isTrue();
  }

  @Test
  void select_検索結果0件() {
    //offset が 0よりも小さい
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.select(-1, 1);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //limit が 1よりも小さい
    selectModel = businessCardRepositoryImpl.select(0, 0);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //offset が 0よりも小さい　
    //limit が 1よりも小さい
    selectModel = businessCardRepositoryImpl.select(-1, 0);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //テーブルを作り直してデータを全て消す。
    createTable(jdbcTemplate);
    selectModel = businessCardRepositoryImpl.select(1, 10);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(1);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(0);
  }

  @Test
  void select_検索結果1件() {
    //先頭の1件
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.select(0, 1);
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(1);
    assertThat(selectModel.getTotal()).isEqualTo(3);

    List<BusinessCardModel> list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    BusinessCardModel actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.getPost().get()).isEqualTo("部長");
    assertThat(actual.getName()).isEqualTo("テスト 太郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    //中間の1件
    selectModel = businessCardRepositoryImpl.select(1, 1);
    assertThat(selectModel.getOffset()).isEqualTo(1);
    assertThat(selectModel.getLimit()).isEqualTo(1);
    assertThat(selectModel.getTotal()).isEqualTo(3);

    list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(3);
    assertThat(actual.getCompanyName().isEmpty()).isTrue();
    assertThat(actual.getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.getPost().isEmpty()).isTrue();
    assertThat(actual.getName()).isEqualTo("テスト 三郎");
    assertThat(actual.getPostalCode().isEmpty()).isTrue();
    assertThat(actual.getAddress().isEmpty()).isTrue();
    assertThat(actual.getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.getFax().isEmpty()).isTrue();
    assertThat(actual.getNote().isEmpty()).isTrue();
    assertThat(actual.getCreateTime().isEmpty()).isTrue();
    assertThat(actual.getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    //最後の1件
    selectModel = businessCardRepositoryImpl.select(2, 10);
    assertThat(selectModel.getOffset()).isEqualTo(2);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(3);

    list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(4);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("管理部");
    assertThat(actual.getPost().get()).isEqualTo("課長");
    assertThat(actual.getName()).isEqualTo("テスト 四郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0004");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-004 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0004");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0004");
    assertThat(actual.getNote().get()).isEqualTo("メモ4");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-04 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-04 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();
  }

  @Test
  void select_検索結果が複数件() {
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.select(0, 10);
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(3);

    List<BusinessCardModel> list = selectModel.getList();
    assertThat(list.size()).isEqualTo(3);

    BusinessCardModel actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.getPost().get()).isEqualTo("部長");
    assertThat(actual.getName()).isEqualTo("テスト 太郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    actual = list.get(1);
    assertThat(actual.getId()).isEqualTo(3);
    assertThat(actual.getCompanyName().isEmpty()).isTrue();
    assertThat(actual.getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.getPost().isEmpty()).isTrue();
    assertThat(actual.getName()).isEqualTo("テスト 三郎");
    assertThat(actual.getPostalCode().isEmpty()).isTrue();
    assertThat(actual.getAddress().isEmpty()).isTrue();
    assertThat(actual.getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.getFax().isEmpty()).isTrue();
    assertThat(actual.getNote().isEmpty()).isTrue();
    assertThat(actual.getCreateTime().isEmpty()).isTrue();
    assertThat(actual.getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    actual = list.get(2);
    assertThat(actual.getId()).isEqualTo(4);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("管理部");
    assertThat(actual.getPost().get()).isEqualTo("課長");
    assertThat(actual.getName()).isEqualTo("テスト 四郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0004");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-004 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0004");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0004");
    assertThat(actual.getNote().get()).isEqualTo("メモ4");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-04 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-04 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();
  }

  @Test
  void insert_引数がnull() {
    int cnt = businessCardRepositoryImpl.insert(null);
    assertThat(cnt).isEqualTo(0);
  }

  @Test
  void insert_データ登録() {
    //現在の時刻取得
    Timestamp now = new Timestamp(System.currentTimeMillis());
    //1秒待つ
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    //テーブルを作り直してデータを全て消す。
    createTable(jdbcTemplate);

    //データ作成
    BusinessCardModel model = BusinessCardModel.builder()
        .id(-1) //not null 項目なのでダミーで-1を入れておく
        .companyName("〇〇株式会社")
        .depaertmentName("営業部")
        .post("主任")
        .name("テスト 五郎")
        .postalCode("000-0005")
        .address("東京都〇〇区×× 9-99-005 △△ビル 8F")
        .phoneNumber("03-0000-0005")
        .fax("03-9999-0005")
        .note("メモ5")
        .createTime(CommonUtil.stringToTimestamp("2023-04-05 10:00:00").get()) //登録した日時に置き換わる
        .updateTime(CommonUtil.stringToTimestamp("2023-04-05 11:00:00").get()) //null に置き換わる
        .deleteTime(CommonUtil.stringToTimestamp("2023-04-05 12:00:00").get()) //null に置き換わる
        .build();

    //登録
    int cnt = businessCardRepositoryImpl.insert(model);
    assertThat(cnt).isEqualTo(1);

    //テーブルを作り直したので ID は 1
    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectById(1);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(1);
    assertThat(actual.get().getCompanyName().get()).isEqualTo("〇〇株式会社");
    assertThat(actual.get().getDepaertmentName().get()).isEqualTo("営業部");
    assertThat(actual.get().getPost().get()).isEqualTo("主任");
    assertThat(actual.get().getName()).isEqualTo("テスト 五郎");
    assertThat(actual.get().getPostalCode().get()).isEqualTo("000-0005");
    assertThat(actual.get().getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-005 △△ビル 8F");
    assertThat(actual.get().getPhoneNumber().get()).isEqualTo("03-0000-0005");
    assertThat(actual.get().getFax().get()).isEqualTo("03-9999-0005");
    assertThat(actual.get().getNote().get()).isEqualTo("メモ5");
    assertThat(actual.get().getCreateTime().get().after(now)).isTrue();
    assertThat(actual.get().getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.get().getDeleteTime().isEmpty()).isTrue();

    //データ作成(not null の項目だけ値を入れる)
    model = BusinessCardModel.builder()
        .id(-1) //not null 項目なのでダミーで-1を入れておく
        .name("テスト 六郎")
        .build();

    //登録
    cnt = businessCardRepositoryImpl.insert(model);
    assertThat(cnt).isEqualTo(1);

    //末尾に追加されるので ID は 2
    actual = businessCardRepositoryImpl.selectById(2);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(2);
    assertThat(actual.get().getCompanyName().isEmpty()).isTrue();
    assertThat(actual.get().getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.get().getPost().isEmpty()).isTrue();
    assertThat(actual.get().getName()).isEqualTo("テスト 六郎");
    assertThat(actual.get().getPostalCode().isEmpty()).isTrue();
    assertThat(actual.get().getAddress().isEmpty()).isTrue();
    assertThat(actual.get().getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.get().getFax().isEmpty()).isTrue();
    assertThat(actual.get().getNote().isEmpty()).isTrue();
    assertThat(actual.get().getCreateTime().get().after(now)).isTrue();
    assertThat(actual.get().getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.get().getDeleteTime().isEmpty()).isTrue();

  }

  @Test
  void update_引数がnull() {
    int cnt = businessCardRepositoryImpl.update(null);
    assertThat(cnt).isEqualTo(0);
  }

  @Test
  void update_既存データを更新() {
    //現在の時刻取得
    Timestamp now = new Timestamp(System.currentTimeMillis());
    //1秒待つ
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    //全ての項目に値が入っているデータを取得
    Optional<BusinessCardModel> model = businessCardRepositoryImpl.selectById(1);
    assertThat(model.isPresent()).isTrue();
    assertThat(model.get().getId()).isEqualTo(1);
    assertThat(model.get().getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(model.get().getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(model.get().getPost().get()).isEqualTo("部長");
    assertThat(model.get().getName()).isEqualTo("テスト 太郎");
    assertThat(model.get().getPostalCode().get()).isEqualTo("000-0001");
    assertThat(model.get().getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(model.get().getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(model.get().getFax().get()).isEqualTo("03-9999-0001");
    assertThat(model.get().getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(model.get().getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(model.get().getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(model.get().getDeleteTime().isEmpty()).isTrue();

    //not null の項目以外全てnullにする
    BusinessCardModel testData = BusinessCardModel.builder()
        .id(model.get().getId())
        .name(model.get().getName())
        .build();

    //更新
    int cnt = businessCardRepositoryImpl.update(testData);
    //更新件数が1件
    assertThat(cnt).isEqualTo(1);

    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectById(1);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(1);
    assertThat(actual.get().getCompanyName().isEmpty()).isTrue();
    assertThat(actual.get().getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.get().getPost().isEmpty()).isTrue();
    assertThat(actual.get().getName()).isEqualTo("テスト 太郎");
    assertThat(actual.get().getPostalCode().isEmpty()).isTrue();
    assertThat(actual.get().getAddress().isEmpty()).isTrue();
    assertThat(actual.get().getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.get().getFax().isEmpty()).isTrue();
    assertThat(actual.get().getNote().isEmpty()).isTrue();
    assertThat(CommonUtil.timestampToString(actual.get().getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(actual.get().getUpdateTime().get().after(now)).isTrue();
    assertThat(actual.get().getDeleteTime().isEmpty()).isTrue();

    //データ作成
    testData = BusinessCardModel.builder()
        .id(model.get().getId())
        .companyName("〇〇株式会社")
        .depaertmentName("営業部")
        .post("主任")
        .name("テスト 五郎")
        .postalCode("000-0005")
        .address("東京都〇〇区×× 9-99-005 △△ビル 8F")
        .phoneNumber("03-0000-0005")
        .fax("03-9999-0005")
        .note("メモ5")
        .createTime(CommonUtil.stringToTimestamp("2023-04-05 10:00:00").get()) //更新されない
        .updateTime(CommonUtil.stringToTimestamp("2023-04-05 11:00:00").get()) //更新した日時に置き換わる
        .deleteTime(CommonUtil.stringToTimestamp("2023-04-05 12:00:00").get()) //更新されない
        .build();

    //更新
    cnt = businessCardRepositoryImpl.update(testData);
    //更新件数が1件
    assertThat(cnt).isEqualTo(1);

    actual = businessCardRepositoryImpl.selectById(1);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(1);
    assertThat(actual.get().getCompanyName().get()).isEqualTo("〇〇株式会社");
    assertThat(actual.get().getDepaertmentName().get()).isEqualTo("営業部");
    assertThat(actual.get().getPost().get()).isEqualTo("主任");
    assertThat(actual.get().getName()).isEqualTo("テスト 五郎");
    assertThat(actual.get().getPostalCode().get()).isEqualTo("000-0005");
    assertThat(actual.get().getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-005 △△ビル 8F");
    assertThat(actual.get().getPhoneNumber().get()).isEqualTo("03-0000-0005");
    assertThat(actual.get().getFax().get()).isEqualTo("03-9999-0005");
    assertThat(actual.get().getNote().get()).isEqualTo("メモ5");
    assertThat(actual.get().getCreateTime().get()).isEqualTo(model.get().getCreateTime().get());
    assertThat(actual.get().getUpdateTime().get().after(now)).isTrue();
    assertThat(actual.get().getDeleteTime().isEmpty()).isTrue();
  }

  @Test
  void update_存在しないIDをもつデータで更新() {
    BusinessCardModel model = BusinessCardModel.builder()
        .id(-1) //not null 項目なのでダミーで-1を入れておく
        .companyName("〇〇株式会社")
        .depaertmentName("営業部")
        .post("主任")
        .name("テスト 五郎")
        .postalCode("000-0005")
        .address("東京都〇〇区×× 9-99-005 △△ビル 8F")
        .phoneNumber("03-0000-0005")
        .fax("03-9999-0005")
        .note("メモ5")
        .createTime(CommonUtil.stringToTimestamp("2023-04-05 10:00:00").get())
        .updateTime(CommonUtil.stringToTimestamp("2023-04-05 11:00:00").get())
        .deleteTime(CommonUtil.stringToTimestamp("2023-04-05 12:00:00").get())
        .build();

    //登録されていないことを確認
    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectAllById(-1);
    assertThat(actual.isEmpty()).isTrue();

    //更新
    int cnt = businessCardRepositoryImpl.update(model);
    //更新件数が0件
    assertThat(cnt).isEqualTo(0);

  }

  @Test
  void delete_引数がnull() {
    int cnt = businessCardRepositoryImpl.delete(null);
    assertThat(cnt).isEqualTo(0);
  }

  @Test
  void delete_既存データを削除() {
    //現在の時刻取得
    Timestamp now = new Timestamp(System.currentTimeMillis());
    //1秒待つ
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    //登録されているデータを取得
    Optional<BusinessCardModel> testData = businessCardRepositoryImpl.selectAllById(1);
    assertThat(testData.isPresent()).isTrue();
    assertThat(testData.get().getId()).isEqualTo(1);
    assertThat(testData.get().getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(testData.get().getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(testData.get().getPost().get()).isEqualTo("部長");
    assertThat(testData.get().getName()).isEqualTo("テスト 太郎");
    assertThat(testData.get().getPostalCode().get()).isEqualTo("000-0001");
    assertThat(testData.get().getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(testData.get().getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(testData.get().getFax().get()).isEqualTo("03-9999-0001");
    assertThat(testData.get().getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(testData.get().getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(testData.get().getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(testData.get().getDeleteTime().isEmpty()).isTrue();

    //更新
    int cnt = businessCardRepositoryImpl.delete(testData.get().getId());
    //更新件数が1件
    assertThat(cnt).isEqualTo(1);

    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectById(testData.get().getId());
    assertThat(actual.isEmpty()).isTrue();

    actual = businessCardRepositoryImpl.selectAllById(testData.get().getId());
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getId()).isEqualTo(1);
    assertThat(actual.get().getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.get().getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.get().getPost().get()).isEqualTo("部長");
    assertThat(actual.get().getName()).isEqualTo("テスト 太郎");
    assertThat(actual.get().getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.get().getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.get().getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.get().getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.get().getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.get().getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.get().getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.get().getDeleteTime().get().after(now)).isTrue();

  }

  @Test
  void delete_存在しないIDをもつデータで更新() {
    Integer testId = -1;
    //登録されていないことを確認
    Optional<BusinessCardModel> actual = businessCardRepositoryImpl.selectAllById(testId);
    assertThat(actual.isEmpty()).isTrue();

    //更新
    int cnt = businessCardRepositoryImpl.delete(testId);
    //更新件数が0件
    assertThat(cnt).isEqualTo(0);

  }

  @Test
  void findName_検索結果0件() {
    //offset が 0よりも小さい
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.findName("テスト", -1, 1);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //limit が 1よりも小さい
    selectModel = businessCardRepositoryImpl.findName("テスト", 0, 0);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //offset が 0よりも小さい　
    //limit が 1よりも小さい
    selectModel = businessCardRepositoryImpl.findName("テスト", -1, 0);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //name が null
    selectModel = businessCardRepositoryImpl.findName(null, 1, 10);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(0);
    assertThat(selectModel.getTotal()).isEqualTo(0);

    //検索結果 0件
    selectModel = businessCardRepositoryImpl.findName("十兵衛", 1, 10);
    assertThat(selectModel.getList().isEmpty()).isTrue();
    assertThat(selectModel.getOffset()).isEqualTo(1);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(0);
  }

  @Test
  void findName_検索結果1件() {
    //先頭の1件
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.findName("テスト", 0, 1);
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(1);
    assertThat(selectModel.getTotal()).isEqualTo(3);

    List<BusinessCardModel> list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    BusinessCardModel actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.getPost().get()).isEqualTo("部長");
    assertThat(actual.getName()).isEqualTo("テスト 太郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    //中間の1件
    selectModel = businessCardRepositoryImpl.findName("テスト", 1, 1);
    assertThat(selectModel.getOffset()).isEqualTo(1);
    assertThat(selectModel.getLimit()).isEqualTo(1);
    assertThat(selectModel.getTotal()).isEqualTo(3);

    list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(3);
    assertThat(actual.getCompanyName().isEmpty()).isTrue();
    assertThat(actual.getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.getPost().isEmpty()).isTrue();
    assertThat(actual.getName()).isEqualTo("テスト 三郎");
    assertThat(actual.getPostalCode().isEmpty()).isTrue();
    assertThat(actual.getAddress().isEmpty()).isTrue();
    assertThat(actual.getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.getFax().isEmpty()).isTrue();
    assertThat(actual.getNote().isEmpty()).isTrue();
    assertThat(actual.getCreateTime().isEmpty()).isTrue();
    assertThat(actual.getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    //最後の1件
    selectModel = businessCardRepositoryImpl.findName("テスト", 2, 10);
    assertThat(selectModel.getOffset()).isEqualTo(2);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(3);

    list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(4);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("管理部");
    assertThat(actual.getPost().get()).isEqualTo("課長");
    assertThat(actual.getName()).isEqualTo("テスト 四郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0004");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-004 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0004");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0004");
    assertThat(actual.getNote().get()).isEqualTo("メモ4");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-04 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-04 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    //検索結果が 1件
    selectModel = businessCardRepositoryImpl.findName("四郎", 0, 10);
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(1);

    list = selectModel.getList();
    assertThat(list.size()).isEqualTo(1);

    actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(4);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("管理部");
    assertThat(actual.getPost().get()).isEqualTo("課長");
    assertThat(actual.getName()).isEqualTo("テスト 四郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0004");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-004 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0004");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0004");
    assertThat(actual.getNote().get()).isEqualTo("メモ4");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-04 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-04 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();
  }

  @Test
  void findName_検索結果が複数件() {
    SelectModel<BusinessCardModel> selectModel = businessCardRepositoryImpl.findName("テスト", 0, 10);
    assertThat(selectModel.getOffset()).isEqualTo(0);
    assertThat(selectModel.getLimit()).isEqualTo(10);
    assertThat(selectModel.getTotal()).isEqualTo(3);

    List<BusinessCardModel> list = selectModel.getList();
    assertThat(list.size()).isEqualTo(3);

    BusinessCardModel actual = list.get(0);
    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("総務部");
    assertThat(actual.getPost().get()).isEqualTo("部長");
    assertThat(actual.getName()).isEqualTo("テスト 太郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0001");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-001 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0001");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0001");
    assertThat(actual.getNote().get()).isEqualTo("メモ1");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-01 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-01 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    actual = list.get(1);
    assertThat(actual.getId()).isEqualTo(3);
    assertThat(actual.getCompanyName().isEmpty()).isTrue();
    assertThat(actual.getDepaertmentName().isEmpty()).isTrue();
    assertThat(actual.getPost().isEmpty()).isTrue();
    assertThat(actual.getName()).isEqualTo("テスト 三郎");
    assertThat(actual.getPostalCode().isEmpty()).isTrue();
    assertThat(actual.getAddress().isEmpty()).isTrue();
    assertThat(actual.getPhoneNumber().isEmpty()).isTrue();
    assertThat(actual.getFax().isEmpty()).isTrue();
    assertThat(actual.getNote().isEmpty()).isTrue();
    assertThat(actual.getCreateTime().isEmpty()).isTrue();
    assertThat(actual.getUpdateTime().isEmpty()).isTrue();
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();

    actual = list.get(2);
    assertThat(actual.getId()).isEqualTo(4);
    assertThat(actual.getCompanyName().get()).isEqualTo("テスト株式会社");
    assertThat(actual.getDepaertmentName().get()).isEqualTo("管理部");
    assertThat(actual.getPost().get()).isEqualTo("課長");
    assertThat(actual.getName()).isEqualTo("テスト 四郎");
    assertThat(actual.getPostalCode().get()).isEqualTo("000-0004");
    assertThat(actual.getAddress().get()).isEqualTo("東京都〇〇区×× 9-99-004 △△ビル 8F");
    assertThat(actual.getPhoneNumber().get()).isEqualTo("03-0000-0004");
    assertThat(actual.getFax().get()).isEqualTo("03-9999-0004");
    assertThat(actual.getNote().get()).isEqualTo("メモ4");
    assertThat(CommonUtil.timestampToString(actual.getCreateTime().get()).get()).isEqualTo("2023-04-04 10:00:00");
    assertThat(CommonUtil.timestampToString(actual.getUpdateTime().get()).get()).isEqualTo("2023-04-04 11:00:00");
    assertThat(actual.getDeleteTime().isEmpty()).isTrue();
  }

  private static void createTable(JdbcTemplate jdbcTemplate) {
    String sql = "DROP TABLE IF EXISTS business_card";
    jdbcTemplate.execute(sql);

    sql = "CREATE TABLE `business_card` (\n" +
        " `id` int NOT NULL AUTO_INCREMENT,\n" +
        "`company_name` text DEFAULT NULL COMMENT '会社名',\n" +
        "`depaertment_name` text DEFAULT NULL COMMENT '部署名',\n" +
        "`post` text DEFAULT NULL COMMENT '役職',\n" +
        "`name` text NOT NULL COMMENT '名前',\n" +
        "`postal_code` text DEFAULT NULL COMMENT '郵便番号',\n" +
        "`address` text DEFAULT NULL COMMENT '住所',\n" +
        "`phone_number` text DEFAULT NULL COMMENT '電話番号',\n" +
        "`fax` text DEFAULT NULL COMMENT 'fax',\n" +
        "`note` text DEFAULT NULL COMMENT '備考',\n" +
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
