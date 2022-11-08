package com.mwx.controller;

import com.alibaba.fastjson.JSON;
import com.mwx.entity.BaseCategoryView;
import com.mwx.entity.ProductSalePropertyKey;
import com.mwx.entity.SkuInfo;
import com.mwx.feign.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Hojin
 * @date 2022/11/2 16:37
 */
@Controller
public class WebSkuDetailController {
    @Resource
    private ProductFeignClient productFeignClient;

    @RequestMapping("{skuId}.html")
    public String getSkuDetail(@PathVariable Long skuId, Model model) {
        //a.根据skuId查询商品的基本信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        model.addAttribute("skuInfo", skuInfo);
        //b.根据三级分类id获取商品的分类信息
        Long category3Id = skuInfo.getCategory3Id();
        BaseCategoryView categoryView = productFeignClient.getCategoryView(category3Id);
        model.addAttribute("categoryView", categoryView);
        //c.获取实时价格
        BigDecimal skuPrice = productFeignClient.getPrice(skuId);
        model.addAttribute("price", skuPrice);
        //d.销售属性id组合与skuId的对应关系
        Long productId = skuInfo.getProductId();
        Map<Object, Object> salePropertyIdAndSkuIdMapping = productFeignClient.getSalePropertyIdAndSkuIdMapping(productId);
        model.addAttribute("salePropertyValueIdJson", JSON.toJSONString(salePropertyIdAndSkuIdMapping));
        //e.获取该sku对应的销售属性(一份)和所有的销售属性(全份)
        List<ProductSalePropertyKey> spuSalePropertyList = productFeignClient.getSpuSalePropertyAndSelected(productId, skuId);
        model.addAttribute("spuSalePropertyList",spuSalePropertyList);
        return "detail/index";
    }
}
