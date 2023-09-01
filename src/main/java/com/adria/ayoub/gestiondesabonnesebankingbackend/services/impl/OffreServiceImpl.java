package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.OffreDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.help.SortEtOrder;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.OffreRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.OffreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OffreServiceImpl implements OffreService {

    private final int PAGE_SIZE = 10;

    private OffreRepository offreRepository;

    public OffreServiceImpl(OffreRepository offreRepository){
        this.offreRepository = offreRepository;
    }

    /**
     * Pour trouver des offres
     * @param search mot clé (libelle ou description)
     * @param val valeur à chercher
     * @param page numero de page
     * @param sort pour filtrer (field et direction)
     * @return une page des offres
     */
    @Override
    public Page<Offre> trouverLesOffres(String search, String val, int page, String[] sort) {

        page = page > 0 ? page : 0;

        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Pageable pagingSort = PageRequest.of(page, PAGE_SIZE, Sort.by(orders));

        Page<Offre> pageOffres;

        if (search == null || val == null){
            pageOffres = offreRepository.findAll(pagingSort);
        }else {
            if(search.equals("libelle")){
                pageOffres = offreRepository.findByLibelleContainingIgnoreCase(val,pagingSort);
            }else if(search.equals("description")){
                pageOffres = offreRepository.findByDescriptionContainingIgnoreCase(val,pagingSort);
            }else{
                pageOffres = Page.empty(pagingSort);
            }
        }

        return pageOffres;
    }

    /**
     * Pour ajouter un offre
     * @param offreDto dto
     * @return un objet de type Offre
     */
    @Override
    public Offre ajouterOffre(OffreDto offreDto) {
        Offre offre = new Offre(null,offreDto.getLibelle(), offreDto.getDescription(),null);
        return offreRepository.save(offre);
    }

    /**
     * Pour modifier un offre
     * @param id de l'offre
     * @param offreDto dto
     * @return objet de type offre
     * @throws NotFoundException
     */
    @Override
    public Offre modifierOffre(Long id, OffreDto offreDto) throws NotFoundException {
        Optional<Offre> offreOptional = offreRepository.findById(id);

        if(offreOptional.isPresent()){
            Offre offre = offreOptional.get();
            offre.setLibelle(offreDto.getLibelle());
            offre.setDescription(offreDto.getDescription());
            return offreRepository.save(offre);
        }else{
            throw new NotFoundException("Cet offre n'existe pas pour etre modifié!");
        }
    }

    /**
     * Pour trouver un offre avec l'id passé au parametre
     * @param id de l'offre
     * @return Optional<Offre>
     */
    @Override
    public Offre trouverUnOffreById(Long id) throws NotFoundException {
        Optional<Offre> offreOptional = offreRepository.findById(id);
        if(offreOptional.isPresent()){
            Offre offre = offreOptional.get();
            return offre;
        }else{
            throw new NotFoundException("Cet offre n'existe pas");
        }
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
