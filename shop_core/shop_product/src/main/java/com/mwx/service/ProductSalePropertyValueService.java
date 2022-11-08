package com.mwx.service;

import com.mwx.entity.ProductSalePropertyValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * spu销售属性值 服务类
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
public interface ProductSalePropertyValueService extends IService<ProductSalePropertyValue> {

    List getSpuSalePropertyAndSelected(Long productId, Long skuId);
}
