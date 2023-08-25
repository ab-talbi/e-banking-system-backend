package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;

import java.util.Optional;

public interface OffreService {
    Offre ajouterOffre(Offre offre);
    Optional<Offre> trouverUnOffreById(Long id);
    void supprimerOffreById(Long id);
    void supprimerTousLesOffres();
}
