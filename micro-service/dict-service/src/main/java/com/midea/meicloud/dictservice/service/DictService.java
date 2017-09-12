package com.midea.meicloud.dictservice.service;

import com.midea.meicloud.dictservice.dao.Dict;
import com.midea.meicloud.dictservice.dao.DictItem;
import com.midea.meicloud.dictservice.dao.DictItemRepo;
import com.midea.meicloud.dictservice.dao.DictRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 11:01 2017-8-23
 */

@Service
public class DictService {

    @Autowired
    DictRepo dictRepo;
    @Autowired
    DictItemRepo dictItemRepo;

    public Dict addDict(Dict item){
        if (item.getDictCode() == null
                ||item.getDictName() == null)
            return null;
        return dictRepo.save(item);
    }

    @Transactional
    public void delDict(Long id){
        dictItemRepo.deleteDictItemsByDictIdEquals(id);
        dictRepo.delete(id);
    }

    public Dict updateDict(Dict item){
        if (item.getDictCode() == null
                ||item.getDictName() == null
                ||item.getId() == null)
            return null;
        return dictRepo.saveAndFlush(item);
    }

    public Dict getDict(Long id){
        return dictRepo.findOne(id);
    }

    public List<Dict> listDict(Pageable page, Example ex){
        return dictRepo.findAll(ex, page).getContent();
    }

    @Transactional
    public DictItem addDictItem(DictItem item){
        if(item.getDictId() == 0)
            return null;
        if(dictRepo.findOne(item.getDictId()) == null)
            return null;
        return dictItemRepo.save(item);
    }

    public void delDictItem(Long id){
        dictItemRepo.delete(id);
    }

    @Transactional
    public DictItem updateDictItem(DictItem item){
        if (item.getName() == null || item.getValue() == null || item.getId() == null)
            return null;
        DictItem old = dictItemRepo.findOne(item.getId());
        if(old == null)
            return null;
        item.setDictId(old.getDictId());
        return dictItemRepo.saveAndFlush(item);
    }

    public List<DictItem> listDictItem(Long dictId, Pageable page){
        return dictItemRepo.findAllByDictIdEquals(dictId, page).getContent();
    }
}
