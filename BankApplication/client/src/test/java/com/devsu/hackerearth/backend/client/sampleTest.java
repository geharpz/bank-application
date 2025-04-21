package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class sampleTest {

    private ClientService clientService = mock(ClientService.class);
    private ClientController clientController = new ClientController(clientService);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createClientTest() {
        // Arrange
        ClientDto newClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        ClientDto createdClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999",
                true);
        when(clientService.create(newClient)).thenReturn(createdClient);

        // Act
        ResponseEntity<ClientDto> response = clientController.create(newClient);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdClient, response.getBody());
    }

    /**
     * Verifies that a {@link Client} object is constructed with all expected field
     * values.
     */
    @Test
    void shouldCreateClientWithCorrectData() {
        Client client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setDni("12345678");
        client.setGender("M");
        client.setAge(30);
        client.setAddress("123 Street");
        client.setPhone("999999999");
        client.setPassword("encodedPassword");
        client.setActive(true);
        client.setCreatedAt(LocalDateTime.now());
        client.setCreatedBy("admin");

        assertEquals(1L, client.getId());
        assertEquals("John Doe", client.getName());
        assertEquals("12345678", client.getDni());
        assertEquals("M", client.getGender());
        assertEquals(30, client.getAge());
        assertEquals("123 Street", client.getAddress());
        assertEquals("999999999", client.getPhone());
        assertEquals("encodedPassword", client.getPassword());
        assertEquals(true, client.isActive());
        assertEquals("admin", client.getCreatedBy());
    }

    /**
     * Ensures that the {@code isActive} flag can be toggled to false for
     * deactivating a client.
     */
    @Test
    void shouldDeactivateClient() {
        Client client = new Client();
        client.setActive(true);

        client.setActive(false);

        assertEquals(false, client.isActive());
    }

    /**
     * Performs an HTTP POST request to create a client and verifies the response
     * payload and status code.
     *
     * @throws Exception if the request execution fails
     */
    @Test
    void shouldCreateClientSuccessfully() throws Exception {
        String requestBody = "{\n" +
                "  \"name\": \"Pedro Silva\",\n" +
                "  \"dni\": \"98765432\",\n" +
                "  \"gender\": \"M\",\n" +
                "  \"age\": 28,\n" +
                "  \"phone\": \"987654321\",\n" +
                "  \"address\": \"Av. Central 456\",\n" +
                "  \"password\": \"1234\"\n" +
                "}";

        mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni").value("98765432"))
                .andExpect(jsonPath("$.name").value("Pedro Silva"));
    }
}
