package com.example.shippingbotserver.entity;

import com.example.shippingbotserver.model.LongModel;
import com.example.shippingbotserver.model.LoverModel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "lover")
@Entity
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor
public class Lover {
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "likes", joinColumns = @JoinColumn(name = "one"), inverseJoinColumns = @JoinColumn(name = "two"))
    Set<LoverModel> like;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "likes", joinColumns = @JoinColumn(name = "two"), inverseJoinColumns = @JoinColumn(name = "one"))
    Set<LoverModel> likeMe;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dislikes", joinColumns = @JoinColumn(name = "whoid"), inverseJoinColumns = @JoinColumn(name = "targetid"))
    Set<LoverModel> dislikes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    LongModel watch;

    public Lover(LoverModel loverModel) {
        this.id = loverModel.getId();
        this.name = loverModel.getName();
        this.gender = loverModel.getGender();
        this.description = loverModel.getDescription();
        this.preference = loverModel.getPreference();
    }

    public LoverModel getLover(boolean increment){
        if (like.isEmpty() && likeMe.isEmpty()){
            return getLoverEmptyModel();
        }
        Set<LoverModel> lovers = new HashSet<>(like);
        lovers.addAll(likeMe);
        List<LoverModel> loverAll = lovers.stream().sorted().collect(Collectors.toList());
        if (watch == null){
            watch = new LongModel(id, -1L);
        }
        if (increment) {
            watch.increment(loverAll.size() - 1);
        }else {
            watch.decrement(loverAll.size() - 1);
        }
        return loverAll.get((int) watch.getCounter());
    }

    private LoverModel getLoverEmptyModel() {
        LoverModel empty = new LoverModel(0L, " ", " ", "Здесь никого нет", "");
        return empty;
    }


    public Set<LoverModel> mutuallyLovers(){
        return like.stream().filter(p -> likeMe.contains(p)).collect(Collectors.toSet());
    }

}
