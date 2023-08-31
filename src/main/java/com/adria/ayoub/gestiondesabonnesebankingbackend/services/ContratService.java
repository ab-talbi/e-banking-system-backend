package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContratService {

    /**
     * Pour trouver tous les contrats
     * @param pageable
     * @return une page des contrats
     */
    Page<Contrat> trouverTousLesContrats(Pageable pageable);

    /**
     * Pour trouver une list des contrats à partir de clé
     * @param search intitule, statut ou abonne
     * @param val clé à chercher
     * @param pageable
     * @return une page des contrats
     */
    Page<Contrat> trouverUneListeDesContrats(String search,String val, Pageable pageable);

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
