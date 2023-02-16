package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Pch
 * @create 2023-02-12 11:44
 * @description:
 */
@RestController
@RequestMapping("/admin/product")
public class SpuManageController {
    @Autowired
    private ManageService manageService;
    //admin/product/{page}/{limit}
    //http://api.gmall.com/admin/product/1/10?category3Id=61
    //?category3Id=61 这个参数是普通传递！ 如何获取这个条件！ JavaWeb

    @GetMapping("{page}/{limit}")
    public Result getSpuInfoList(@PathVariable Long page,
                                 @PathVariable Long limit,
                                 SpuInfo spuinfo){
        //  创建一个page对象
        Page<SpuInfo> supInfoPage = new Page<>(page,limit);
        //  调用服务层方法查询。
        IPage iPage = this.manageService.getSpuInfoList(supInfoPage,spuinfo.getCategory3Id());
        //  返回数据。
        return Result.ok(iPage);
    }

    /**
     * 获取销售属性数据
     * /admin/product/baseSaleAttrList
     * @return
     */
    @GetMapping("/baseSaleAttrList")
    public Result getBaseSaleAttrList(){
        //  select * from base_sale_attr;
        List<BaseSaleAttr> baseSaleAttrList = this.manageService.getBaseSaleAttrList();
        //返回销售属性列表
        return Result.ok(baseSaleAttrList);
    }

    /**
     * 保存spu
     *  /admin/product/saveSpuInfo
     *  Json --> JavaObject
     * @return
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        //  调用服务层方法。
        this.manageService.saveSpuInfo(spuInfo);

        return Result.ok();
    }

    /**
     *  根据spuId 获取图片列表
     * @param spuId
     * @return
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable Long spuId){
        //获取数据
        List<SpuImage> spuImageList = this.manageService.getSpuImageList(spuId);
        //返回数据
        return Result.ok(spuImageList);
    }

    /**
     *  回显销售属性+销售属性值
     * @param spuId
     * @return
     */
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable Long spuId){
        //获取返回数据
        List<SpuSaleAttr> spuSaleAttrList = manageService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }
}
