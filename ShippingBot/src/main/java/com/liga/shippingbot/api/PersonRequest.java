package com.liga.shippingbot.api;

import com.liga.shippingbot.bot.BotState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class PersonRequest {
    private Long id;
    private String name;
    private String gender;
    private String description;
    private String preference;
    private BotState botState;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonRequest personRequest = (PersonRequest) o;
        return Objects.equals(id, personRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
