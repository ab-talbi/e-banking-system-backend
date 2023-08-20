package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbonneRepository extends JpaRepository<Abonne, Long> {
    Page<Abonne> findByNomContainingIgnoreCase(String nom, Pageable pageable, Sort sort);
    Page<Abonne> findByPrenomContainingIgnoreCase(String prenom, Pageable pageable, Sort sort);
    Page<Abonne> findByEmailContainingIgnoreCase(String email, Pageable pageable, Sort sort);
    Page<Abonne> findByAdresseContainingIgnoreCase(String adresse, Pageable pageable, Sort sort);
    Page<Abonne> findByTelephoneContainingIgnoreCase(String telephone, Pageable pageable, Sort sort);
    Page<Abonne> findBySexeContainingIgnoreCase(String sexe, Pageable pageable, Sort sort);
    Page<Abonne> findByStatutContainingIgnoreCase(String statut, Pageable pageable, Sort sort);

    @Query("SELECT a FROM Abonne a WHERE a.contrat.intitule LIKE %:intitule%")
    Page<Abonne> findByIntituleContratContains(String intitule, Pageable pageable, Sort sort);

    @Query("SELECT a FROM Abonne a WHERE a.agence.nom LIKE %:nom%")
    Page<Abonne> findByNomAgenceContains(String nom, Pageable pageable, Sort sort);

    @Query("SELECT a FROM Abonne a WHERE a.backOffice.nom LIKE %:value% or a.backOffice.prenom LIKE %:value%")
    Page<Abonne> findByNomOuPrenomBackOfficeContains(String value, Pageable pageable, Sort sort);
}
