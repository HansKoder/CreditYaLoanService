package org.pragma.creditya.consumer.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class RestConsumerConfig {

    private final String url;

    private final int timeout;

    private final Logger logger = LoggerFactory.getLogger(RestConsumerConfig.class);

    public RestConsumerConfig(@Value("${adapter.restconsumer.url}") String url,
                              @Value("${adapter.restconsumer.timeout}") int timeout) {
        logger.info("[infra.rest-consume] (construct) extract env vars payload=[ url:{}, timeout:{} ]", url, timeout);
        this.url = url;
        this.timeout = timeout;
    }

    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        logger.info("[infra.rest-consume] (getWebClient) consume user-service to check identify payload [ url:{}, timeout:{} ]", url, timeout);

        String urlCheck = "http://creditya-user-service:9081/api/v1/users/verify-ownership-customer";
        // String urlCheck = "http://creditya-user-service:9081";

        logger.info("[infra.rest-consume] (getWebClient) compare url and url constant custom [ url:{}, url-custom:{}, equals:{} ]", url, urlCheck, url.equals(urlCheck));

        return builder
            .baseUrl(urlCheck)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .clientConnector(getClientHttpConnector())
            .build();
    }


    private ClientHttpConnector getClientHttpConnector() {
        /*
        IF YO REQUIRE APPEND SSL CERTIFICATE SELF SIGNED: this should be in the default cacerts trustore
        */
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                }));
    }

}
