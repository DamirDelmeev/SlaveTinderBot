package com.liga.shippingbot.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Gender implements ConstantsName {
    MALE("Сударь"),
    FEMALE("Сударыня");

    public final String name;

    Gender(String name) {
        this.name = name;
    }

    public static Boolean matches(String text) {
        List<String> collect =
                Arrays.stream(values()).map(preferences -> preferences.getName().toLowerCase()).filter(s -> s.equals(text.toLowerCase())).collect(Collectors.toList());
        return !collect.isEmpty();
    }
}
