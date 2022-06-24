package com.pablohorst.booking.api.filter;

import com.pablohorst.booking.api.logging.MultiReadHttpServletRequest;
import com.pablohorst.booking.api.logging.MultiReadHttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to wrap the request and response with a cache so we can read the content multiple times
 *
 * @author Pablo Horst
 */
@Component
@ConditionalOnProperty(name = "ols.logging.request-response.enabled", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RequestResponseCacheContentFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        MultiReadHttpServletRequest requestWrapper = new MultiReadHttpServletRequest(request);
        MultiReadHttpServletResponse responseWrapper = new MultiReadHttpServletResponse(response);

        filterChain.doFilter(requestWrapper, responseWrapper);
    }
}
