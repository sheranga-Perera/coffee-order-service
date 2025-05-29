package com.coffee.common.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Queue {

    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID shopId;
    private int queueNo;
    private int maxSize;
    private int currentSize;
    private String status;
}

