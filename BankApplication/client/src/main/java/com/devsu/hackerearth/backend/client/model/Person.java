package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Person extends Base {
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "dni", nullable = false, length = 50, unique = true, updatable = false)
	private String dni;

	@Column(name = "gender", nullable = false, length = 10)
	private String gender;

	@Column(name = "age", nullable = false)
	private int age;

	@Column(name = "address", nullable = true, length = 255)
	private String address;

	@Column(name = "phone", nullable = false, unique = true, length = 20)
	private String phone;
}
