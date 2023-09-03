package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.AbonneDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.BackOffice;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyRelatedException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AbonneService {

    /**
     * Pour trouver une list des abonnes à partir de clé
     * @param search nom, prenom, tel, email, statut, sexe...
     * @param val clé à chercher
     * @param page numero
     * @param sort pour filtrer (field,direction)
     * @return une page des abonnes
     */
    Page<Abonne> trouverLesAbonnes(String search,String val, int page, String[] sort);

    /**
     * Pour trouver un abonné avec l'id passé au parametre
     * @param id de l'abonné
     * @return Abonne
     */
    Abonne trouverUnAbonneById(Long id) throws NotFoundException;

    /**
     * Pour ajouter un abonné
     * @param abonneDto
     * @return un objet de type abonné
     */
    Abonne ajouterAbonne(AbonneDto abonneDto) throws NotFoundException, AlreadyRelatedException;

    /**
     * Pour modifier un abonné
     * @param id de l'abonné
     * @param abonneDto dto
     * @return un objet de type Abonne
     */
    Abonne modifierAbonne(Long id, AbonneDto abonneDto) throws NotFoundException, AlreadyRelatedException;

    /**
     * Pour supprimer un abonné
     * @param id de l'abonné à supprimer
     */
    void supprimerAbonneById(Long id);

    /**
     * Pour supprimer tous les abonnés
     */
    void supprimerTousLesAbonnes();

    /**
     * Pour changer le statut de l'abonné
     * @param id de l'abonné
     * @param requestBody la valeur de statut
     * @return un objet de type Abonne
     */
    Abonne changerLeStatutDeLAbonne(Long id, String requestBody) throws NotFoundException;

    /**
     * Pour associer une agence à un abonné
     * @param abonne_id de l'abonné
     * @param agence_id de l'agence
     * @return un Abonne
     */
    Abonne associerAgence(Long abonne_id,Long agence_id) throws NotFoundException;

    /**
     * Pour disassocier une agence d'un abonné
     * @param abonne_id de l'abonné
     * @return un Abonne
     */
    Abonne disassocierAgence(Long abonne_id) throws NotFoundException;

    /**
     * Pour associer un backoofice à un abonné
     * @param abonne_id de l'abonné
     * @param backoffice_id du backoffice
     * @return un Abonne
     */
    Abonne associerBackOffice(Long abonne_id,Long backoffice_id) throws NotFoundException;

    /**
     * Pour disassocier un backoffice d'un abonné
     * @param abonne_id de l'abonné
     * @return un Abonne
     */
    Abonne disassocierBackOffice(Long abonne_id) throws NotFoundException;

    /**
     * Pour associer un contrat à un abonné
     * @param abonne_id de l'abonné
     * @param contrat_id du contrat
     * @return un Abonne
     */
    Abonne associerContrat(Long abonne_id,Long contrat_id) throws NotFoundException, AlreadyRelatedException;

    /**
     * Pour disassocier un contrat d'un abonné
     * @param abonne_id de l'abonné
     * @return un Abonne
     */
    Abonne disassocierContrat(Long abonne_id) throws NotFoundException;

    /**
     * Pour convertir un String à un Long pour les deux methodes de l'agence et backoffice
     * @param input string
     * @return Long
     */
    Long stringToLong(String input);
}
