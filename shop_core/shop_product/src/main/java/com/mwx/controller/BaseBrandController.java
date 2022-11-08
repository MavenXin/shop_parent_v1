package com.mwx.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mwx.entity.BaseBrand;
import com.mwx.result.RetVal;
import com.mwx.service.BaseBrandService;
import com.mwx.utils.MinioUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author Hojin
 * @since 2022-11-01
 */
@RestController
@RequestMapping("/product/brand")
public class BaseBrandController {
    @Resource
    private BaseBrandService brandService;

    @Resource
    private MinioUploader minioUploader;
    //1.分页查询品牌信息
    @GetMapping("queryBrandByPage/{pageNum}/{pageSize}")
    public RetVal queryBrandByPage(@PathVariable Integer pageNum,
                                   @PathVariable Integer pageSize){

        IPage<BaseBrand> page = new Page<>(pageNum,pageSize);
        brandService.page(page,null);
        return RetVal.ok(page);
    }
    //http://127.0.0.1/product/brand
    //2.添加品牌
    @PostMapping
    public RetVal saveBrand(@RequestBody BaseBrand brand) {
        brandService.save(brand);
        return RetVal.ok();
    }

    //http://127.0.0.1/product/brand/4
    //3.根据id查询品牌信息
    @GetMapping("{brandId}")
    public RetVal saveBrand(@PathVariable Long brandId) {
        BaseBrand brand = brandService.getById(brandId);
        return RetVal.ok(brand);
    }

    //4.更新品牌信息
    @PutMapping
    public RetVal updateBrand(@RequestBody BaseBrand brand) {
        brandService.updateById(brand);
        return RetVal.ok();
    }

    //5.删除品牌信息
    @DeleteMapping("{brandId}")
    public RetVal remove(@PathVariable Long brandId) {
        brandService.removeById(brandId);
        return RetVal.ok();
    }

    //6.查询所有的品牌
    @GetMapping("getAllBrand")
    public RetVal getAllBrand() {
        List<BaseBrand> brandList = brandService.list(null);
        return RetVal.ok(brandList);
    }
    //7.TODO:文件上传 fastdfs


    //8.文件上传 MinIO http://api.gmall.com/product/brand/fileUpload
    @PostMapping("fileUpload")
    public RetVal fileUpload(MultipartFile file) throws Exception {
        String Url = minioUploader.uploadFile(file);
        return RetVal.ok(Url);
    }

}

