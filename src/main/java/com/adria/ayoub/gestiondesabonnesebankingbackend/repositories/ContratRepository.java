package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratRepository extends JpaRepository<Contrat, Long> {
    Page<Contrat> findByIntituleContainingIgnoreCase(String intitule, Pageable pageable);
    Page<Contrat> findByStatutContainingIgnoreCase(String statut, Pageable pageable);
}
