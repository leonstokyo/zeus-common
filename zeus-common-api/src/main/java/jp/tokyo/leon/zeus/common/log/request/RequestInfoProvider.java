package jp.tokyo.leon.zeus.common.log.request;

/**
 * @author leon
 * @date 2024/4/10 23:30
 */
public interface RequestInfoProvider {
    String getRequestUri();

    String getRequestUrl();
}
