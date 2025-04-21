package com.devsu.hackerearth.backend.account.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account extends Base {
    @Column(name = "number", length = 50, nullable = false, unique = true, updatable = false)
    private String number;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "initial_amount", nullable = false, updatable = false, precision = 19, scale = 4)
    private BigDecimal initialAmount;

    @Column(name = "current_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal currentBalance;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
