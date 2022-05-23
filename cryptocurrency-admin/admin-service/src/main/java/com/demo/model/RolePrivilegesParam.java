package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(value = "About Role's ID and Privilege")
public class RolePrivilegesParam {

    @ApiModelProperty(value = "Role's ID")
    private Long roleId;

    @ApiModelProperty(value = "Role's Privilege by ID")
    private List<Long> privilegeIds = Collections.emptyList();
}
