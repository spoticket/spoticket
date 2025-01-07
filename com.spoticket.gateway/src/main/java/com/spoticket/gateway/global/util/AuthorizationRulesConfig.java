package com.spoticket.gateway.global.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "authorization")
public class AuthorizationRulesConfig {
    private List<Rule> rules;

    @Data
    public static class Rule {
        private String path;
        private List<String> method;
        private List<String> roles;
    }
}
