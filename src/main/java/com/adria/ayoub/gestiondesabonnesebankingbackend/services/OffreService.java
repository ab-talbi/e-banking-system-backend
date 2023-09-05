package com.adria.ayoub.gestiondesabonnesebankingbackend.services;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.OffreDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

public interface OffreService {

    /**
     * Pour trouver une list des offres
     * @param search mot clé (libelle ou description)
     * @param val valeur à chercher
     * @param page numero de page
     * @param sort pour filtrer (field et direction)
     * @return une page des offres
     */
    Page<Offre> trouverLesOffres(String search,String val, int page, String[] sort);

    /**
     * Pour trouver un offre
     * @param id de l'offre à trouver
     * @return Optional<Offre>
     */
    Offre trouverUnOffreById(Long id) throws NotFoundException;

    /**
     * Pour ajouter un offre
     * @param offreDto à ajouter
     * @return objet de type offre
     */
    Offre ajouterOffre(OffreDto offreDto);

    /**
     * Pour modifier un offre
     * @param id de l'offre
     * @param offreDto dto
     * @return un objet de type Offre
     */
    Offre modifierOffre(Long id,OffreDto offreDto) throws NotFoundException;

    /**
     * Pour supprimer un offre
     * @param id de l'offre à supprimer
     */
    void supprimerOffreById(Long id);

    /**
     * Pour supprimer tous les offres
     */
    void supprimerTousLesOffres();
}
