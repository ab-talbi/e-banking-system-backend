package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffreRepository extends JpaRepository<Offre, Long> {
    Page<Offre> findByLibelleContainingIgnoreCase(String libelle, Pageable pageable, Sort sort);
    Page<Offre> findByDescriptionContainingIgnoreCase(String description, Pageable pageable, Sort sort);
}
