package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OffreService {

    /**
     * Pour trouver tous les offres
     * @param pageable
     * @return une page des offres
     */
    Page<Offre> trouverTousLesOffres(Pageable pageable);

    /**
     * Pour trouver une list des offres à partir de clé
     * @param search libelle ou description
     * @param val clé à chercher
     * @param pageable
     * @return une page des offres
     */
    Page<Offre> trouverUneListeDesOffres(String search,String val, Pageable pageable);

    /**
     * Pour ajouter un offre
     * @param offre à ajouter
     * @return objet de type offre
     */
    Offre ajouterOffre(Offre offre);

    /**
     * Pour trouver un offre
     * @param id de l'offre à trouver
     * @return Optional<Offre>
     */
    Optional<Offre> trouverUnOffreById(Long id);

    /**
     * Pour supprimer un offre
     * @param id de l'offre à supprimer
     */
    void supprimerOffreById(Long id);

    /**
     * Pour supprimer tous les offres
     */
    void supprimerTousLesOffres();
}
