package com.example.mnaganu.bcm.infrastructure.repository;

import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import com.example.mnaganu.bcm.domain.model.SelectModel;
import com.example.mnaganu.bcm.domain.repository.BusinessCardRepository;
import com.example.mnaganu.bcm.infrastructure.handler.BusinessCardRowCountCallbackHandler;
import com.example.mnaganu.bcm.infrastructure.mapper.BusinessCardRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BusinessCardRepositoryImpl implements BusinessCardRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public BusinessCardRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * 全検索（削除済みのものを含む）
   *
   * @param offset 位置(前頭からの場合0を指定)
   * @param limit  最大取得件数
   * @return 検索結果
   */
  @Override
  public SelectModel<BusinessCardModel> selectAll(int offset, int limit) {
    SelectModel<BusinessCardModel> selectModel =
        SelectModel.<BusinessCardModel>builder()
            .list(new ArrayList<BusinessCardModel>())
            .offset(0)
            .limit(0)
            .total(0)
            .build();

    if (offset < 0 || limit < 1) {
      return selectModel;
    }

    String sql = "SELECT * FROM business_card ORDER BY id";
    SqlParameterSource parameterSource = new MapSqlParameterSource();
    BusinessCardRowCountCallbackHandler handler = new BusinessCardRowCountCallbackHandler(offset, limit);
    namedParameterJdbcTemplate.query(sql, parameterSource, handler);

    return SelectModel.<BusinessCardModel>builder()
        .list(handler.getList())
        .offset(offset)
        .limit(limit)
        .total(handler.getRowCount())
        .build();
  }

  /**
   * id 指定でデータを取得（削除済みのものを含む）
   *
   * @param id 　id
   * @return 検索結果
   */
  @Override
  public Optional<BusinessCardModel> selectAllById(Integer id) {
    String sql = "SELECT * FROM business_card WHERE id = :id";
    BusinessCardRowMapper rowMapper = new BusinessCardRowMapper();
    SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id", id);

    List<BusinessCardModel> list = namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
    if (list.size() > 0) {
      return Optional.of(list.get(0));
    }
    return Optional.empty();
  }

  /**
   * 全検索
   *
   * @param offset 位置(前頭からの場合0を指定)
   * @param limit  最大取得件数
   * @return 検索結果
   */
  @Override
  @Transactional
  public SelectModel<BusinessCardModel> select(int offset, int limit) {
    SelectModel<BusinessCardModel> selectModel =
        SelectModel.<BusinessCardModel>builder()
            .list(new ArrayList<BusinessCardModel>())
            .offset(0)
            .limit(0)
            .total(0)
            .build();

    if (offset < 0 || limit < 1) {
      return selectModel;
    }

    String sql = "SELECT * FROM business_card WHERE delete_time is NULL ORDER BY id";
    SqlParameterSource parameterSource = new MapSqlParameterSource();
    BusinessCardRowCountCallbackHandler handler = new BusinessCardRowCountCallbackHandler(offset, limit);
    namedParameterJdbcTemplate.query(sql, parameterSource, handler);

    return SelectModel.<BusinessCardModel>builder()
        .list(handler.getList())
        .offset(offset)
        .limit(limit)
        .total(handler.getRowCount())
        .build();
  }

  /**
   * id 指定でデータを取得
   *
   * @param id id
   * @return 検索結果
   */
  @Override
  public Optional<BusinessCardModel> selectById(Integer id) {
    String sql = "SELECT * FROM business_card WHERE delete_time is NULL AND id = :id";
    BusinessCardRowMapper rowMapper = new BusinessCardRowMapper();
    SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id", id);

    List<BusinessCardModel> list = namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
    if (list.size() > 0) {
      return Optional.of(list.get(0));
    }
    return Optional.empty();
  }

  /**
   * データ登録
   *
   * @param model 登録するデータ
   * @return 登録したデータ数(0の場合は登録に失敗)
   */
  @Override
  @Transactional
  public int insert(BusinessCardModel model) {
    if (ObjectUtils.isEmpty(model)) {
      return 0;
    }

    Timestamp now = new Timestamp(System.currentTimeMillis());

    String sql = "INSERT INTO business_card ( " +
        "company_name, " +
        "depaertment_name, " +
        "post, " +
        "name, " +
        "postal_code, " +
        "address, " +
        "phone_number, " +
        "fax, " +
        "note, " +
        "create_time, " +
        "update_time, " +
        "delete_time " +
        " ) VALUES ( " +
        ":companyName, " +
        ":depaertmentName, " +
        ":post, " +
        ":name, " +
        ":postalCode, " +
        ":address, " +
        ":phoneNumber, " +
        ":fax, " +
        ":note, " +
        ":createTime, " +
        ":updateTime, " +
        ":deleteTime " +
        " ) ";
    SqlParameterSource parameters = new MapSqlParameterSource("companyName", model.getCompanyName().orElse(null))
        .addValue("depaertmentName", model.getDepaertmentName().orElse(null))
        .addValue("post", model.getPost().orElse(null))
        .addValue("name", model.getName())
        .addValue("postalCode", model.getPostalCode().orElse(null))
        .addValue("address", model.getAddress().orElse(null))
        .addValue("phoneNumber", model.getPhoneNumber().orElse(null))
        .addValue("fax", model.getFax().orElse(null))
        .addValue("note", model.getNote().orElse(null))
        .addValue("createTime", now)
        .addValue("updateTime", null)
        .addValue("deleteTime", null);

    return namedParameterJdbcTemplate.update(sql, parameters);
  }

  /**
   * データ更新
   *
   * @param model 更新するデータ
   * @return 更新したデータ数(0の場合は更新に失敗)
   */
  @Override
  @Transactional
  public int update(BusinessCardModel model) {
    if (ObjectUtils.isEmpty(model)) {
      return 0;
    }

    Timestamp now = new Timestamp(System.currentTimeMillis());

    String sql = "UPDATE business_card SET " +
        "company_name = :companyName, " +
        "depaertment_name = :depaertmentName, " +
        "post = :post, " +
        "name = :name, " +
        "postal_code = :postalCode, " +
        "address = :address, " +
        "phone_number = :phoneNumber, " +
        "fax = :fax, " +
        "note = :note, " +
        "update_time = :updateTime " +
        "WHERE id = :id ";
    SqlParameterSource parameters = new MapSqlParameterSource("id", model.getId())
        .addValue("companyName", model.getCompanyName().orElse(null))
        .addValue("depaertmentName", model.getDepaertmentName().orElse(null))
        .addValue("post", model.getPost().orElse(null))
        .addValue("name", model.getName())
        .addValue("postalCode", model.getPostalCode().orElse(null))
        .addValue("address", model.getAddress().orElse(null))
        .addValue("phoneNumber", model.getPhoneNumber().orElse(null))
        .addValue("fax", model.getFax().orElse(null))
        .addValue("note", model.getNote().orElse(null))
        .addValue("updateTime", now);

    return namedParameterJdbcTemplate.update(sql, parameters);
  }

  @Override
  public int delete(Integer id) {
    if (ObjectUtils.isEmpty(id)) {
      return 0;
    }

    Timestamp now = new Timestamp(System.currentTimeMillis());

    String sql = "UPDATE business_card SET " +
        "delete_time = :deleteTime " +
        "WHERE id = :id ";
    SqlParameterSource parameters = new MapSqlParameterSource("id", id)
        .addValue("deleteTime", now);

    return namedParameterJdbcTemplate.update(sql, parameters);
  }

  /**
   * 名前で検索
   * 名前に指定した文字列が含まれているものも取得する。
   *
   * @param name   名前に含まれる文字列
   * @param offset 位置(前頭からの場合0を指定)
   * @param limit  最大取得件数
   * @return 検索結果
   */
  @Override
  @Transactional
  public SelectModel<BusinessCardModel> findName(String name, int offset, int limit) {
    SelectModel<BusinessCardModel> selectModel =
        SelectModel.<BusinessCardModel>builder()
            .list(new ArrayList<BusinessCardModel>())
            .offset(0)
            .limit(0)
            .total(0)
            .build();

    if (ObjectUtils.isEmpty(name) || offset < 0 || limit < 1) {
      return selectModel;
    }

    String sql = "SELECT * FROM business_card WHERE delete_time is NULL AND name like :name ORDER BY id";
    SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("name", "%" + name + "%");
    BusinessCardRowCountCallbackHandler handler = new BusinessCardRowCountCallbackHandler(offset, limit);
    namedParameterJdbcTemplate.query(sql, parameterSource, handler);

    return SelectModel.<BusinessCardModel>builder()
        .list(handler.getList())
        .offset(offset)
        .limit(limit)
        .total(handler.getRowCount())
        .build();
  }
}
