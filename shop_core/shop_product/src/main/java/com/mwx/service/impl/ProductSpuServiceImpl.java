package com.mwx.service.impl;

import com.mwx.entity.ProductImage;
import com.mwx.entity.ProductSalePropertyKey;
import com.mwx.entity.ProductSalePropertyValue;
import com.mwx.entity.ProductSpu;
import com.mwx.mapper.ProductSpuMapper;
import com.mwx.service.ProductImageService;
import com.mwx.service.ProductSalePropertyKeyService;
import com.mwx.service.ProductSalePropertyValueService;
import com.mwx.service.ProductSpuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
@Service
public class ProductSpuServiceImpl extends ServiceImpl<ProductSpuMapper, ProductSpu> implements ProductSpuService {
    @Resource
    private ProductImageService imageService;
    @Resource
    private ProductSalePropertyKeyService keyService;
    @Resource
    private ProductSalePropertyValueService valueService;

    @Override
    public void saveProductSpu(ProductSpu productSpu) {
        //保存spu基本信息
        baseMapper.insert(productSpu);
        Long spuId = productSpu.getId();
        //保存图片信息
        List<ProductImage> imageList = productSpu.getProductImageList();
        if (!CollectionUtils.isEmpty(imageList)) {
            for (ProductImage productImage : imageList) {
                productImage.setProductId(spuId);
            }
            imageService.saveBatch(imageList);
        }
        //保存销售属性关键字
        List<ProductSalePropertyKey> propertyKeyList = productSpu.getSalePropertyKeyList();
        if (!CollectionUtils.isEmpty(propertyKeyList)) {
            for (ProductSalePropertyKey propertyKey : propertyKeyList) {
                propertyKey.setProductId(spuId);
                //保存销售属性值
                List<ProductSalePropertyValue> propertyValueList = propertyKey.getSalePropertyValueList();
                if (!CollectionUtils.isEmpty(propertyKeyList)) {
                    for (ProductSalePropertyValue propertyValue : propertyValueList) {
                        propertyValue.setProductId(spuId);
                        propertyValue.setSalePropertyKeyName(propertyKey.getSalePropertyKeyName());
                    }
                    valueService.saveBatch(propertyValueList);
                }
            }
            keyService.saveBatch(propertyKeyList);
        }

    }
}
