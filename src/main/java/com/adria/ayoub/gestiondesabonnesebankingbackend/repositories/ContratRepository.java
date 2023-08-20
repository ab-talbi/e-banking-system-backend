package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Statut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContratRepository extends JpaRepository<Contrat, Long> {
    Page<Contrat> findByIntituleContainingIgnoreCase(String intitule, Pageable pageable, Sort sort);
    Page<Contrat> findByStatut(Statut statut, Pageable pageable, Sort sort);

    @Query("SELECT c FROM Contrat c WHERE c.abonne.nom LIKE %:value% or c.abonne.prenom LIKE %:value%")
    Page<Contrat> findByNomOuPrenomAbonneContains(String value, Pageable pageable, Sort sort);
}
