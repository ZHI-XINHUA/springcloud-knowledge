package com.zxh.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AccessFilter
 * @Description TODO
 * @Author xh.zhi
 * @Date 2019-9-29 10:35
 * @Version 1.0
 **/
@Component
public class AccessFilter extends ZuulFilter {
    /**
     * 返回过滤器的类型，pre、route、post、error 过滤器类型
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 指定过滤器的执行顺序，数字越大，优先级越低
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
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
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        System.out.println("PreRequestLogFilter>>>请求地址："+request.getRequestURL().toString()+"    请求方法："+request.getMethod());
        Object accessToken = request.getParameter("accessToken");
        if(accessToken==null){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        }

        System.out.println("验证通过......");
        return null;
    }
}
