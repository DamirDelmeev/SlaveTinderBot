package com.liga.shippingbot.controller;

import com.liga.shippingbot.bot.BotMassage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final BotMassage writeReadBot;

    @Autowired
    public WebHookController(BotMassage writeReadBot) {
        this.writeReadBot = writeReadBot;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return writeReadBot.onWebhookUpdateReceived(update);
    }
}
