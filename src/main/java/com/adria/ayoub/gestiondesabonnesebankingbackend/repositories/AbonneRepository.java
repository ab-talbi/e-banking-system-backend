package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Sexe;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Statut;
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
    Page<Abonne> findBySexe(Sexe sexe, Pageable pageable, Sort sort);
    Page<Abonne> findByStatut(Statut statut, Pageable pageable, Sort sort);

    @Query("SELECT a FROM Abonne a WHERE a.contrat.intitule LIKE %:intitule%")
    Page<Abonne> findByIntituleContratContains(String intitule, Pageable pageable, Sort sort);

    @Query("SELECT a FROM Abonne a WHERE a.agence.id = :id")
    Page<Abonne> findByAgence(Long id, Pageable pageable, Sort sort);

    @Query("SELECT a FROM Abonne a WHERE a.backOffice.id = :value")
    Page<Abonne> findByBackOffice(Long id, Pageable pageable, Sort sort);
}
