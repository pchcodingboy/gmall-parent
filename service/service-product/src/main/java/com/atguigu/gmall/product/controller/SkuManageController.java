package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pch
 * @create 2023-02-16 10:28
 * @description:
 */
@RestController
@RequestMapping("/admin/product")
public class SkuManageController {
    @Autowired
    private ManageService manageService;

    /**
     * 保存sku
     * @param skuInfo
     * @return
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        //调用服务层方法
        this.manageService.saveSkuInfo(skuInfo);
        return Result.ok();
    }
}
