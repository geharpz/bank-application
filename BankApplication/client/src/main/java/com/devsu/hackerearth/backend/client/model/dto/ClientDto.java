package com.devsu.hackerearth.backend.client.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {

	private Long id;
	private String dni;
	private String name;
	private String password;
	private String gender;
	private int age;
	private String address;
	private String phone;
	private boolean isActive;
}
