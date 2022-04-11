package com.example.shippingbotserver.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "watch_lover")
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Counter {
    @Id
    @Column(name = "id")
    Long loverId;

    @Column(name = "counter")
    long counter;

    public void increment(long size) {
        if (++counter > size || counter < 0)
            counter = 0;
    }

    public void decrement(long sizeDefault) {
        if (--counter < 0 || counter > sizeDefault)
            counter = sizeDefault;
    }
}
