package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Pch
 * @create 2023-02-13 19:10
 * @description:
 */
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper,BaseTrademark> implements BaseTrademarkService {
    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;
    @Override
    public IPage<BaseTrademark> getBaseTradeMarkList(Page<BaseTrademark> baseTrademarkPage) {
        //  没有查询条件，但是可以设置一个排序条件
        LambdaQueryWrapper<BaseTrademark> baseTrademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        baseTrademarkLambdaQueryWrapper.orderByDesc(BaseTrademark::getId);
        return baseTrademarkMapper.selectPage(baseTrademarkPage,baseTrademarkLambdaQueryWrapper);
    }
}
