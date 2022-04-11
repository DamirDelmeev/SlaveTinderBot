package com.example.shippingbotserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "attitude")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Attitude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiator", referencedColumnName = "chat_id")
    private User initId;

    @OneToOne
    @JoinColumn(name = "target", referencedColumnName = "chat_id")
    private User targetId;

    @Column(name = "name_of_action")
    private String nameOfAction;

    public Attitude(User initId, User targetId, String nameOfAction) {
        this.initId = initId;
        this.targetId = targetId;
        this.nameOfAction = nameOfAction;
    }
}
