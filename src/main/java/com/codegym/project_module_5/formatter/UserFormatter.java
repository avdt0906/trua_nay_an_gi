package com.codegym.project_module_5.formatter;

import com.codegym.project_module_5.model.user_model.User;
import com.codegym.project_module_5.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@Component
public class UserFormatter implements Formatter<User> {
    @Autowired
    private IUserService userService;

    @Override
    public User parse(String text, Locale locale) throws ParseException {
        Optional<User> user = userService.findByUsername(text);
        return user.get();
    }

    @Override
    public String print(User object, Locale locale) {
        return object.getUsername();
    }
}
