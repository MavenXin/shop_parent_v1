package com.mwx.service.impl;

import com.mwx.mapper.SkuSalePropertyValueMapper;
import com.mwx.service.SkuDetailService;
import com.mwx.service.SkuSalePropertyValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hojin
 * @date 2022/11/2 20:42
 */
@Service
public class SkuDetailServiceImpl implements SkuDetailService {
    @Resource
    private SkuSalePropertyValueMapper skuSalePropertyValueMapper;

    @Override
    public Map getSalePropertyIdAndSkuIdMapping(Long productId) {
        Map retMap = new HashMap<Object, Object>();
        List<Map> retMapList = skuSalePropertyValueMapper.getSalePropertyIdAndSkuIdMapping(productId);
        for (Map map : retMapList) {
            retMap.put(map.get("sale_property_value_id"),map.get("sku_id"));
        }
        return retMap;
    }
}
