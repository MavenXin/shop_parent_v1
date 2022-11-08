package com.mwx.controller;


import com.mwx.entity.BaseCategory1;
import com.mwx.entity.BaseCategory2;
import com.mwx.entity.BaseCategory3;
import com.mwx.result.RetVal;
import com.mwx.service.BaseCategory1Service;
import com.mwx.service.BaseCategory2Service;
import com.mwx.service.BaseCategory3Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 一级分类表 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2022-10-29
 */
@RestController
@RequestMapping("/product")
public class CategoryController {
    @Autowired
    private BaseCategory1Service category1Service;
    @Autowired
    private BaseCategory2Service category2Service;
    @Autowired
    private BaseCategory3Service category3Service;
    //1.查询商品一级分类 http://127.0.0.1:8000/product/getCategory1
    @GetMapping("getCategory1")
    public RetVal getCategory1(){
        List<BaseCategory1> category1List = category1Service.list(null);
        return RetVal.ok(category1List);
    }
    //2.查询商品二级分类 http://192.168.16.218/product/getCategory2/2
    @GetMapping("getCategory2/{category1Id}")
    public RetVal getCategory2(@PathVariable Long category1Id){
        QueryWrapper<BaseCategory2> wrapper = new QueryWrapper<>();
        wrapper.eq("category1_id",category1Id);
        List<BaseCategory2> category2List = category2Service.list(wrapper);
        return RetVal.ok(category2List);
    }

    //3.查询商品三级分类 http://192.168.16.218/product/getCategory3/13
    @GetMapping("getCategory3/{category2Id}")
    public RetVal getCategory3(@PathVariable Long category2Id){
        QueryWrapper<BaseCategory3> wrapper = new QueryWrapper<>();
        wrapper.eq("category2_id",category2Id);
        List<BaseCategory3> category3List = category3Service.list(wrapper);
        return RetVal.ok(category3List);
    }
}

