package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonneRepository extends JpaRepository<Abonne, Long> {
    Page<Abonne> findByNomContainingIgnoreCase(String nom, Pageable pageable);
    Page<Abonne> findByPrenomContainingIgnoreCase(String prenom, Pageable pageable);
    Page<Abonne> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    Page<Abonne> findByAdresseContainingIgnoreCase(String adresse, Pageable pageable);
    Page<Abonne> findByTelephoneContainingIgnoreCase(String telephone, Pageable pageable);
    Page<Abonne> findBySexeContainingIgnoreCase(String sexe, Pageable pageable);
    Page<Abonne> findByStatutContainingIgnoreCase(String statut, Pageable pageable);



}
