package jp.tokyo.leon.zeus.common.log;

import jakarta.annotation.PostConstruct;
import jp.tokyo.leon.zeus.common.log.request.RequestInfoProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author leon
 * @date 2024/4/9 18:59
 */
@Aspect
public class ZeusApiLogAspect {

    private final EnableLogResolver enableLogResolver;

    private final RequestInfoProvider requestInfoProvider;

    private Set<String> scannedPackage = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        scannedPackage = enableLogResolver.findClassesWithAnnotation(EnableZeusApiLog.class);
    }

    public ZeusApiLogAspect(EnableLogResolver enableLogResolver, RequestInfoProvider requestInfoProvider) {
        this.enableLogResolver = enableLogResolver;
        this.requestInfoProvider = requestInfoProvider;
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object recordLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());

        if (!scannedPackage.isEmpty() && !isPackageOrSubpackage(scannedPackage, method.getDeclaringClass().getName())) {
            return result;
        }
        long end = System.currentTimeMillis();
        ZeusApiLogEntity apiLog = getZeusApiLogEntity(method, end, start);
        logger.info("{}", apiLog);
        return result;
    }

    private ZeusApiLogEntity getZeusApiLogEntity(Method method, long end, long start) {
        ZeusApiLog annotation = method.getAnnotation(ZeusApiLog.class);
        String description = "";
        if (Objects.nonNull(annotation)) {
            description = annotation.value();
        }

        ZeusApiLogEntity apiLog = new ZeusApiLogEntity();
        apiLog.setSpendTime(end - start);
        apiLog.setUri(requestInfoProvider.getRequestUri());
        apiLog.setUrl(requestInfoProvider.getRequestUrl());
        apiLog.setDescription(description);
        return apiLog;
    }

    private boolean isPackageOrSubpackage(Set<String> packageSet, String packageName) {
        for (String pkg : packageSet) {
            if (packageName.startsWith(pkg + ".") || packageName.equals(pkg)) {
                return true;
            }
        }
        return false;
    }
}
