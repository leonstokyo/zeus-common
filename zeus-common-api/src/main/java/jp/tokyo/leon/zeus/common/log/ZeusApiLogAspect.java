package jp.tokyo.leon.zeus.common.log;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author leon
 * @date 2024/4/9 18:59
 */
@Aspect
@Component
public class ZeusApiLogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    @Pointcut("within(jp.tokyo.leon.zeus)")
    public void controllerPointcut() {
    }

    @Around("logPointCut()")
    public Object recordLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 先获取方法注解，没有日志注解不记录日志
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        ZeusApiLog annotation = method.getAnnotation(ZeusApiLog.class);
        if (Objects.isNull(annotation)) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }

        ZeusApiLogEntity apiLog = new ZeusApiLogEntity();

        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long end = System.currentTimeMillis();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        String url = request.getRequestURL().toString();
        apiLog.setSpendTime((int)(start - end) / 1000);
        apiLog.setUri(request.getRequestURI());
        apiLog.setUrl(url);
        apiLog.setDescription(annotation.value());

        logger.info("{}", apiLog);

        return result;

    }
}
