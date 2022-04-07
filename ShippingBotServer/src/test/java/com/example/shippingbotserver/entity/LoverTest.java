package com.example.shippingbotserver.entity;

import com.example.shippingbotserver.model.LoverModel;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoverTest {

    @Test
    void getLoverIncrement() {
        Lover lover = new Lover(123L, "", "", "", "",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        lover.getLike().add(new LoverModel(3L, "one", "", "", ""));
        lover.getLike().add(new LoverModel(1L, "two", "", "", ""));
        lover.getLike().add(new LoverModel(43L, "three", "", "", ""));
        lover.getLike().add(new LoverModel(12L, "four", "", "", ""));
        boolean increment = true;
        assertEquals("two", lover.getLover(increment).getName());
        assertEquals("one", lover.getLover(increment).getName());
        assertEquals("four", lover.getLover(increment).getName());
        assertEquals("three", lover.getLover(increment).getName());
    }

    @Test
    void getLoverIncrementWithLikeMe() {
        Lover lover = new Lover(123L, "", "", "", "",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        LoverModel duplicate = new LoverModel(12L, "four", "", "", "");
        lover.getLike().add(new LoverModel(3L, "one", "", "", ""));
        lover.getLike().add(new LoverModel(1L, "two", "", "", ""));
        lover.getLike().add(new LoverModel(43L, "three", "", "", ""));
        lover.getLike().add(duplicate);
        lover.getLikeMe().add(duplicate);
        boolean increment = true;
        assertEquals("two", lover.getLover(increment).getName());
        assertEquals("one", lover.getLover(increment).getName());
        assertEquals("four", lover.getLover(increment).getName());
        assertEquals("three", lover.getLover(increment).getName());
    }

    @Test
    void getLoverDecrement() {
        Lover lover = new Lover(123L, "", "", "", "",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        lover.getLike().add(new LoverModel(3L, "one", "", "", ""));
        lover.getLike().add(new LoverModel(1L, "two", "", "", ""));
        lover.getLike().add(new LoverModel(43L, "three", "", "", ""));
        lover.getLike().add(new LoverModel(12L, "four", "", "", ""));
        boolean increment = false;
        assertEquals("three", lover.getLover(increment).getName());
        assertEquals("four", lover.getLover(increment).getName());
        assertEquals("one", lover.getLover(increment).getName());
        assertEquals("two", lover.getLover(increment).getName());
    }

    @Test
    void getLoverDecrementWithLike() {
        Lover lover = new Lover(123L, "", "", "", "",
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
        LoverModel duplicate = new LoverModel(12L, "four", "", "", "");
        lover.getLike().add(new LoverModel(3L, "one", "", "", ""));
        lover.getLike().add(new LoverModel(1L, "two", "", "", ""));
        lover.getLike().add(new LoverModel(43L, "three", "", "", ""));
        lover.getLike().add(duplicate);
        lover.getLikeMe().add(duplicate);
        boolean increment = false;
        assertEquals("three", lover.getLover(increment).getName());
        assertEquals("four", lover.getLover(increment).getName());
        assertEquals("one", lover.getLover(increment).getName());
        assertEquals("two", lover.getLover(increment).getName());
    }
}