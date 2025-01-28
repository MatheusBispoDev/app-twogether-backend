package com.app.us_twogether.config;

import com.app.us_twogether.domain.space.Space;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.exception.UnauthorizedAccessException;
import com.app.us_twogether.domain.user.UserSpaceRoleRepository;
import com.app.us_twogether.domain.space.SpaceService;
import com.app.us_twogether.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class SpaceAccessInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Autowired
    UserSpaceRoleRepository userSpaceRoleRepository;

    @Autowired
    SpaceService spaceService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        try {
            Long spaceId = Long.valueOf(pathVariables.get("spaceId"));

            UsernamePasswordAuthenticationToken authentication = (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !isAuthorized(authentication.getName(), spaceId)) {
                throw new UnauthorizedAccessException("Usuário não tem acesso a esse Espaço");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "spaceId deve ser um número válido.");
            return false;
        }

        return true;
    }

    private boolean isAuthorized(String authUsername, Long spaceId) {
        User user = userService.getUserByUsername(authUsername);
        Space space = spaceService.findSpaceById(spaceId);
        return userSpaceRoleRepository.existsByUserAndSpace(user, space);
    }
}