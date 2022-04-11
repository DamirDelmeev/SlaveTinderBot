package com.example.shippingbotserver.service;

import com.example.shippingbotserver.entity.User;
import com.example.shippingbotserver.service.translation.TextTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TranslateService {
    private final TextTranslator textTranslator;

    public void translate(User user) {
        if (user.getGender() == null || user.getPreference() == null) {
            return;
        }
        textTranslator.userTranslate(user);
    }
}
