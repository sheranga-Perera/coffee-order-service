package com.coffee.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Customer {
    @Id
    private UUID id;
    private String name;
    private String address;
    private String mobile_no;
}