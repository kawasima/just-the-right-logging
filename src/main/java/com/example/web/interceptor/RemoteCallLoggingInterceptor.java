package com.example.web.interceptor;

import com.example.logging.Markers;
import io.micrometer.common.lang.NonNullApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@NonNullApi
public class RemoteCallLoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger("restClient");
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        MDC.put("exchangeType", "request");
        MDC.put("method", request.getMethod().name());
        MDC.put("uri", request.getURI().toString());
        MDC.put("headers", Objects.toString(request.getHeaders()));
        LOG.info(Markers.REMOTE_CALL, "{}", new String(body, StandardCharsets.UTF_8));

        try {
            ClientHttpResponse response = execution.execute(request, body);
            MDC.put("exchangeType", "response");
            MDC.put("headers", Objects.toString(response.getHeaders()));
            LOG.info(Markers.REMOTE_CALL, "{}", new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
            return response;
        } finally {
            MDC.remove("exchangeType");
            MDC.remove("method");
            MDC.remove("uri");
            MDC.remove("headers");
        }
    }
}
