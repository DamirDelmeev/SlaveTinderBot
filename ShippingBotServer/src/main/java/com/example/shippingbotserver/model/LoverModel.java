package com.example.shippingbotserver.model;

import com.example.shippingbotserver.entity.Lover;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.List;

@Table(name = "lover")
@Entity
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor
public class LoverModel implements Comparable<LoverModel>{
    @Id
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "gender")
    String gender;

    @Column(name = "description")
    String description;

    @Column(name = "preference")
    String preference;

    public LoverModel(Lover lover) {
        id = lover.getId();
        name = lover.getName();
        gender = lover.getGender();
        description = lover.getDescription();
        preference = lover.getPreference();
    }

    @Override
    public int compareTo(LoverModel o) {
        return id.intValue() - o.id.intValue();
    }
}
