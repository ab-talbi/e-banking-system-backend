package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Agence;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.BackOffice;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8089")
@RestController
@RequestMapping("/api/abonnes")
public class AbonneController {

    private final int PAGE_SIZE = 10;

    private AbonneService abonneService;
 
    public AbonneController(AbonneService abonneService){
        this.abonneService = abonneService;
    }

    /**
     * Get request pour chercher/filtrer des abonnés
     * @param search pour identifier quelle partie de recherche (nom, prenom, email, tel, sexe, statut...)
     * @param val valeur à chercher
     * @param page numero de page
     * @param sort filtre (field et direction)
     * @return List des abonnés si existe et le statut ok, no content ou server error, et d'autre info comme page, totalpages...
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getLesAbonnesPage(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String val,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        if(page < 0){
            page = 0;
        }

        try {
            List<Sort.Order> orders = getOrdersFromSortParam(sort);

            Pageable pagingSort = PageRequest.of(page, PAGE_SIZE, Sort.by(orders));

            Page<Abonne> pageAbonnes;
            if (search == null || val == null){
                pageAbonnes = abonneService.trouverTousLesAbonnes(pagingSort);
            }else {
                pageAbonnes = abonneService.trouverUneListeDesAbonnes(search, val, pagingSort);
            }

            List<Abonne> abonnes = pageAbonnes.getContent();

            if (abonnes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("abonnes", abonnes);
            response.put("page", pageAbonnes.getNumber());
            response.put("totalDesAbonnes", pageAbonnes.getTotalElements());
            response.put("totalDesPages", pageAbonnes.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get request pour trouver un seul abonné
     * @param id de l'abonné
     * @return ResponseEntity<Abonne>
     */
    @GetMapping("/{id}")
    public ResponseEntity<Abonne> getUnSeulAbonne(@PathVariable Long id){
        try{
            Abonne abonne = abonneService.trouverUnAbonneById(id).get();
            return new ResponseEntity<>(abonne,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * methode Post pour la creation d'un abonné
     * @param abonne entity
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Abonne> ajouterUnAbonne(@RequestBody Abonne abonne) {
        try {
            Abonne _abonne = abonneService.ajouterAbonne(new Abonne(null,abonne.getNom(), abonne.getPrenom(),abonne.getEmail(),abonne.getAdresse(),abonne.getTelephone(),abonne.getSexe(),abonne.getStatut(),null,abonne.getAgence(),abonne.getBackOffice()));
            return new ResponseEntity<>(_abonne, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put methode pour modifier un abonné
     * @param id de l'abonné
     * @param abonne l'objet envoyé par le client
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Abonne> modifierUnAbonne(@PathVariable("id") Long id, @RequestBody Abonne abonne) {
        Optional<Abonne> abonneOptional = abonneService.trouverUnAbonneById(id);

        if (abonneOptional.isPresent()) {
            Abonne _abonne = abonneOptional.get();
            _abonne.setNom(abonne.getNom());
            _abonne.setPrenom(abonne.getPrenom());
            _abonne.setEmail(abonne.getEmail());
            _abonne.setAdresse(abonne.getAdresse());
            _abonne.setTelephone(abonne.getTelephone());
            _abonne.setSexe(abonne.getSexe());
            _abonne.setStatut(abonne.getStatut());
            if(abonne.getAgence() != null){
                _abonne.setAgence(abonne.getAgence());
            }
            if(abonne.getBackOffice() != null){
                _abonne.setBackOffice(abonne.getBackOffice());
            }
            return new ResponseEntity<>(abonneService.ajouterAbonne(_abonne), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete request pour supprimer un seul abonné par l'id
     * @param id de l'abonné à supprimer
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerUnAbonne(@PathVariable("id") Long id) {
        try {
            abonneService.supprimerAbonneById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete request pour supprimer tous les abonnés
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> supprimerTousLesAbonnes() {
        try {
            abonneService.supprimerTousLesAbonnes();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put Request Pour associer une agence à un abonné
     * @param id de l'abonné
     * @param agence_id de l'agence
     * @return un Abonné
     */
    @PutMapping("/{id}/agence/{agence_id}")
    public ResponseEntity<Abonne> associerUneAgence(@PathVariable Long id, @PathVariable Long agence_id){
        Abonne abonne = abonneService.trouverUnAbonneById(id).get();
        Agence agence = abonneService.trouverUneAgenceById(agence_id).get();

        abonne.setAgence(agence);

        return new ResponseEntity<>(abonneService.ajouterAbonne(abonne), HttpStatus.OK);
    }

    /**
     * Put request pour retirer l'agence associé
     * @param id de l'abonné
     * @return l'abonné modifié
     */
    @PutMapping("/{id}/retirer_agence")
    public ResponseEntity<Abonne> disassocierUneAgence(@PathVariable Long id){
        Abonne abonne = abonneService.trouverUnAbonneById(id).get();

        abonne.setAgence(null);

        return new ResponseEntity<>(abonneService.ajouterAbonne(abonne), HttpStatus.OK);
    }

    /**
     * Put Request pour associer un backoffice à un abonné
     * @param id de l'abonné
     * @param backoffice_id de backoffice
     * @return un Abonné
     */
    @PutMapping("/{id}/backoffice/{backoffice_id}")
    public ResponseEntity<Abonne> associerUnBackOffice(@PathVariable Long id, @PathVariable Long backoffice_id){
        Abonne abonne = abonneService.trouverUnAbonneById(id).get();
        BackOffice backOffice = abonneService.trouverUnBackOfficeById(backoffice_id).get();

        abonne.setBackOffice(backOffice);

        return new ResponseEntity<>(abonneService.ajouterAbonne(abonne), HttpStatus.OK);
    }

    /**
     * Put request pour retirer un backoffice
     * @param id de l'abonné
     * @return l'abonné modifié
     */
    @PutMapping("/{id}/retirer_backoffice")
    public ResponseEntity<Abonne> disassocierUnBackOffice(@PathVariable Long id){
        Abonne abonne = abonneService.trouverUnAbonneById(id).get();

        abonne.setBackOffice(null);

        return new ResponseEntity<>(abonneService.ajouterAbonne(abonne), HttpStatus.OK);
    }

    /**
     * Put request pour associer un contrat à un abonné
     * @param id de l'abonné
     * @param contrat_id
     * @return l'abonné modifié
     */
    @PutMapping("/{id}/contrat/{contrat_id}")
    public ResponseEntity<Abonne> associerUnContrat(@PathVariable Long id, @PathVariable Long contrat_id){
        Abonne abonne = abonneService.trouverUnAbonneById(id).get();
        Contrat contrat = abonneService.trouverUnContratById(contrat_id).get();

        abonne.associerContrat(contrat);

        return new ResponseEntity<>(abonneService.ajouterAbonne(abonne), HttpStatus.OK);
    }

    /**
     * Put request pour disassocier un contrat
     * @param id de l'abonné
     * @return l'abonné modifié
     */
    @PutMapping("/{id}/retirer_contrat")
    public ResponseEntity<Abonne> disassocierUnContrat(@PathVariable Long id){
        Abonne abonne = abonneService.trouverUnAbonneById(id).get();

        abonne.disassocierContrat();

        return new ResponseEntity<>(abonneService.ajouterAbonne(abonne), HttpStatus.OK);
    }

    /**
     * Put request pour changer le statut de l'abonné
     * @param id de l'abonné
     * @param requestBody new statut
     * @return un abonné
     */
    @PutMapping("{id}/statut")
    public ResponseEntity<Abonne> changerLeStatutDeLAbonne(@PathVariable Long id, @RequestBody String requestBody){
        Abonne abonne = abonneService.trouverUnAbonneById(id).get();

        String statutString = requestBody.replaceAll("\"", "").trim();

        Statut statut = Statut.valueOf(statutString.toUpperCase());

        abonne.setStatut(statut);

        return new ResponseEntity<>(abonneService.ajouterAbonne(abonne), HttpStatus.OK);
    }

    /**
     * Methode pour obtiens une liste des ordres pour les filtres
     * @param sort de type String[]
     * @return une liste des Order
     */
    private List<Sort.Order> getOrdersFromSortParam(String[] sort){
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        return orders;
    }

    /**
     * Methode pour obtenir la direction de Sort (pour convertir la deriction de String vers Direction)
     * @param direction de type String
     * @return soit Direction.ASC ou Direction.DESC
     */
    private Sort.Direction getSortDirection(String direction){
        if(direction.equals("desc")){
            return Sort.Direction.DESC;
        }else{
            return Sort.Direction.ASC;
        }
    }
}
