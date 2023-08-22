package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Sexe;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbonneRepository extends JpaRepository<Abonne, Long> {

    /**
     * chercher les abonnés avec le nom
     * @param nom de l'abonné
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Abonne> findByNomContainingIgnoreCase(String nom, Pageable pageable);

    /**
     * chercher les abonnés avec le prenom
     * @param prenom de l'abonné
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Abonne> findByPrenomContainingIgnoreCase(String prenom, Pageable pageable);

    /**
     * chercher les abonnés avec l'email
     * @param email de l'abonné
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Abonne> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    /**
     * chercher les abonnés avec l'adresse
     * @param adresse de l'abonné
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Abonne> findByAdresseContainingIgnoreCase(String adresse, Pageable pageable);

    /**
     * chercher les abonnés avec le numero de tel
     * @param telephone de l'abonné
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Abonne> findByTelephoneContainingIgnoreCase(String telephone, Pageable pageable);

    /**
     * chercher les abonnés avec le sexe
     * @param sexe de l'abonné
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Abonne> findBySexe(Sexe sexe, Pageable pageable);

    /**
     * chercher les abonnés par le statut
     * @param statut du compte
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    Page<Abonne> findByStatut(Statut statut, Pageable pageable);

    /**
     * chercher les abonnés avec l'intitule de contrat associé
     * @param intitule du contrat associé à l'abonné
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    @Query("SELECT a FROM Abonne a WHERE a.contrat.intitule LIKE %:intitule%")
    Page<Abonne> findByIntituleContratContains(String intitule, Pageable pageable);

    /**
     * chercher les abonnés avec l'agence associé
     * @param id de l'agence associé
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    @Query("SELECT a FROM Abonne a WHERE a.agence.id = :id")
    Page<Abonne> findByAgence(Long id, Pageable pageable);

    /**
     * chercher les abonnés avec le back office
     * @param id du back office associé
     * @param pageable pour numero de page et son size
     * @return une page des abonnés
     */
    @Query("SELECT a FROM Abonne a WHERE a.backOffice.id = :id")
    Page<Abonne> findByBackOffice(Long id, Pageable pageable);
}
