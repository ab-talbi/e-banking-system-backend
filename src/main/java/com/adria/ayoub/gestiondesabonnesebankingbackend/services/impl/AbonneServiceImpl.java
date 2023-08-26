package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AbonneRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AbonneServiceImpl implements AbonneService {

    private AbonneRepository abonneRepository;

    public AbonneServiceImpl(AbonneRepository abonneRepository){
        this.abonneRepository = abonneRepository;
    }

    /**
     * Pour ajouter un abonné
     * @param abonne
     * @return un objet de type abonné
     */
    @Override
    public Abonne ajouterAbonne(Abonne abonne) {
        return abonneRepository.save(abonne);
    }

    /**
     * Pour trouver un abonné avec l'id passé au parametre
     * @param id de l'abonné
     * @return Optional<Abonne>
     */
    @Override
    public Optional<Abonne> trouverUnAbonneById(Long id) {
        return abonneRepository.findById(id);
    }

    /**
     * Pour supprimer l'abonné de cette id
     * @param id de l'abonné à supprimer
     */
    @Override
    public void supprimerAbonneById(Long id) {
        abonneRepository.deleteById(id);
    }

    /**
     * Pour supprimer tous les abonnés
     */
    @Override
    public void supprimerTousLesAbonnes() {
        abonneRepository.deleteAll();
    }
}
