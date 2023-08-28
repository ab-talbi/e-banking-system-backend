package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Agence;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.BackOffice;

import java.util.Optional;

public interface AbonneService {
    /**
     * Pour ajouter un abonné
     * @param abonne
     * @return un objet de type abonné
     */
    Abonne ajouterAbonne(Abonne abonne);

    /**
     * Pour trouver un abonné avec l'id passé au parametre
     * @param id de l'abonné
     * @return Optional<Abonne>
     */
    Optional<Abonne> trouverUnAbonneById(Long id);

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
     * Pour trouver une agence
     * @param agence_id de l'agence à trouver
     * @return Optional<Agence>
     */
    Optional<Agence> trouverUneAgenceById(Long agence_id);

    /**
     * Pour teouver un backoffice
     * @param backoffice_id de backoffice
     * @return Optional<BackOffice>
     */
    Optional<BackOffice> trouverUnBackOfficeById(Long backoffice_id);
}
