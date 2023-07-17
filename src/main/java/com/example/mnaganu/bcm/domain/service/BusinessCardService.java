package com.example.mnaganu.bcm.domain.service;

import com.example.mnaganu.bcm.domain.model.BusinessCardModel;
import com.example.mnaganu.bcm.domain.model.SelectModel;
import com.example.mnaganu.bcm.domain.repository.BusinessCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessCardService {
  private final BusinessCardRepository businessCardRepository;

  @Autowired
  public BusinessCardService(BusinessCardRepository businessCardRepository) {
    this.businessCardRepository = businessCardRepository;
  }

  public SelectModel<BusinessCardModel> select(int offset, int limit) {
    return businessCardRepository.select(offset, limit);
  }

  public SelectModel<BusinessCardModel> findName(String name, int offset, int limit) {
    return businessCardRepository.findName(name, offset, limit);
  }

  public Optional<BusinessCardModel> selectById(int id) {
    return businessCardRepository.selectById(id);
  }

  public int insert(BusinessCardModel model) {
    return businessCardRepository.insert(model);
  }

  public int update(BusinessCardModel model) {
    return businessCardRepository.update(model);
  }

  public int delete(int id) {
    return businessCardRepository.delete(id);
  }


}
