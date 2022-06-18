package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload {

    private String userId;

    @NonNull
    private String channel;

    @NonNull
    private String body;
}