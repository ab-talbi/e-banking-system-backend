package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Agence;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.BackOffice;

import java.util.Optional;

public interface AbonneService {
    Abonne ajouterAbonne(Abonne abonne);
    Optional<Abonne> trouverUnAbonneById(Long id);
    void supprimerAbonneById(Long id);
    void supprimerTousLesAbonnes();

    Optional<Agence> trouverUneAgenceById(Long agence_id);
    Optional<BackOffice> trouverUnBackOfficeById(Long backoffice_id);
}
