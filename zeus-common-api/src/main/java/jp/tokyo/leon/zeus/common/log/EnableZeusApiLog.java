package jp.tokyo.leon.zeus.common.log;

import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leon
 * @date 2024/4/9 18:58
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ZeusApiLogConfiguration.class})
public @interface EnableZeusApiLog {

    @AliasFor("basePackages")
    String[] value() default {};
    /**
     * Specifies the base packages to scan for components to apply logging.
     * If not specified, logging will be applied to all components in the application.
     *
     * @return the base packages to scan
     */
    String[] basePackages() default {};

    /**
     * Specifies the level of logging to apply.
     * Default value is LogLevel.DEBUG.
     *
     * @return the log level
     */
    LogLevel level() default LogLevel.DEBUG;

    /**
     * Specifies whether to include method arguments in log messages.
     * Default value is true.
     *
     * @return true if method arguments should be included, false otherwise
     */
    boolean includeArguments() default true;

    /**
     * Specifies whether to include method return values in log messages.
     * Default value is true.
     *
     * @return true if method return values should be included, false otherwise
     */
    boolean includeReturnValue() default true;

    /**
     * Specifies whether to include timestamps in log messages.
     * Default value is true.
     *
     * @return true if timestamps should be included, false otherwise
     */
    boolean includeTimestamp() default true;
}
