package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;

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
}
