package com.demo.match;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "spring.match")
public class MatchEngineProperties {

    // Symbols
    private Map<String, CoinScale> symbols;

    @Data
    public static class CoinScale {
        private int coinScale; // Coin scale
        private int baseCoinScale; // Base coin scale
    }
}
