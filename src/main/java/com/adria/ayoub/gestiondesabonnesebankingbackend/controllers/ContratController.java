package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.ContratService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8089")
@RestController
@RequestMapping("/api/contrats")
public class ContratController {

    private ContratService contratService;

    public ContratController(ContratService contratService){
        this.contratService = contratService;
    }

    /**
     * Post request pour ajouter un contrat
     * @param contrat
     * @return ResponseEntity<Contrat>
     */
    @PostMapping
    public ResponseEntity<Contrat> ajouterUnContrat(@RequestBody Contrat contrat) {
        try {
            Contrat _contrat = contratService.ajouterContrat(new Contrat(null,contrat.getIntitule(), contrat.getStatut(),contrat.getAbonne(),contrat.getOffres()));
            return new ResponseEntity<>(_contrat, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put request pour modifier un offre
     * @param id de l'offre à modifier
     * @param contrat fournit par l'utilisateur
     * @return ResponseEntity<Contrat>
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contrat> modifierUnContrat(@PathVariable("id") Long id, @RequestBody Contrat contrat) {
        Optional<Contrat> contratOptional = contratService.trouverUnContratById(id);

        if (contratOptional.isPresent()) {
            Contrat _contrat = contratOptional.get();
            _contrat.setIntitule(contrat.getIntitule());
            _contrat.setStatut(contrat.getStatut());
            if(contrat.getAbonne() != null){
                _contrat.setAbonne(contrat.getAbonne());
            }
            if(contrat.getOffres() != null){
                _contrat.setOffres(contrat.getOffres());
            }
            return new ResponseEntity<>(contratService.ajouterContrat(_contrat), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete request pour supprimer un contrat
     * @param id du contrat à supprimer
     * @return ResponseEntity<HttpStatus>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerUnContrat(@PathVariable("id") Long id) {
        try {
            contratService.supprimerContratById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete request pour supprimer tous les contrats
     * @return ResponseEntity<HttpStatus>
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> supprimerTousLesContrats() {
        try {
            contratService.supprimerTousLesContrats();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put request pur changer le statut d'un contrat
     * @param id de contrat
     * @param requestBody contient le nouveau statut envoyé par l'utilisateur
     * @return un objet de type Contrat
     */
    @PutMapping("{id}/statut")
    public Contrat changerLeStatutDuContrat(@PathVariable Long id, @RequestBody String requestBody){
        Contrat contrat = contratService.trouverUnContratById(id).get();

        String statutString = requestBody.replaceAll("\"", "").trim();

        Statut statut = Statut.valueOf(statutString.toUpperCase());

        contrat.setStatut(statut);

        return contratService.ajouterContrat(contrat);
    }

}
