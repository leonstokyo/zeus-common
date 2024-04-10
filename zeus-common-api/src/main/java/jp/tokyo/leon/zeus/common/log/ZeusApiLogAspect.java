package jp.tokyo.leon.zeus.common.log;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    private Set<String> scannedPackage = new HashSet<>();

    @PostConstruct
    public void init() {
        scannedPackage = enableLogResolver.findClassesWithAnnotation(EnableZeusApiLog.class);
    }


    public ZeusApiLogAspect(EnableLogResolver enableLogResolver) {
        this.enableLogResolver = enableLogResolver;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object recordLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        if (!scannedPackage.isEmpty() && !isPackageOrSubpackage(scannedPackage, method.getDeclaringClass().getName())) {
            return null;
        }

        System.out.println(method.getDeclaringClass().getName());
        ZeusApiLog annotation = method.getAnnotation(ZeusApiLog.class);
        String description = "";
        if (Objects.nonNull(annotation)) {
            description = annotation.value();
        }

        ZeusApiLogEntity apiLog = new ZeusApiLogEntity();

        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long end = System.currentTimeMillis();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        String url = request.getRequestURL().toString();
        apiLog.setSpendTime(end - start);
        apiLog.setUri(request.getRequestURI());
        apiLog.setUrl(url);
        apiLog.setDescription(description);

        logger.info("{}", apiLog);

        return result;
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
