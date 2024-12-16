package com.epam.wca.authentication.interceptor;

import com.epam.wca.common.gymcommon.util.AppConstants;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Value("${route.allowed.roles}")
    private String allowedRolesString;

    private Map<String, Set<String>> allowedRolesForRoutes;


    @PostConstruct
    public void initializeAllowedRoles() {
        allowedRolesForRoutes = Arrays.stream(allowedRolesString.split(";"))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(
                        entry -> entry[0],
                        entry -> Set.of(entry[1].split(","))
                ));
    }

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws IOException {
        String uriAccessed = request.getHeader(AppConstants.URI_ACCESSED_HEADER);

        if (uriAccessed == null) {
            return true;
        }

        log.info("URI Accessed requiring Authorization. URI: {}", uriAccessed);

        // Fetch the current user's granted authorities
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<GrantedAuthority> authorities = new HashSet<>(authentication.getAuthorities());

        Set<String> allowedRoles = getAllowedRolesForRoute(uriAccessed);

        log.info("Allowed Roles for URI: {}", Arrays.toString(allowedRoles.toArray()));

        // Map allowed roles to SimpleGrantedAuthority
        Set<GrantedAuthority> allowedAuthorities = allowedRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        boolean hasAccess = authorities.stream().anyMatch(allowedAuthorities::contains);

        if (!hasAccess) {
            log.info("User has no access to URI.");

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("You have no access.");
            return false; // Reject access
        }

        log.info("User has access to URI.");

        return true; // Allow access
    }

    private Set<String> getAllowedRolesForRoute(String requestUri) {
        // Iterate over the map entries to find a matching route
        return allowedRolesForRoutes.entrySet().stream()
                .filter(entry -> requestUri.startsWith(entry.getKey())) // Check if the URI starts with the route
                .map(Map.Entry::getValue) // Extract the roles
                .findFirst() // Return the first match if any
                .orElse(Set.of()); // Default to an empty set if no match is found
    }
}
