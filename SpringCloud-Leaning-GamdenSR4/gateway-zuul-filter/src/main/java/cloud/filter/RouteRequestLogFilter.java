package cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义前置过滤器
 */
@Component
public class RouteRequestLogFilter extends ZuulFilter {
    /**
     * 返回过滤器的类型，pre、route、post、error 过滤器类型
     * @return
     */
    @Override
    public String filterType() {
        return "error";
    }

    /**
     * 指定过滤器的执行顺序，数字越大，优先级越低
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 判断是否执行这过滤器，true表示执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 编写过滤器的具体业务逻辑
     * @return
     */
    @Override
    public Object run() {
        //获取上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        System.out.println("RouteRequestLogFilter>>>>>>请求地址："+request.getRequestURL().toString()+"    请求方法："+request.getMethod());
        return null;
    }
}
