package com.coffee.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.Instant;
import java.util.UUID;


@Entity
@Getter
@Setter
public class Shop {
    @Id
    private UUID id;
    private String name;
    private UUID menuId;
    private UUID queue_id;
    private String longitude;
    private String latitude;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Instant createdAt;
}
