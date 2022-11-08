package com.mwx.service;

import java.util.Map;

/**
 * @author Hojin
 * @date 2022/11/2 20:42
 */
public interface SkuDetailService {
    Map getSalePropertyIdAndSkuIdMapping(Long productId);
}
