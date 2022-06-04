package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.WebConfig;
import com.demo.mapper.WebConfigMapper;
import com.demo.service.WebConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService{

    /**
    * @Author Yaozheng Wang
    * @Description Find web config by page
    * @Date 2022/6/4 15:36
    **/
    @Override
    public Page<WebConfig> findByPage(Page<WebConfig> page, String name, String type) {

        return page(page ,new LambdaQueryWrapper<WebConfig>()
            .like(!StringUtils.isEmpty(name), WebConfig::getName, name)
            .eq(!StringUtils.isEmpty(type), WebConfig::getType, type)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Find web banners
    * @Date 2022/6/4 15:36
    **/
    @Override
    public List<WebConfig> findWebBanners() {
        return list(new LambdaQueryWrapper<WebConfig>()
            .eq(WebConfig::getType,"WEB_BANNER")
            .eq(WebConfig::getStatus,1)
            .orderByAsc(WebConfig::getSort)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Get pc banners
    * @Date 2022/6/4 15:35
    **/
    @Override
    public List<WebConfig> getPcBanners() {
        return list(new LambdaQueryWrapper<WebConfig>()
            .eq(WebConfig::getType,"WEB_BANNER")
            .eq(WebConfig::getStatus,1)
            .orderByAsc(WebConfig::getSort)
        );
    }
}
