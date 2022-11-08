package com.mwx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwx.entity.PlatformPropertyKey;
import com.mwx.entity.PlatformPropertyValue;
import com.mwx.result.RetVal;
import com.mwx.service.PlatformPropertyKeyService;
import com.mwx.service.PlatformPropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 属性表 前端控制器
 * </p>
 *
 * @author Hojin
 * @since 2022-10-31
 */
@RestController
@RequestMapping("/product")
public class PlatformPropertyController {
    @Autowired
    private PlatformPropertyKeyService propertyKeyService;

    @Autowired
    private PlatformPropertyValueService propertyValueService;

    //根据id查询平台属性 http://127.0.0.1:8000/product/getPlatformPropertyByCategoryId/2/13/0
    @GetMapping("getPlatformPropertyByCategoryId/{category1Id}/{category2Id}/{category3Id}")
    public RetVal getPlatformPropertyByCategoryId(@PathVariable Long category1Id,
                                                  @PathVariable Long category2Id,
                                                  @PathVariable Long category3Id) {

        List<PlatformPropertyKey> propertyKeyList = propertyKeyService.getPlatformPropertyByCategoryId(category1Id, category2Id, category3Id);
        return RetVal.ok(propertyKeyList);
    }

    //根据平台属性key 查询平台属性的值 http://127.0.0.1:8000/product/getPropertyValueByPropertyKeyId/4
    @GetMapping("getPropertyValueByPropertyKeyId/{propertyKeyId}")
    public RetVal getPropertyValueByPropertyKeyId(@PathVariable Long propertyKeyId) {
        QueryWrapper<PlatformPropertyValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("property_key_id", propertyKeyId);
        List<PlatformPropertyValue> propertyValueList = propertyValueService.list(queryWrapper);
        return RetVal.ok(propertyValueList);
    }

    //保存平台属性 http://127.0.0.1:8000/product/savePlatformProperty
    @PostMapping("savePlatformProperty")
    public RetVal savePlatformProperty(@RequestBody PlatformPropertyKey platformPropertyKey){
        propertyKeyService.savePlatformProperty(platformPropertyKey);
        return RetVal.ok();
    }
}

