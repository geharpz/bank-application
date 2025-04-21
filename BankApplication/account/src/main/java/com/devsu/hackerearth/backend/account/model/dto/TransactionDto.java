package com.devsu.hackerearth.backend.account.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.devsu.hackerearth.backend.account.type.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

	private Long id;
	private LocalDate date;
	private BigDecimal amount;
	private TransactionType type;
	private BigDecimal balance;
	private Long accountId;
}
