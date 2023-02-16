package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pch
 * @create 2023-02-13 18:55
 * @description:
 */
@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    /**
     * 品牌列表查询
     * /admin/product/baseTrademark/{page}/{limit}
     * @param page
     * @param limit
     * @return Result
     */
    @GetMapping("/{page}/{limit}")
    public Result getBaseTradeMarkList(@PathVariable Long page,
                                       @PathVariable Long limit) {
        //创建一个Page 对象
        Page<BaseTrademark> baseTrademarkPage = new Page<BaseTrademark>(page, limit);
        //调用服务层的方法
         IPage<BaseTrademark> iPage = this.baseTrademarkService.getBaseTradeMarkList(baseTrademarkPage);
        //返回数据。
        return Result.ok(iPage);
    }

    /**
     *  保存品牌
     *  /admin/product/baseTrademark/save
     * @param baseTrademark
     * @return
     */
    @PostMapping("/save")
    public Result saveBaseTradeMark(@RequestBody BaseTrademark baseTrademark){
        //调用服务层方法
        this.baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    /**
     * 根据id删除品牌
     *  /admin/product/baseTrademark/remove/{id}
     * @param id
     * @return
     */
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        this.baseTrademarkService.removeById(id);
        return Result.ok();
    }

    /**
     *  修改品牌信息：先回显，再修改
     *  根据品牌id 获取到品牌对象
     *  /admin/product/baseTrademark/get/{id}
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable long id){
        BaseTrademark baseTrademark = this.baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

    /**
     * 保存修改的内容：
     * /admin/product/baseTrademark/update
     * @param baseTrademark
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseTrademark baseTrademark){
        this.baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }
}
