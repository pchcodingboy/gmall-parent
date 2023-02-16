package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategoryTrademark;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.CategoryTrademarkVo;
import com.atguigu.gmall.product.mapper.BaseCategoryTrademarkMapper;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.atguigu.gmall.product.service.BaseCategoryTrademarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pch
 * @create 2023-02-13 21:16
 * @description:
 */
@Service
public class BaseCategoryTrademarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper, BaseCategoryTrademark> implements BaseCategoryTrademarkService {
    @Autowired
    private BaseCategoryTrademarkMapper baseCategoryTrademarkMapper;

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;
    //表category3_id 来获取 trademark_id

    @Override
    public List<BaseTrademark> getCurrentTrademarkList(Long category3Id) {
        /*
        1.当前这个分类Id 已经绑定了哪些品牌！
            categoryId = 61  手机
            1,3,2:  小米，华为，苹果
        2.获取所有品牌数据
            1,小米
            2,苹果
            3,华为
            4,AA
            5,TT
        3.获取两个结果集的差集数据
         */
        //select * from base_category_trademark where category3_id = ?;
        LambdaQueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        baseCategoryTrademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(baseCategoryTrademarkLambdaQueryWrapper);
        //  已绑定的数据： 1,3,2:  小米，华为，苹果
        //  判断
        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            //  有已绑定的品牌列表
            //  所有品牌列表：baseTrademarkMapper.selectList(null)
            //  tmIdList = 1,3,2
            List<Long> tmIdList = baseCategoryTrademarkList.stream().map(BaseCategoryTrademark::getTrademarkId).collect(Collectors.toList());
            //  使用过滤方式：1,3,2,5,6
            List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectList(null).stream().filter(
                    //  boolean test(T t);
                    baseTrademark -> !tmIdList.contains(baseTrademark.getId())
            ).collect(Collectors.toList());
            //  返回可选品牌列表.
            return baseTrademarkList;
        }
        //  当前这个分类没有绑定！直接查询所有品牌列表！
        return baseTrademarkMapper.selectList(null);
    }

    @Override
    public List<BaseTrademark> getTrademarkList(Long category3Id) {
        //根据 category3Id，查询品牌id
        //select * from base_category_trademark where category3_id = ?;
        LambdaQueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        baseCategoryTrademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(baseCategoryTrademarkLambdaQueryWrapper);
       /*
           5,61,1,...
           9,61,3,...
           11,61,2,...
        */
        //  获取到品牌Id集合
//        ArrayList<Long> tmIdList = new ArrayList<>();
//        baseCategoryTrademarksList.forEach(baseCategoryTrademark -> {
//            Long trademarkId = baseCategoryTrademark.getTrademarkId();
//            tmIdList.add(trademarkId);
//        });


        //判断

        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            //  拉姆达表达式：要谁就返回谁！返回映射结果集！  {1,3,2}
            List<Long> tmIdList = baseCategoryTrademarkList.stream().map(BaseCategoryTrademark::getTrademarkId).collect(Collectors.toList());
            //  select * from base_trademark where id in(1,2,3);
            List<BaseTrademark> baseTrademarkList = baseTrademarkMapper.selectBatchIds(tmIdList);
            //  返回数据.
             /*
                1,小米,...
                3,苹果,...
                2,华为,...
             */
            return baseTrademarkList;
        } else {
            return null;
        }
    }

    @Override
    public void save(CategoryTrademarkVo categoryTrademarkVo) {
        //  插入数据 -> base_category_trademark
        //  先获取到对应的品牌Id：category3Id = 1,3,2   5,6={trademarkIdList}
        //  {"category3Id":61,{"trademarkIdList":[5,6]}}
        //  61:5 61:6
        //  方式一：

//        ArrayList<BaseCategoryTrademark> baseCategoryTrademarkArrayList = new ArrayList<>();
//        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();
//        if (!CollectionUtils.isEmpty(trademarkIdList)) {
//            trademarkIdList.forEach(tmId -> {
//                //  构建对象
//                BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
//                baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
//                baseCategoryTrademark.setTrademarkId(tmId);
//                baseCategoryTrademarkArrayList.add(baseCategoryTrademark);
//                //  执行次数，是看集合的长度
//               // baseCategoryTrademarkMapper.insert(baseCategoryTrademark);
//            });
//        }

        //  方式二：
        //  批量插入数据 :insert into values();
        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();
        List<BaseCategoryTrademark> trademarkList = trademarkIdList.stream().map(tmId -> {
            //构建对象
            BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
            baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
            baseCategoryTrademark.setTrademarkId(tmId);
            return baseCategoryTrademark;
        }).collect(Collectors.toList());
        this.saveBatch(trademarkList);
    }

    @Override
    public void removeBaseCategoryTrademark(Long category3Id, Long trademarkId) {
        //  创建删除条件
        LambdaQueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        baseCategoryTrademarkLambdaQueryWrapper.eq(BaseCategoryTrademark::getCategory3Id,category3Id).eq(BaseCategoryTrademark::getTrademarkId,trademarkId);
        //  删除方法.
        this.baseCategoryTrademarkMapper.delete(baseCategoryTrademarkLambdaQueryWrapper);
    }
}
