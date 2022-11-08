package com.mwx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mwx.entity.BaseSaleProperty;
import com.mwx.entity.ProductSpu;
import com.mwx.result.RetVal;
import com.mwx.service.BaseSalePropertyService;
import com.mwx.service.ProductSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
@RestController
@RequestMapping("/product")
public class ProductSpuController {
    @Autowired
    private ProductSpuService spuService;
    @Autowired
    private BaseSalePropertyService salePropertyService;

    //1.根据商品的分类id查询商品SPU列表 product/queryProductSpuByPage/1/10/62
    @GetMapping("queryProductSpuByPage/{pageNum}/{pageSize}/{category3Id}")
    public RetVal queryProductSpuByPage(@PathVariable Long pageNum,
                                        @PathVariable Long pageSize,
                                        @PathVariable Long category3Id) {
        IPage<ProductSpu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ProductSpu> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id", category3Id);
        spuService.page(page, wrapper);
        return RetVal.ok(page);
    }

    //2.查询所有的销售属性 http://127.0.0.1/product/queryAllSaleProperty
    @GetMapping("queryAllSaleProperty")
    public RetVal queryAllSaleProperty() {
        List<BaseSaleProperty> salePropertyList = salePropertyService.list(null);
        return RetVal.ok(salePropertyList);
    }

    //3.添加SPU信息实战 http://127.0.0.1/product/saveProductSpu
    @PostMapping("saveProductSpu")
    public RetVal saveProductSpu(@RequestBody ProductSpu productSpu) {
        spuService.saveProductSpu(productSpu);
        return RetVal.ok();
    }
}

