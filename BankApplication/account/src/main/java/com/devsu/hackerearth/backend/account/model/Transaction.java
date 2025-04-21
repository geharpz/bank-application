package com.devsu.hackerearth.backend.account.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction extends Base {

	@Column(name = "date", nullable = false, updatable = false)
	private LocalDate date;

	@Column(name = "type", nullable = false, length = 100, updatable = false)
	private String type;

	@Column(name = "amount", nullable = false, updatable = false, precision = 19, scale = 4)
	private BigDecimal amount;

	@Column(name = "balance", nullable = false, updatable = false, precision = 19, scale = 4)
	private BigDecimal balance;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", updatable = false)
	private Account account;

	@PreUpdate
	private void preventUpdate() {
		throw new UnsupportedOperationException("Transactions cannot be updated.");
	}
}
