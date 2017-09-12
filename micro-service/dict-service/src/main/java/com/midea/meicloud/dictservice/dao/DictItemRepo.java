package com.midea.meicloud.dictservice.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 11:10 2017-8-23
 */
public interface DictItemRepo extends JpaRepository<DictItem, Long> {
    void deleteDictItemsByDictIdEquals(Long dictId);
    Page<DictItem> findAllByDictIdEquals(Long dictId, Pageable page);
}
