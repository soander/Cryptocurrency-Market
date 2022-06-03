package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.CoinType;
import com.demo.mapper.CoinTypeMapper;
import com.demo.service.CoinTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CoinTypeServiceImpl extends ServiceImpl<CoinTypeMapper, CoinType> implements CoinTypeService {

    /**
    * @Author Yaozheng Wang
    * @Description Query Coin Type List
    * @Date 2022/5/31 10:18
    * @Param page and coin code
    * @Return coin list
    **/
    @Override
    public Page<CoinType> findByPage(Page<CoinType> page, String code) {
        return page(page, new LambdaQueryWrapper<CoinType>()
                                .like(!StringUtils.isEmpty(code), CoinType::getCode,code));
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query Coin Type List by status
    * @Date 2022/5/31 10:31
    * @Param The coin status
    * @Return coin list
    **/
    @Override
    public List<CoinType> listByStatus(Byte status) {
        return list(new LambdaQueryWrapper<CoinType>().eq(status!=null, CoinType::getStatus,status));
    }
}

