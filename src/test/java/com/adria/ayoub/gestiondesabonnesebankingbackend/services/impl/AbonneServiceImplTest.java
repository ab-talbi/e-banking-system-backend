package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.AbonneDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.*;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Sexe;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyRelatedException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.help.SortEtOrder;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AbonneRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AgenceRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.BackOfficeRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.ContratRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
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
public class AbonneServiceImplTest {

    @Autowired
    private AbonneService abonneService;
    @MockBean
    private AbonneRepository abonneRepository;
    @MockBean
    private AgenceRepository agenceRepository;
    @MockBean
    private BackOfficeRepository backOfficeRepository;
    @MockBean
    private ContratRepository contratRepository;

    /**
     * Pour creer un abonneDto
     * @param contratId si existe
     * @param agenceId si existe
     * @param backOfficeId si existe
     * @return AbonneDto
     */
    private AbonneDto createAbonneDto(Long contratId, Long agenceId, Long backOfficeId){
        AbonneDto abonneDto = AbonneDto.build("NOM","PRENOM","EMAIL@EXAMPLE.COM","ADRESSE","TEL","HOMME","ACTIF",contratId,agenceId,backOfficeId);
        return abonneDto;
    }

    /**
     * Pour creer un abonne
     * @param id de l'abonne
     * @param contrat si existe
     * @param agence si existe
     * @param backOffice si existe
     * @return un Abonne
     */
    private Abonne createAbonne(Long id, Contrat contrat, Agence agence, BackOffice backOffice){
        Abonne abonne = new Abonne(id,"NOM","PRENOM","EMAIL@EXAMPLE.COM","ADRESSE","TEL", Sexe.HOMME, Statut.ACTIF,contrat,agence,backOffice);
        return abonne;
    }

    /**
     * Pour creer un contrat
     * @param id de contrat
     * @param abonne si existe
     * @return Contrat
     */
    private Contrat createContrat(Long id, Abonne abonne){
        List<Offre> offres = new ArrayList<>();
        Contrat contrat = new Contrat(id,"Contrat "+id,Statut.ACTIF,abonne,offres);
        return contrat;
    }

    /**
     * Pour creer une agence
     * @param id de l'agence
     * @return Agence
     */
    private Agence createAgence(Long id){
        List<Abonne> abonnes = new ArrayList<>();
        Agence agence = new Agence(id,"Agence "+id,"Adresse de l'agence "+id,abonnes);
        return agence;
    }

    /**
     * Pour creer un backoffice
     * @param id de backoffice
     * @return un backoffice
     */
    private BackOffice createBackOffice(Long id){
        List<Abonne> abonnes = new ArrayList<>();
        BackOffice backOffice = new BackOffice(id,"Nom de Backoffice "+id,"Prenom de Backoffice "+id,"email de backoffice "+id,abonnes);
        return backOffice;
    }

    /**
     * Pour tester la methode get les abonnes sans search (tous les abonnes)
     */
    @Test
    public void givenDefaultSearch_whenTrouverLesAbonnes_thenReturnPageDeTousLesAbonnes(){
        String search = null;
        String val = null;
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findAll(pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par nom
     */
    @Test
    public void givenSearchByNom_whenTrouverLesAbonnes_thenReturnPageDesAbonnes(){
        String search = "nom";
        String val = "";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByNomContainingIgnoreCase(val,pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par prenom
     */
    @Test
    public void givenSearchByPrenom_whenTrouverLesAbonnes_thenReturnPageDesAbonnes(){
        String search = "prenom";
        String val = "";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByPrenomContainingIgnoreCase(val,pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par email
     */
    @Test
    public void givenSearchByEmail_whenTrouverLesAbonnes_thenReturnPageDesAbonnes(){
        String search = "email";
        String val = "";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByEmailContainingIgnoreCase(val,pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par adresse
     */
    @Test
    public void givenSearchByAdresse_whenTrouverLesAbonnes_thenReturnPageDesAbonnes(){
        String search = "adresse";
        String val = "";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByAdresseContainingIgnoreCase(val,pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par telephone
     */
    @Test
    public void givenSearchByTelephone_whenTrouverLesAbonnes_thenReturnPageDesAbonnes(){
        String search = "telephone";
        String val = "";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByTelephoneContainingIgnoreCase(val,pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par statut valide (actif ou suspendu)
     */
    @Test
    public void givenSearchByStatutValide_whenTrouverLesContrats_thenReturnPageDesAbonnes(){
        String search = "statut";
        String val = "actif"; //valide, meme pour suspendu
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByStatut(Statut.ACTIF,pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par statut pas valide
     */
    @Test
    public void givenSearchByStatutPasValide_whenTrouverLesContrats_thenReturnPageVide(){
        String search = "statut";
        String val = "test"; //pas valide
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertEquals(Page.empty(pageable),abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par sexe valide (homme ou femme)
     */
    @Test
    public void givenSearchBySexeValide_whenTrouverLesContrats_thenReturnPageDesAbonnes(){
        String search = "sexe";
        String val = "homme"; //valide, meme pour femme
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findBySexe(Sexe.HOMME,pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par sexe pas valide
     */
    @Test
    public void givenSearchBySexePasValide_whenTrouverLesContrats_thenReturnPageVide(){
        String search = "sexe";
        String val = "autre"; //pas valide
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertEquals(Page.empty(pageable),abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par contrat
     */
    @Test
    public void givenSearchByContrat_whenTrouverLesAbonnes_thenReturnPageDesAbonnes(){
        String search = "contrat";
        String val = "intitule de contrat";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Abonne abonne1 = createAbonne(1L,null,null,null);
        Abonne abonne2 = createAbonne(2L,null,null,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByIntituleContratContains(val,pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search, val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par agence valide
     */
    @Test
    public void givenSearchByAgenceValide_whenTrouverLesAbonnes_thenReturnPageDesAbonnes(){
        String search = "agence";
        String val = "1";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Agence agence = createAgence(1L);

        Abonne abonne1 = createAbonne(1L,null,agence,null);
        Abonne abonne2 = createAbonne(2L,null,agence,null);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByAgence(agence.getId(),pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search, val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par agence pas valide
     */
    @Test
    public void givenSearchByAgencePasValide_whenTrouverLesAbonnes_thenReturnPageVide(){
        String search = "agence";
        String val = ""; //pas valide
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search, val, page, sort);

        assertEquals(Page.empty(pageable),abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par backoffice valide
     */
    @Test
    public void givenSearchByBackOfficeValide_whenTrouverLesAbonnes_thenReturnPageDesAbonnes(){
        String search = "backoffice";
        String val = "1";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        BackOffice backoffice = createBackOffice(1L);

        Abonne abonne1 = createAbonne(1L,null,null,backoffice);
        Abonne abonne2 = createAbonne(2L,null,null,backoffice);

        List<Abonne> abonnes = Arrays.asList(abonne1, abonne2);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));
        Page<Abonne> abonnePage = new PageImpl<>(abonnes, pageable, abonnes.size());

        when(abonneRepository.findByBackOffice(backoffice.getId(),pageable)).thenReturn(abonnePage);

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search, val, page, sort);

        assertNotNull(abonnePageTrouve);
        assertEquals(2,abonnePageTrouve.getTotalElements());
        assertEquals(abonnePage,abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search par backoffice pas valide
     */
    @Test
    public void givenSearchByBackOfficePasValide_whenTrouverLesAbonnes_thenReturnPageVide(){
        String search = "backoffice";
        String val = ""; //pas valide
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search, val, page, sort);

        assertEquals(Page.empty(pageable),abonnePageTrouve);
    }

    /**
     * Pour tester la methode get les abonnes avec search pas valide
     */
    @Test
    public void givenSearchPasValide_whenTrouverLesAbonnes_thenReturnPageVide(){
        String search = "attribut n'existe pas";
        String val = "";
        int page = 0;
        String[] sort = new String[]{"id","desc"};
        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Pageable pageable = PageRequest.of(page, 10, Sort.by(orders));

        Page<Abonne> abonnePageTrouve = abonneService.trouverLesAbonnes(search,val, page, sort);

        assertEquals(Page.empty(pageable),abonnePageTrouve);
    }

    /**
     * Pour tester la fonctionnalité de get un abonne by id valide
     */
    @Test
    public void givenIdValide_whenTrouverUnAbonneById_thenReturnAbonneObject() throws NotFoundException {
        Long abonneId = 1L;
        Abonne abonne = createAbonne(1L,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));

        Abonne abonneTrouve = abonneService.trouverUnAbonneById(abonneId);

        assertNotNull(abonneTrouve);
        assertEquals(abonne,abonneTrouve);
    }

    /**
     * Pour tester la fonctionnalité de get un abonne by id pas valide
     */
    @Test
    public void givenIdPasValide_whenTrouverUnAbonneById_thenReturnNotFounException() {
        Long abonneId = 1L;

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.trouverUnAbonneById(abonneId);
        });
    }

    /**
     * Pour tester la methode de l'ajout de l'abonné (avec contrat, agence et backoffice null)
     */
    @Test
    public void givenAbonneDtoObjectWithoutContratEtAgenceEtBackOffice_whenAjouterAbonne_thenReturnAbonneObject() throws AlreadyRelatedException, NotFoundException {
        AbonneDto abonneDto = createAbonneDto(null,null,null);
        Abonne abonne = createAbonne(1L,null,null,null);

        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneAjoute = abonneService.ajouterAbonne(abonneDto);

        assertNotNull(abonneAjoute);
        assertEquals(abonne,abonneAjoute);
    }

    /**
     * Pour tester la methode de l'ajout de l'abonne (avec contrat existe et valide et agence null et backoffice null)
     * exemple je veux enregistrer un abonne avec un contrat existe dans la base de donnés + n'est pas lié à un autre abonne
     */
    @Test
    public void givenAbonneDtoObjectWithContratExisteEtValide_whenAjouterAbonne_thenReturnAbonneObject() throws AlreadyRelatedException, NotFoundException {
        AbonneDto abonneDto = createAbonneDto(1L,null,null);
        Contrat contrat = createContrat(1L,null); //valide car n'est pas lié à un autre abonné
        Abonne abonne = createAbonne(1L,contrat,null,null);

        when(contratRepository.findById(abonneDto.getContratId())).thenReturn(Optional.of(contrat));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneAjoute = abonneService.ajouterAbonne(abonneDto);

        assertNotNull(abonneAjoute);
        assertEquals(abonne,abonneAjoute);
    }

    /**
     * Pour tester la methode de l'ajout de l'abonne (avec contrat existe mais pas valide et agence null et backoffice null)
     * exemple je veux enregistrer un abonne avec un contrat existe dans la base de donnés mais lié à un autre abonne
     */
    @Test
    public void givenAbonneDtoObjectWithContratExisteEtPasValide_whenAjouterAbonne_thenReturnAbonneObject(){
        AbonneDto abonneDto = createAbonneDto(1L,null,null);

        Abonne autreAbonne = createAbonne(1L, null, null,null);
        Contrat contrat = createContrat(1L,autreAbonne); //pas valide car lié à un autre abonné

        when(contratRepository.findById(abonneDto.getContratId())).thenReturn(Optional.of(contrat));

        assertThrows(AlreadyRelatedException.class, () -> {
            abonneService.ajouterAbonne(abonneDto);
        });
    }

    /**
     * Pour tester la methode de l'ajout de l'abonne (avec contrat n'existe pas et agence null et backoffice null)
     * exemple je veux enregistrer un abonne avec un contrat qui n'existe pas dans la base de donnés --> NotFoundException
     */
    @Test
    public void givenAbonneDtoObjectWithContratPasExiste_whenAjouterAbonne_thenReturnNotFoundException(){
        AbonneDto abonneDto = createAbonneDto(1L,null,null);

        when(contratRepository.findById(abonneDto.getContratId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.ajouterAbonne(abonneDto);
        });
    }

    /**
     * Pour tester la methode de l'ajout de l'abonne (avec contrat null, agence existe et backoffice null)
     * exemple je veux enregistrer un abonne avec une agence existe dans la base de donnés
     */
    @Test
    public void givenAbonneDtoObjectWithAgenceExiste_whenAjouterAbonne_thenReturnAbonneObject() throws AlreadyRelatedException, NotFoundException {
        AbonneDto abonneDto = createAbonneDto(null,1L,null);
        Agence agence = createAgence(1L);
        Abonne abonne = createAbonne(1L,null,agence,null);

        when(agenceRepository.findById(abonneDto.getAgenceId())).thenReturn(Optional.of(agence));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneAjoute = abonneService.ajouterAbonne(abonneDto);

        assertNotNull(abonneAjoute);
        assertEquals(abonne,abonneAjoute);
    }

    /**
     * Pour tester la methode de l'ajout de l'abonne (avec contrat null, agence existe pas et backoffice null)
     * exemple je veux enregistrer un abonne avec une agence qui n'existe pas dans la base de donnés --> NotFoundException
     */
    @Test
    public void givenAbonneDtoObjectWithAgencePasExiste_whenAjouterAbonne_thenReturnNotFoundException(){
        AbonneDto abonneDto = createAbonneDto(null,5L,null);

        when(agenceRepository.findById(abonneDto.getAgenceId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.ajouterAbonne(abonneDto);
        });
    }

    /**
     * Pour tester la methode de l'ajout de l'abonne (avec contrat null, agence null et backoffice existe)
     * exemple je veux enregistrer un abonne avec un backoffice existe dans la base de donnés
     */
    @Test
    public void givenAbonneDtoObjectWithBackOfficeExiste_whenAjouterAbonne_thenReturnAbonneObject() throws AlreadyRelatedException, NotFoundException {
        AbonneDto abonneDto = createAbonneDto(null,null,1L);
        BackOffice backOffice = createBackOffice(1L);
        Abonne abonne = createAbonne(1L,null,null,backOffice);

        when(backOfficeRepository.findById(abonneDto.getBackOfficeId())).thenReturn(Optional.of(backOffice));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneAjoute = abonneService.ajouterAbonne(abonneDto);

        assertNotNull(abonneAjoute);
        assertEquals(abonne,abonneAjoute);
    }

    /**
     * Pour tester la methode de l'ajout de l'abonne (avec contrat null, agence null et backoffice existe pas)
     * exemple je veux enregistrer un abonne avec un backoffice qui n'existe pas dans la base de donnés --> NotFoundException
     */
    @Test
    public void givenAbonneDtoObjectWithBackOfficePasExiste_whenAjouterAbonne_thenReturnNotFoundException(){
        AbonneDto abonneDto = createAbonneDto(null,null,1L);

        when(backOfficeRepository.findById(abonneDto.getBackOfficeId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.ajouterAbonne(abonneDto);
        });
    }

    /**
     * Pour tester la methode de mise à jour de l'abonné qui deja existe sans modifier contrat,agence ou backoffice
     */
    @Test
    public void givenAbonneDtoObjectEtIdValide_whenModifierAbonne_ThenReturnAbonneObject() throws AlreadyRelatedException, NotFoundException {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(null,null,null);
        Abonne abonne = createAbonne(1L,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneModifie = abonneService.modifierAbonne(abonneId,abonneDto);

        assertNotNull(abonneModifie);
        assertEquals(abonne,abonneModifie);
    }

    /**
     * Pour tester la methode de mise à jour d'un abonné qui existe, avec contrat pas null et existe et pas lié à un autre abonné
     */
    @Test
    public void givenAbonneDtoObjectEtIdValideEtContratValide_whenModifierAbonne_ThenReturnAbonneObject() throws AlreadyRelatedException, NotFoundException {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(1L,null,null);

        Contrat contrat = createContrat(1L,null); //valide car pas lié à un autre abonné (ou lié au meme abonné)

        Abonne abonne = createAbonne(1L,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(contratRepository.findById(abonneDto.getContratId())).thenReturn(Optional.of(contrat));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneModifie = abonneService.modifierAbonne(abonneId,abonneDto);

        assertNotNull(abonneModifie);
        assertEquals(abonne,abonneModifie);
    }

    /**
     * Pour tester la methode de mise à jour d'un abonné qui existe, avec contrat pas null et existe mais lié à un autre abonné
     */
    @Test
    public void givenAbonneDtoObjectEtIdValideEtContratPasValide_whenModifierAbonne_ThenReturnAlreadyRelatedException() {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(1L,null,null);

        Abonne autreAbonne = createAbonne(2L,null,null,null);
        Contrat contrat = createContrat(1L,autreAbonne); //pas valide car lié à un autre abonné

        Abonne abonne = createAbonne(1L,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(contratRepository.findById(abonneDto.getContratId())).thenReturn(Optional.of(contrat));

        assertThrows(AlreadyRelatedException.class, () -> {
            abonneService.modifierAbonne(abonneId,abonneDto);
        });
    }

    /**
     * Pour tester la methode de mise à jour d'un abonné qui existe, avec contrat pas null, mais n'existe pas
     */
    @Test
    public void givenAbonneDtoObjectEtIdValideEtContratPasExiste_whenModifierAbonne_ThenReturnNotFoundException() {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(1L,null,null);

        Abonne abonne = createAbonne(1L,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(contratRepository.findById(abonneDto.getContratId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.modifierAbonne(abonneId,abonneDto);
        });
    }

    /**
     * Pour tester la methode de mise à jour d'un abonné qui existe, avec une agence pas null et existe
     */
    @Test
    public void givenAbonneDtoObjectEtIdValideEtAgenceExiste_whenModifierAbonne_ThenReturnAbonneObject() throws AlreadyRelatedException, NotFoundException {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(null,1L,null);

        Agence agence = createAgence(1L);

        Abonne abonne = createAbonne(1L,null,agence,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(agenceRepository.findById(abonneDto.getAgenceId())).thenReturn(Optional.of(agence));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneModifie = abonneService.modifierAbonne(abonneId,abonneDto);

        assertNotNull(abonneModifie);
        assertEquals(abonne,abonneModifie);
    }

    /**
     * Pour tester la methode de mise à jour d'un abonné qui existe, avec agence pas null, mais n'existe pas
     */
    @Test
    public void givenAbonneDtoObjectEtIdValideEtAgencePasExiste_whenModifierAbonne_ThenReturnNotFoundException() {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(null,1L,null);

        Abonne abonne = createAbonne(1L,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(agenceRepository.findById(abonneDto.getAgenceId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.modifierAbonne(abonneId,abonneDto);
        });
    }

    /**
     * Pour tester la methode de mise à jour d'un abonné qui existe, avec un backoffice pas null et existe
     */
    @Test
    public void givenAbonneDtoObjectEtIdValideEtBackOfficeExiste_whenModifierAbonne_ThenReturnAbonneObject() throws AlreadyRelatedException, NotFoundException {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(null,null,1L);

        BackOffice backOffice = createBackOffice(1L);

        Abonne abonne = createAbonne(1L,null,null,backOffice);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(backOfficeRepository.findById(abonneDto.getBackOfficeId())).thenReturn(Optional.of(backOffice));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneModifie = abonneService.modifierAbonne(abonneId,abonneDto);

        assertNotNull(abonneModifie);
        assertEquals(abonne,abonneModifie);
    }

    /**
     * Pour tester la methode de mise à jour d'un abonné qui existe, avec backoffice pas null, mais n'existe pas
     */
    @Test
    public void givenAbonneDtoObjectEtIdValideEtBackOfficePasExiste_whenModifierAbonne_ThenReturnNotFoundException() {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(null,null,1L);

        Abonne abonne = createAbonne(1L,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(backOfficeRepository.findById(abonneDto.getBackOfficeId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.modifierAbonne(abonneId,abonneDto);
        });
    }
    
    /**
     * Pour tester la methode de mise à jour de l'abonné qui pas existe
     */
    @Test
    public void givenAbonneDtoObjectEtIdPasValide_whenModifierAbonne_ThenReturnNotFoundException() {
        Long abonneId = 1L;
        AbonneDto abonneDto = createAbonneDto(null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.modifierAbonne(abonneId,abonneDto);
        });
    }

    /**
     * Pour tester la suppression d'un abonné by id
     */
    @Test
    public void givenId_whenSupprimerAbonneById_thenDoNothing(){

        Abonne abonne = createAbonne(1L,null,null,null);

        abonneService.supprimerAbonneById(abonne.getId());

        verify(abonneRepository, times(1)).deleteById(abonne.getId());
    }

    /**
     * Pour tester la suppression de tous les abonnés
     */
    @Test
    public void givenNothing_whenSupprimerTousLesAbonnes_thenDoNothing(){

        abonneService.supprimerTousLesAbonnes();

        verify(abonneRepository, times(1)).deleteAll();
    }

    /**
     * Pour tester la methode changerLeStatutDeLAbonne avec id valide
     */
    @Test
    public void givenIdValide_whenChangerLeStatutDeLAbonne_thenReturnAbonneObject() throws NotFoundException {
        Long abonneId = 1L;
        Abonne abonne = createAbonne(abonneId,null,null,null);
        String statut = "suspendu";

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneApresChangementDuStatut = abonneService.changerLeStatutDeLAbonne(abonneId,statut);

        assertNotNull(abonneApresChangementDuStatut);
        assertEquals(abonne, abonneApresChangementDuStatut);
    }

    /**
     * Pour tester la methode changerLeStatutDeLAbonne avec id pas valide
     */
    @Test
    public void givenIdPasValide_whenChangerLeStatutDeLAbonne_thenReturnNotFoundException() {
        Long abonneId = 1L;
        String statut = "suspendu";

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->{
            abonneService.changerLeStatutDeLAbonne(abonneId,statut);
        });
    }

    /**
     * Pour tester la methode associerAgence avec id_abonne valide et id_agence valide
     */
    @Test
    public void givenIdAbonneValideEtIdAgenceValide_whenAssocierAgence_thenReturnAbonneObject() throws NotFoundException {
        Long abonneId = 1L;
        Long agenceId = 1L;

        Abonne abonne = createAbonne(abonneId,null,null,null);
        Agence agence = createAgence(agenceId);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(agenceRepository.findById(agenceId)).thenReturn(Optional.of(agence));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneApresAssocierAgence = abonneService.associerAgence(abonneId,agenceId);

        assertNotNull(abonneApresAssocierAgence);
        assertEquals(abonne, abonneApresAssocierAgence);
    }

    /**
     * Pour tester la methode associerAgence avec id_abonne valide et id_agence pas existe
     */
    @Test
    public void givenIdAbonneValideEtIdAgencePasValide_whenAssocierAgence_thenReturnNotFoundException() {
        Long abonneId = 1L;
        Long agenceId = 1L;

        Abonne abonne = createAbonne(abonneId,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(agenceRepository.findById(agenceId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.associerAgence(abonneId,agenceId);
        });
    }

    /**
     * Pour tester la methode associerAgence avec id_abonne pas valide
     */
    @Test
    public void givenIdAbonnePasValide_whenAssocierAgence_thenReturnNotFoundException() {
        Long abonneId = 1L;
        Long agenceId = 1L;

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.associerAgence(abonneId,agenceId);
        });
    }

    /**
     * Pour tester la methode disassocierAgence avec id_abonne valide
     */
    @Test
    public void givenIdAbonneValide_whenDisassocierAgence_thenReturnAbonneObject() throws NotFoundException {
        Long abonneId = 1L;

        Abonne abonne = createAbonne(abonneId,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneApresDisassocierAgence = abonneService.disassocierAgence(abonneId);

        assertNotNull(abonneApresDisassocierAgence);
        assertEquals(abonne, abonneApresDisassocierAgence);
    }

    /**
     * Pour tester la methode disassocierAgence avec id_abonne pas valide
     */
    @Test
    public void givenIdAbonnePasValide_whenDisassocierAgence_thenReturnNotFoundException() {
        Long abonneId = 1L;

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.disassocierAgence(abonneId);
        });
    }

    /**
     * Pour tester la methode associerBackOffice avec id_abonne valide et id_backoffice valide
     */
    @Test
    public void givenIdAbonneValideEtIdBackOfficeValide_whenAssocierBackOffice_thenReturnAbonneObject() throws NotFoundException {
        Long abonneId = 1L;
        Long backOfficeId = 1L;

        Abonne abonne = createAbonne(abonneId,null,null,null);
        BackOffice backOffice = createBackOffice(backOfficeId);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(backOfficeRepository.findById(backOfficeId)).thenReturn(Optional.of(backOffice));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneApresAssocierBackOffice = abonneService.associerBackOffice(abonneId,backOfficeId);

        assertNotNull(abonneApresAssocierBackOffice);
        assertEquals(abonne, abonneApresAssocierBackOffice);
    }

    /**
     * Pour tester la methode associerBackOffice avec id_abonne valide et id_agence pas existe
     */
    @Test
    public void givenIdAbonneValideEtIdBackOfficePasValide_whenAssocierBackOffice_thenReturnNotFoundException() {
        Long abonneId = 1L;
        Long backOfficeId = 1L;

        Abonne abonne = createAbonne(abonneId,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(backOfficeRepository.findById(backOfficeId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.associerBackOffice(abonneId,backOfficeId);
        });
    }

    /**
     * Pour tester la methode associerBackOffice avec id_abonne pas valide
     */
    @Test
    public void givenIdAbonnePasValide_whenAssocierBackOffice_thenReturnNotFoundException() {
        Long abonneId = 1L;
        Long backOfficeId = 1L;

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.associerBackOffice(abonneId,backOfficeId);
        });
    }

    /**
     * Pour tester la methode disassocierBackOffice avec id_abonne valide
     */
    @Test
    public void givenIdAbonneValide_whenDisassocierBackOffice_thenReturnAbonneObject() throws NotFoundException {
        Long abonneId = 1L;

        Abonne abonne = createAbonne(abonneId,null,null,null);

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.of(abonne));
        when(abonneRepository.save(any(Abonne.class))).thenReturn(abonne);

        Abonne abonneApresDisassocierBackOffice = abonneService.disassocierBackOffice(abonneId);

        assertNotNull(abonneApresDisassocierBackOffice);
        assertEquals(abonne, abonneApresDisassocierBackOffice);
    }

    /**
     * Pour tester la methode disassocierBackOffice avec id_abonne pas valide
     */
    @Test
    public void givenIdAbonnePasValide_whenDisassocierBackOffice_thenReturnNotFoundException() {
        Long abonneId = 1L;

        when(abonneRepository.findById(abonneId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            abonneService.disassocierBackOffice(abonneId);
        });
    }

}
