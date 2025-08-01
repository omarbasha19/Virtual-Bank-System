package com.VirtualBankingSystem.Common.Logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class RequestResponseLoggingFilter implements Filter {

    @Autowired
    private LoggingService loggingService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Wrap the request and response to cache their content
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        
        try {
            // Log the request
            logRequest(requestWrapper);
            
            // Process the request
            chain.doFilter(requestWrapper, responseWrapper);
            
            // Log the response after processing
            logResponse(responseWrapper);
        } finally {
            // Copy the cached response back to the original response
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        // Log only if there's body content
        if (request.getContentAsByteArray().length > 0) {
            try {
                String requestBody = new String(request.getContentAsByteArray());
                loggingService.logRequest(requestBody);
            } catch (Exception e) {
                System.err.println("Failed to log request: " + e.getMessage());
            }
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        // Log only if there's body content
        if (response.getContentAsByteArray().length > 0) {
            try {
                String responseBody = new String(response.getContentAsByteArray());
                loggingService.logResponse(responseBody);
            } catch (Exception e) {
                System.err.println("Failed to log response: " + e.getMessage());
            }
        }
    }
}
