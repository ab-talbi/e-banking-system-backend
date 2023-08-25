package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.OffreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8089")
@RestController
@RequestMapping("/api/offres")
public class OffreController {

    private OffreService offreService;

    public OffreController(OffreService offreService){
        this.offreService = offreService;
    }

    /**
     * methode Post pour la creation d'un offre
     * @param offre entity
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Offre> ajouterUnOffre(@RequestBody Offre offre) {
        try {
            Offre _offre = offreService.ajouterOffre(new Offre(null,offre.getLibelle(), offre.getDescription(),null));
            return new ResponseEntity<>(_offre, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put methode pour modifier un offre
     * @param id de l'offre
     * @param offre l'objet envoyé par le client
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Offre> modifierUnOffre(@PathVariable("id") Long id, @RequestBody Offre offre) {
        Optional<Offre> offreOptional = offreService.trouverUnOffreById(id);

        if (offreOptional.isPresent()) {
            Offre _offre = offreOptional.get();
            _offre.setLibelle(offre.getLibelle());
            _offre.setDescription(offre.getDescription());
            return new ResponseEntity<>(offreService.ajouterOffre(_offre), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete request pour supprimer un seul offre par l'id
     * @param id de l'offre à supprimer
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimerUnOffre(@PathVariable("id") Long id) {
        try {
            offreService.supprimerOffreById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete request pour supprimer tous les offres
     * @return ResponseEntity<HttpStatus> No content si tous va bien, Server error sinon
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> supprimerTousLesOffres() {
        try {
            offreService.supprimerTousLesOffres();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
