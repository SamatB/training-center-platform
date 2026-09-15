package com.training.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Глобальный фильтр для логирования входящих запросов в API Gateway.
 *
 * <p>Так как класс реализует {@link GlobalFilter} и является Spring-компонентом,
 * фильтр автоматически применяется ко всем запросам, проходящим через API Gateway.</p>
 *
 * <p>До передачи запроса дальше фильтр:</p>
 * <ul>
 *     <li>получает HTTP-метод запроса (GET, POST, PUT и т.д.);</li>
 *     <li>получает путь запроса;</li>
 *     <li>получает IP-адрес клиента;</li>
 *     <li>записывает эту информацию в лог.</li>
 * </ul>
 *
 * <p>После выполнения своей логики фильтр вызывает
 * {@code chain.filter(exchange)}, передавая запрос дальше по цепочке
 * фильтров и затем в нужный микросервис.</p>
 *
 * <p>{@code Mono<Void>} используется потому, что Spring Cloud Gateway
 * работает на реактивном стеке WebFlux. На данном этапе достаточно понимать,
 * что возвращаемый Mono представляет продолжение обработки запроса.</p>
 * <p>
 * Схема работы:
 * Client -> API Gateway -> LoggingFilter -> Route -> Microservice
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getURI().getPath();
        String clientIp = exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress();

        long startTime = System.currentTimeMillis();

        // Логируем входящий запрос ДО передачи в микросервис
        log.info(
                "ЗАПРОС: {} {} | IP: {}",
                method,
                path,
                clientIp
        );

        // Передаём запрос дальше по цепочке
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    // Этот код выполняется ПОСЛЕ обработки запроса
                    var status = exchange.getResponse().getStatusCode();
                    long duration = System.currentTimeMillis() - startTime;

                    log.info(
                            "ОТВЕТ: {} {} -> {} | {} ms",
                            method,
                            path,
                            status,
                            duration
                    );
                }));
    }
}