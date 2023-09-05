package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.ContratDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.help.SortEtOrder;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AbonneRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.ContratRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.OffreRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.ContratService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContratServiceImplTest {

    @Autowired
    private ContratService contratService;
    @MockBean
    private ContratRepository contratRepository;
    @MockBean
    private OffreRepository offreRepository;
    @MockBean
    private AbonneRepository abonneRepository;

    /**
     * Pour creer un objet de type ContratDto
     * @param intitule
     * @return ContratDto
     */
    private ContratDto createContratDto(String intitule){
        ContratDto contratDto = ContratDto.build(intitule,"Actif",null,null);
        return contratDto;
    }

    /**
     * Pour creer un objet de type Contrat
     * @param intitule
     * @return Contrat
     */
    private Contrat createContrat(Long id, String intitule){
        Contrat contrat = new Contrat(id, intitule, Statut.ACTIF,null,null);
        return contrat;
    }

    /**
     * Pour tester la methode get les contrats sans search (tous les contrats)
     */
    @Test
    public void givenDefaultSearch_whenTrouverLesContrats_thenReturnPageDeTousLesContrats(){
        String search = null;
        String val = null;
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Contrat contrat1 = createContrat(1L,"Contrat 1");
        Contrat contrat2  = createContrat(2L,"Contrat 2");

        List<Contrat> contrats = Arrays.asList(contrat1, contrat2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Contrat> contratPage = new PageImpl<>(contrats, pageable, contrats.size());

        when(contratRepository.findAll(pageable)).thenReturn(contratPage);

        Page<Contrat> contratPageTrouve = contratService.trouverLesContrats(search,val, page, sort);

        assertNotNull(contratPageTrouve);
        assertEquals(2,contratPageTrouve.getTotalElements());
        assertEquals(contratPage,contratPageTrouve);
    }

    /**
     * Pour tester la methode get les contrats avec search par intitule
     */
    @Test
    public void givenSearchByIntitule_whenTrouverLesContrats_thenReturnPageDesContrats(){
        String search = "intitule";
        String val = "";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Contrat contrat1 = createContrat(1L,"Contrat 1");
        Contrat contrat2 = createContrat(2L,"Contrat 2");

        List<Contrat> contrats = Arrays.asList(contrat1, contrat2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Contrat> contratPage = new PageImpl<>(contrats, pageable, contrats.size());

        when(contratRepository.findByIntituleContainingIgnoreCase(val,pageable)).thenReturn(contratPage);

        Page<Contrat> contratPageTrouve = contratService.trouverLesContrats(search,val, page, sort);

        assertNotNull(contratPageTrouve);
        assertEquals(2,contratPageTrouve.getTotalElements());
        assertEquals(contratPage,contratPageTrouve);
    }

    /**
     * Pour tester la methode get les contrats avec search par statut valide (actif ou suspendu)
     */
    @Test
    public void givenSearchByStatutValide_whenTrouverLesContrats_thenReturnPageDesContrats(){
        String search = "statut";
        String val = "actif"; //valide, meme pour suspendu
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Contrat contrat1 = createContrat(1L,"Contrat 1");
        Contrat contrat2 = createContrat(2L,"Contrat 2");

        List<Contrat> contrats = Arrays.asList(contrat1, contrat2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Contrat> contratPage = new PageImpl<>(contrats, pageable, contrats.size());

        when(contratRepository.findByStatut(Statut.ACTIF,pageable)).thenReturn(contratPage);

        Page<Contrat> contratPageTrouve = contratService.trouverLesContrats(search,val, page, sort);

        assertNotNull(contratPageTrouve);
        assertEquals(2,contratPageTrouve.getTotalElements());
        assertEquals(contratPage,contratPageTrouve);
    }

    /**
     * Pour tester la methode get les contrats avec search par statut pas valide
     */
    @Test
    public void givenSearchByStatutPasValide_whenTrouverLesContrats_thenReturnPageVide(){
        String search = "statut";
        String val = "test"; //pas valide
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));

        Page<Contrat> contratPageTrouve = contratService.trouverLesContrats(search,val, page, sort);

        assertEquals(Page.empty(pageable),contratPageTrouve);
    }

    /**
     * Pour tester la methode get les contrats avec search par abonne
     */
    @Test
    public void givenSearchByAbonne_whenTrouverLesContrats_thenReturnPageDesContrats(){
        String search = "abonne";
        String val = "nom ou prenom de l'abonné";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Contrat contrat1 = createContrat(1L,"Contrat 1");
        Contrat contrat2 = createContrat(2L,"Contrat 2");

        List<Contrat> contrats = Arrays.asList(contrat1, contrat2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Contrat> contratPage = new PageImpl<>(contrats, pageable, contrats.size());

        when(contratRepository.findByNomOuPrenomAbonneContains(val,pageable)).thenReturn(contratPage);

        Page<Contrat> contratPageTrouve = contratService.trouverLesContrats(search,val, page, sort);

        assertNotNull(contratPageTrouve);
        assertEquals(2,contratPageTrouve.getTotalElements());
        assertEquals(contratPage,contratPageTrouve);
    }

    /**
     * Pour tester la methode get les contrats avec search pas valide
     */
    @Test
    public void givenSearchPasValide_whenTrouverLesContrats_thenReturnPageVide(){
        String search = "attribut n'existe pas";
        String val = "";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));

        Page<Contrat> contratPageTrouve = contratService.trouverLesContrats(search,val, page, sort);

        assertEquals(Page.empty(pageable),contratPageTrouve);
    }

    /**
     * Pour tester la fonctionnalité de get un contrat by id valide
     */
    @Test
    public void givenIdValide_whenTrouverUnContratById_thenReturnContratObject() throws NotFoundException {
        Long contratId = 1L;
        Contrat contrat = createContrat(1L,"Contrat 1");

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));

        Contrat contratTrouve = contratService.trouverUnContratById(contratId);

        assertNotNull(contratTrouve);
        assertEquals(contrat,contratTrouve);
    }

    /**
     * Pour tester la fonctionnalité de get un contrat by id pas valide
     */
    @Test
    public void givenIdPasValide_whenTrouverUnContratById_thenReturnNotFounException() {
        Long contratId = 1L;

        when(contratRepository.findById(contratId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            contratService.trouverUnContratById(contratId);
        });
    }

}
