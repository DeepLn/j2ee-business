package com.midea.meicloud.filter;

import com.midea.meicloud.common.Constants;
import com.midea.meicloud.common.TempUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Order(1)
//重点
@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*")
@Component
public class UserLoginFilter implements Filter {
    @Value("${user-security-filter.default}")
    private boolean default_public;
    @Value("${user-security-filter.exceptions}")
    private String filterUrls;

    private static List<String> patterns = new ArrayList<String>();

    private boolean isInclude(String url) {
        for (String pattern : patterns) {
            if (url.indexOf(pattern) == 0) {
                return !default_public;
            }
        }
        return default_public;

//        patterns.add(Pattern.compile("/*/api/*"));
//        patterns.add(Pattern.compile("/swagger-ui/*"));
//        patterns.add(Pattern.compile("/web/*"));
//        patterns.add(Pattern.compile("/home.html"));
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        String[] strArr = filterUrls.split(",");
        for(String str : strArr){
            patterns.add(str);
        }
        patterns.add("/error.html");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        //跨域
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");

        String url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (isInclude(url)) {
            //不需要进行登录验证的直接访问
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }
        do {
            HttpSession session = httpRequest.getSession();
            TempUserInfo tempUserInfo = (TempUserInfo)session.getAttribute(Constants.sessionAttributeUserInfo);
            if(tempUserInfo == null){
                break;
            }
            tempUserInfo.setLastRequestDate(new Date());
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        } while (false);
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: ");
    }

    public void destroy() {

    }
}
