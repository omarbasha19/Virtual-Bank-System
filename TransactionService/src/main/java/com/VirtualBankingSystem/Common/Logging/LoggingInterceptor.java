package com.VirtualBankingSystem.Common.Logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Autowired
    private LoggingService loggingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // Wrap the request to make it re-readable
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        try {
            if (request instanceof ContentCachingRequestWrapper && response instanceof ContentCachingResponseWrapper) {
                ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
                ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
                
                // Log request
                byte[] requestBytes = requestWrapper.getContentAsByteArray();
                if (requestBytes.length > 0) {
                    String requestBody = new String(requestBytes);
                    Map<String, Object> requestLog = new HashMap<>();
                    requestLog.put("body", objectMapper.readTree(requestBody));
                    requestLog.put("path", request.getRequestURI());
                    requestLog.put("method", request.getMethod());
                    loggingService.logRequest(requestLog);
                }
                
                // Log response
                byte[] responseBytes = responseWrapper.getContentAsByteArray();
                if (responseBytes.length > 0) {
                    String responseBody = new String(responseBytes);
                    Map<String, Object> responseLog = new HashMap<>();
                    try {
                        responseLog.put("body", objectMapper.readTree(responseBody));
                    } catch (Exception e) {
                        responseLog.put("body", responseBody);
                    }
                    responseLog.put("status", response.getStatus());
                    loggingService.logResponse(responseLog);
                }
                
                // Important: Copy content back since we've consumed it
                responseWrapper.copyBodyToResponse();
            }
        } catch (Exception e) {
            System.err.println("Error in logging interceptor: " + e.getMessage());
        }
    }
}
