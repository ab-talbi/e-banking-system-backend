package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8089")
@RestController
@RequestMapping("/api/abonnes")
public class AbonneController {

    private AbonneService abonneService;
 
    public AbonneController(AbonneService abonneService){
        this.abonneService = abonneService;
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
            _abonne.setAgence(abonne.getAgence());
            _abonne.setBackOffice(abonne.getBackOffice());
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

}
