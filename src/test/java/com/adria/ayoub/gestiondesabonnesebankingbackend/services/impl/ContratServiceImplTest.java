package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.ContratDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Sexe;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyExistsException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyRelatedException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private ContratDto createContratDto(String intitule,Long abonneId, List<Long> offresIds){
        ContratDto contratDto = ContratDto.build(intitule,"Actif",abonneId,offresIds);
        return contratDto;
    }

    /**
     * Pour creer un objet de type Contrat
     * @return Contrat
     */
    private Contrat createContrat(Long id){
        List<Offre> offres = new ArrayList<>();

        Contrat contrat = new Contrat(id, "Contrat "+id, Statut.ACTIF,null,offres);
        return contrat;
    }

    /**
     * Pour creer un objet de type Abonne
     * @param id de l'abonné
     * @param contrat
     * @return Abonne
     */
    private Abonne createAbonne(Long id, Contrat contrat){
        Abonne abonne = new Abonne(id,"NOM","PRENOM","EMAIL@EXAMPLE.COM","ADRESSE","TEL", Sexe.HOMME, Statut.ACTIF,contrat,null,null);
        return abonne;
    }

    /**
     * Pour creer un offre
     * @param id de l'offre
     * @return Offre
     */
    private Offre createOffre(Long id){

        List<Contrat> contrats = new ArrayList<>();

        Offre offre = new Offre(id, "Offre "+id,"Description de l'offre "+id,contrats);

        return offre;
    }

    /**
     * Pour creer une liste des offers de type Offre
     * @return List<Offre>
     */
    private List<Offre> createOffres(){

        Offre offre1 = createOffre(1L);
        Offre offre2 = createOffre(2L);

        List<Offre> offres = new ArrayList<>();
        offres.add(offre1);
        offres.add(offre2);

        return offres;
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

        Contrat contrat1 = createContrat(1L);
        Contrat contrat2  = createContrat(2L);

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

        Contrat contrat1 = createContrat(1L);
        Contrat contrat2 = createContrat(2L);

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

        Contrat contrat1 = createContrat(1L);
        Contrat contrat2 = createContrat(2L);

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

        Contrat contrat1 = createContrat(1L);
        Contrat contrat2 = createContrat(2L);

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
        Contrat contrat = createContrat(1L);

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

    /**
     * Pour tester la methode de l'ajout du contrat (avec abonné et offres null)
     */
    @Test
    public void givenContratDtoObjectWithoutAbonneEtOffres_whenAjouterContrat_thenReturnContratObject() throws AlreadyRelatedException, NotFoundException {
        ContratDto contratDto = createContratDto("Contrat 1",null,null);
        Contrat contrat = createContrat(1L);

        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratAjoute = contratService.ajouterContrat(contratDto);

        assertNotNull(contratAjoute);
        assertEquals(contrat,contratAjoute);
    }

    /**
     * Pour tester la methode de l'ajout du contrat (avec abonné existe et valide et offres null)
     * exemple je veux enregistrer un contrat avec un abonné existe dans la base de donnés + n'est pas lié à un autre contrat
     */
    @Test
    public void givenContratDtoObjectWithAbonneExisteEtValide_whenAjouterContrat_thenReturnContratObject() throws AlreadyRelatedException, NotFoundException {
        ContratDto contratDto = createContratDto("Contrat 1",1L,null);
        Contrat contrat = createContrat(1L);

        Abonne abonne = createAbonne(1L,null); //abonné valide car existe et pas lié à un autre contrat

        when(abonneRepository.findById(contratDto.getAbonneId())).thenReturn(Optional.of(abonne));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratAjoute = contratService.ajouterContrat(contratDto);

        assertNotNull(contratAjoute);
        assertEquals(contrat,contratAjoute);
    }

    /**
     * Pour tester la methode de l'ajout du contrat (avec abonné existe mais pas valide et offres null)
     * exemple je veux enregistrer un contrat avec un abonné existe dans la base de donnés et aussi lié à un autre contrat --> genere exeption AlreadyRelatedException
     */
    @Test
    public void givenContratDtoObjectWithAbonneExisteEtPasValide_whenAjouterContrat_thenReturnAlreadyRelatedException(){
        ContratDto contratDto = createContratDto("Contrat 1",1L,null);

        Contrat autreContrat = createContrat(2L);
        Abonne abonne = createAbonne(1L,autreContrat); //abonné valide car existe et lié à un autre contrat

        when(abonneRepository.findById(contratDto.getAbonneId())).thenReturn(Optional.of(abonne));

        assertThrows(AlreadyRelatedException.class, () -> {
            contratService.ajouterContrat(contratDto);
        });
    }

    /**
     * Pour tester la methode de l'ajout du contrat (avec abonné qui n'existe pas et offres null)
     * exemple je veux enregistrer un contrat avec un abonné qui n'existe pas dans la base de donnés --> genere exeption NotFoundException
     */
    @Test
    public void givenContratDtoObjectWithAbonnePasExiste_whenAjouterContrat_thenReturnNotFoundException(){
        ContratDto contratDto = createContratDto("Contrat 1",1L,null);

        when(abonneRepository.findById(contratDto.getAbonneId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            contratService.ajouterContrat(contratDto);
        });
    }

    /**
     * Pour tester la methode de l'ajout du contrat (avec offres different de null, et abonne null)
     * exemple je veux enregistrer un contrat avec des offres
     */
    @Test
    public void givenContratDtoObjectWithOffres_whenAjouterContrat_thenReturnContratObject() throws AlreadyRelatedException, NotFoundException {

        List<Long> offresIds = new ArrayList<>(){};
        offresIds.add(1L);
        offresIds.add(2L);

        ContratDto contratDto = createContratDto("Contrat 1",null,offresIds);
        Contrat contrat = createContrat(1L);

        List<Offre> offres = createOffres();

        when(offreRepository.findAllById(contratDto.getOffresIds())).thenReturn(offres);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratAjoute = contratService.ajouterContrat(contratDto);

        assertNotNull(contratAjoute);
        assertEquals(contrat,contratAjoute);
    }

    /**
     * Pour tester la methode de mise à jour du contrat qui deja existe sans modifier abonne ou offres associés
     */
    @Test
    public void givenContratDtoObjectEtIdValide_whenModifierContrat_ThenReturnContratObject() throws AlreadyRelatedException, NotFoundException {
        Long contratId = 1L;
        ContratDto contratDto = createContratDto("Contrat 1",null,null);
        Contrat contrat = createContrat(1L);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratModifie = contratService.modifierContrat(contratId,contratDto);

        assertNotNull(contratModifie);
        assertEquals(contrat,contratModifie);
    }

    /**
     * Pour tester la methode de mise à jour du contrat qui deja existe avec modification de l'abonne qui existe et valide
     */
    @Test
    public void givenContratDtoObjectEtIdValideEtAbonneExisteEtValide_whenModifierContrat_ThenReturnContratObject() throws AlreadyRelatedException, NotFoundException {
        Long contratId = 1L;
        ContratDto contratDto = createContratDto("Contrat 1",1L,null);
        Contrat contrat = createContrat(1L);

        Abonne abonne = createAbonne(1L,null); //abonne existe et valide

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(abonneRepository.findById(contratDto.getAbonneId())).thenReturn(Optional.of(abonne));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratModifie = contratService.modifierContrat(contratId,contratDto);

        assertNotNull(contratModifie);
        assertEquals(contrat,contratModifie);
    }

    /**
     * Pour tester la methode de mise à jour du contrat qui deja existe avec modification de l'abonne qui existe mais pas valide
     */
    @Test
    public void givenContratDtoObjectEtIdValideEtAbonneExisteEtPasValide_whenModifierContrat_ThenReturnAlreadyRelatedException() {
        Long contratId = 1L;
        ContratDto contratDto = createContratDto("Contrat 1",1L,null);
        Contrat contrat = createContrat(1L);

        Contrat autreContrat = createContrat(2L);
        Abonne abonne = createAbonne(1L,autreContrat); //abonne existe et pas valide (lié à un autre contrat)

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(abonneRepository.findById(contratDto.getAbonneId())).thenReturn(Optional.of(abonne));

        assertThrows(AlreadyRelatedException.class, () -> {
            contratService.modifierContrat(contratId,contratDto);
        });
    }

    /**
     * Pour tester la methode de mise à jour du contrat qui deja existe avec modification de l'abonne qui n'existe pas
     */
    @Test
    public void givenContratDtoObjectEtIdValideEtAbonnePasExiste_whenModifierContrat_ThenReturnNotFoundException() {
        Long contratId = 1L;
        ContratDto contratDto = createContratDto("Contrat 1",1L,null);
        Contrat contrat = createContrat(1L);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(abonneRepository.findById(contratDto.getAbonneId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            contratService.modifierContrat(contratId,contratDto);
        });
    }

    /**
     * Pour tester la methode de mise à jour du contrat qui deja existe avec modification des offres
     */
    @Test
    public void givenContratDtoObjectEtIdValideEtOffresValidesOuPas_whenModifierContrat_ThenReturnContratObject() throws AlreadyRelatedException, NotFoundException {
        Long contratId = 1L;

        List<Long> offresIds = new ArrayList<>(){};
        offresIds.add(1L);
        offresIds.add(2L);

        ContratDto contratDto = createContratDto("Contrat 1",null,offresIds);
        Contrat contrat = createContrat(1L);

        List<Offre> offres = createOffres();

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(offreRepository.findAllById(contratDto.getOffresIds())).thenReturn(offres);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratModifie = contratService.modifierContrat(contratId,contratDto);

        assertNotNull(contratModifie);
        assertEquals(contrat,contratModifie);
    }

    /**
     * Pour tester la methode de mise à jour du contrat qui n'existe pas
     */
    @Test
    public void givenContratDtoObjectEtIdPasValide_whenModifierContrat_ThenReturnNotFoundException() {
        Long contratId = 1L;
        ContratDto contratDto = createContratDto("Contrat 1",null,null);

        when(contratRepository.findById(contratId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->{
            contratService.modifierContrat(contratId,contratDto);
        });

    }

    /**
     * Pour tester la suppression d'un contrat by id
     */
    @Test
    public void givenId_whenSupprimerContratById_thenDoNothing(){

        Contrat contrat = createContrat(1L);

        contratService.supprimerContratById(contrat.getId());

        verify(contratRepository, times(1)).deleteById(contrat.getId());
    }

    /**
     * Pour tester la suppression de tous les contrats
     */
    @Test
    public void givenNothing_whenSupprimerTousLesContrats_thenDoNothing(){

        contratService.supprimerTousLesContrats();

        verify(contratRepository, times(1)).deleteAll();
    }

    /**
     * Pour tester la methode changerLeStatutDuContrat avec id valide
     */
    @Test
    public void givenIdValide_whenChangerLeStatutDuContrat_thenReturnContratObject() throws NotFoundException {
        Long contratId = 1L;
        Contrat contrat = createContrat(contratId);
        String statut = "suspendu";

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratApresChangementDuStatut = contratService.changerLeStatutDuContrat(contratId,statut);

        assertNotNull(contratApresChangementDuStatut);
        assertEquals(contrat, contratApresChangementDuStatut);
    }

    /**
     * Pour tester la methode changerLeStatutDuContrat avec id pas valide
     */
    @Test
    public void givenIdPasValide_whenChangerLeStatutDuContrat_thenReturnNotFoundException() {
        Long contratId = 1L;
        String statut = "suspendu";

        when(contratRepository.findById(contratId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->{
            contratService.changerLeStatutDuContrat(contratId,statut);
        });

    }

    /**
     * Pour tester la methode ajouterUnOffreAUnContrat avec id_contrat valide et id_offre existe et pas ajouté précedement
     */
    @Test
    public void givenIdContratValideEtIdOffreValideEtPasAjoutePrecedemet_whenAjouterUnOffreAUnContrat_thenReturnContratObject() throws AlreadyExistsException, NotFoundException {
        Long contratId = 1L;
        Long offreId = 1L;

        Contrat contrat = createContrat(contratId);
        Offre offre = createOffre(offreId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(offreRepository.findById(offreId)).thenReturn(Optional.of(offre));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratApresAjoutOffre = contratService.ajouterUnOffreAUnContrat(contratId,offreId);

        assertNotNull(contratApresAjoutOffre);
        assertEquals(contrat, contratApresAjoutOffre);
    }

    /**
     * Pour tester la methode ajouterUnOffreAUnContrat avec id_contrat valide et id_offre existe mais dejà ajouté
     */
    @Test
    public void givenIdContratValideEtIdOffreValideEtDejaAjoute_whenAjouterUnOffreAUnContrat_thenReturnAlreadyExistsException() {
        Long contratId = 1L;
        Long offreId = 1L;

        Contrat contrat = createContrat(contratId);
        Offre offre = createOffre(offreId);

        List<Offre> offres = new ArrayList<>();
        offres.add(offre);
        //donc cette offre deja ajouté au contrat
        contrat.setOffres(offres);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(offreRepository.findById(offreId)).thenReturn(Optional.of(offre));

        assertThrows(AlreadyExistsException.class, ()->{
            contratService.ajouterUnOffreAUnContrat(contratId,offreId);
        });
    }

    /**
     * Pour tester la methode ajouterUnOffreAUnContrat avec id_contrat valide mais id_offre pas inexistant
     */
    @Test
    public void givenIdContratValideEtIdOffrePasValide_whenAjouterUnOffreAUnContrat_thenReturnNotFoundException() {
        Long contratId = 1L;
        Long offreId = 1L;

        Contrat contrat = createContrat(contratId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(offreRepository.findById(offreId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->{
            contratService.ajouterUnOffreAUnContrat(contratId,offreId);
        });
    }

    /**
     * Pour tester la methode ajouterUnOffreAUnContrat avec id_contrat pas valide
     */
    @Test
    public void givenIdContratPasValide_whenAjouterUnOffreAUnContrat_thenReturnNotFoundException() {
        Long contratId = 1L;
        Long offreId = 1L;

        when(contratRepository.findById(contratId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->{
            contratService.ajouterUnOffreAUnContrat(contratId,offreId);
        });
    }

    /**
     * Pour tester la methode retirerUnOffreDansUnContrat avec id_contrat valide et id_offre existe
     */
    @Test
    public void givenIdContratValideEtIdOffreValide_whenRetirerUnOffreDansUnContrat_thenReturnContratObject() throws NotFoundException {
        Long contratId = 1L;
        Long offreId = 1L;

        Contrat contrat = createContrat(contratId);
        Offre offre = createOffre(offreId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(offreRepository.findById(offreId)).thenReturn(Optional.of(offre));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratApresAjoutOffre = contratService.retirerUnOffreDansUnContrat(contratId,offreId);

        assertNotNull(contratApresAjoutOffre);
        assertEquals(contrat, contratApresAjoutOffre);
    }

    /**
     * Pour tester la methode retirerUnOffreDansUnContrat avec id_contrat valide mais id_offre pas inexistant
     */
    @Test
    public void givenIdContratValideEtIdOffrePasValide_whenRetirerUnOffreDansUnContrat_thenReturnNotFoundException() {
        Long contratId = 1L;
        Long offreId = 1L;

        Contrat contrat = createContrat(contratId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(offreRepository.findById(offreId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->{
            contratService.retirerUnOffreDansUnContrat(contratId,offreId);
        });
    }

    /**
     * Pour tester la methode retirerUnOffreDansUnContrat avec id_contrat pas valide
     */
    @Test
    public void givenIdContratPasValide_whenRetirerUnOffreDansUnContrat_thenReturnNotFoundException() {
        Long contratId = 1L;
        Long offreId = 1L;

        when(contratRepository.findById(contratId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->{
            contratService.retirerUnOffreDansUnContrat(contratId,offreId);
        });
    }

    /**
     * Pour tester la methode retirerTousLesOffreDansUnContrat avec id_contrat pas valide
     */
    @Test
    public void givenIdContratPasValide_whenRetirerTousLesOffreDansUnContrat_thenReturnNotFoundException() {
        Long contratId = 1L;

        when(contratRepository.findById(contratId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->{
            contratService.retirerTousLesOffreDansUnContrat(contratId);
        });
    }

    /**
     * Pour tester la methode retirerTousLesOffreDansUnContrat avec id_contrat valide
     */
    @Test
    public void givenIdContratValide_whenRetirerTousLesOffreDansUnContrat_thenReturnContratObject() throws NotFoundException {
        Long contratId = 1L;

        Contrat contrat = createContrat(contratId);

        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat contratApresAjoutOffre = contratService.retirerTousLesOffreDansUnContrat(contratId);

        assertNotNull(contratApresAjoutOffre);
        assertEquals(contrat, contratApresAjoutOffre);
    }

}