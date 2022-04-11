package com.liga.shippingbot.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PersonResponse {
    private byte[] bytesFromFile;
    private PersonRequest user;
    private String status;

    @Override
    public String toString() {
        return user.getGender() + ", " + user.getName();
    }
}