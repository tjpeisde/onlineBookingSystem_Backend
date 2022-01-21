package com.tjpeisde.onlinebooking.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest; import javax.servlet.http.HttpServletResponse; import java.io.IOException;
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.setHeader("Access-Control-Allow-Origin",
                "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods",
                "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                "Authorization, Content-Type");
        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) { httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else { filterChain.doFilter(httpServletRequest,
                httpServletResponse);
        }
    } }

/*
Client -> Server
HTTP GET (lat, lon, user, xxx)
Client -> HTTP OPTIONS () -> Set ResponseHeader(1,2,3), Set ResponseStatus(OK)
    -> Return Response to Client.
Client(Browser) check response header, decide whether server supports CORS request
    If not, return error to javascript(frontend code)
    If so, HTTP GET (lat, lon, user, xxx) -> Server -> Set ResponseHeader(1,2,3)
        -> DoFilter(jwt, userAuthenticationFilter, xxx)
        -> Dispatch request to SearchController
            If !DoFilter -> 401 Unauthorized error to client

 */
