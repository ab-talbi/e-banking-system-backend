package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.OffreRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.OffreService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OffreServiceImpl implements OffreService {

    private OffreRepository offreRepository;

    public OffreServiceImpl(OffreRepository offreRepository){
        this.offreRepository = offreRepository;
    }

    /**
     * Pour ajouter un offre
     * @param offre entity
     * @return un objet de type Offre
     */
    @Override
    public Offre ajouterOffre(Offre offre) {
        return offreRepository.save(offre);
    }

    /**
     * Pour trouver un offre avec l'id passé au parametre
     * @param id de l'offre
     * @return Optional<Offre>
     */
    @Override
    public Optional<Offre> trouverUnOffreById(Long id) {
        return offreRepository.findById(id);
    }

    /**
     * Pour supprimer l'offre de cette id
     * @param id de l'offre à supprimer
     */
    @Override
    public void supprimerOffreById(Long id) {
        offreRepository.deleteById(id);
    }

    /**
     * Pour supprimer tous les offres
     */
    @Override
    public void supprimerTousLesOffres() {
        offreRepository.deleteAll();
    }
}
