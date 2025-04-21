package com.devsu.hackerearth.backend.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.client.model.Client;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing {@link Client} entities.
 * Provides methods to perform database operations on active clients.
 * 
 * @author Germán Ponce
 * @version 1.0
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findActiveById(@Param("id") Long id);

    @Query("SELECT c FROM Client c WHERE c.dni = :dni AND c.isActive = true")
    Optional<Client> findActiveByDni(@Param("dni") String dni);

    @Query("SELECT c FROM Client c WHERE c.isActive = true")
    List<Client> findAllActiveClientList();
}
