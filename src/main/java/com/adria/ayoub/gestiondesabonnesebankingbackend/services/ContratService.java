package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.ContratDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyExistsException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyRelatedException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

public interface ContratService {

    /**
     * Pour trouver une list des contrats
     * @param search mot clé (intitule, statut ou abonné)
     * @param val valeur à chercher
     * @param page numero de page
     * @param sort pour filtrer (field et direction)
     * @return une page des contrats
     */
    Page<Contrat> trouverLesContrats(String search, String val, int page, String[] sort);

    /**
     * Pour teouver un contrat
     * @param id de contrat à trouver
     * @return Optional<Contrat>
     */
    Contrat trouverUnContratById(Long id) throws NotFoundException;

    /**
     * Pour ajouter un contrat
     * @param contratDto
     * @return un objet de type Contrat
     */
    Contrat ajouterContrat(ContratDto contratDto) throws AlreadyRelatedException, NotFoundException;

    /**
     * Pour modifier un contrat
     * @param id du contrat
     * @param contratDto dto
     * @return un objet de type Contrat
     */
    Contrat modifierContrat(Long id,ContratDto contratDto) throws NotFoundException, AlreadyRelatedException;

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
     * Pour changer le statut du cintrat
     * @param id du contrat
     * @param requestBody la valeur de statut
     * @return un objet de type Contrat
     */
    Contrat changerLeStatutDuContrat(Long id, String requestBody) throws NotFoundException;

    /**
     * Pour ajouter un offre à un contrat
     * @param id du contrat
     * @param offre_id
     * @return un objet de type contrat
     */
    Contrat ajouterUnOffreAUnContrat(Long id, Long offre_id) throws NotFoundException, AlreadyExistsException;

    /**
     * Pour retirer un offre d'un contrat
     * @param id du contrat
     * @param offre_id
     * @return un objet de type Contrat
     */
    Contrat retirerUnOffreDansUnContrat(Long id, Long offre_id) throws NotFoundException;

    /**
     * Pour retirer tous les offres du contrat
     * @param id du contrat
     * @return un objet de type Contrat
     */
    Contrat retirerTousLesOffreDansUnContrat(Long id) throws NotFoundException;
}
