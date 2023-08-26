package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;

import java.util.Optional;

public interface AbonneService {
    Abonne ajouterAbonne(Abonne abonne);
    Optional<Abonne> trouverUnAbonneById(Long id);
    void supprimerAbonneById(Long id);
    void supprimerTousLesAbonnes();
}
