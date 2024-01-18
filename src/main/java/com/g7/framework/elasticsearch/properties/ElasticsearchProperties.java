package com.g7.framework.elasticsearch.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dreamyao
 * @title
 * @date 2021/11/29 2:44 下午
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "spring.elasticsearch.rest.auth")
public class ElasticsearchProperties {

    private boolean ssl = true;
    /**
     * 授权KEY
     */
    private String key;

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
