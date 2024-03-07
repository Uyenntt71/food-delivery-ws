package com.food_delivery.conf;

import static com.food_delivery.constant.CommonParams.ADD_ADDRESS_PATH;
import static com.food_delivery.constant.CommonParams.AUTHORIZATION_HEADER;
import static com.food_delivery.constant.CommonParams.EMPTY_STRING;
import static com.food_delivery.constant.CommonParams.FORGOT_PASSWORD_PATH;
import static com.food_delivery.constant.CommonParams.LOGIN_PATH;
import static com.food_delivery.constant.CommonParams.REGISTER_PATH;
import static com.food_delivery.constant.CommonParams.RESET_PASSWORD_PATH;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


import com.food_delivery.util.JWTUtils;

import io.vavr.control.Try;
import lombok.SneakyThrows;

public class AccessControlInterceptor extends OncePerRequestFilter {
//    private static final String HEADER_PREFIX = "Bearer ";
    private static final String DEFAULT_CUSTOMER_ROLE = "CUSTOMER_USER";

    private static final String INVALID_TOKEN = "Invalid token";

    private static AntPathMatcher pathMatcher = new AntPathMatcher("/");

    @Autowired
    AppProps appProps;

    @Override
    @SneakyThrows
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) {
        List<String> excludePath = Arrays.asList(LOGIN_PATH, REGISTER_PATH, FORGOT_PASSWORD_PATH, RESET_PASSWORD_PATH);
        boolean isExcluded = excludePath.stream().anyMatch(matchServletPath(httpServletRequest.getServletPath()));
        boolean isAuthorized = true;

        if (!isExcluded) {
            try {
                String token = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
                JWTUtils.verifyToken(token, appProps.tokenSigningKey());

                String customerId = JWTUtils.claimCustomerId(token);
                List<GrantedAuthority> defaultRoles = Arrays.asList(new GrantedAuthority[]{ new SimpleGrantedAuthority(DEFAULT_CUSTOMER_ROLE)});

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customerId, null, defaultRoles);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                isAuthorized = false;
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        if (isAuthorized) filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Predicate<String> matchServletPath(String path) {
        return pathPettern -> Try.of(() -> pathMatcher.match(pathPettern, path)).getOrElse(false);
    }
}
