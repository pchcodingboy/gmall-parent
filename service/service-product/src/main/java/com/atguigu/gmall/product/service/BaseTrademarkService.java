package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Pch
 * @create 2023-02-13 19:09
 * @description:
 */
public interface BaseTrademarkService extends IService<BaseTrademark> {
    /**
     * 分页查询品牌列表
     * @param baseTrademarkPage
     * @return
     */
    IPage<BaseTrademark> getBaseTradeMarkList(Page<BaseTrademark> baseTrademarkPage);
}
