package com.freesocial.gateway.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
public class UserToken {

    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String token;

    private String userUuid;

}
