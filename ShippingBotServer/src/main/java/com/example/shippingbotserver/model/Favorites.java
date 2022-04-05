package com.example.shippingbotserver.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Favorites {
    Map<String, Set<LoverModel>> favorites;

    public Favorites(Map<String, Set<LoverModel>> favorites) {
        this.favorites = favorites;
    }
}

