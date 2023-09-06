package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.OffreDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.help.SortEtOrder;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.OffreRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.OffreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OffreServiceImplTest {

    @Autowired
    private OffreService offreService;

    @MockBean
    private OffreRepository offreRepository;

    /**
     * Pour creer un objet de type OffreDto
     * @param libelle de l'offre
     * @return OffreDto
     */
    private OffreDto createOffreDto(String libelle){
        return OffreDto.build(libelle, "Description de l'"+libelle.toLowerCase());
    }

    /**
     * Pour creer un objet de type Offre
     * @return Offre
     */
    private Offre createOffre(Long id){
        List<Contrat> contrats = new ArrayList<>();
        return new Offre(id, "Offre "+id,"Description de l'offre "+id,contrats);
    }

    /**
     * Pour tester la methode get les offres sans search (tous les offres)
     */
    @Test
    public void givenDefaultSearch_whenTrouverLesOffres_ThenReturnPageDeTousLesOffres(){
        String search = null;
        String val = null;
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Offre offre1 = createOffre(1L);
        Offre offre2 = createOffre(2L);

        List<Offre> offres = Arrays.asList(offre1, offre2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Offre> offrePage = new PageImpl<>(offres, pageable, offres.size());

        when(offreRepository.findAll(pageable)).thenReturn(offrePage);

        Page<Offre> offrePageTrouve = offreService.trouverLesOffres(search,val, page, sort);

        assertNotNull(offrePageTrouve);
        assertEquals(2,offrePageTrouve.getTotalElements());
        assertEquals(offrePage,offrePageTrouve);
    }

    /**
     * Pour tester la methode get les offres avec search par libelle
     */
    @Test
    public void givenSearchByLibelle_whenTrouverLesOffres_ThenReturnPageDesOffres(){
        String search = "libelle";
        String val = "ff";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Offre offre1 = createOffre(1L);
        Offre offre2 = createOffre(2L);

        List<Offre> offres = Arrays.asList(offre1, offre2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Offre> offrePage = new PageImpl<>(offres, pageable, offres.size());

        when(offreRepository.findByLibelleContainingIgnoreCase(val,pageable)).thenReturn(offrePage);

        Page<Offre> offrePageTrouve = offreService.trouverLesOffres(search,val, page, sort);

        assertNotNull(offrePageTrouve);
        assertEquals(2,offrePageTrouve.getTotalElements());
        assertEquals(offrePage,offrePageTrouve);
    }

    /**
     * Pour tester la methode get les offres avec search par description
     */
    @Test
    public void givenSearchByDescription_whenTrouverLesOffres_ThenReturnPageDesOffres(){
        String search = "description";
        String val = "ff";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Offre offre1 = createOffre(1L);
        Offre offre2 = createOffre(2L);

        List<Offre> offres = Arrays.asList(offre1, offre2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Offre> offrePage = new PageImpl<>(offres, pageable, offres.size());

        when(offreRepository.findByDescriptionContainingIgnoreCase(val,pageable)).thenReturn(offrePage);

        Page<Offre> offrePageTrouve = offreService.trouverLesOffres(search,val, page, sort);

        assertNotNull(offrePageTrouve);
        assertEquals(2,offrePageTrouve.getTotalElements());
        assertEquals(offrePage,offrePageTrouve);
    }

    /**
     * Pour tester la methode get les offres avec search pas valide
     */
    @Test
    public void givenSearchBySomethingPasValide_whenTrouverLesOffres_ThenReturnPageVide(){
        String search = "autre";
        String val = "ff";
        int page = 0;
        String[] sort = new String[]{"id","desc"};

        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);
        Pageable pagingSort = PageRequest.of(page, 10, Sort.by(orders));

        Page<Offre> offrePageTrouve = offreService.trouverLesOffres(search,val, page, sort);

        assertEquals(Page.empty(pagingSort),offrePageTrouve);
    }

    /**
     * Pour tester la fonctionnalité de get un offre by id valide
     */
    @Test
    public void givenIdValide_whenTrouverUnOffreById_ThenReturnOffreObject() throws NotFoundException {
        Long offreId = 1L;
        Offre offre = createOffre(1L);

        when(offreRepository.findById(offreId)).thenReturn(Optional.of(offre));

        Offre offreTrouve = offreService.trouverUnOffreById(offreId);

        assertNotNull(offreTrouve);
        assertEquals(offre,offreTrouve);
    }

    /**
     * Pour tester la fonctionnalité de get un offre by id pas valide
     */
    @Test
    public void givenIdPasValide_whenTrouverUnOffreById_ThenReturnNotFounException() {
        Long offreId = 1L;

        when(offreRepository.findById(offreId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> offreService.trouverUnOffreById(offreId));
    }

    /**
     * Pour tester la fonctionnalité de l'ajout de l'offre
     */
    @Test
    public void givenOffreDtoObject_whenAjouterOffre_ThenReturnOffreObject(){
        OffreDto offreDto = createOffreDto("Offre 1");
        Offre offre = createOffre(1L);

        when(offreRepository.save(any(Offre.class))).thenReturn(offre);

        Offre offreAjoute = offreService.ajouterOffre(offreDto);

        System.out.println("offre : "+offre);
        System.out.println("offre ajouté : "+offreAjoute);

        assertNotNull(offreAjoute);
        assertEquals(offre,offreAjoute);
    }

    /**
     * Pour tester la fonctionnalité de mise à jour de l'offre qui deja existe
     */
    @Test
    public void givenOffreDtoObjectEtIdValide_whenModifierOffre_ThenReturnOffreObject() throws NotFoundException {
        Long offreId = 1L;
        OffreDto offreDto = createOffreDto("Offre 1");
        Offre offre = createOffre(1L);

        when(offreRepository.findById(offreId)).thenReturn(Optional.of(offre));
        when(offreRepository.save(any(Offre.class))).thenReturn(offre);

        Offre offreModifie = offreService.modifierOffre(offreId,offreDto);

        System.out.println("offre : "+offre);
        System.out.println("offre modifié : "+offreModifie);

        assertNotNull(offreModifie);
        assertEquals(offre,offreModifie);
    }

    /**
     * Pour tester la fonctionnalité de mise à jour d'un offre qui pas existant
     */
    @Test
    public void givenOffreDtoObjectEtIdPasValide_whenModifierOffre_ThenReturnNotFounException() {
        Long offreId = 1L;
        OffreDto offreDto = createOffreDto("Offre 1");

        when(offreRepository.findById(offreId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> offreService.modifierOffre(offreId,offreDto));
    }

    /**
     * Pour tester la suppression d'un offre by id
     */
    @Test
    public void givenId_whenSupprimerOffreById_thenDoNothing(){

        Offre offre = createOffre(1L);

        offreService.supprimerOffreById(offre.getId());

        verify(offreRepository, times(1)).deleteById(offre.getId());
    }

    /**
     * Pour tester la suppression de tous les offres
     */
    @Test
    public void givenNothing_whenSupprimerTousLesOffres_thenDoNothing(){

        offreService.supprimerTousLesOffres();

        verify(offreRepository, times(1)).deleteAll();
    }
}
