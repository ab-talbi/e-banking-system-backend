package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.ContratDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyExistsException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyRelatedException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.ContratService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/contrats")
public class ContratController {

    private ContratService contratService;

    public ContratController(ContratService contratService){
        this.contratService = contratService;
    }

    /**
     * Get request pour chercher/filtrer des contrats
     * @param search pour identifier quelle partie de recherche (intitule, statut ou abonne)
     * @param val valeur à chercher
     * @param page numero de page
     * @param sort filtre (field et direction)
     * @return List des contrats  si existe et le statut ok, no content ou server error, et d'autre info comme page, totalpages...
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getLesContratsPage(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String val,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        try {

            Page<Contrat> pageContrats = contratService.trouverLesContrats(search,val,page,sort);

            List<Contrat> contrats = pageContrats.getContent();

            if (contrats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("contrats", contrats);
            response.put("page", pageContrats.getNumber());
            response.put("totalDesContrats", pageContrats.getTotalElements());
            response.put("totalDesPages", pageContrats.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get request pour trouver un seul contrat
     * @param id du contrat
     * @return ResponseEntity<Contrat>
     * @throws NotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contrat> getUnSeulContrat(@PathVariable Long id) throws NotFoundException {
        Contrat contrat = contratService.trouverUnContratById(id);
        return new ResponseEntity<>(contrat,HttpStatus.OK);
    }

    /**
     * Post request pour ajouter un contrat
     * @param contratDto
     * @return ResponseEntity<Contrat>
     */
    @PostMapping
    public ResponseEntity<Contrat> ajouterUnContrat(@RequestBody @Valid ContratDto contratDto) throws AlreadyRelatedException, NotFoundException {
        Contrat contrat = contratService.ajouterContrat(contratDto);
        return new ResponseEntity<>(contrat, HttpStatus.CREATED);
    }

    /**
     * Put request pour modifier un offre
     * @param id de l'offre à modifier
     * @param contratDto fournit par l'utilisateur
     * @return ResponseEntity<Contrat>
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contrat> modifierUnContrat(@PathVariable("id") Long id, @RequestBody @Valid ContratDto contratDto) throws NotFoundException, AlreadyRelatedException {
        Contrat contrat = contratService.modifierContrat(id,contratDto);
        return new ResponseEntity<>(contrat, HttpStatus.OK);
    }

    /**
     * Delete request pour supprimer un contrat
     * @param id du contrat à supprimer
     * @return ResponseEntity<HttpStatus>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerUnContrat(@PathVariable("id") Long id) {
        contratService.supprimerContratById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete request pour supprimer tous les contrats
     * @return ResponseEntity<HttpStatus>
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> supprimerTousLesContrats() {
        contratService.supprimerTousLesContrats();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Put request pur changer le statut d'un contrat
     * @param id de contrat
     * @param requestBody contient le nouveau statut envoyé par l'utilisateur
     * @return un objet de type Contrat
     */
    @PutMapping("{id}/statut")
    public ResponseEntity<Contrat> changerLeStatutDuContrat(@PathVariable Long id, @RequestBody String requestBody) throws NotFoundException {
        Contrat contrat = contratService.changerLeStatutDuContrat(id,requestBody);
        return new ResponseEntity<>(contrat, HttpStatus.OK);
    }

    /**
     * Put request pour ajouter un offre à un contrat
     * @param id du contrat
     * @param offre_id
     * @return ResponseEntity<Contrat>, Ok si ajouté, Not ok si deja existe
     */
    @PutMapping("{id}/offres/{offre_id}")
    public ResponseEntity<Contrat> ajouterUnOffreAUnContrat(@PathVariable Long id, @PathVariable Long offre_id) throws NotFoundException, AlreadyExistsException {
        Contrat contrat = contratService.ajouterUnOffreAUnContrat(id,offre_id);
        return new ResponseEntity<>(contrat,HttpStatus.OK);
    }

    /**
     * Put request pour modifier un contrat par elemination d'un offre
     * @param id du contrat
     * @param offre_id
     * @return ResponseEntity<Contrat>
     */
    @PutMapping("{id}/offres/{offre_id}/retirer")
    public ResponseEntity<Contrat> retirerUnOffreDansUnContrat(@PathVariable Long id, @PathVariable Long offre_id) throws NotFoundException {
        Contrat contrat = contratService.retirerUnOffreDansUnContrat(id,offre_id);
        return new ResponseEntity<>(contrat,HttpStatus.OK);
    }

    /**
     * Put request pour retirer tous les offres d'un contrat
     * @param id du contrat
     * @return ResponseEntity<Contrat>
     */
    @PutMapping("{id}/retirer_tous_les_offres")
    public ResponseEntity<Contrat> retirerTousLesOffreDansUnContrat(@PathVariable Long id) throws NotFoundException {
        Contrat contrat = contratService.retirerTousLesOffreDansUnContrat(id);
        return new ResponseEntity<>(contrat,HttpStatus.OK);
    }

}
