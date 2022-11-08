package com.mwx.feign;

import com.mwx.entity.BaseCategoryView;
import com.mwx.entity.ProductSalePropertyKey;
import com.mwx.entity.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(value = "shop-product")
public interface ProductFeignClient {
    //a.根据skuId查询商品的基本信息
    @GetMapping("/sku/getSkuInfo/{skuId}")
    SkuInfo getSkuInfo(@PathVariable Long skuId);


    //b.根据三级分类id获取商品的分类信息
    @GetMapping("/sku/getCategoryView/{category3Id}")
    BaseCategoryView getCategoryView(@PathVariable Long category3Id);


    //c.获取实时价格
    @GetMapping("/sku/getPrice/{skuId}")
    BigDecimal getPrice(@PathVariable Long skuId);

    //d.销售属性组合与skuId的对应关系
    @GetMapping("/sku/getSalePropertyIdAndSkuIdMapping/{productId}")
    Map getSalePropertyIdAndSkuIdMapping(@PathVariable Long productId);

    //e.获取该sku对应的销售属性(一份)和所有的销售属性(全份)
    @GetMapping("/sku/getSpuSalePropertyAndSelected/{productId}/{skuId}")
    public List getSpuSalePropertyAndSelected(@PathVariable Long productId,
                                              @PathVariable Long skuId);

}