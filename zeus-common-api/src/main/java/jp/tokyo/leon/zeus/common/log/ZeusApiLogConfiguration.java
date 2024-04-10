package jp.tokyo.leon.zeus.common.log;

 import jp.tokyo.leon.zeus.common.log.request.DefaultRequestInfoProvider;
import jp.tokyo.leon.zeus.common.log.request.RequestInfoProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
    //@ConditionalOnBean(ServletHttpHandlerAdapter.class)
    public RequestInfoProvider requestInfoProvider() {
        return new DefaultRequestInfoProvider();
    }

    @Bean
    public ZeusApiLogAspect zeusApiLogAspect(EnableLogResolver enableLogResolver, RequestInfoProvider requestInfoProvider) {
        return new ZeusApiLogAspect(enableLogResolver, requestInfoProvider);
    }
}
