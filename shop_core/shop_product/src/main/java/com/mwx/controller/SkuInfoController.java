package com.mwx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwx.entity.ProductImage;
import com.mwx.entity.ProductSalePropertyKey;
import com.mwx.entity.SkuInfo;
import com.mwx.mapper.ProductSalePropertyKeyMapper;
import com.mwx.result.RetVal;
import com.mwx.service.ProductImageService;
import com.mwx.service.ProductSalePropertyValueService;
import com.mwx.service.SkuInfoService;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 库存单元表 前端控制器
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
@RestController
@RequestMapping("/product")
public class SkuInfoController {

    @Resource
    private SkuInfoService skuInfoService;

    @Resource
    private ProductSalePropertyKeyMapper keyMapper;

    @Resource
    private ProductImageService imageService;

    //1.根据spuId查询所有的销售属性 http://127.0.0.1/product/querySalePropertyByProductId/15
    @GetMapping("querySalePropertyByProductId/{spuId}")
    public RetVal querySalePropertyByProductId(@PathVariable Long spuId) {
        List<ProductSalePropertyKey> salePropertyKeyList = keyMapper.querySalePropertyByProductId(spuId);
        return RetVal.ok(salePropertyKeyList);
    }

    //2.根据spuId查询所有的图片 http://127.0.0.1/product/queryProductImageByProductId/15
    @GetMapping("queryProductImageByProductId/{spuId}")
    public RetVal queryProductImageByProductId(@PathVariable Long spuId) {
        QueryWrapper<ProductImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", spuId);
        List<ProductImage> imageList = imageService.list(queryWrapper);
        return RetVal.ok(imageList);
    }
    //3.TODO:添加SKU信息的实战 http://127.0.0.1/product/saveSkuInfo
    @PostMapping("saveSkuInfo")
    public RetVal saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        skuInfoService.saveSkuInfo(skuInfo);
        return RetVal.ok();
    }
    //4.sku列表显示 http://192.168.16.218/product/querySkuInfoByPage/1/10
    @GetMapping("querySkuInfoByPage/{pageNum}/{pageSize}")
    public RetVal querySkuInfoByPage(@PathVariable Integer pageNum,
                                     @PathVariable Integer pageSize) {
        IPage<SkuInfo> page = new Page<>(pageNum, pageSize);
        skuInfoService.page(page, null);
        return RetVal.ok(page);
    }

    //5.商品上架 product/onSale/24 是否销售（1：是 0：否）
    @GetMapping("onSale/{skuId}")
    public RetVal onSale(@PathVariable Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setIsSale(1);
        skuInfo.setId(skuId);
        boolean flag = skuInfoService.updateById(skuInfo);
        //TODO:有关于ES的后续操作
        return flag ? RetVal.ok() : RetVal.fail();
    }

    //6.商品下架 product/offSale/24
    @GetMapping("offSale/{skuId}")
    public RetVal offSale(@PathVariable Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setIsSale(0);
        skuInfo.setId(skuId);
        boolean flag = skuInfoService.updateById(skuInfo);
        //TODO:有关于ES的后续操作
        return flag ? RetVal.ok() : RetVal.fail();
    }
}

