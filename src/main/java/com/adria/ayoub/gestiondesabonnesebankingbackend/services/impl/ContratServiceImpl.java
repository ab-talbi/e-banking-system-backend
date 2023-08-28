package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.ContratRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.ContratService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContratServiceImpl implements ContratService {

    private ContratRepository contratRepository;

    public ContratServiceImpl(ContratRepository contratRepository){
        this.contratRepository = contratRepository;
    }

    /**
     * Pour ajouter un contrat
     * @param contrat
     * @return objet de type Contrat
     */
    @Override
    public Contrat ajouterContrat(Contrat contrat) {
        return contratRepository.save(contrat);
    }

    /**
     * Pour trouver un contrat
     * @param id de contrat à trouver
     * @return Optional<Contrat>
     */
    @Override
    public Optional<Contrat> trouverUnContratById(Long id) {
        return contratRepository.findById(id);
    }

    /**
     * Pour supprimer un contrat
     * @param id du contrat à supprimer
     */
    @Override
    public void supprimerContratById(Long id) {
        contratRepository.deleteById(id);
    }

    /**
     * Pour supprimer tous les contrats
     */
    @Override
    public void supprimerTousLesContrats() {
        contratRepository.deleteAll();
    }
}
