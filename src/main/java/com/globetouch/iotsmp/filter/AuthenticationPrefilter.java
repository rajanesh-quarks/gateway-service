package com.globetouch.iotsmp.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationPrefilter extends AbstractGatewayFilterFactory<AuthenticationPrefilter.Config> {

    public AuthenticationPrefilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Pre-processing
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        // Post-processing
                    }));
        };
    }


    @Getter
    @Setter
    public static class Config {
        private String name;
        private boolean apiAuthorization;
    }


}
