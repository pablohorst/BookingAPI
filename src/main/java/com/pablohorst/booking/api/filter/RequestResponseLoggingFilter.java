package com.pablohorst.booking.api.filter;

import com.pablohorst.booking.api.logging.LoggingMessage;
import com.pablohorst.booking.api.logging.MultiReadHttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Once per request Filter to log request and response information
 *
 * @author Pablo Horst
 */
@Component
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            logRequest(request);
            chain.doFilter(request, response);
        } finally {
            logResponse(request, response);
        }
    }

    public void logRequest(HttpServletRequest request) throws IOException {
        LoggingMessage requestLoggingMessage = LoggingMessage
                .builder()
                .type("request")
                .method(request.getMethod())
                .url(request.getRequestURI())
                .headers(requestHeadersToMap(request))
                .build();

        String body;

        try {
            body = IOUtils.toString(request.getReader());
        } catch (IllegalStateException e) {
            body = "";
        }

        if (!body.isEmpty()) {
            body = body.replaceAll("[\\s]*", "");
            requestLoggingMessage.setBody(body);
        }

        log.info(requestLoggingMessage.toJson());
    }

    public void logResponse(HttpServletRequest request, HttpServletResponse response) {
        LoggingMessage responseLoggingMessage = LoggingMessage
                .builder()
                .type("response")
                .statusCode(String.valueOf(response.getStatus()))
                .url(request.getRequestURI())
                .headers(responseHeadersToMap(response))
                .build();

        String body = getResponseBody(response);

        if (body != null && !body.isEmpty()) {
            responseLoggingMessage.setBody(body);
        }

        log.info(responseLoggingMessage.toJson());
    }

    private Map<String, String> requestHeadersToMap(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();

        while (names.hasMoreElements()) {
            String headerName = names.nextElement();
            headersMap.put(headerName, request.getHeader(headerName));
        }

        return headersMap;
    }

    private Map<String, String> responseHeadersToMap(HttpServletResponse response) {
        Map<String, String> headersMap = new HashMap<>();
        Collection<String> names = response.getHeaderNames();

        names.forEach(headerName -> headersMap.put(headerName, response.getHeader(headerName)));

        return headersMap;
    }

    private String getResponseBody(HttpServletResponse response) {
        String body = null;

        if (response instanceof MultiReadHttpServletResponse) {
            body = ((MultiReadHttpServletResponse) response).getContent();
        } else {
            try {
                body = (new MultiReadHttpServletResponse(response)).getContent();
            } catch (Exception e) {
                log.error("Error trying to cast response to MultiReadHttpServletResponse to access body content", e);
            }
        }

        return body;
    }
}