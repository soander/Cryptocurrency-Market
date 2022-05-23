package com.demo.model;

import com.demo.domain.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * @ClassName LoginResult
 * @Description return login result
 * @Author Yaozheng Wang
 * @Date 2022/5/20 13:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Login Result")
public class LoginResult {

    // Login's token
    @ApiModelProperty(value = "Login success token")
    private String token;

    // Menu information
    @ApiModelProperty(value = "Menu information")
    private List<SysMenu> menus;

    // Authorities information
    @ApiModelProperty(value = "Authorities information")
    private List<SimpleGrantedAuthority> authorities;

}
