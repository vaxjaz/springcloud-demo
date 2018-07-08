package com.jay.li.springcloud.zuul.filter;


import com.jay.li.springcloud.zuul.util.ParseRequestUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jay
 * @date 2018/6/21
 */
@Component
public class AuthFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String body = ParseRequestUtils.getParamString(request);
        // todo 验签
        if (true) {
            // 验签通过
            currentContext.setSendZuulResponse(true);
        } else {
            // 验签失败
            currentContext.set("key", HttpServletResponse.SC_BAD_REQUEST);
            // 设置返回Body
            currentContext.setResponseBody("123123");
            currentContext.setSendZuulResponse(false);
        }
        return null;
    }
}
