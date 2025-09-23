package com.codegym.project_module_5.config;

import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.impl.cart_service_impl.CartService;
import com.codegym.project_module_5.service.user_service.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private IUserService userService;

    @Autowired
    private CartService cartService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        mergeSessionCart(request, authentication);

        HttpSession session = request.getSession(false);
        String redirectUrl = null;

        if (session != null) {
            redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if (redirectUrl != null) {
                @SuppressWarnings("unchecked")
                List<Long> selectedDishIds = (List<Long>) session.getAttribute("selectedDishIds");
                if (selectedDishIds != null && !selectedDishIds.isEmpty()) {
                    String params = selectedDishIds.stream()
                            .map(id -> "selectedItems=" + id)
                            .collect(Collectors.joining("&"));
                    redirectUrl = redirectUrl + "?" + params;
                }
                session.removeAttribute("redirectAfterLogin");
                session.removeAttribute("selectedDishIds");
            }
        }

        if (redirectUrl == null || redirectUrl.isEmpty()) {
            redirectUrl = "";
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (grantedAuthority.getAuthority().equals("ADMIN")) {
                    redirectUrl = "/admin/dashboard";
                    break;
                } else if (grantedAuthority.getAuthority().equals("OWNER")) {
                    redirectUrl = "/restaurants/dashboard";
                    break;
                }
            }
        }
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }

    private void mergeSessionCart(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Map<Long, Integer> sessionCart = (Map<Long, Integer>) session.getAttribute("cart");

            if (sessionCart != null && !sessionCart.isEmpty()) {
                String username = authentication.getName();
                User user = userService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found after login"));
                cartService.mergeSessionCartWithDbCart(sessionCart, user);
                session.removeAttribute("cart");
            }
        }
    }
}
