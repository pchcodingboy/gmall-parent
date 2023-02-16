package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @author Pch
 * @create 2023-02-07 18:55
 * @description:
 */

@Service

public class ManageServiceImpl implements ManageService {
    /**
     * mapper:主要负责查询数据库，与数据库交互获取数据！
     * select * from base_category1 where is_deleted = 0; 用哪个mapper 主要看查询哪个表！
     */
    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuPosterMapper spuPosterMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Override
    public List<BaseCategory1> getCategory1() {
        //调用方法
        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {

        // select * from base_category2 where category1_id = 2;
        //Wrapper：构建条件：查询 删除 修改 T:泛型 对应的实体类
        QueryWrapper<BaseCategory2> baseCategory2QueryWrapper = new QueryWrapper<>();
        baseCategory2QueryWrapper.eq("category1_id", category1Id);
        return baseCategory2Mapper.selectList(baseCategory2QueryWrapper);
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        //构件查询条件：
        QueryWrapper<BaseCategory3> baseCategory3QueryWrapper = new QueryWrapper<>();
        baseCategory3QueryWrapper.eq("category2_id", category2Id);
        return baseCategory3Mapper.selectList(baseCategory3QueryWrapper);
    }

    @Override
    public BaseAttrInfo getBaseAttrInfo(Long attrId) {
        //查询平台属性
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrId);
        //判断
        if (baseAttrInfo != null) {
            baseAttrInfo.setAttrValueList(getAttrValueList(attrId));
        }
        //返回对象
        return baseAttrInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        /*
        sku_attr_value
        sku_image
        sku_info
        sku_sale_attr_value
         */
        //  sku_info
        skuInfoMapper.insert(skuInfo);

        //  sku_image
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (!CollectionUtils.isEmpty(skuImageList)){
            skuImageList.forEach(skuImage -> {
                //  赋值skuId
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            });
        }

        //  sku_attr_value  skuId 与 平台属性值Id 的中间表
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)){
            skuAttrValueList.forEach(skuAttrValue -> {
               //   赋值skuId
               skuAttrValue.setSkuId(skuInfo.getId());
               skuAttrValueMapper.insert(skuAttrValue);
            });
        }

        //sku_sale_attr_value   skuId 与 销售属性值Id 的中间表
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(!CollectionUtils.isEmpty(skuSaleAttrValueList)){
            skuSaleAttrValueList.forEach(skuSaleAttrValue -> {
                //  赋值skuId
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                //  赋值spuId = spuInfo.getId(); = skuInfo.getSpuId();
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            });
        }
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
        //  多表关联查询：推荐使用xml  服务层方法命名：get/find save remove  mapper: select insert delete

        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    @Override
    public List<SpuImage> getSpuImageList(Long spuId) {
        // select * from spu_image where spu_id = ?
        LambdaQueryWrapper<SpuImage> spuImageQueryWrapper = new LambdaQueryWrapper<>();
        spuImageQueryWrapper.eq(SpuImage::getSpuId,spuId);
        return spuImageMapper.selectList(spuImageQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuInfo spuInfo) {
        /*
        spu_image
        spu_info    商品信息表
        spu_poster
        spu_sale_attr
        spu_sale_attr_value
         */
        spuInfoMapper.insert(spuInfo);
        //获取图片列表
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(!CollectionUtils.isEmpty(spuImageList)){
            spuImageList.forEach(spuImage -> {
                //  赋值spuId
                spuImage.setSpuId(spuInfo.getId());
                spuImageMapper.insert(spuImage);
            });
        }

        //  获取海报信息.
        List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
        if(!CollectionUtils.isEmpty(spuPosterList)){
            spuPosterList.forEach(spuPoster -> {
                ///  赋值spuId
                spuPoster.setSpuId(spuInfo.getId());
                spuPosterMapper.insert(spuPoster);
            });
        }

        //  销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (!CollectionUtils.isEmpty(spuSaleAttrList)){
            spuSaleAttrList.forEach(spuSaleAttr -> {
                //  赋值spuId
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);

                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (!CollectionUtils.isEmpty(spuSaleAttrValueList)){
                    spuSaleAttrValueList.forEach(spuSaleAttrValue -> {
                        //  赋值spuId
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        //  sale_attr_name
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    });
                }
            });
        }
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        //  返回数据
        return baseSaleAttrMapper.selectList(null);
    }

    @Override
    public IPage getSpuInfoList(Page<SpuInfo> supInfoPage, Long category3Id) {
        //  select * from spu_info where category3_id = 61 order by id desc limit 0,2;
        LambdaQueryWrapper<SpuInfo> spuInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        spuInfoLambdaQueryWrapper.eq(SpuInfo::getCategory3Id, category3Id);
        spuInfoLambdaQueryWrapper.orderByDesc(SpuInfo::getId);
        //SpuInfoMapper
        return spuInfoMapper.selectPage(supInfoPage, spuInfoLambdaQueryWrapper);
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        // select * from base_attr_value where attr_id = 1;
        LambdaQueryWrapper<BaseAttrValue> baseAttrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        baseAttrValueLambdaQueryWrapper.eq(BaseAttrValue::getAttrId, attrId);
        return baseAttrValueMapper.selectList(baseAttrValueLambdaQueryWrapper);
    }

    @Override
    //只要有异常就回滚数据，如果没有 rollbackFor = Exception.class 默认 RuntimeException

    @Transactional(rollbackFor = Exception.class)
    public void saveAttrInfoList(BaseAttrInfo baseAttrInfo) {
        //  保存：id null  修改  id!=null
        if (baseAttrInfo.getId() != null) {
            //  修改: base_attr_info 或 base_attr_value:
            baseAttrInfoMapper.updateById(baseAttrInfo);
            //  先删除，再新增： delete from base_attr_value where attr_id = ?;
            LambdaQueryWrapper<BaseAttrValue> baseAttrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            baseAttrValueLambdaQueryWrapper.eq(BaseAttrValue::getAttrId, baseAttrInfo.getId());
            baseAttrValueMapper.delete(baseAttrValueLambdaQueryWrapper);
        } else {
            //  base_attr_info 平台属性数据保存
            baseAttrInfoMapper.insert(baseAttrInfo);
        }
        /*
            base_attr_info
            base_attr_value
            使用mapper
            for循环少写。多写拉姆达表达式
         */

        // @TableId(type = IdType.AUTO) 表示获取到插入之后的主键值！
        //获取到平台属性数据
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        // 将attrValueList放到base_attr_value表中
        if (!CollectionUtils.isEmpty(attrValueList)) {
            //函数式接口 复制小括号 写死右箭头 落地大括号！
            //t:attrValueList 集合泛型
            attrValueList.forEach((baseAttrValue) -> {
                //  attr_id 赋值 = base_attr_info.id;
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                //  调用插入方法
                baseAttrValueMapper.insert(baseAttrValue);
            });
        }
    }

    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        /*
        #多表联查
        select *
        from base_attr_info bai
                 inner join base_attr_value bav on bai.id = bav.attr_id
        where (category_id = ? and category_level = 1
            or category_id = ? and category_level = 2
            or category_id = ? and category_level = 3);
         */
        //多表关联mapper的选择：使用一的mapper！
        return baseAttrInfoMapper.selectAttrInfoList(category1Id, category2Id, category3Id);
    }
}
