package com.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.WebConfig;

import java.util.List;

public interface WebConfigService extends IService<WebConfig> {

    /**
     * find By Page
     * @param page
     * @param name
     * @param type
     * @return
     */
    Page<WebConfig> findByPage(Page<WebConfig> page, String name, String type);

    List<WebConfig> findWebBanners();

    List<WebConfig> getPcBanners();
}
