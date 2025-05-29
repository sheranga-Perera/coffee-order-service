package com.coffee.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Orders {
    @Id
    private UUID id;
    private UUID customerId;
    private UUID shopId;
    private String menuItem;
    private int queuePosition;
    private String status;
    private Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;
}
