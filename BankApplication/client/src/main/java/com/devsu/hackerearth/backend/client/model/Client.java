package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client")
public class Client extends Person {

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;
}
