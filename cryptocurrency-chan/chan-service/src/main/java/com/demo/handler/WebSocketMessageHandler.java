package com.demo.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.vo.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Objects;

@Component
@Slf4j
public class WebSocketMessageHandler implements IWsMsgHandler {

    /**
    * @Author Yaozheng Wang
    * @Description Handle the http response
    * @Date 2022/6/15 23:46
    **/
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) {
        String clientIp = httpRequest.getClientIp();
        log.info("Connect with {} client", clientIp);
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) {
        log.info("Hand shake success");
    }

    /**
    * @Author Yaozheng Wang
    * @Description Hand the bytes
    * @Date 2022/6/15 23:49
    **/
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) {
        return null;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Hand the close
    * @Date 2022/6/15 23:49
    **/
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) {
        Tio.remove(channelContext, "remove channelContext");
        return null;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Handle the text
    * @Date 2022/6/15 23:49
    **/
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {

        if(Objects.equals("ping",text)){
            return "pong" ;
        }
        log.info(text);
        JSONObject payload = JSON.parseObject(text);

        String sub = payload.getString("sub"); // 要订阅的组
        String req = payload.getString("req"); // 当前的request(预留字段)
        String cancel = payload.getString("cancel");// 要取消订阅的组
        String id = payload.getString("id"); // 订阅的id(预留字段)
        String authorization = payload.getString("authorization");

        if(StringUtils.hasText(sub)){ // 订阅的组有内容
            Tio.bindGroup(channelContext,sub);
        }
        if(StringUtils.hasText(cancel)){
            Tio.unbindGroup(cancel,channelContext) ;
        }
        if(StringUtils.hasText(authorization) && authorization.startsWith("bearer ")){
            String token = authorization.replaceAll("bearer ","") ;
            // 2 查询我们的菜单数据
            Jwt jwt = JwtHelper.decode(token);
            String jwtJsonStr = jwt.getClaims();
            JSONObject jwtJson = JSON.parseObject(jwtJsonStr);
            String userId = jwtJson.getString("user_name") ;
            // 有用户时绑定用户
            Tio.unbindUser(channelContext.getTioConfig(),userId);
        }
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setCanceled(cancel) ;
        responseEntity.setSubbed(sub) ;
        responseEntity.setId(id) ;
        responseEntity.setStatus("OK") ;
        responseEntity.setCh(sub) ;
        responseEntity.setEvent(req) ;
        return responseEntity.build() ;
    }
}
