package com.coffee.common.entity;

import com.coffee.common.validation.OpenForBusiness;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.Instant;
import java.util.UUID;


@Entity
@Getter
@Setter
@OpenForBusiness
public class Shop {
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    private String longitude;
    private String latitude;
    
    @Column(name = "menu_id")
    private UUID menuId;
    
    private Integer queues;
    private Integer maxQueueSize;
    
    @Column(nullable = false)
    private LocalTime openTime;
    
    @Column(nullable = false)
    private LocalTime closeTime;
    
    private Instant createdAt;
}
