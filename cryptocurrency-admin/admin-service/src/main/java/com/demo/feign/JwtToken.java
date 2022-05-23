package com.demo.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName JwtToken
 * @Description The JwtToken class
 * @Author Yaozheng Wang
 * @Date 2022/5/20 13:53
 **/
@Data
public class JwtToken {

    // access_token
    @JsonProperty("access_token")
    private String accessToken;

    // The token type
    @JsonProperty("token_type")
    private String tokenType;

    // The refresh token
    @JsonProperty("refresh_token")
    private String refreshToken;

    // The expires time
    @JsonProperty("expires_in")
    private Long expiresIn;

    // The token scope
    private String scope;

    // The jti
    private String jti;
}
