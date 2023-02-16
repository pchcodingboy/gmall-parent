package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Pch
 * @create 2023-02-07 19:00
 * @description: BaseMapper:mybatis-plus 内部提供的接口，这个接口中有对单表实现的CRUd操作，规定好当前mapper的泛型
 */

@Mapper
public interface BaseCategory1Mapper extends BaseMapper<BaseCategory1> {
}
