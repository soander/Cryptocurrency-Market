package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.WebConfig;

import java.util.List;

public interface WebConfigService extends IService<WebConfig> {

    // Find web config by page
    Page<WebConfig> findByPage(Page<WebConfig> page, String name, String type);

    // Find web banners
    List<WebConfig> findWebBanners();

    // Get pc banners
    List<WebConfig> getPcBanners();
}
