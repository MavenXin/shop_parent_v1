package com.mwx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwx.entity.PlatformPropertyKey;
import com.mwx.entity.PlatformPropertyValue;
import com.mwx.mapper.PlatformPropertyKeyMapper;
import com.mwx.service.PlatformPropertyKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mwx.service.PlatformPropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 属性表 服务实现类
 * </p>
 *
 * @author Hojin
 * @since 2022-10-31
 */
@Service
public class PlatformPropertyKeyServiceImpl extends ServiceImpl<PlatformPropertyKeyMapper, PlatformPropertyKey> implements PlatformPropertyKeyService {
    @Autowired
    private PlatformPropertyValueService propertyValueService;

    /**TODO:优化此方法
     * 根据分类id查询平台属性列表 (方案一:可优化 -> for循环在高并发高访问量时对数据库的访问量是极高的,需要讲其优化成一条sql语句 )
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    @Override
    public List<PlatformPropertyKey> getPlatformPropertyByCategoryId(Long category1Id, Long category2Id, Long category3Id) {
        List<PlatformPropertyKey> propertyKeyList = baseMapper.getPlatformPropertyKeyByCategoryId(category1Id, category2Id, category3Id);

        if (!CollectionUtils.isEmpty(propertyKeyList)) {
            for (PlatformPropertyKey platformPropertyKey : propertyKeyList) {
                QueryWrapper<PlatformPropertyValue> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("property_key_id", platformPropertyKey.getId());
                List<PlatformPropertyValue> PlatformPropertyValue = propertyValueService.list(queryWrapper);
                platformPropertyKey.setPropertyValueList(PlatformPropertyValue);
            }
        }
        return propertyKeyList;
    }

    /**
     * 保存或修改平台属性
     * @param platformPropertyKey
     */
    @Transactional
    @Override
    public void savePlatformProperty(PlatformPropertyKey platformPropertyKey) {
        //如果是修改平台属性(修改平台属性 传参无id )
        if(platformPropertyKey.getId()!=null){
            baseMapper.updateById(platformPropertyKey);
            //删除该平台属性所有名称对应的值
            QueryWrapper<PlatformPropertyValue> wrapper = new QueryWrapper<>();
            wrapper.eq("property_key_id",platformPropertyKey.getId());
            propertyValueService.remove(wrapper);
        }else{
            //a.保存平台属性key
            baseMapper.insert(platformPropertyKey);
        }
        //b.保存平台属性value
        List<PlatformPropertyValue> propertyValueList = platformPropertyKey.getPropertyValueList();
        if(!CollectionUtils.isEmpty(propertyValueList)){
            for (PlatformPropertyValue platformPropertyValue : propertyValueList) {
                //这个地方是否能拿到id(mybatis-plus自动生成主键)
                platformPropertyValue.setPropertyKeyId(platformPropertyKey.getId());
            }
            propertyValueService.saveBatch(propertyValueList);
        }
    }
}
