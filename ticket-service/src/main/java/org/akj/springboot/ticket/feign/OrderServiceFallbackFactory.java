package org.akj.springboot.ticket.feign;

import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class OrderServiceFallbackFactory implements FallbackFactory<OrderClient> {
    @Autowired
    private OrderServiceFallback orderServiceFallback;

    @Override
    public OrderClient create(Throwable throwable) {
        log.error(throwable.getMessage());

        return orderServiceFallback;
    }
}
