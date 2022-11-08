package com.mwx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwx.entity.BaseCategoryView;
import com.mwx.entity.SkuInfo;
import com.mwx.service.BaseCategoryViewService;
import com.mwx.service.ProductSalePropertyValueService;
import com.mwx.service.SkuDetailService;
import com.mwx.service.SkuInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Hojin
 * @date 2022/11/2 20:28
 */
@RestController
@RequestMapping("/sku")
public class SkuDetailController {

    @Resource
    private SkuInfoService skuInfoService;

    @Resource
    private BaseCategoryViewService viewService;

    @Resource
    private SkuDetailService skuDetailService;

    @Resource
    private ProductSalePropertyValueService salePropertyService;

    //a.根据skuId查询商品的基本信息
    @GetMapping("/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable Long skuId) {
        SkuInfo skuInfo = skuInfoService.getSkuInfo(skuId);
        return skuInfo;

    }

    //b.根据三级分类id获取商品的分类信息
    @GetMapping("/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable Long category3Id) {
        BaseCategoryView categoryView = viewService.getById(category3Id);
        return categoryView;
    }

    //c.获取实时价格
    @GetMapping("/getPrice/{skuId}")
    public BigDecimal getPrice(@PathVariable Long skuId) {
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        return skuInfo.getPrice();
    }

    //d.销售属性组合与skuId的对应关系
    @GetMapping("/getSalePropertyIdAndSkuIdMapping/{productId}")
    public Map getSalePropertyIdAndSkuIdMapping(@PathVariable Long productId) {
        return skuDetailService.getSalePropertyIdAndSkuIdMapping(productId);
    }

    //e.获取该sku对应的销售属性(一份)和所有的销售属性(全份)
    @GetMapping("/getSpuSalePropertyAndSelected/{productId}/{skuId}")
    public List getSpuSalePropertyAndSelected(@PathVariable Long productId,
                                              @PathVariable Long skuId) {
        return salePropertyService.getSpuSalePropertyAndSelected(productId, skuId);
    }
}
