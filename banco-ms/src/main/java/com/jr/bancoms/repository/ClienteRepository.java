package com.jr.bancoms.repository;

import com.jr.bancoms.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {}
