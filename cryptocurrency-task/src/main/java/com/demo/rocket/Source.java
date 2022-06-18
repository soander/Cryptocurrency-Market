package com.demo.rocket;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {

    @Output("group_message_out")
    MessageChannel subscribeGroupOutput();
}
