package com.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.domain.WebConfig;
import com.demo.model.R;
import com.demo.service.WebConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@RestController
@Api(tags = "webConfig's controller")
@RequestMapping("/webConfigs")
public class WebConfigController {

    @Autowired
    private WebConfigService webConfigService ;

    @GetMapping
    @ApiOperation(value ="Query webConfig by page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "webConfig's name"),
            @ApiImplicitParam(name = "type",value = "webConfig's type"),
            @ApiImplicitParam(name = "current",value = "current page"),
            @ApiImplicitParam(name = "size",value = "current number"),
    })
    @PreAuthorize("hasAuthority('web_config_query')")
    public R<Page<WebConfig>> findByPage(@ApiIgnore Page<WebConfig> page, String name, String type) {
        Page<WebConfig> webConfigPage = webConfigService.findByPage(page, name, type);
        return R.ok(webConfigPage);
    }

    @PostMapping
    @ApiOperation(value = "Add a WebConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webConfig" ,value = "webConfig's json information")
    })
    @PreAuthorize("hasAuthority('web_config_create')")
    public R add(@RequestBody  @Validated  WebConfig webConfig) {
        boolean save = webConfigService.save(webConfig);
        if(save) {
            return R.ok();
        }
        return R.fail("Add fail");
    }


    @PatchMapping
    @ApiOperation(value = "Update a WebConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webConfig" ,value = "webConfig's json information")
    })
    @PreAuthorize("hasAuthority('web_config_update')")
    public R update(@RequestBody  @Validated  WebConfig webConfig) {
        boolean save = webConfigService.updateById(webConfig);
        if(save) {
            return R.ok();
        }
        return R.fail("Update fail");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "Delete WebConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids" ,value = "Delete ids")
    })
    @PreAuthorize("hasAuthority('web_config_delete')")
    public R update(@RequestBody  String[] ids) {
      if(ids == null || ids.length == 0) {
          return R.fail("Need id when you want to delete web config");
      }
        boolean b = webConfigService.removeByIds(Arrays.asList(ids));
      if(b) {
          return R.ok();
      }
      return R.fail("Delete fail");
    }

    @GetMapping("/banners")
    @ApiOperation(value = "Get banners")
    public R<List<WebConfig>> banners() {
        List<WebConfig> banners = webConfigService.getPcBanners();
        return R.ok(banners);
    }
}
