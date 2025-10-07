package org.pragma.creditya.consumer.machine.config;

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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class MachineRestConsumerConfig {

    private final String url;
    private final int timeout;

    private final Logger logger = LoggerFactory.getLogger(MachineRestConsumerConfig.class);

    public MachineRestConsumerConfig(@Value("${adapter.restconsumer.machine.url}") String url,
                                     @Value("${adapter.restconsumer.machine.timeout}") int timeout) {
        logger.info("[infra.rest-consumer.machine] (construct) extract env vars payload=[ url:{}, timeout:{} ]", url, timeout);
        this.url = url;
        this.timeout = timeout;
    }

    @Bean("MachineGetWebClient")
    public WebClient getWebClient(WebClient.Builder builder) {
        logger.info("[infra.rest-consumer.machine] (getWebClient) consume user-service to check identify payload [ url:{}, timeout:{} ]", url, timeout);

        return builder
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .clientConnector(getClientHttpConnector())
            .build();
    }


    private ClientHttpConnector getClientHttpConnector() {
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
