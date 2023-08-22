package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContratRepository extends JpaRepository<Contrat, Long> {

    /**
     * chercher les contrats avec l'intitule
     * @param intitule du contrat
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Contrat> findByIntituleContainingIgnoreCase(String intitule, Pageable pageable);

    /**
     * chercher les contrats avec le statut
     * @param statut du contrat
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Contrat> findByStatut(Statut statut, Pageable pageable);

    /**
     * chercher les contrats avec l'abonné
     * @param value nom ou prenom de l'abonné associé
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    @Query("SELECT c FROM Contrat c WHERE c.abonne.nom LIKE %:value% or c.abonne.prenom LIKE %:value%")
    Page<Contrat> findByNomOuPrenomAbonneContains(String value, Pageable pageable);
}
