package com.mwx.service.impl;

import com.mwx.entity.ProductSalePropertyValue;
import com.mwx.mapper.ProductSalePropertyValueMapper;
import com.mwx.service.ProductSalePropertyValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * spu销售属性值 服务实现类
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
@Service
public class ProductSalePropertyValueServiceImpl extends ServiceImpl<ProductSalePropertyValueMapper, ProductSalePropertyValue> implements ProductSalePropertyValueService {

    @Resource
    private ProductSalePropertyValueMapper salePropertyMapper;

    @Override
    public List getSpuSalePropertyAndSelected(Long productId, Long skuId) {
        return salePropertyMapper.getSpuSalePropertyAndSelected(productId,skuId);
    }
}
