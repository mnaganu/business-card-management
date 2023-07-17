package com.example.mnaganu.bcm.application.controller;

import com.example.mnaganu.bcm.application.exception.BusinessCardIdFormatException;
import com.example.mnaganu.bcm.application.exception.BusinessCardNameNotFoundException;
import com.example.mnaganu.bcm.application.exception.LimitFormatException;
import com.example.mnaganu.bcm.application.exception.OffsetFormatException;
import com.example.mnaganu.bcm.application.model.ApiStatus;
import com.example.mnaganu.bcm.application.model.BusinessCardData;
import com.example.mnaganu.bcm.application.model.request.BusinessCardInsertRequest;
import com.example.mnaganu.bcm.application.model.request.BusinessCardUpdateRequest;
import com.example.mnaganu.bcm.application.model.response.BusinessCardSelectResponse;
import com.example.mnaganu.bcm.application.model.response.BusinessCardUpdateResponse;
import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import com.example.mnaganu.bcm.domain.model.SelectModel;
import com.example.mnaganu.bcm.domain.service.BusinessCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/v1/business-card", produces = "application/json")
public class BusinessCardController {
  private final BusinessCardService businessCardService;

  @Autowired
  public BusinessCardController(BusinessCardService businessCardService) {
    this.businessCardService = businessCardService;
  }

  @RequestMapping(method = RequestMethod.GET)
  BusinessCardSelectResponse getBusinessCard(@RequestParam(name = "offset", defaultValue = "0", required = false) String paramOffset,
                                             @RequestParam(name = "limit", defaultValue = "5", required = false) String paramLimit) {

    Integer offset = paramCheckOffset(paramOffset);
    Integer limit = paramCheckLimit(paramLimit);
    List<BusinessCardData> list = new ArrayList<BusinessCardData>();
    SelectModel<BusinessCardModel> selectModel = businessCardService.select(offset, limit);

    selectModel.getList() //List<BusinessCardModel> を取得
        .stream()  //stream api を利用するため stream を呼び出す。
        .map(item -> BusinessCardData.convertTo(item)) //map で IndexTestModel を Optional<BusinessCardDataResponse> に変換
        .filter(response -> response.isPresent()) // Optional<BusinessCardDataResponse> が null のものを省く
        .forEach(response -> list.add(response.get())); //list に追加

    return new BusinessCardSelectResponse(
        selectModel.getTotal(),
        selectModel.getOffset(),
        selectModel.getLimit(),
        list);
  }

  @RequestMapping(value = "name/{name}", method = RequestMethod.GET)
  BusinessCardSelectResponse getBusinessCard(@PathVariable("name") String name,
                                             @RequestParam(name = "offset", defaultValue = "0", required = false) String paramOffset,
                                             @RequestParam(name = "limit", defaultValue = "5", required = false) String paramLimit) {

    Integer offset = paramCheckOffset(paramOffset);
    Integer limit = paramCheckLimit(paramLimit);
    List<BusinessCardData> list = new ArrayList<BusinessCardData>();
    SelectModel<BusinessCardModel> selectModel = businessCardService.findName(name, offset, limit);

    selectModel.getList() //List<BusinessCardModel> を取得
        .stream()  //stream api を利用するため stream を呼び出す。
        .map(item -> BusinessCardData.convertTo(item)) //map で IndexTestModel を Optional<BusinessCardDataResponse> に変換
        .filter(response -> response.isPresent()) // Optional<BusinessCardDataResponse> が null のものを省く
        .forEach(response -> list.add(response.get())); //list に追加

    return new BusinessCardSelectResponse(
        selectModel.getTotal(),
        selectModel.getOffset(),
        selectModel.getLimit(),
        list);
  }

  @RequestMapping(value = "id/{id}", method = RequestMethod.GET)
  BusinessCardSelectResponse getBusinessCard(@PathVariable("id") String paramId) {

    //パラメータチェック
    Integer id = paramCheckId(paramId);

    List<BusinessCardData> list = new ArrayList<BusinessCardData>();
    Optional<BusinessCardModel> model = businessCardService.selectById(id);

    if (model.isPresent()) {
      Optional<BusinessCardData> data = BusinessCardData.convertTo(model.get());
      if (data.isPresent()) {
        list.add(data.get());
      }
    }

    return new BusinessCardSelectResponse(
        list.size(),
        0,
        list.size(),
        list);
  }

  @RequestMapping(method = RequestMethod.POST)
  public BusinessCardUpdateResponse insert(
      @RequestBody BusinessCardInsertRequest request) {

    BusinessCardModel model = paramCheckBusinessCardModel(request);
    int cnt = 0;

    try {
      cnt = businessCardService.insert(model);
    } catch (DuplicateKeyException e) {
      //今回は id 以外重複チェックしていないので発生しない。
      return new BusinessCardUpdateResponse(ApiStatus.NG, "既に登録済みです。");
    }

    if (cnt > 0) {
      return new BusinessCardUpdateResponse(ApiStatus.OK, "登録完了");
    }

    return new BusinessCardUpdateResponse(ApiStatus.NG, "登録に失敗しました。");
  }

  @RequestMapping(method = RequestMethod.PUT)
  public BusinessCardUpdateResponse update(
      @RequestBody BusinessCardUpdateRequest request) {

    BusinessCardModel model = paramCheckBusinessCardModel(request);
    int cnt = 0;

    cnt = businessCardService.update(model);

    if (cnt > 0) {
      return new BusinessCardUpdateResponse(ApiStatus.OK, "登録完了");
    }

    return new BusinessCardUpdateResponse(ApiStatus.NG, "登録に失敗しました。");
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  public BusinessCardUpdateResponse delete(@PathVariable("id") String paramId) {

    Integer id = paramCheckId(paramId);
    int cnt = 0;

    cnt = businessCardService.delete(id);

    if (cnt > 0) {
      return new BusinessCardUpdateResponse(ApiStatus.OK, "削除完了");
    }

    return new BusinessCardUpdateResponse(ApiStatus.NG, "削除に失敗しました。");
  }

  private Integer paramCheckId(String paramId) {
    try {
      Integer id = Integer.valueOf(paramId);
      if (id.intValue() < 1) {
        throw new BusinessCardIdFormatException("BusinessCardModel ID は1以上の数字で指定してください。");
      }
      return id;
    } catch (Exception e) {
      throw new BusinessCardIdFormatException("BusinessCardModel ID は1以上の数字で指定してください。");
    }
  }

  private Integer paramCheckOffset(String paramOffset) {
    try {
      Integer offset = Integer.valueOf(paramOffset);
      if (offset.intValue() < 0) {
        throw new OffsetFormatException("offset は0以上の数字で指定してください。");
      }
      return offset;
    } catch (Exception e) {
      throw new OffsetFormatException("offset は0以上の数字で指定してください。");
    }
  }

  private Integer paramCheckLimit(String paramLimit) {
    try {
      Integer limit = Integer.valueOf(paramLimit);
      if (limit.intValue() < 0) {
        throw new LimitFormatException("limit は1以上の数字で指定してください。");
      }
      return limit;
    } catch (Exception e) {
      throw new LimitFormatException("limit は1以上の数字で指定してください。");
    }
  }

  private BusinessCardModel paramCheckBusinessCardModel(BusinessCardInsertRequest request) {
    if (request.getName().isEmpty()) {
      throw new BusinessCardNameNotFoundException("BusinessCardModel Name は必須項目です。");
    }
    return BusinessCardInsertRequest.convertToBusinessCardModel(request).get();
  }

  private BusinessCardModel paramCheckBusinessCardModel(BusinessCardUpdateRequest request) {
    paramCheckId(request.getId().toString());
    if (request.getName().isEmpty()) {
      throw new BusinessCardNameNotFoundException("BusinessCardModel Name は必須項目です。");
    }
    return BusinessCardUpdateRequest.convertToBusinessCardModel(request).get();
  }
}
