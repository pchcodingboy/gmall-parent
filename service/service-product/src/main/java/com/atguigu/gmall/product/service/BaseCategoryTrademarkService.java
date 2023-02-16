package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pch
 * @create 2023-02-13 21:11
 * @description:
 */
public interface BaseCategoryTrademarkService extends IService<BaseCategoryTrademark> {
    /**
     * 根据分类id 查询品牌列表
     * @param category3Id
     * @return
     */
    List<BaseTrademark> getTrademarkList(Long category3Id);

    /**
     *  根据三级Id获取品牌列表！
     * @param category3Id
     * @return
     */
    List<BaseTrademark> getCurrentTrademarkList(Long category3Id);

    /**
     * 保存数据
     * @param categoryTrademarkVo
     */
    void save(CategoryTrademarkVo categoryTrademarkVo);

    /**
     * 删除数据
     * @param category3Id
     * @param trademarkId
     */
    void removeBaseCategoryTrademark(Long category3Id, Long trademarkId);
}
