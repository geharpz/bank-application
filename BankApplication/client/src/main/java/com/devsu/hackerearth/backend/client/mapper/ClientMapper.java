package com.devsu.hackerearth.backend.client.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;

@Component
public class ClientMapper {

    public Client toEntity(ClientDto dto) {
        Client client = new Client();
        dto.setId(client.getId());
        client.setName(dto.getName());
        client.setDni(dto.getDni());
        client.setGender(dto.getGender());
        client.setAge(dto.getAge());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setPassword(dto.getPassword());
        dto.setPhone(client.getPhone());
        client.setActive(dto.isActive());
        return client;
    }

    public Client updateEntity(Client client, ClientDto dto) {
        if (StringUtils.isNotBlank(dto.getName()))
            client.setName(dto.getName());
        if (StringUtils.isNotBlank(dto.getPhone()))
            client.setPhone(dto.getPhone());
        if (StringUtils.isNotBlank(dto.getAddress()))
            client.setAddress(dto.getAddress());
        if (StringUtils.isNotBlank(dto.getPassword()))
            client.setPassword(dto.getPassword());
        return client;
    }

    public ClientDto toDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setDni(client.getDni());
        dto.setGender(client.getGender());
        dto.setAge(client.getAge());
        dto.setAddress(client.getAddress());
        dto.setActive(client.isActive());
        return dto;
    }
}