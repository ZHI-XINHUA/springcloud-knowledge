package cloud.mylog;

import feign.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignLoggerFactory;

/**
 * @ClassName InfoFeignLoggerFactory
 * @Description TODO
 * @Author xh.zhi
 * @Date 2019-9-26 17:05
 * @Version 1.0
 **/
public class InfoFeignLoggerFactory implements FeignLoggerFactory {
    @Override
    public Logger create(Class<?> type) {
        return new InfoFeignLogger(LoggerFactory.getLogger(type));
    }
}
