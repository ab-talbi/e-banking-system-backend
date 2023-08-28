package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;

import java.util.Optional;

public interface OffreService {

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
