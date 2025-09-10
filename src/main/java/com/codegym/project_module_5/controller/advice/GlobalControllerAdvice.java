package com.codegym.project_module_5.controller.advice;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * This method provides a global model attribute 'isAuthenticated' to all templates.
     * It checks the current security context to determine if a user is fully authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // An user is authenticated if authentication is not null, is authenticated, and is not an anonymous user.
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
