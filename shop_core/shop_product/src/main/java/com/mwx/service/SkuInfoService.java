package com.mwx.service;

import com.mwx.entity.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 库存单元表 服务类
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo skuInfo);

    SkuInfo getSkuInfo(Long skuId);
}
