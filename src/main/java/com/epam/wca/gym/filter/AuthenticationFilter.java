package com.epam.wca.gym.filter;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends HttpFilter {
    private static final String REGISTRATION_URI = "/gym/register";
    @NonNull
    private UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        Optional<String> uri = Optional.ofNullable(httpRequest.getRequestURI());

        // excluding registration URI from checking
        if (uri.isPresent() && httpRequest.getRequestURI().startsWith(REGISTRATION_URI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Extract and decode the Base64 encoded login:password
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            // credentials = "username:password"
            String[] values = credentials.split(":", 2);

            if (values.length == 2) {
                String username = values[0];
                String password = values[1];

                User user = userService.findByUniqueName(username);

                if (user != null && user.getPassword().equals(password)) {
                    log.info("Successful login.");

                    log.info("User authenticated: " + username);

                    // Store user in the request attributes
                    httpRequest.setAttribute("authenticatedUser", user);

                    // Continue the request if authentication is successful
                    filterChain.doFilter(servletRequest, servletResponse);

                    return;
                }
            }
        }

        // If authentication fails, respond with 401 Unauthorized
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("HTTP Status 401 - Unauthorized");
    }
}
