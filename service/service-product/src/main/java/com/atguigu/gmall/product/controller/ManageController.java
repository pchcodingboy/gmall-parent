package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.service.ManageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Pch
 * @create 2023-02-07 18:39
 * @description:
 */

@Api("商品管理后端接口")
@RestController //@Controller:控制器 @ResponseBody：1.返回Json依赖一个jar包 2.直接将数据输出页面
//@CrossOrigin

@RefreshScope
@RequestMapping("/admin/product")
public class ManageController {

    @Autowired
    private ManageService manageService;

    /**
     * 获取一级分类数据
     * 返回数据统一封装到Result对象
     */
    @GetMapping("/getCategory1")
    public Result getCategory1() {
        //调用服务层方法
        List<BaseCategory1> baseCategory1List = manageService.getCategory1();
        //返回数据
        return Result.ok(baseCategory1List);

    }

    /**
     * 根据一级分类id 获取二级分类数据
     */
    @GetMapping("/getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable Long category1Id) {
        //调用服务层方法
        List<BaseCategory2> baseCategory2List = manageService.getCategory2(category1Id);
        return Result.ok(baseCategory2List);
    }

    /**
     * 根据二级分类id 获取二级分类数据
     */
    @GetMapping("/getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable Long category2Id) {
        //调用服务层方法
        List<BaseCategory3> baseCategory2List = manageService.getCategory3(category2Id);
        return Result.ok(baseCategory2List);
    }

    /**
     * 根据分类id 获取平台属性集合数据
     */
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result getAttrInfoList(@PathVariable Long category1Id,
                                  @PathVariable Long category2Id,
                                  @PathVariable Long category3Id
    ) {
        //调用服务层方法。
        List<BaseAttrInfo> baseAttrInfoList = manageService.getAttrInfoList(category1Id, category2Id, category3Id);
        //返回数据。
        return Result.ok(baseAttrInfoList);
    }

    /*
     * 保存平台属性 : springMVC
     */
    //  /admin/product/saveAttrInfo
    //  如何接收json数据 ，并将数据转换成JsonObject！
    // 如果有实体类直接使用即可，如果没有需要创建对应的 DTO（数据的载体）{vo 是数据的展示}
    // @RequestBody --> 将Json==>JavaObject
    // ]@ResponseBody --> 将JavaObject ==> Json

    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        //调用服务层方法
        manageService.saveAttrInfoList(baseAttrInfo);
        // 默认返回、
        return Result.ok();
    }

    /**
     * /admin/product/getAttrValueList/{attrId}
     */
    @GetMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable Long attrId) {
        //方式一：
        // 直接查询的是平台属性值表：select * from base_attr_value where attr_id = 1;
        //List<BaseAttrValue> baseAttrValueList = manageService.getAttrValueList(attrId);
        //从业务上优化代码: 先查询平台属性：有属性才会获得属性值

        //方式二：
        BaseAttrInfo baseAttrInfo = this.manageService.getBaseAttrInfo(attrId);

        //返回result：平台属性值集合。
        return Result.ok(baseAttrInfo.getAttrValueList());
    }
}
