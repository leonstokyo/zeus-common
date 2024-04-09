package jp.tokyo.leon.zeus.common.log;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author leon
 * @date 2024/4/9 22:28
 */
@Component
public class EnableLogResolver {

    private final ApplicationContext applicationContext;

    public EnableLogResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean shouldEnableLogging(EnableZeusApiLog enableLog, Class<?> targetClass) {
        Set<String> basePackages = new HashSet<>(Arrays.asList(enableLog.basePackages()));

        if (basePackages.isEmpty()) {
            return true; // Enable logging for all components if no base packages specified
        }

        String className = targetClass.getName();
        for (String basePackage : basePackages) {
            if (className.startsWith(basePackage)) {
                return true; // Enable logging for components within specified base packages
            }
        }

        return false;
    }
}
