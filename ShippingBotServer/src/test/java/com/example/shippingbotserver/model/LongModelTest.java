package com.example.shippingbotserver.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongModelTest {

    @Test
    void incrementNormally() {
        LongModel longModel = new LongModel(1L, 0);
        longModel.increment(3);
        assertEquals(1, longModel.getCounter());

        longModel.increment(3);
        assertEquals(2, longModel.getCounter());

        longModel.increment(3);
        assertEquals(3, longModel.getCounter());

        longModel.increment(3);
        assertEquals(0, longModel.getCounter());

        longModel.increment(3);
        assertEquals(1, longModel.getCounter());

        longModel.increment(3);
        assertEquals(2, longModel.getCounter());

        longModel.increment(3);
        assertEquals(3, longModel.getCounter());
    }

    @Test
    void incrementNotNormally() {
        LongModel longModel = new LongModel(1L, -123123);
        longModel.increment(3);
        assertEquals(0, longModel.getCounter());

        longModel.increment(3);
        assertEquals(1, longModel.getCounter());

        longModel.increment(3);
        assertEquals(2, longModel.getCounter());

        longModel.increment(3);
        assertEquals(3, longModel.getCounter());

        longModel.increment(3);
        assertEquals(0, longModel.getCounter());

        longModel.increment(3);
        assertEquals(1, longModel.getCounter());

        longModel.increment(3);
        assertEquals(2, longModel.getCounter());

        longModel.increment(3);
        assertEquals(3, longModel.getCounter());
    }

    @Test
    void decrementNormally() {
        LongModel longModel = new LongModel(1L, 0);
        longModel.decrement(3);
        assertEquals(3, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(2, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(1, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(0, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(3, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(2, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(1, longModel.getCounter());
    }

    @Test
    void decrementNotNormally() {
        LongModel longModel = new LongModel(1L, 1232143234);
        longModel.decrement(3);
        assertEquals(3, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(2, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(1, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(0, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(3, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(2, longModel.getCounter());

        longModel.decrement(3);
        assertEquals(1, longModel.getCounter());
    }
}