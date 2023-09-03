package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.AbonneDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyRelatedException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8089")
@RestController
@RequestMapping("/api/abonnes")
public class AbonneController {

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

        try {

            Page<Abonne> pageAbonnes = abonneService.trouverLesAbonnes(search,val,page,sort);

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
    public ResponseEntity<Abonne> getUnSeulAbonne(@PathVariable Long id) throws NotFoundException {
        Abonne abonne = abonneService.trouverUnAbonneById(id);
        return new ResponseEntity<>(abonne,HttpStatus.OK);
    }

    /**
     * methode Post pour la creation d'un abonné
     * @param abonneDto entity
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Abonne> ajouterUnAbonne(@RequestBody @Valid AbonneDto abonneDto) throws AlreadyRelatedException, NotFoundException {
        Abonne abonne = abonneService.ajouterAbonne(abonneDto);
        return new ResponseEntity<>(abonne, HttpStatus.CREATED);
    }

    /**
     * Put methode pour modifier un abonné
     * @param id de l'abonné
     * @param abonneDto l'objet envoyé par le client
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Abonne> modifierUnAbonne(@PathVariable("id") Long id, @RequestBody @Valid AbonneDto abonneDto) throws AlreadyRelatedException, NotFoundException {
        Abonne abonne = abonneService.modifierAbonne(id,abonneDto);
        return new ResponseEntity<>(abonne, HttpStatus.OK);
    }

    /**
     * Delete request pour supprimer un seul abonné par l'id
     * @param id de l'abonné à supprimer
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerUnAbonne(@PathVariable("id") Long id) {
        abonneService.supprimerAbonneById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete request pour supprimer tous les abonnés
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> supprimerTousLesAbonnes() {
        abonneService.supprimerTousLesAbonnes();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Put request pour changer le statut de l'abonné
     * @param id de l'abonné
     * @param requestBody new statut
     * @return un abonné
     */
    @PutMapping("{id}/statut")
    public ResponseEntity<Abonne> changerLeStatutDeLAbonne(@PathVariable Long id, @RequestBody String requestBody) throws NotFoundException {
        Abonne abonne = abonneService.changerLeStatutDeLAbonne(id,requestBody);
        return new ResponseEntity<>(abonne, HttpStatus.OK);
    }

    /**
     * Put Request Pour associer une agence à un abonné
     * @param id de l'abonné
     * @param agence_id de l'agence
     * @return un Abonné
     */
    @PutMapping("/{id}/agence/{agence_id}")
    public ResponseEntity<Abonne> associerUneAgence(@PathVariable Long id, @PathVariable Long agence_id) throws NotFoundException {
        Abonne abonne = abonneService.associerAgence(id,agence_id);
        return new ResponseEntity<>(abonne, HttpStatus.OK);
    }

    /**
     * Put request pour retirer l'agence associé
     * @param id de l'abonné
     * @return l'abonné modifié
     */
    @PutMapping("/{id}/retirer_agence")
    public ResponseEntity<Abonne> disassocierUneAgence(@PathVariable Long id) throws NotFoundException {
        Abonne abonne = abonneService.disassocierAgence(id);
        return new ResponseEntity<>(abonne, HttpStatus.OK);
    }

    /**
     * Put Request pour associer un backoffice à un abonné
     * @param id de l'abonné
     * @param backoffice_id de backoffice
     * @return un Abonné
     */
    @PutMapping("/{id}/backoffice/{backoffice_id}")
    public ResponseEntity<Abonne> associerUnBackOffice(@PathVariable Long id, @PathVariable Long backoffice_id) throws NotFoundException {
        Abonne abonne = abonneService.associerBackOffice(id,backoffice_id);
        return new ResponseEntity<>(abonne, HttpStatus.OK);
    }

    /**
     * Put request pour retirer un backoffice
     * @param id de l'abonné
     * @return l'abonné modifié
     */
    @PutMapping("/{id}/retirer_backoffice")
    public ResponseEntity<Abonne> disassocierUnBackOffice(@PathVariable Long id) throws NotFoundException {
        Abonne abonne = abonneService.disassocierBackOffice(id);
        return new ResponseEntity<>(abonne, HttpStatus.OK);
    }

    /**
     * Put request pour associer un contrat à un abonné
     * @param id de l'abonné
     * @param contrat_id
     * @return l'abonné modifié
     */
    @PutMapping("/{id}/contrat/{contrat_id}")
    public ResponseEntity<Abonne> associerUnContrat(@PathVariable Long id, @PathVariable Long contrat_id) throws NotFoundException, AlreadyRelatedException {
        Abonne abonne = abonneService.associerContrat(id, contrat_id);
        return new ResponseEntity<>(abonne, HttpStatus.OK);
    }

    /**
     * Put request pour disassocier un contrat
     * @param id de l'abonné
     * @return l'abonné modifié
     */
    @PutMapping("/{id}/retirer_contrat")
    public ResponseEntity<Abonne> disassocierUnContrat(@PathVariable Long id) throws NotFoundException {
        Abonne abonne = abonneService.disassocierContrat(id);
        return new ResponseEntity<>(abonne, HttpStatus.OK);
    }

}
