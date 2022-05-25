package com.demo.valueObject;

import com.demo.domain.User;
import com.demo.domain.UserAuthAuditRecord;
import com.demo.domain.UserAuthInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "User's detailed authorization information")
public class UseAuthInfoVo  implements Serializable {

    @ApiModelProperty(value = "User")
    private User user;

    @ApiModelProperty(value = "User authorization information list")
    private List<UserAuthInfo> userAuthInfoList;

    @ApiModelProperty(value = "User authorization record list")
    private List<UserAuthAuditRecord> authAuditRecordList;
}
