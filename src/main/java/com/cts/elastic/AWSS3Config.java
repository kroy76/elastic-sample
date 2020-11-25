package com.cts.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Map;

/**
 * @author Biswanath Mukherjee
 */

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.cts.elastic")
public class AWSS3Config {
	
    @Value("${amazon.elasticsearch.endpoint}")
    private String elasticSearcEndpoint ;
    
    @Value("${amazon.elasticsearch.username}")
    private String elasticSearcUser;

    @Value("${amazon.elasticsearch.password}")
    private String elasticSearcPassword ;

    private static Logger logger = LoggerFactory.getLogger(AWSS3Config.class);
	

    /*@Bean
    public RestHighLevelClient restHighLevelClient() {
        ClientConfiguration clientConfiguration 
            = ClientConfiguration.builder()
                .connectedTo("search-oriental-elasticsearch-local-lqa3qycehghvfxe3a7pgcmtrgq.us-east-1.es.amazonaws.com:443")
                .withBasicAuth(elasticSearcUser, elasticSearcPassword)
                .build();
 
        return RestClients.create(clientConfiguration).rest();
    }*/
    
    @Bean
    public RestHighLevelClient client() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        logger.info("Elasticsearch "+elasticSearcEndpoint);
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticSearcUser, elasticSearcPassword));
        RestClientBuilder builder = RestClient.builder(new HttpHost(elasticSearcEndpoint,443,"https"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        return new RestHighLevelClient(builder);
    }

 
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }	
	
}