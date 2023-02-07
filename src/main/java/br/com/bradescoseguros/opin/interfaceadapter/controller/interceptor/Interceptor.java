package br.com.bradescoseguros.opin.interfaceadapter.controller.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class Interceptor implements HandlerInterceptor {

    private static final String TRACE_ID_HEADER = "trace-id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = "";
        String headerTraceId = request.getHeader(TRACE_ID_HEADER);

        if(headerTraceId == null || headerTraceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        } else {
            traceId = headerTraceId;
        }

        MDC.put("TRACE_ID", traceId);
        return true;
    }

}
