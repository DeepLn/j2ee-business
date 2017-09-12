package com.midea.meicloud.entitybase;

import com.midea.meicloud.common.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 13:35 2017-8-29
 */
public class UserInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        Long userId = ThreadInfo.instance().getUserId();
        headers.add(Constants.userId, userId.toString());
        return execution.execute(request, body);
    }
}
