package com.example.shippingbotserver.utils;

import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.model.LoverModel;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LoverShowerTest {

    @Test
    void translateLoverBoy() {
        LoverShower loverShower = new LoverShower();
        assertEquals("Сударь", loverShower.translateLover("boy"));
    }

    @Test
    void translateLoverGirl() {
        LoverShower loverShower = new LoverShower();
        assertEquals("Сударыня", loverShower.translateLover("girl"));
    }

    @Test
    void translateLoverNull() {
        LoverShower loverShower = new LoverShower();
        assertEquals("GIRLANDBOY", loverShower.translateLover("GIRLANDBOY"));
    }

    @Test
    void translateLoverInvalid() {
        LoverShower loverShower = new LoverShower();
        assertNull(loverShower.translateLover(null));
    }

    @Test
    void translateLoverEmpty() {
        LoverShower loverShower = new LoverShower();
        assertEquals("", loverShower.translateLover(""));
    }

    @Test
    void getLoverModelStatusMyLike() {
        LoverShower loverShower = new LoverShower();
        LoverModel loverModel = new LoverModel(1L, "", "", "", "");
        Set<LoverModel> like = new HashSet<>();
        like.add(loverModel);
        Lover lover = new Lover(1L, "", "", "", "", like, new HashSet<>(), new HashSet<>(), null);
        assertEquals("Любимъ вами", loverShower.getLoverModelStatus(lover, loverModel));
    }

    @Test
    void getLoverModelStatusDislike() {
        LoverShower loverShower = new LoverShower();
        LoverModel loverModel = new LoverModel(1L, "", "", "", "");
        Set<LoverModel> like = new HashSet<>();
        like.add(loverModel);
        Lover lover = new Lover(1L, "", "", "", "", new HashSet<>(), new HashSet<>(), like, null);
        assertEquals("", loverShower.getLoverModelStatus(lover, loverModel));
    }

    @Test
    void getLoverModelStatusLikeMe() {
        LoverShower loverShower = new LoverShower();
        LoverModel loverModel = new LoverModel(1L, "", "", "", "");
        Set<LoverModel> like = new HashSet<>();
        like.add(loverModel);
        Lover lover = new Lover(1L, "", "", "", "", new HashSet<>(), like, new HashSet<>(), null);
        assertEquals("Вы любимъ", loverShower.getLoverModelStatus(lover, loverModel));
    }

    @Test
    void getLoverModelStatusMatch() {
        LoverShower loverShower = new LoverShower();
        LoverModel loverModel = new LoverModel(1L, "", "", "", "");
        Set<LoverModel> like = new HashSet<>();
        like.add(loverModel);
        Lover lover = new Lover(1L, "", "", "", "", new HashSet<>(like), like, new HashSet<>(), null);
        assertEquals("Взаимность", loverShower.getLoverModelStatus(lover, loverModel));
    }
}