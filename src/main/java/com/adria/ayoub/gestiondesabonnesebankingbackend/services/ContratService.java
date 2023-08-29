package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;

import java.util.Optional;

public interface ContratService {

    /**
     * Pour ajouter un contrat
     * @param contrat
     * @return un objet de type Contrat
     */
    Contrat ajouterContrat(Contrat contrat);

    /**
     * Pour teouver un contrat
     * @param id de contrat à trouver
     * @return Optional<Contrat>
     */
    Optional<Contrat> trouverUnContratById(Long id);

    /**
     * Pour supprimer un contrat
     * @param id du contrat à supprimer
     */
    void supprimerContratById(Long id);

    /**
     * Pour supprimer tous les contrats
     */
    void supprimerTousLesContrats();

    /**
     * Trouver un offre by id
     * @param offre_id de l'offre
     * @return Optional<Offre>
     */
    Optional<Offre> trouverUnOffreById(Long offre_id);
}
