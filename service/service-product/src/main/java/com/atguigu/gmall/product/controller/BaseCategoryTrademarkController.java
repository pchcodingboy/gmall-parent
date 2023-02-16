package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.service.BaseCategoryTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Pch
 * @create 2023-02-13 20:52
 * @description:
 */
@RestController
@RequestMapping("admin/product/baseCategoryTrademark")
public class BaseCategoryTrademarkController {
    @Autowired
    private BaseCategoryTrademarkService baseCategoryTrademarkService;


    /**
     *根据三级分类id 查询品牌列表
     */
    @GetMapping("/findTrademarkList/{category3Id}")
    public Result findTrademarkList(@PathVariable Long category3Id){
        //  查询品牌列表时，传递的参数是三级分类Id，但是表中不存在！
        //  找中间表category3_id 来获取 trademark_id;
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.getTrademarkList(category3Id);
        return Result.ok(baseTrademarkList);
    }

    /**
     *  根据三级分类Id获取可选择品牌列表：  select * from xxx where category3Id;
     *  1.  条件：
     *  2.  主体信息
     */
    @GetMapping("/findCurrentTrademarkList/{category3Id}")
    public Result findCurrentTrademarkList(@PathVariable Long category3Id){
        //  获取可选品牌列表
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.getCurrentTrademarkList(category3Id);
        //  返回数据：可选品牌列表
        return Result.ok(baseTrademarkList);
    }

    /**
     *  保存数据
     *  /admin/product/baseCategoryTrademark/save
     *
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody CategoryTrademarkVo categoryTrademarkVo){
        //  保存数据
        baseCategoryTrademarkService.save(categoryTrademarkVo);
        return Result.ok();
    }

    /**
     * 删除数据 /admin/product/baseCategoryTrademark/remove/{category3Id}/{trademarkId}
     * @param category3Id
     * @param trademarkId
     * @return
     */
    @DeleteMapping("/remove/{category3Id}/{trademarkId}")
    public Result remove(@PathVariable Long category3Id,
                        @PathVariable Long trademarkId){
        //  删除数据
        baseCategoryTrademarkService.removeBaseCategoryTrademark(category3Id,trademarkId);
        return Result.ok();
    }
}
