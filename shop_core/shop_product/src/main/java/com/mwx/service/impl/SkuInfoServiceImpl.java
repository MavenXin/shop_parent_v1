package com.mwx.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwx.constant.RedisConst;
import com.mwx.entity.SkuImage;
import com.mwx.entity.SkuInfo;
import com.mwx.entity.SkuPlatformPropertyValue;
import com.mwx.entity.SkuSalePropertyValue;
import com.mwx.mapper.SkuInfoMapper;
import com.mwx.service.SkuImageService;
import com.mwx.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwx.service.SkuPlatformPropertyValueService;
import com.mwx.service.SkuSalePropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 库存单元表 服务实现类
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Resource
    private SkuPlatformPropertyValueService skuPlatformValueService;
    @Resource
    private SkuSalePropertyValueService skuSaleValueService;
    @Resource
    private SkuImageService skuImageService;
    @Resource
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //a.保存sku的基本信息
        baseMapper.insert(skuInfo);
        //b.保存sku的平台属性
        Long skuId = skuInfo.getId();
        Long productId = skuInfo.getProductId();
        List<SkuPlatformPropertyValue> skuPlatformValueList = skuInfo.getSkuPlatformPropertyValueList();
        if (!CollectionUtils.isEmpty(skuPlatformValueList)) {
            for (SkuPlatformPropertyValue skuPropertyValue : skuPlatformValueList) {
                skuPropertyValue.setSkuId(skuId);
            }
            skuPlatformValueService.saveBatch(skuPlatformValueList);
        }
        //c.保存sku的销售属性
        List<SkuSalePropertyValue> skuSaleValueList = skuInfo.getSkuSalePropertyValueList();
        if (!CollectionUtils.isEmpty(skuSaleValueList)) {
            for (SkuSalePropertyValue skuSaleValue : skuSaleValueList) {
                skuSaleValue.setSkuId(skuId);
                skuSaleValue.setProductId(productId);
            }
            skuSaleValueService.saveBatch(skuSaleValueList);
        }
        //d.保存sku的图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (!CollectionUtils.isEmpty(skuImageList)) {
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuId);
            }
            skuImageService.saveBatch(skuImageList);
        }
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        SkuInfo skuInfo = this.getInfoFromRedis(skuId);
        return skuInfo;
    }

    private SkuInfo getInfoFromRedis(Long skuId) {
        String cacheKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKUKEY_SUFFIX;
        SkuInfo skuInfoRedis = (SkuInfo) redisTemplate.opsForValue().get(cacheKey);
        if (skuInfoRedis == null) {
            SkuInfo skuInfoDb = this.getInfoFromDb(skuId);
            redisTemplate.opsForValue().set(cacheKey, skuInfoDb, RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
            return skuInfoDb;
        }
        return skuInfoRedis;
    }

    private SkuInfo getInfoFromDb(Long skuId) {
        SkuInfo skuInfo = baseMapper.selectById(skuId);
        if (null != skuInfo) {
            QueryWrapper<SkuImage> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sku_id", skuId);
            List<SkuImage> imageList = skuImageService.list(queryWrapper);
            skuInfo.setSkuImageList(imageList);
        }
        return skuInfo;
    }
}
