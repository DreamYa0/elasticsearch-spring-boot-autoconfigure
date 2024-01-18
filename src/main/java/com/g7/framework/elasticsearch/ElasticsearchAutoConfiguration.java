package com.g7.framework.elasticsearch;

import com.g7.framework.elasticsearch.properties.ElasticsearchProperties;
import org.apache.http.Header;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;

/**
 * @author dreamyao
 * @title
 * @date 2021/11/29 2:42 下午
 * @since 1.0.0
 */
@Configuration
@EnableElasticsearchAuditing
@EnableElasticsearchRepositories(basePackages = "com.**.elasticsearch.**")
@EnableConfigurationProperties({ElasticsearchRestClientProperties.class, ElasticsearchProperties.class})
public class ElasticsearchAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchAutoConfiguration.class);
    private final ElasticsearchProperties elasticsearchProperties;

    public ElasticsearchAutoConfiguration(ElasticsearchProperties elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RestHighLevelClient restHighLevelClient(RestClientBuilder restClientBuilder) {

        restClientBuilder.setHttpClientConfigCallback(httpBuilder -> {
            final BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
            httpBuilder.setDefaultCredentialsProvider(basicCredentialsProvider);
            try {
                final SSLContext ssl = new SSLContextBuilder().loadTrustMaterial(null,
                        (unused1, unused2) -> elasticsearchProperties.isSsl()).build();
                httpBuilder.setSSLContext(ssl);
            } catch (Exception e) {
                logger.error("Elasticsearch connect ssl failed", e);
            }
            return httpBuilder;
        });

        final String key = elasticsearchProperties.getKey();
        if (!StringUtils.isEmpty(key)) {
            restClientBuilder.setDefaultHeaders(new Header[]{new BasicHeader("Authorization",
                    "ApiKey " + key)});
        }

        return new RestHighLevelClient(restClientBuilder);
    }
}
