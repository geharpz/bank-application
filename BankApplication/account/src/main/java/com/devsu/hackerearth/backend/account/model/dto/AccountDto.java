package com.devsu.hackerearth.backend.account.model.dto;

import java.math.BigDecimal;

import com.devsu.hackerearth.backend.account.type.AccountType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

	private Long id;
	private String number;
	private AccountType type;
	private BigDecimal initialAmount;
	private BigDecimal currentBalance;
	private boolean isActive;
	private Long clientId;
}
