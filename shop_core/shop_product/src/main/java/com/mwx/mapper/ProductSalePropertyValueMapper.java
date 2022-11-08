package com.mwx.mapper;

import com.mwx.entity.ProductSalePropertyValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * spu销售属性值 Mapper 接口
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
public interface ProductSalePropertyValueMapper extends BaseMapper<ProductSalePropertyValue> {

    List getSpuSalePropertyAndSelected(@Param("productId") Long productId, @Param("skuId") Long skuId);
}
