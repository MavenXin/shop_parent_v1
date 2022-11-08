package com.mwx.mapper;

import com.mwx.entity.PlatformPropertyKey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 属性表 Mapper 接口
 * </p>
 *
 * @author Hojin
 * @since 2022-10-31
 */
public interface PlatformPropertyKeyMapper extends BaseMapper<PlatformPropertyKey> {

    List<PlatformPropertyKey> getPlatformPropertyKeyByCategoryId(Long category1Id, Long category2Id, Long category3Id);

}
