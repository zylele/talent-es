package com.talent.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.host}")
    private String host;//elasticsearch的地址

    @Value("${spring.elasticsearch.port}")
    private Integer port;//elasticsearch的端口 elasticsearch_le

    private static final Logger LOG = LogManager.getLogger(ElasticSearchConfig.class);

    @Bean
    public TransportClient client(){
        TransportClient client = null;
        try {
            Settings settings = Settings.builder()
                    .put("client.transport.ignore_cluster_name", true)
                    .put("client.transport.sniff", true).build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            LOG.error("创建elasticsearch客户端失败 e={}", e);
        }
        LOG.info("创建elasticsearch客户端成功");
        return client;
    }

}
