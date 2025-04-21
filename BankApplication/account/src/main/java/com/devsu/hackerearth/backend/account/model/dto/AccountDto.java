package com.devsu.hackerearth.backend.account.model.dto;

import java.math.BigDecimal;

import com.devsu.hackerearth.backend.account.type.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

	private Long id;
	private String number;
	private AccountType type;
	private BigDecimal initialAmount;
	private BigDecimal currentBalance;
	private boolean isActive;
	private Long clientId;
}
