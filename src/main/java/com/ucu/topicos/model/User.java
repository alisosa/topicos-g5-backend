package com.ucu.topicos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    private String id;

    @Column(length = 255, nullable = true)
    private String name;

    @Column(length = 255)
    private String mail;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(length = 255, nullable = true)
    private String rut;

    @OneToMany(mappedBy = "inviter")
    private List<Invitation> sentInvitations;

    @OneToMany(mappedBy = "invitee")
    private List<Invitation> receivedInvitations;
}
