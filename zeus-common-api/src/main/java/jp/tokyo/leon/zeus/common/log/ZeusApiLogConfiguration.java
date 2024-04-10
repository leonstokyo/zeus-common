package jp.tokyo.leon.zeus.common.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author leon
 * @date 2024/4/10 13:11
 */
@Configuration
public class ZeusApiLogConfiguration {

    @Bean
    public EnableLogResolver enableLogResolver() {
        return new EnableLogResolver();
    }

    @Bean
    public ZeusApiLogAspect zeusApiLogAspect(EnableLogResolver eenableLogResolver) {
        return new ZeusApiLogAspect(eenableLogResolver);
    }
}
