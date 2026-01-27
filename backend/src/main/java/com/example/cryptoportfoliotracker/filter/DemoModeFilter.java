package com.example.cryptoportfoliotracker.filter;

import com.example.cryptoportfoliotracker.service.DemoService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DemoModeFilter implements Filter {

    @Autowired
    private DemoService demoService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean isDemo = "true".equals(httpRequest.getHeader("X-DEMO-MODE"));

        if (isDemo) {
            // Block writes (e.g., POST/PUT/DELETE)
            if (!httpRequest.getMethod().equals("GET")) {
                httpResponse.setStatus(403);
                httpResponse.getWriter().write("Demo mode: Write operations not allowed");
                return;
            }
            // Enable mock data in service
            demoService.setDemoMode(true);
        } else {
            demoService.setDemoMode(false);
        }

        chain.doFilter(request, response);
    }
}
