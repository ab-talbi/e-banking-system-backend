package com.adria.ayoub.gestiondesabonnesebankingbackend.repositories;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffreRepository extends JpaRepository<Offre, Long> {

    /**
     * chercher les offres par le libelle
     * @param libelle de l'offre
     * @param pageable pour numero de page et son size
     * @param sort pour les filtres
     * @return une page des abonnés
     */
    Page<Offre> findByLibelleContainingIgnoreCase(String libelle, Pageable pageable, Sort sort);

    /**
     * chercher les offres par la descripton
     * @param description de l'offre
     * @param pageable pour numero de page et son size
     * @param sort pour les filtres
     * @return une page des abonnés
     */
    Page<Offre> findByDescriptionContainingIgnoreCase(String description, Pageable pageable, Sort sort);
}
