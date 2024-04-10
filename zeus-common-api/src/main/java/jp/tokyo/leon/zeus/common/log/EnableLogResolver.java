package jp.tokyo.leon.zeus.common.log;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author leon
 * @date 2024/4/9 22:28
 */
public class EnableLogResolver {
    public Set<String> findClassesWithAnnotation(Class<? extends Annotation> annotation) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));

        Set<String> annotatedClasses = new HashSet<>();
        for (org.springframework.beans.factory.config.BeanDefinition beanDefinition : scanner.findCandidateComponents("")) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                System.out.println(clazz.getName());
                Annotation ann = clazz.getAnnotation(annotation);
                if (ann != null) {
                    Method method = ann.annotationType().getMethod("value");
                    String[] value = (String[]) method.invoke(ann);
                    Set<String> collect = Arrays.stream(value).collect(Collectors.toSet());
                    annotatedClasses.addAll(collect);
                }
            } catch (Exception e) {
                // 处理异常
                e.printStackTrace();
            }
        }
        return annotatedClasses;
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
