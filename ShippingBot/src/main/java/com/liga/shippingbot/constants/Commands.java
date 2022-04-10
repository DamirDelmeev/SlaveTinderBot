package com.liga.shippingbot.constants;

import lombok.Getter;

public enum Commands implements ConstantsName {

    START("/start"),
    CONTINUE("/continue");
    @Getter
    final String name;

    Commands(String name) {
        this.name = name;
    }
}
