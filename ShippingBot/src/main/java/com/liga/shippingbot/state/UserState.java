package com.liga.shippingbot.state;

import com.liga.shippingbot.bot.BotState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserState {
    private Long id;
    private BotState botState;
    private String status;
}
