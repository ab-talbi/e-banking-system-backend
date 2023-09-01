package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Agence;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.BackOffice;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Sexe;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AbonneRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AgenceRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.BackOfficeRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.ContratRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AbonneServiceImpl implements AbonneService {

    private AbonneRepository abonneRepository;
    private AgenceRepository agenceRepository;
    private BackOfficeRepository backOfficeRepository;
    private ContratRepository contratRepository;

    public AbonneServiceImpl(AbonneRepository abonneRepository, AgenceRepository agenceRepository, BackOfficeRepository backOfficeRepository, ContratRepository contratRepository){
        this.abonneRepository = abonneRepository;
        this.agenceRepository = agenceRepository;
        this.backOfficeRepository = backOfficeRepository;
        this.contratRepository = contratRepository;
    }

    /**
     * Pour trouver tous les abonnes
     * @param pageable
     * @return une page des abonnes
     */
    @Override
    public Page<Abonne> trouverTousLesAbonnes(Pageable pageable) {
        return abonneRepository.findAll(pageable);
    }

    /**
     * Pour trouver une list des abonnes à partir de clé
     * @param search nom, prenom, tel, email, statut, sexe...
     * @param val clé à chercher
     * @param pageable
     * @return une page des abonnes
     */
    @Override
    public Page<Abonne> trouverUneListeDesAbonnes(String search, String val, Pageable pageable) {
        if(search.equals("nom")){
            return abonneRepository.findByNomContainingIgnoreCase(val,pageable);
        }else if(search.equals("prenom")){
            return abonneRepository.findByPrenomContainingIgnoreCase(val,pageable);
        }else if(search.equals("email")){
            return abonneRepository.findByEmailContainingIgnoreCase(val,pageable);
        }else if(search.equals("adresse")){
            return abonneRepository.findByAdresseContainingIgnoreCase(val,pageable);
        }else if(search.equals("telephone")){
            return abonneRepository.findByTelephoneContainingIgnoreCase(val,pageable);
        }else if(search.equals("statut")){
            if(val.equals("ACTIF") || val.equals("SUSPENDU")){
                Statut statut = Statut.valueOf(val);
                return abonneRepository.findByStatut(statut,pageable);
            }
            return Page.empty(pageable);
        }else if(search.equals("sexe")){
            if(val.equals("HOMME") || val.equals("FEMME")){
                Sexe sexe = Sexe.valueOf(val);
                return abonneRepository.findBySexe(sexe,pageable);
            }
            return Page.empty(pageable);
        }else if(search.equals("contrat")){
            return abonneRepository.findByIntituleContratContains(val,pageable);
        }else if(search.equals("agence")){
            Long id = stringToLong(val);
            if(id != null){
                return abonneRepository.findByAgence(id,pageable);
            }
            return Page.empty(pageable);
        }else if(search.equals("backoffice")){
            Long id = stringToLong(val);
            if(id != null){
                return abonneRepository.findByBackOffice(id,pageable);
            }
            return Page.empty(pageable);
        }else{
            return Page.empty(pageable);
        }
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

    /**
     * Pour trouver une agence
     * @param agence_id
     * @return une Agence
     */
    @Override
    public Optional<Agence> trouverUneAgenceById(Long agence_id) {
        return agenceRepository.findById(agence_id);
    }

    /**
     * Pour associé un back office
     * @param backoffice_id
     * @return BackOffice
     */
    @Override
    public Optional<BackOffice> trouverUnBackOfficeById(Long backoffice_id) {
        return backOfficeRepository.findById(backoffice_id);
    }

    /**
     * Pour trouver un contrat
     * @param contrat_id
     * @return Optional<Contrat>
     */
    @Override
    public Optional<Contrat> trouverUnContratById(Long contrat_id) {
        return contratRepository.findById(contrat_id);
    }

    /**
     * Pour convertir un String à un Long pour les deux methodes de l'agence et backoffice
     * @param input string
     * @return Long
     */
    @Override
    public Long stringToLong(String input) {
        if (input.matches("\\d+")) { // si contient des numeriques
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        return null;
    }
}
