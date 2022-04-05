package com.ru.liga.shippingbot.entity;

import com.ru.liga.shippingbot.bot.BotState;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    Long id;
    String name;
    String gender;
    String description;
    String preference;
    BotState botState;
}
