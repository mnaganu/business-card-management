package com.example.mnaganu.bcm.domain.repository;

import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import com.example.mnaganu.bcm.domain.model.SelectModel;

import java.util.Optional;

public interface BusinessCardRepository {

  /**
   * 全検索（削除済みのものを含む）
   *
   * @param offset 位置(前頭からの場合0を指定)
   * @param limit  最大取得件数
   * @return 検索結果
   */
  public SelectModel<BusinessCardModel> selectAll(int offset, int limit);

  /**
   * id 指定でデータを取得（削除済みのものを含む）
   *
   * @param id 　id
   * @return 検索結果
   */
  public Optional<BusinessCardModel> selectAllById(Integer id);

  /**
   * 全検索（削除済みのものを含まない）
   *
   * @param offset 位置(前頭からの場合0を指定)
   * @param limit  最大取得件数
   * @return 検索結果
   */
  public SelectModel<BusinessCardModel> select(int offset, int limit);

  /**
   * id 指定でデータを取得（削除済みのものを含まない）
   *
   * @param id 　id
   * @return 検索結果
   */
  public Optional<BusinessCardModel> selectById(Integer id);

  /**
   * データ登録
   *
   * @param model 登録するデータ
   * @return 登録したデータ数(0の場合は登録に失敗)
   */
  public int insert(BusinessCardModel model);

  /**
   * データ更新
   *
   * @param model 更新するデータ
   * @return 更新したデータ数(0の場合は更新に失敗)
   */
  public int update(BusinessCardModel model);

  /**
   * 削除
   *
   * @param id 　削除対象の id
   * @return 更新したデータ数(0の場合は更新に失敗)
   */
  public int delete(Integer id);

  /**
   * 名前で検索
   * 名前に指定した文字列が含まれているものも取得する。
   *
   * @param name   名前に含まれる文字列
   * @param offset 位置(前頭からの場合0を指定)
   * @param limit  最大取得件数
   * @return 検索結果
   */
  public SelectModel<BusinessCardModel> findName(String name, int offset, int limit);
}
