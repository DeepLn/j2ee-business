package com.midea.meicloud.dictservice.controller;

import com.midea.meicloud.dictservice.dao.Dict;
import com.midea.meicloud.dictservice.dao.DictItem;
import com.midea.meicloud.dictservice.service.DictService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 11:37 2017-8-22
 */

@RestController
@RequestMapping("/api/authed/")
public class DictController {
    protected Logger log = Logger.getLogger(DictController.class);

    @Autowired
    DictService dictService;

    @PostMapping("dict")
    public Dict addDict(Dict dict){
        return dictService.addDict(dict);
    }

    @DeleteMapping("dict")
    public void delDict(Dict dict){
        dictService.delDict(dict.getId());
    }

    @PutMapping("dict")
    public Dict updateDict(Dict dict){
        return dictService.updateDict(dict);
    }


    @GetMapping("dicts")
    public List<Dict> queryDict(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                @RequestParam(value = "size", defaultValue = "15") Integer size,
                                @RequestParam(value = "sortby", defaultValue = "id") String sortby,
                                @RequestParam(value = "order", defaultValue = "desc") String order,
                                Dict examDict) {
        Sort sort = new Sort(order.equals("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);
        Pageable pageable = new PageRequest(page, size, sort);
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("dictName", ExampleMatcher.GenericPropertyMatchers.contains()) //姓名采用“包含匹配”的方式查询
                .withIgnorePaths("id");  //忽略属性
        Example<Dict> ex = Example.of(examDict, matcher);
        List<Dict> lst = dictService.listDict(pageable, ex);
        return lst;
    }

    @PostMapping("dict_item")
    public DictItem addDictItem(DictItem item){
        return dictService.addDictItem(item);
    }

    @DeleteMapping("dict_item")
    public void delDictItem(DictItem item){
        dictService.delDictItem(item.getId());
    }

    @PutMapping("dict_item")
    public DictItem updateDictItem(DictItem item){
        return dictService.updateDictItem(item);
    }

    @GetMapping("dict_items")
    public List<DictItem> queryDictItem(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "15") Integer size,
                                      @RequestParam(value = "sortby", defaultValue = "id") String sortby,
                                      @RequestParam(value = "order", defaultValue = "desc") String order,
                                      Long dictId) {
        Sort sort = new Sort(order.equals("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);
        Pageable pageable = new PageRequest(page, size, sort);

        return dictService.listDictItem(dictId, pageable);
    }

}
