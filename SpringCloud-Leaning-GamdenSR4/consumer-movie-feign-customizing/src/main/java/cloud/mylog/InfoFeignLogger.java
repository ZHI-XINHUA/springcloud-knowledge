package cloud.mylog;

import org.slf4j.Logger;

/**
 * @ClassName InfoFeignLogger
 * @Description TODO
 * @Author xh.zhi
 * @Date 2019-9-26 17:06
 * @Version 1.0
 **/
public class InfoFeignLogger extends feign.Logger {

    private final Logger logger;

    public InfoFeignLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format(methodTag(configKey) + format, args));
        }
    }

}
